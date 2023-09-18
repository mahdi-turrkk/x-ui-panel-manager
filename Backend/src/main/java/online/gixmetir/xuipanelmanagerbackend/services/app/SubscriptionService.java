package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.ClientRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionReNewLogRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionRepository;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final InboundRepository inboundRepository;
    private final PanelService panelService;
    private final ClientRepository clientRepository;
    private final SubscriptionReNewLogRepository subscriptionReNewLogRepository;
    private final ClientService clientService;

    public SubscriptionService(SubscriptionRepository repository, InboundRepository inboundRepository, PanelService panelService, ClientRepository clientRepository, SubscriptionReNewLogRepository subscriptionReNewLogRepository, ClientService clientService) {
        this.subscriptionRepository = repository;
        this.inboundRepository = inboundRepository;
        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.subscriptionReNewLogRepository = subscriptionReNewLogRepository;
        this.clientService = clientService;
    }

    @Transactional
    public List<SubscriptionDto> createSubscription(SubscriptionRequest request) throws Exception {
        List<SubscriptionEntity> subscriptionEntities = new ArrayList<>();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            request.setUserId(userEntity.getId());
        }
        for (int i = 0; i < request.getNumberSubscriptionsToGenerate(); i++) {
            SubscriptionEntity entity = request.toEntity();
            entity.setUuid(UUID.randomUUID().toString());
            entity.setStatus(true);
            subscriptionEntities.add(entity);

        }
        subscriptionRepository.saveAll(subscriptionEntities);
        addOrUpdateClientsRelatedToSubscription(subscriptionEntities);
        return subscriptionEntities.stream().map(SubscriptionDto::new).toList();
    }

    @Transactional
    public void addOrUpdateClientsRelatedToSubscription(List<SubscriptionEntity> entities) throws Exception {
        List<InboundEntity> inbounds = inboundRepository.findAllByGeneratable(true);
        List<ClientEntity> clientEntitiesToSaveInDb = new ArrayList<>();
        for (InboundEntity inbound : inbounds) {
            List<ClientModel> clientModelsAddToPanel = new ArrayList<>();
            List<ClientEntity> clientEntitiesUpdateInPanel = new ArrayList<>();
            for (SubscriptionEntity subscription : entities) {
                ClientEntity clientEntity = null;
                ClientEntity clientEntityFromDb = clientRepository.findByInboundIdAndSubscriptionId(inbound.getId(), subscription.getId());
                if (clientEntityFromDb != null) {
                    clientEntityFromDb.setEnable(subscription.getStatus());
                    clientEntity = clientEntityFromDb;
                    clientEntitiesUpdateInPanel.add(clientEntity);
                } else {
                    ClientModel clientModel = ClientModel.builder()
                            .id(UUID.randomUUID().toString())
                            .email(inbound.getRemark() + "-" + subscription.getTitle() + "-" + new Helper().byteToGB(subscription.getTotalFlow()) + "GB-" + new Random().nextLong())
                            .flow("")
                            .totalGb(0)
                            .expiryTime(0L)
                            .enable(subscription.getStatus())
                            .tgId("")
                            .subId("")
                            .limitIp(1)
                            .build();
                    clientModelsAddToPanel.add(clientModel);
                    clientEntity = clientModel.toEntity();
                    clientEntity.setInboundId(inbound.getId());
                    clientEntity.setSubscriptionId(subscription.getId());
                }
                clientEntitiesToSaveInDb.add(clientEntity);
            }
            panelService.addClient(clientModelsAddToPanel, new ServerDto(inbound.getServer()), inbound.getIdFromPanel());
            panelService.updateClients(clientEntitiesUpdateInPanel);
        }
        clientRepository.saveAll(clientEntitiesToSaveInDb);
    }


    public SubscriptionDto update(Long id, SubscriptionRequest request, SubscriptionUpdateType updateType) throws Exception {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        subscriptionEntity = request.toEntity(subscriptionEntity);
        if (Objects.requireNonNull(updateType) == SubscriptionUpdateType.ReNew) {
            SubscriptionReNewLogEntity logEntity = SubscriptionReNewLogEntity.builder()
                    .subscriptionId(subscriptionEntity.getId())
                    .date(LocalDate.now())
                    .periodLength(subscriptionEntity.getPeriodLength())
                    .totalFlow(subscriptionEntity.getTotalFlow())
                    .build();
            reNewSubscription(subscriptionEntity.getId());
            subscriptionReNewLogRepository.save(logEntity);

        }
        subscriptionRepository.save(subscriptionEntity);
        return new SubscriptionDto(subscriptionEntity);
    }

    private void reNewSubscription(Long id) throws Exception {
        List<ClientEntity> clientEntities = clientRepository.findAllBySubscriptionId(id);
        List<ClientEntity> clientEntitiesMustToUpdateInPanel = new ArrayList<>();
        clientEntities.forEach(client -> {
            if (!client.getEnable()) {
                client.setEnable(true);
                clientEntitiesMustToUpdateInPanel.add(client);
            }
        });
        panelService.updateClients(clientEntitiesMustToUpdateInPanel);
        clientRepository.saveAll(clientEntities);
    }


    @Transactional
    public void delete(Long id) throws Exception {
        List<ClientEntity> clients = clientRepository.findAllBySubscriptionId(id);
        panelService.deleteClients(clients);
        clientRepository.deleteAll(clients);
        subscriptionRepository.deleteById(id);
    }

    public Page<SubscriptionDto> getAll(SubscriptionFilter filter, Pageable pageable, Boolean selfSubs) {
        UserEntity entity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (entity.getRole() == Role.Customer) {
            SubscriptionFilter filter1 = new SubscriptionFilter(filter.id(), filter.Uuid(), filter.status(), filter.title(), entity.getId());
            return subscriptionRepository.findAll(filter1, pageable).map(SubscriptionDto::new);
        }
        if (selfSubs && entity.getRole() == Role.Admin) {
            SubscriptionFilter filter1 = new SubscriptionFilter(entity.getId(), filter.Uuid(), filter.status(), filter.title(), entity.getId());
            return subscriptionRepository.findAll(filter1, pageable).map(SubscriptionDto::new);
        }
        return subscriptionRepository.findAll(filter, pageable).map(SubscriptionDto::new);
    }

    @Transactional
    public SubscriptionDto changeStatus(Boolean newStatus, Long id) throws Exception {
        SubscriptionEntity entity = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        entity.setStatus(newStatus);
        addOrUpdateClientsRelatedToSubscription(List.of(entity));
        return new SubscriptionDto(entity);
    }

    public SubscriptionDto report(String subLink) throws Exception {
        String uuid = new Helper().extractUuidFromLink(subLink);
        SubscriptionEntity entity = subscriptionRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("Subscription with uuid: " + subLink + " non found"));
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userEntity.getId() == entity.getUserId() || userEntity.getRole() == Role.Admin)
            return new SubscriptionDto(entity);
        else throw new EntityNotFoundException("link is invalid.");
    }

    public String getConfig(String uuid) throws Exception {
//        String uuid = new Helper().extractUuidFromLink(subLink);
        SubscriptionEntity subscription = subscriptionRepository.findByUuid(uuid).orElseThrow(() -> new Exception("subscription not found"));
        List<ClientEntity> entities = clientRepository.findAllBySubscriptionId(subscription.getId());
        StringBuilder configs = new StringBuilder();
        for (ClientEntity entity : entities) {
            configs.append(clientService.generateClientString(entity)).append("\n");
        }


        return configs.toString();
    }

    @Transactional
    public void expiration() throws Exception {
        //todo
        List<SubscriptionEntity> expiredSubs = subscriptionRepository.findAllByExpireDateAfterAndStatus(LocalDateTime.now(), true);
        for (SubscriptionEntity entity : expiredSubs) {
            entity.setStatus(false);
            addOrUpdateClientsRelatedToSubscription(List.of(entity));
        }
        subscriptionRepository.saveAll(expiredSubs);
    }

}

