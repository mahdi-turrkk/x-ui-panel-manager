package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.*;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final InboundRepository inboundRepository;
    private final PanelService panelService;
    private final ClientRepository clientRepository;
    private final SubscriptionReNewLogRepository subscriptionReNewLogRepository;
    private final ClientService clientService;
    private final ServerRepository serverRepository;

    public SubscriptionService(SubscriptionRepository repository, InboundRepository inboundRepository, PanelService panelService, ClientRepository clientRepository, SubscriptionReNewLogRepository subscriptionReNewLogRepository, ClientService clientService, ServerRepository serverRepository) {
        this.subscriptionRepository = repository;
        this.inboundRepository = inboundRepository;
        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.subscriptionReNewLogRepository = subscriptionReNewLogRepository;
        this.clientService = clientService;
        this.serverRepository = serverRepository;
    }

    @Transactional
    public List<SubscriptionDto> createSubscription(SubscriptionRequest request) throws Exception {
        List<SubscriptionEntity> subscriptionEntities = new ArrayList<>();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            UserEntity userEntity = new Helper().getUserFromContext();
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
                    clientEntity.setSendToUser(true);
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
                    clientEntity.setSendToUser(true);
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
        SubscriptionEntity subscriptionEntityFromDb = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        if (Objects.requireNonNull(updateType) == SubscriptionUpdateType.ReNew) {
            reNewSubscription(subscriptionEntityFromDb, request);

            SubscriptionReNewLogEntity logEntity = SubscriptionReNewLogEntity.builder()
                    .subscriptionId(subscriptionEntityFromDb.getId())
                    .date(LocalDate.now())
                    .periodLength(subscriptionEntityFromDb.getPeriodLength())
                    .totalFlow(subscriptionEntityFromDb.getTotalFlow())
                    .build();
            subscriptionReNewLogRepository.save(logEntity);

        }
        subscriptionRepository.save(subscriptionEntityFromDb);
        return new SubscriptionDto(subscriptionEntityFromDb);
    }

    private void reNewSubscription(SubscriptionEntity subscription, SubscriptionRequest request) throws Exception {
        subscription.setPeriodLength(request.getPeriodLength());
        subscription.setTotalFlow(subscription.getTotalFlow() + new Helper().GBToByte(request.getTotalFlow()));
        if (subscription.getExpireDate() != null)
            subscription.setExpireDate(subscription.getExpireDate().plusDays(subscription.getPeriodLength()));
        subscription.setStatus(true);
        List<ClientEntity> clientEntities = clientRepository.findAllBySubscriptionId(subscription.getId());
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

    public Page<SubscriptionDto> getAll(SubscriptionFilter filter, Pageable pageable, Boolean selfSubs) throws Exception {
        UserEntity entity = new Helper().getUserFromContext();
        if (entity.getRole() == Role.Customer) {
            SubscriptionFilter filter1 = new SubscriptionFilter(filter.id(), filter.Uuid(), filter.status(), filter.title(), entity.getId());
            return subscriptionRepository.findAll(filter1, pageable).map(SubscriptionDto::new);
        }
        if (selfSubs && entity.getRole() == Role.Admin) {
            SubscriptionFilter filter1 = new SubscriptionFilter(filter.id(), filter.Uuid(), filter.status(), filter.title(), entity.getId());
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
        UserEntity userEntity = new Helper().getUserFromContext();
        if (userEntity.getId() == entity.getUserId() || userEntity.getRole() == Role.Admin)
            return new SubscriptionDto(entity);
        if (userEntity.getId() != entity.getUserId())
            throw new Exception("doesnt access to this method.");
        throw new Exception("link is invalid.");
    }

    public String getSubscriptionData(String uuid) throws Exception {
        SubscriptionEntity subscription = subscriptionRepository.findByUuid(uuid).orElseThrow(() -> new Exception("subscription not found"));
        List<ClientEntity> entities = clientRepository.findAllBySubscriptionIdAndSendToUser(subscription.getId(), true);
        StringBuilder configs = new StringBuilder();
        for (ClientEntity entity : entities) {
            configs.append(clientService.generateClientString(entity)).append("\n");
        }
        return configs.toString();
    }

    public SummaryModel getSummary() throws Exception {
        SummaryModel model = new SummaryModel();
        model.setTotalDownload(subscriptionRepository.getTotalDownload());
        model.setTotalUpload(subscriptionRepository.getTotalUpload());
        model.setTotalSold(subscriptionRepository.getNumberOfAllSubscriptions());
        model.setTotalLastMonthSold(subscriptionRepository.getNumberOfSubscriptionsCreatedLastMonth());
        return model;
    }
}

