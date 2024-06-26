package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ClientModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.exceptions.CustomException;
import online.gixmetir.xuipanelmanagerbackend.exceptions.ForbiddenException;
import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionFilter;
import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionRenewLogFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.*;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    @Value("${app.url}")
    private String appUrl;
    private final OsSettingRepository osSettingRepository;

    public SubscriptionService(SubscriptionRepository repository, InboundRepository inboundRepository, PanelService panelService, ClientRepository clientRepository, SubscriptionRenewLogRepository subscriptionReNewLogRepository, ClientService clientService, UserRepository userRepository, PlanRepository planRepository, OsSettingRepository osSettingRepository) {
        this.subscriptionRepository = repository;
        this.inboundRepository = inboundRepository;
        this.panelService = panelService;
        this.clientRepository = clientRepository;
        this.subscriptionReNewLogRepository = subscriptionReNewLogRepository;
        this.clientService = clientService;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.osSettingRepository = osSettingRepository;
    }

    @Transactional
    public List<SubscriptionDto> createSubscription(SubscriptionRequest request) throws Exception {
        List<SubscriptionEntity> subscriptionEntities = new ArrayList<>();
        UserEntity userEntity = new Helper().getUserFromContext();
        boolean hasAccess = isUserAllowToCreateSubscription(userEntity, request);
        if (!hasAccess)
            throw new ForbiddenException("user is not allowed to create subscription.");
        request.setUserId(userEntity.getId());
        for (int i = 0; i < request.getNumberSubscriptionsToGenerate(); i++) {
            SubscriptionEntity entity = request.toEntity();
            entity.setUuid(UUID.randomUUID().toString());
            entity.setStatus(true);
            subscriptionEntities.add(entity);
            // increase user total used
            long totalUsed = (userEntity.getTotalUsed() == null ? 0 : userEntity.getTotalUsed()) + entity.getTotalFlow();
            userEntity.setTotalUsed(totalUsed);
        }

        subscriptionRepository.saveAll(subscriptionEntities);
        subscriptionEntities.forEach(a -> {
            if (userEntity.getRole() == Role.SuperCustomer || userEntity.getRole() == Role.Admin) {
                double price = userEntity.getPricePerGb() == null ? 0 : userEntity.getPricePerGb();

                createLog(a, request, PlanEntity.builder().price(price * request.getTotalFlow()).build(), SubscriptionLogType.CREATE, userEntity);
            } else {
                PlanEntity planEntity = getPriceOfSubscription((long) new Helper().byteToGB(a.getTotalFlow()), a.getPeriodLength());
                createLog(a, request, planEntity, SubscriptionLogType.CREATE, userEntity);
            }

        });
        userRepository.save(userEntity);
        addOrUpdateClientsRelatedToSubscription(subscriptionEntities);
        return subscriptionEntities.stream().map(SubscriptionDto::new).toList();
    }

    private boolean isUserAllowToCreateSubscription(UserEntity userEntity, SubscriptionRequest request) {

        Helper helper = new Helper();
        Long totalFlow = userEntity.getTotalFlow() == null ? 0 : userEntity.getTotalFlow();
        Long totalUsed = userEntity.getTotalUsed() == null ? 0 : userEntity.getTotalUsed();
        long remainingFlow = totalFlow - totalUsed;
        long requestedFlow = helper.GBToByte(request.getTotalFlow() * request.getNumberSubscriptionsToGenerate());
        if (userEntity.getRole() == Role.SuperCustomer) {
            if (userEntity.getIsIndefiniteExpirationTime()) {
                if (userEntity.getIsIndefiniteFlow()) {
                    return true;
                } else {
                    return remainingFlow >= requestedFlow;
                }
            } else {
                if (userEntity.getIsIndefiniteFlow()) {
                    return userEntity.getExpirationDateTime().isAfter(LocalDateTime.now());
                } else {
                    return remainingFlow >= requestedFlow && userEntity.getExpirationDateTime().isAfter(LocalDateTime.now());
                }
            }
        }
        return true;
    }

    private PlanEntity getPriceOfSubscription(Long totalFlow, Integer periodLength) {
        return planRepository.findByTotalFlowAndPeriodLength(totalFlow, periodLength).orElseThrow(() -> new EntityNotFoundException("Plan not found"));
    }

    @Transactional
    public void addOrUpdateClientsRelatedToSubscription(List<SubscriptionEntity> subEntities) throws Exception {
        List<InboundEntity> inboundEntities = inboundRepository.findAllByGeneratable(true);
        addOrUpdateClientsRelatedToSubscriptionCore(subEntities, inboundEntities);
    }

    public void addOrUpdateClientsRelatedToSubscriptionCore(List<SubscriptionEntity> subEntities, List<InboundEntity> inboundEntities) throws Exception {
        List<ClientEntity> clientEntitiesToSaveInDb = new ArrayList<>();
        for (InboundEntity inbound : inboundEntities) {
            List<ClientModel> clientModelsAddToPanel = new ArrayList<>();
            List<ClientEntity> clientEntitiesUpdateInPanel = new ArrayList<>();
            for (SubscriptionEntity subscription : subEntities) {
                ClientEntity clientEntity = null;
                ClientEntity clientEntityFromDb = clientRepository.findByInboundIdAndSubscriptionId(inbound.getId(), subscription.getId());
                if (clientEntityFromDb != null) {
                    // این جا متغیر flag را قرار دادم تا اگر وضعیت کانفیگ تغییر نکند درخواست اضافه ای به سرور ها ارسال نشود
                    boolean flag = clientEntityFromDb.getEnable() == subscription.getStatus();
                    clientEntityFromDb.setEnable(subscription.getStatus());
                    clientEntity = clientEntityFromDb;
                    clientEntity.setSendToUser(true);
                    if (!flag)
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
            if (!clientModelsAddToPanel.isEmpty()) {
                panelService.addClient(clientModelsAddToPanel, new ServerDto(inbound.getServer()), inbound.getIdFromPanel());
            }
            if (!clientEntitiesUpdateInPanel.isEmpty()) {
                panelService.updateClients(clientEntitiesUpdateInPanel);
            }
        }
        clientRepository.saveAll(clientEntitiesToSaveInDb);
    }

    @Transactional
    public SubscriptionDto update(Long id, SubscriptionRequest request, SubscriptionUpdateType updateType) throws Exception {
        SubscriptionEntity subscriptionEntityFromDb = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        UserEntity userEntity = new Helper().getUserFromContext();

        if (Objects.requireNonNull(updateType) == SubscriptionUpdateType.ReNew) {
            reNewSubscription(subscriptionEntityFromDb, request);
            if (userEntity.getRole() == Role.SuperCustomer || userEntity.getRole() == Role.Admin) {
                double price = userEntity.getPricePerGb() == null ? 0 : userEntity.getPricePerGb();
                createLog(subscriptionEntityFromDb, request, PlanEntity.builder().price(price * request.getTotalFlow()).build(), SubscriptionLogType.RENEW, userEntity);
            } else {
                PlanEntity planEntity = getPriceOfSubscription(request.getTotalFlow(), request.getPeriodLength());

                createLog(subscriptionEntityFromDb, request, planEntity, SubscriptionLogType.RENEW, userEntity);
            }
            subscriptionEntityFromDb.setMarkAsPaid(false);
            addOrUpdateClientsRelatedToSubscription(List.of(subscriptionEntityFromDb));
        } else {
            request.toEntity(subscriptionEntityFromDb);
            // todo edit log for edited sub  and log type is create
//            subscriptionEntityFromDb.setPrice(planEntity.getPrice());
        }
        subscriptionRepository.save(subscriptionEntityFromDb);
        return new SubscriptionDto(subscriptionEntityFromDb);
    }

    private void createLog(SubscriptionEntity subscriptionEntityFromDb, SubscriptionRequest request, PlanEntity planEntity, SubscriptionLogType logType, UserEntity user) {
        SubscriptionLogEntity logEntity = SubscriptionLogEntity.builder()
                .subscriptionId(subscriptionEntityFromDb.getId())
                .periodLength(request.getPeriodLength())
                .totalFlow(new Helper().GBToByte(request.getTotalFlow()))
                .price(planEntity.getPrice())
                .logType(logType)
                .markAsPaid(false)
                .build();

        if (user.getRole() == Role.SuperCustomer) {
            double extraPrice = Math.ceil((double) request.getPeriodLength() / 30 - 1) * 10000;
            logEntity.setPrice(logEntity.getPrice() + extraPrice);
        }
        subscriptionReNewLogRepository.save(logEntity);
    }

    /*
    this method do renew action for subscription
    at first it will increase expire date of subscription
    then it will increase total used of user
    then it will enable the clients if that are disabled
     */
    private void reNewSubscription(SubscriptionEntity subscription, SubscriptionRequest request) throws Exception {
        subscription.setPeriodLength(subscription.getPeriodLength() + request.getPeriodLength());
        // increase user total user when renew subscription
        UserEntity userEntity = userRepository.findById(subscription.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Helper helper = new Helper();
        userEntity.setTotalUsed(userEntity.getTotalUsed() + helper.GBToByte(request.getTotalFlow()));

        if (subscription.getExpireDate() != null) {
            if (subscription.getExpireDate().isBefore(LocalDateTime.now())) {
                subscription.setTotalUsed(subscription.getTotalFlow());
                subscription.setExpireDate(LocalDateTime.now().plusDays(request.getPeriodLength()));
            } else {
                if (subscription.getTotalUsed() >= subscription.getTotalFlow()) {
                    subscription.setExpireDate(LocalDateTime.now().plusDays(request.getPeriodLength()));
                } else
                    subscription.setExpireDate(subscription.getExpireDate().plusDays(request.getPeriodLength()));
            }
        }
        subscription.setTotalFlow(subscription.getTotalFlow() + helper.GBToByte(request.getTotalFlow()));
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
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
        UserEntity userEntity = userRepository.findById(subscriptionEntity.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Helper helper = new Helper();
        long notUsedFlow = Math.abs((long) helper.byteToGB(subscriptionEntity.getTotalFlow() - (subscriptionEntity.getTotalUsed() == null ? 0 : subscriptionEntity.getTotalUsed())));
        userEntity.setTotalUsed(userEntity.getTotalUsed() == null ? 0 : userEntity.getTotalUsed() - helper.GBToByte(notUsedFlow));
        subscriptionReNewLogRepository.deleteAllBySubscriptionId(subscriptionEntity.getId());
        subscriptionRepository.delete(subscriptionEntity);
        userRepository.save(userEntity);
    }

    public Page<SubscriptionDto> getAll(SubscriptionFilter filter, Pageable pageable, Boolean selfSubs) throws Exception {
        UserEntity entity = new Helper().getUserFromContext();
        if (entity.getRole() == Role.Customer || entity.getRole() == Role.SuperCustomer) {
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
        if (entity.isExpired() && !entity.getStatus() && newStatus) {
            throw new CustomException("اشتراک منقضی شده است نمیتوان دوباره آن را فعال کرد ");
        }
        entity.setStatus(newStatus);
        //موقعی که اشتراک غیر فعال میشود کلاینت های مربوطه را از پنل ثنایی حذف میکند
        if (!newStatus) {
            List<ClientEntity> clients = clientRepository.findAllBySubscriptionId(id);
            panelService.deleteClients(clients);
            clientRepository.deleteAll(clients);
        } else
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
        if (userEntity.getId() == entity.getUserId() || userEntity.getRole() == Role.Admin || userEntity.getRole() == Role.Bot)
            return new SubscriptionDto(entity);
        if (userEntity.getId() != entity.getUserId())
            throw new ForbiddenException("doesnt access to this method.");
        throw new IllegalArgumentException("link is invalid.");
    }

    public Object getSubscriptionData(String uuid, HttpServletRequest request) throws Exception {
        DeviceValidationModel deviceValidationModel = validateDevice(request);
        SubscriptionEntity subscription = subscriptionRepository.findByUuid(uuid).orElseThrow(() -> new Exception("subscription not found"));
        List<ClientEntity> entities = clientRepository.findAllBySubscriptionIdAndSendToUser(subscription.getId(), true);
        StringBuilder configs = new StringBuilder();
        Object jsonConfigs = null;

        if (deviceValidationModel == null) {
            configs.append(appUrl).append("/api/v1/subscriptions/client/").append(subscription.getUuid()).append("\r\n");
            for (ClientEntity entity : entities) {
                configs.append(clientService.generateFragmentLink(entity)).append("\r\n");
            }
        } else {
            if (deviceValidationModel.isGenerateV2rayLink()) {
                if (deviceValidationModel.isApplyFragment())
                    for (ClientEntity entity : entities) {
                        configs.append(clientService.generateClientString(entity, deviceValidationModel, true)).append("\r\n");
                    }
                for (ClientEntity entity : entities) {
                    configs.append(clientService.generateClientString(entity, deviceValidationModel, false)).append("\r\n");
                }
            } else if (deviceValidationModel.isGenerateJson()) {
                List<Object> objects = new ArrayList<>();
                if (deviceValidationModel.isApplyFragment())
                    for (ClientEntity entity : entities) {
                        objects.add(clientService.generateClientJson(entity, deviceValidationModel, true));
                    }
                for (ClientEntity entity : entities) {
                    objects.add(clientService.generateClientJson(entity, deviceValidationModel, false));
                }
                jsonConfigs = objects;
            }
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
        if (jsonConfigs != null) return jsonConfigs;
        if (deviceValidationModel == null) {
            return generateQrcode(configs);
        } else {
            return configs.toString();
        }

    }

    private String generateQrcode(StringBuilder stringBuilder) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>QR code</title>");
        htmlBuilder.append("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js\"></script>");
        htmlBuilder.append("</head><body>");
        htmlBuilder.append("<div style=\"height: 100dvh;width: 100vw;display: flex;justify-content: center;align-items: center\">");
        htmlBuilder.append("<img id='barcodeImage' src=\"https://api.qrserver.com/v1/create-qr-code/?data=").append(stringBuilder.toString().replace("\r\n", "%0A")).append("&amp;size=500x500\" alt=\"qr code\" width=\"300\" height=\"300\"/>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("<script>");
        htmlBuilder.append("document.querySelector(\"#barcodeImage\").addEventListener('click' , (e)=>{e.preventDefault();copyUrl()});");
        htmlBuilder.append("const copyUrl = () => {let urlToCopy = \"").append(stringBuilder.toString().replace("\r\n", "\\n")).append("\";");
        htmlBuilder.append("navigator.clipboard.writeText(urlToCopy).then(() => {alert('URL copied successfully!');}).catch(err => {alert('Failed to copy URL: ', err);});};");
        htmlBuilder.append("</script></html>");


        return htmlBuilder.toString();
    }

    private DeviceValidationModel validateDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        System.out.println(userAgent);
        userAgent = userAgent.toLowerCase();
        List<OsSettingEntity> osSettingEntities = osSettingRepository.findAll();
        for (OsSettingEntity osSetting : osSettingEntities) {
            List<String> agents = Arrays.stream(osSetting.getClients().split(",")).toList();
            for (String agent : agents) {
                if (userAgent.contains(agent)) {
                    DeviceValidationModel deviceValidationModel = new DeviceValidationModel();
                    deviceValidationModel.setApplyFragment(osSetting.getApplyFragment());
                    deviceValidationModel.setFragmentInterval(osSetting.getFragmentInterval());
                    deviceValidationModel.setFragmentLength(osSetting.getFragmentLength());
                    deviceValidationModel.setGenerateJson(osSetting.getGenerateJson());
                    deviceValidationModel.setGenerateV2rayLink(osSetting.getGenerateV2rayLink());
                    deviceValidationModel.setPackets(osSetting.getPackets());
                    if (agent.equals("v2rayng")) {
                        String version = userAgent.split("/")[1];
                        String[] segments = version.split("\\.");
                        if (!(Integer.parseInt(segments[1]) >= 8 && Integer.parseInt(segments[2]) >= 16)) {
                            deviceValidationModel.setGenerateJson(false);
                            deviceValidationModel.setGenerateV2rayLink(true);
                        }
                    }
                    return deviceValidationModel;
                }
            }

        }
        return null;
//            return !(userAgent.contains("iphone") || userAgent.contains("ios") || userAgent.contains("streisand") || userAgent.contains("v2box") || userAgent.contains("v2rayng") || userAgent.contains("nekoray") || userAgent.contains("v2rayn"));
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
        model.setTotalLastMonthRenew(subscriptionReNewLogRepository.getNumberOfRenewLastMonth(startOfMonth, SubscriptionLogType.RENEW));
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

    public void deleteRelatedClientsForSubscriptions(List<SubscriptionEntity> subscriptionEntities) throws Exception {
        List<ClientEntity> clientEntities = clientRepository.findAllBySubscriptionIdIn(subscriptionEntities.stream().map(SubscriptionEntity::getId).toList());
        panelService.deleteClients(clientEntities);
        clientRepository.deleteAll(clientEntities);
    }

    public ResponseEntity<Object> getFragData(String uuid, HttpServletRequest request) throws IOException {
        DeviceValidationModel deviceValidationModel = validateDevice(request);
        ClientEntity client = clientRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        if (client.getInbound().getStreamSettingsObj().getNetwork().equals("ws")) {
            InboundEntity inbound = client.getInbound();
            switch (inbound.getProtocol()) {
                case "vless":
                    return new ResponseEntity<>(clientService.createVlessJsonObj(client, deviceValidationModel, true), HttpStatus.OK);
                case "vmess":
                    return new ResponseEntity<>(clientService.createVmessJsonObj(client, deviceValidationModel, true), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(clientService.generateClientString(client, deviceValidationModel, true), HttpStatus.OK);
        }
        return null;
    }


}

