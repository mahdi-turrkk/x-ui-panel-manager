package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientStatsModel;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.repositories.ClientRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.SubscriptionRepository;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SyncService {
    private final SubscriptionRepository subscriptionRepository;
    private final ServerRepository serverRepository;
    private final PanelService panelService;
    private final ClientRepository clientRepository;
    private final InboundRepository inboundRepository;
    private final SubscriptionService subscriptionService;

    public SyncService(SubscriptionRepository subscriptionRepository, ServerRepository serverRepository, PanelService panelService, ClientRepository clientRepository, InboundRepository inboundRepository, SubscriptionService subscriptionService) {
        this.subscriptionRepository = subscriptionRepository;
        this.serverRepository = serverRepository;

        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.inboundRepository = inboundRepository;
        this.subscriptionService = subscriptionService;
    }

    public void expiration() throws Exception {
        List<SubscriptionEntity> expiredSubs = subscriptionRepository.getAllExpiredSubscriptions(LocalDateTime.now(), true);
        expiredSubs.forEach(a -> a.setStatus(false));
        subscriptionService.addOrUpdateClientsRelatedToSubscription(expiredSubs);
        subscriptionRepository.saveAll(expiredSubs);
    }

    public void syncWithPanels() throws Exception {
        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAll();
        subscriptionEntities.forEach(a -> {
            a.setUpload(0L);
            a.setDownload(0L);
            a.setTotalUsed(0L);
        });
        List<ServerEntity> serverEntities = serverRepository.findAll();
        for (ServerEntity serverEntity : serverEntities) {
            String sessionKey = panelService.login(new ServerDto(serverEntity));
            List<InboundEntity> inbounds = inboundRepository.findByServerId(serverEntity.getId());
            for (InboundEntity inboundEntity : inbounds) {
                List<ClientEntity> clientEntities = clientRepository.findAllByInboundId(inboundEntity.getId());
                for (ClientEntity clientEntity : clientEntities) {
                    ClientStatsModel model = panelService.clientLog(clientEntity, sessionKey);
                    clientEntity.setUp(Long.parseLong(model.getUp()));
                    clientEntity.setDown(Long.parseLong(model.getDown()));
                    clientEntity.setTotalUsed(clientEntity.getUp() + clientEntity.getDown());
                    SubscriptionEntity subscriptionEntity = subscriptionEntities.stream().filter(a -> a.getId() == clientEntity.getSubscriptionId()).toList().get(0);
                    subscriptionEntity.setUpload(subscriptionEntity.getUpload() + clientEntity.getUp());
                    subscriptionEntity.setDownload(subscriptionEntity.getDownload() + clientEntity.getDown());
                    subscriptionEntity.setTotalUsed(subscriptionEntity.getTotalUsed() + clientEntity.getTotalUsed());
                    setExpirationDateToSubscription(subscriptionEntity);
                }
                clientRepository.saveAll(clientEntities);
            }
        }
        subscriptionRepository.saveAll(subscriptionEntities);
    }

    private void setExpirationDateToSubscription(SubscriptionEntity entity) {
        if (entity.getTotalUsed() > 1024 && entity.getExpireDate() == null && entity.getStartDate() == null) {
            entity.setStartDate(LocalDateTime.now());
            entity.setExpireDate(LocalDateTime.now().plusDays(entity.getPeriodLength()));
        }
    }
}
