package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ResponseModel;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionDto;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionRequest;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionUpdateType;
import online.gixmetir.xuipanelmanagerbackend.repositories.ClientRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionRepository;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final InboundRepository inboundRepository;
    private final PanelService panelService;
    private final ClientRepository clientRepository;

    public SubscriptionService(SubscriptionRepository repository, InboundRepository inboundRepository, PanelService panelService, ClientRepository clientRepository) {
        this.subscriptionRepository = repository;
        this.inboundRepository = inboundRepository;
        this.panelService = panelService;
        this.clientRepository = clientRepository;
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
        addClientsRelatedToSubscription(subscriptionEntities);
        return subscriptionEntities.stream().map(SubscriptionDto::new).toList();
    }

    @Transactional
    public void addClientsRelatedToSubscription(List<SubscriptionEntity> entities) throws Exception {
        List<InboundEntity> inbounds = inboundRepository.findAllByGeneratable(true);
        List<ClientEntity> clientEntities = new ArrayList<>();
        for (InboundEntity inbound : inbounds) {
            List<ClientModel> clientModels = new ArrayList<>();
            for (SubscriptionEntity subscription : entities) {
                ClientModel clientModel = ClientModel.builder()
                        .id(UUID.randomUUID().toString())
                        .email(inbound.getRemark() + "-" + subscription.getTotalFlow() + "GB-" + new Random().nextLong())
                        .flow("")
                        .totalGb(subscription.getTotalFlow())
                        .expiryTime(0L)
                        .enable(true)
                        .tgId("")
                        .subId("")
                        .limitIp(1)
                        .enable(true)
                        .build();
                clientModels.add(clientModel);
                ClientEntity clientEntity = clientModel.toEntity();
                clientEntity.setInboundId(inbound.getId());
                clientEntity.setSubscriptionId(subscription.getId());
                clientEntities.add(clientEntity);

            }
            ResponseEntity<ResponseModel> response = panelService.addClient(clientModels, new ServerDto(inbound.getServer()), inbound.getId());
            if (!response.getBody().getSuccess()) {
                throw new Exception(response.getBody().getMsg());
            }
        }
        clientRepository.saveAll(clientEntities);
    }

    public SubscriptionDto update(Long id, SubscriptionRequest request, SubscriptionUpdateType updateType) {
        switch (updateType) {
            case Edit:
                return null;
            case ReNew:
                return null;
        }
        return null;
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

