package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ResponseModel;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionReNewLogEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionDto;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionRequest;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionUpdateType;
import online.gixmetir.xuipanelmanagerbackend.repositories.ClientRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionReNewLogRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionRepository;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    public SubscriptionService(SubscriptionRepository repository, InboundRepository inboundRepository, PanelService panelService, ClientRepository clientRepository, SubscriptionReNewLogRepository subscriptionReNewLogRepository) {
        this.subscriptionRepository = repository;
        this.inboundRepository = inboundRepository;
        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.subscriptionReNewLogRepository = subscriptionReNewLogRepository;
    }

    @Transactional
    public List<SubscriptionDto> createSubscription(SubscriptionRequest request) throws Exception {
        List<SubscriptionEntity> subscriptionEntities = new ArrayList<>();
        for (int i = 0; i < request.getNumberSubscriptionsToGenerate(); i++) {
            SubscriptionEntity entity = request.toEntity();
            entity.setUuid(UUID.randomUUID().toString());
            subscriptionEntities.add(entity);
        }
        subscriptionRepository.saveAll(subscriptionEntities);
        addOrUpdateClientsRelatedToSubscription(subscriptionEntities);
        return subscriptionEntities.stream().map(SubscriptionDto::new).toList();
    }

    @Transactional
    public void addOrUpdateClientsRelatedToSubscription(List<SubscriptionEntity> entities) throws Exception {
        List<InboundEntity> inbounds = inboundRepository.findAllByGeneratable(true);
        List<ClientEntity> clientEntitiesToAdd = new ArrayList<>();
        for (InboundEntity inbound : inbounds) {
            List<ClientModel> clientModelsToAdd = new ArrayList<>();
            for (SubscriptionEntity subscription : entities) {
                ClientEntity clientEntity = clientRepository.findByInboundIdAndSubscriptionId(inbound.getId(), subscription.getId());
                if (clientEntity != null) {
                    clientEntity.setTotalGb(subscription.getTotalUsed());
                } else {
                    ClientModel clientModel = ClientModel.builder()
                            .id(UUID.randomUUID().toString())
                            .email(inbound.getRemark() + "-" + subscription.getTitle() + "-" + subscription.getTotalFlow() + "GB-" + new Random().nextLong())
                            .flow("")
                            .totalGb(subscription.getTotalFlow())
                            .expiryTime(0L)
                            .enable(true)
                            .tgId("")
                            .subId("")
                            .limitIp(1)
                            .enable(true)
                            .build();
                    clientModelsToAdd.add(clientModel);
                    clientEntity = clientModel.toEntity();
                    clientEntity.setInboundId(inbound.getId());
                    clientEntity.setSubscriptionId(subscription.getId());
                }
                clientEntitiesToAdd.add(clientEntity);
            }
            ResponseEntity<ResponseModel> response = panelService.addClient(clientModelsToAdd, new ServerDto(inbound.getServer()), inbound.getId());
            if (!response.getBody().getSuccess()) {
                throw new Exception(response.getBody().getMsg());
            }
        }
        clientRepository.saveAll(clientEntitiesToAdd);
    }


    public SubscriptionDto update(Long id, SubscriptionRequest request, SubscriptionUpdateType updateType) {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        subscriptionEntity = request.toEntity(subscriptionEntity);
        if (Objects.requireNonNull(updateType) == SubscriptionUpdateType.ReNew) {
            SubscriptionReNewLogEntity logEntity = SubscriptionReNewLogEntity.builder()
                    .subscriptionId(subscriptionEntity.getId())
                    .date(LocalDate.now())
                    .periodLength(subscriptionEntity.getPeriodLength())
                    .totalFlow(subscriptionEntity.getTotalFlow())
                    .build();
            subscriptionReNewLogRepository.save(logEntity);
        }
        subscriptionRepository.save(subscriptionEntity);
        return new SubscriptionDto(subscriptionEntity);
    }

    @Transactional
    public void delete(Long id) throws Exception {
        List<ClientEntity> clients = clientRepository.findAllBySubscriptionId(id);
        panelService.deleteClients(clients);
        clientRepository.deleteAll(clients);
        subscriptionRepository.deleteById(id);

    }

    public Page<SubscriptionDto> getAll(Pageable pageable) {
        return subscriptionRepository.findAll(pageable).map(SubscriptionDto::new);
    }
}

