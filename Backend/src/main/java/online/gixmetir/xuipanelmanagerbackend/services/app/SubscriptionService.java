package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionFilter;
import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionRenewLogFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.*;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final InboundRepository inboundRepository;
    private final PanelService panelService;
    private final ClientRepository clientRepository;
    private final SubscriptionRenewLogRepository subscriptionReNewLogRepository;
    private final ClientService clientService;
    private final UserRepository userRepository;
    private PlanRepository planRepository;

    public SubscriptionService(SubscriptionRepository repository, InboundRepository inboundRepository, PanelService panelService, ClientRepository clientRepository, SubscriptionRenewLogRepository subscriptionReNewLogRepository, ClientService clientService, UserRepository userRepository, PlanRepository planRepository) {
        this.subscriptionRepository = repository;
        this.inboundRepository = inboundRepository;
        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.subscriptionReNewLogRepository = subscriptionReNewLogRepository;
        this.clientService = clientService;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Transactional
    public List<SubscriptionDto> createSubscription(SubscriptionRequest request) throws Exception {
        List<SubscriptionEntity> subscriptionEntities = new ArrayList<>();
        UserEntity userEntity = new Helper().getUserFromContext();
        request.setUserId(userEntity.getId());
        for (int i = 0; i < request.getNumberSubscriptionsToGenerate(); i++) {
            SubscriptionEntity entity = request.toEntity();
            entity.setUuid(UUID.randomUUID().toString());
            entity.setStatus(true);
            subscriptionEntities.add(entity);
            PlanEntity planEntity = getPriceOfSubscription(entity.getTotalFlow(), entity.getPeriodLength());
            createLog(entity, request, planEntity, SubscriptionLogType.CREATE);
//            entity.setPrice(planEntity.getPrice());
            // increase user total used
            long totalUsed = (userEntity.getTotalUsed() == null ? 0 : userEntity.getTotalUsed()) + entity.getTotalFlow();
            userEntity.setTotalUsed(totalUsed);
        }

        subscriptionRepository.saveAll(subscriptionEntities);
        userRepository.save(userEntity);
        addOrUpdateClientsRelatedToSubscription(subscriptionEntities);
        return subscriptionEntities.stream().map(SubscriptionDto::new).toList();
    }

    private PlanEntity getPriceOfSubscription(Long totalFlow, Integer periodLength) {
        double flow = new Helper().byteToGB(totalFlow);
        return planRepository.findByTotalFlowAndPeriodLength((long) flow, periodLength).orElseThrow(() -> new EntityNotFoundException("Plan not found"));
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
        PlanEntity planEntity = getPriceOfSubscription(request.getTotalFlow(), request.getPeriodLength());

        if (Objects.requireNonNull(updateType) == SubscriptionUpdateType.ReNew) {
            reNewSubscription(subscriptionEntityFromDb, request);

            createLog(subscriptionEntityFromDb, request, planEntity, SubscriptionLogType.RENEW);

        } else {
            request.toEntity(subscriptionEntityFromDb);
//            subscriptionEntityFromDb.setPrice(planEntity.getPrice());
        }

        subscriptionRepository.save(subscriptionEntityFromDb);
        return new SubscriptionDto(subscriptionEntityFromDb);
    }

    private void createLog(SubscriptionEntity subscriptionEntityFromDb, SubscriptionRequest request, PlanEntity planEntity, SubscriptionLogType logType) {
        SubscriptionLogEntity logEntity = SubscriptionLogEntity.builder()
                .subscriptionId(subscriptionEntityFromDb.getId())
                .periodLength(request.getPeriodLength())
                .totalFlow(request.getTotalFlow())
                .price(planEntity.getPrice())
                .logType(logType)
                .markAsPaid(false)
                .build();

        subscriptionReNewLogRepository.save(logEntity);
    }

    /*
    this method do renew action for subscription
    at first it will increase expire date of subscription
    then it will increase total used of user
    then it will enable the clients if that are disabled
     */
    private void reNewSubscription(SubscriptionEntity subscription, SubscriptionRequest request) throws Exception {
        subscription.setPeriodLength(request.getPeriodLength());
        // increase user total user when renew subscription
        UserEntity userEntity = userRepository.findById(subscription.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userEntity.setTotalUsed(userEntity.getTotalUsed() + request.getTotalFlow());

        if (subscription.getExpireDate() != null) {
            if (subscription.getExpireDate().isBefore(LocalDateTime.now())) {
                subscription.setTotalUsed(subscription.getTotalUsed() + request.getTotalFlow());
                subscription.setExpireDate(LocalDateTime.now().plusDays(request.getPeriodLength()));
            } else {
                if (subscription.getTotalUsed() >= subscription.getTotalFlow()) {
                    subscription.setExpireDate(LocalDateTime.now().plusDays(request.getPeriodLength()));
                } else
                    subscription.setExpireDate(subscription.getExpireDate().plusDays(request.getPeriodLength()));
            }
        }
        subscription.setTotalFlow(subscription.getTotalFlow() + new Helper().GBToByte(request.getTotalFlow()));
        subscription.setPeriodLength(subscription.getPeriodLength() + request.getPeriodLength());

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
        String uuid = "";
        Helper helper = new Helper();
        SubscriptionEntity entity = null;
        if (subLink.contains("vless") || subLink.contains("vmess")) {
            uuid = helper.extractUuidFromConfig(subLink);
            ClientEntity clientEntity = clientRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("Client not found"));
            entity = clientEntity.getSubscription();
        } else {
            uuid = helper.extractUuidFromSubscriptionLink(subLink);
            entity = subscriptionRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("Subscription with uuid: " + subLink + " non found"));
        }
        UserEntity userEntity = new Helper().getUserFromContext();
        if (userEntity.getId() == entity.getUserId() || userEntity.getRole() == Role.Admin)
            return new SubscriptionDto(entity);
        if (userEntity.getId() != entity.getUserId())
            throw new Exception("doesnt access to this method.");
        throw new Exception("link is invalid.");
    }

    public ResponseEntity<String> getSubscriptionData(String uuid) throws Exception {
        SubscriptionEntity subscription = subscriptionRepository.findByUuid(uuid).orElseThrow(() -> new Exception("subscription not found"));
        List<ClientEntity> entities = clientRepository.findAllBySubscriptionIdAndSendToUser(subscription.getId(), true);
        StringBuilder configs = new StringBuilder();
        Helper helper = new Helper();
        double remainingFlow = helper.byteToGB((subscription.getTotalFlow() == null ? 0 : subscription.getTotalFlow()) - (subscription.getTotalUsed() == null ? 0 : subscription.getTotalUsed()));
        long days = 0;
        if (subscription.getExpireDate() != null)
            days = ChronoUnit.DAYS.between(subscription.getExpireDate(), LocalDateTime.now());
        else
            days = subscription.getPeriodLength();

        for (ClientEntity entity : entities) {
            configs.append(clientService.generateClientString(entity, days, remainingFlow)).append("\r\n");
        }
        HttpHeaders headers = new HttpHeaders();
        String header = "";
        if (subscription.getUpload() != null)
            header += "upload=" + subscription.getUpload() + "; ";
        if (subscription.getDownload() != null)
            header += "download=" + subscription.getDownload() + "; ";
        if (subscription.getTotalFlow() != null)
            header += "total=" + subscription.getTotalFlow() + "; ";
        if (subscription.getExpireDate() != null)
            header += "expire=" + subscription.getExpireDate().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Subscription-Userinfo", header);

        // Create the response entity with headers, body, and status code
        ResponseEntity<String> responseEntity = new ResponseEntity<>(configs.toString(),
                headers, HttpStatus.OK);

        return responseEntity;
    }

    public SummaryModel getSummary() {
        SummaryModel model = new SummaryModel();
        Helper helper = new Helper();

        LocalDateTime currentDateTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime startOfMonth = currentDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);


        model.setTotalDownload(helper.byteToGB(subscriptionRepository.getTotalDownload()));
        model.setTotalUpload(helper.byteToGB(subscriptionRepository.getTotalUpload()));
        model.setTotalSold(subscriptionRepository.getNumberOfAllSubscriptions());
        model.setTotalLastMonthSold(subscriptionRepository.getNumberOfSubscriptionsCreatedLastMonth(startOfMonth));
        return model;
    }

    private void syncMarkAsPaidForSubscription(SubscriptionEntity subscription) {
        List<SubscriptionLogEntity> subscriptionLogEntities = subscriptionReNewLogRepository.findAllBySubscriptionId(subscription.getId());
        List<SubscriptionLogEntity> entities = subscriptionLogEntities.stream().filter(a -> !a.getMarkAsPaid()).toList();
        subscription.setMarkAsPaid(entities.isEmpty());
        subscriptionRepository.save(subscription);
    }

    public SubscriptionRenewDto changePayStatusForSubscriptionRenewLog(Boolean newPayStatus, Long id) {
        SubscriptionLogEntity subscriptionRenewLogEntity = subscriptionReNewLogRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription renew not found"));
        subscriptionRenewLogEntity.setMarkAsPaid(newPayStatus);
        subscriptionReNewLogRepository.save(subscriptionRenewLogEntity);
        syncMarkAsPaidForSubscription(subscriptionRenewLogEntity.getSubscription());
        return new SubscriptionRenewDto(subscriptionRenewLogEntity);
    }

    public Page<SubscriptionRenewDto> getAllSubscriptionLogList(SubscriptionRenewLogFilter filter, Pageable pageable) {
        return subscriptionReNewLogRepository.findAll(filter, pageable).map(SubscriptionRenewDto::new);
    }


    public SubscriptionDto changePayStatusForSubscription(Boolean newPayStatus, Long id) {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        subscriptionEntity.setMarkAsPaid(newPayStatus);
        subscriptionRepository.save(subscriptionEntity);

        List<SubscriptionLogEntity> subscriptionLogEntities = subscriptionReNewLogRepository.findAllBySubscriptionId(id);
        subscriptionLogEntities.forEach(a -> a.setMarkAsPaid(newPayStatus));
        subscriptionReNewLogRepository.saveAll(subscriptionLogEntities);
        return new SubscriptionDto(subscriptionEntity);
    }

//    public void createRenewLogForAllSubscriptions() {
//
//
//        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAll();
//        List<SubscriptionLogEntity> subscriptionLogEntities = new ArrayList<>(List.of());
//        subscriptionEntities.forEach(a -> {
//            if (a.getId() == 7 || a.getId() == 34) {
//
//            } else {
//                PlanEntity planEntity = getPriceOfSubscription(a.getTotalFlow(), a.getPeriodLength());
//                SubscriptionLogEntity logEntity = SubscriptionLogEntity.builder()
//                        .subscriptionId(a.getId())
//                        .periodLength(a.getPeriodLength())
//                        .totalFlow(a.getTotalFlow())
//                        .price(planEntity.getPrice())
//                        .logType(SubscriptionLogType.CREATE)
//                        .markAsPaid(false)
//                        .build();
//                subscriptionLogEntities.add(logEntity);
//            }
//        });
//        subscriptionReNewLogRepository.saveAll(subscriptionLogEntities);
//    }
}

