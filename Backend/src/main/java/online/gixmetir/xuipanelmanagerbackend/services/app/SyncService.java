package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientStatsModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.InboundModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.repositories.*;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class SyncService {
    private final SubscriptionRepository subscriptionRepository;
    private final ServerRepository serverRepository;
    private final PanelService panelService;
    private final ClientRepository clientRepository;
    private final InboundRepository inboundRepository;
    private final UserRepository userRepository;

    public SyncService(SubscriptionRepository subscriptionRepository, ServerRepository serverRepository, PanelService panelService, ClientRepository clientRepository, InboundRepository inboundRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.serverRepository = serverRepository;

        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.inboundRepository = inboundRepository;
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
//        subscriptionService.addOrUpdateClientsRelatedToSubscription(expiredSubs);
        // delete related client from x-ui panels and database
        List<ClientEntity> clientEntities = clientRepository.findAllBySubscriptionIdIn(expiredSubs.stream().map(SubscriptionEntity::getId).toList());
        panelService.deleteClients(clientEntities);
        clientRepository.deleteAll(clientEntities);
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

        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAllByStatus(true);
        List<ServerEntity> serverEntities = serverRepository.findAll();
        for (ServerEntity serverEntity : serverEntities) {
            String sessionKey = "";
            if (serverEntity.getIsDeleted() == null || !serverEntity.getIsDeleted() && serverEntity.getStatus())
                sessionKey = panelService.login(new ServerDto(serverEntity));
            else continue;
            List<InboundEntity> inbounds = inboundRepository.findByServerIdAndGeneratable(serverEntity.getId(), true);
            //  get all clients from x-ui panel
            List<InboundModel> inboundModels = List.of(panelService.loadAllInboundsFromXuiPanel(new ServerDto(serverEntity), sessionKey));
            //panelService.resetInboundTraffic(50L, new ServerDto(serverEntity));
            for (InboundEntity inboundEntity : inbounds) {
                List<ClientEntity> clientEntities = clientRepository.findAllByInboundId(inboundEntity.getId());
                InboundModel inboundModel = inboundModels.stream().filter(a -> a.getId().equals(inboundEntity.getIdFromPanel())).findFirst().orElse(null);
                if (inboundModel == null) continue;

                for (ClientEntity clientEntity : clientEntities) {
                    //  get all clients related to inbound from x-ui panel
                    ClientStatsModel clientModel = Arrays.stream(inboundModel.getClientStats()).filter(a -> a.getEmail().equals(clientEntity.getEmail())).findFirst().orElse(null);
                    if (clientModel == null) continue;

                    List<SubscriptionEntity> list = subscriptionEntities.stream().filter(a -> Objects.equals(a.getId(), clientEntity.getSubscriptionId())).toList();
                    if (list.isEmpty()) continue;
                    SubscriptionEntity subscriptionEntity = list.get(0);
                    subscriptionEntity.setUpload((subscriptionEntity.getUpload() == null ? 0 : subscriptionEntity.getUpload()) + (Long.parseLong(clientModel.getUp() == null ? "0" : clientModel.getUp())));
                    subscriptionEntity.setDownload((subscriptionEntity.getDownload() == null ? 0 : subscriptionEntity.getDownload()) + (Long.parseLong(clientModel.getDown() == null ? "0" : clientModel.getDown())));
                    subscriptionEntity.setTotalUsed(subscriptionEntity.getUpload() + subscriptionEntity.getDownload());
                    setExpirationDateToSubscription(subscriptionEntity);
                }
                clientRepository.saveAll(clientEntities);
                // reset all clients traffic related to inbound
                panelService.resetInboundTraffic(inboundEntity.getIdFromPanel(), new ServerDto(serverEntity));
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
