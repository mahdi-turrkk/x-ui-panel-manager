package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientStatsModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.repositories.*;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SyncService {
    private final SubscriptionRepository subscriptionRepository;
    private final ServerRepository serverRepository;
    private final PanelService panelService;
    private final ClientRepository clientRepository;
    private final InboundRepository inboundRepository;
    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    public SyncService(SubscriptionRepository subscriptionRepository, ServerRepository serverRepository, PanelService panelService, ClientRepository clientRepository, InboundRepository inboundRepository, SubscriptionService subscriptionService, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.serverRepository = serverRepository;

        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.inboundRepository = inboundRepository;
        this.subscriptionService = subscriptionService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void expiration() throws Exception {
        // get all expired subscriptions
        List<SubscriptionEntity> expiredSubs = subscriptionRepository.getAllExpiredSubscriptions(LocalDateTime.now(), true);
        // disable all expired subscriptions
        expiredSubs.forEach(a -> a.setStatus(false));
        // get all expired users
        List<UserEntity> expiredUsers = userRepository.getAllExpiredUsers(LocalDateTime.now());
        // disable all expired users
        expiredUsers.forEach(a -> a.setEnabled(false));
        userRepository.saveAll(expiredUsers);
        // disable all subscriptions in panels
        subscriptionService.addOrUpdateClientsRelatedToSubscription(expiredSubs);
        subscriptionRepository.saveAll(expiredSubs);
    }

    /*
    this method will sync all subscriptions with panels
    at first it will get all subscriptions from database
    then it will get all servers from database
    then it will get all inbound from database
    then it will get all clients from database
    then it will get client logs (total used) from panel
    finally update clients and subscription (total used , upload, download)
    */
    @Transactional
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
                    clientEntity.setUp(Long.parseLong(model.getUp() == null ? "0" : model.getUp()));
                    clientEntity.setDown(Long.parseLong(model.getDown() == null ? "0" : model.getDown()));
                    clientEntity.setTotalUsed(clientEntity.getUp() + clientEntity.getDown());
                    List<SubscriptionEntity> list = subscriptionEntities.stream().filter(a -> Objects.equals(a.getId(), clientEntity.getSubscriptionId())).toList();
                    SubscriptionEntity subscriptionEntity = list.get(0);
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

    /*
    this method set expiration date to subscription
    if total used is greater than specific number
    this method will set expire date
    */
    private void setExpirationDateToSubscription(SubscriptionEntity entity) {
        if (entity.getTotalUsed() > 1024 && entity.getExpireDate() == null && entity.getStartDate() == null) {
            entity.setStartDate(LocalDateTime.now());
            entity.setExpireDate(LocalDateTime.now().plusDays(entity.getPeriodLength()));
        }
    }
}
