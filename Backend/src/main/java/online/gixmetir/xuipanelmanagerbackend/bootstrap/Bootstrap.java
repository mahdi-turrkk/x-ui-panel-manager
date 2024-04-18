package online.gixmetir.xuipanelmanagerbackend.bootstrap;

import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.AuthenticationRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.GlobalSettingRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.services.app.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Bootstrap implements ApplicationRunner {
    private final UserService userService;
    private final AuthenticationRepository authenticationRepository;
    private final ServerService serverService;
    private final ServerRepository serverRepository;
    private final InboundService inboundService;
    private final SubscriptionService subscriptionService;
    private final InboundRepository inboundRepository;
    private final PlanService planService;
    private final GlobalSettingService globalSettingService;
    private final GlobalSettingRepository globalSettingRepository;

    public Bootstrap(UserService userService, AuthenticationRepository authenticationRepository, ServerService serverService, ServerRepository serverRepository, InboundService inboundService, SubscriptionService subscriptionService, InboundRepository inboundRepository, PlanService planService, GlobalSettingService globalSettingService, GlobalSettingRepository globalSettingRepository) {
        this.userService = userService;
        this.authenticationRepository = authenticationRepository;
        this.serverService = serverService;
        this.serverRepository = serverRepository;
        this.inboundService = inboundService;
        this.subscriptionService = subscriptionService;
        this.inboundRepository = inboundRepository;
        this.planService = planService;
        this.globalSettingService = globalSettingService;
        this.globalSettingRepository = globalSettingRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bootstrapSetting();
        if (authenticationRepository.findByUsername("ADMIN@ADMIN").orElse(null) != null) return;

        bootstrapServer();
        bootstrapUser();
        bootstrapInbound();
        bootstrapSubscription();
        bootstrapPlans();

    }

    private void bootstrapInbound() throws Exception {
//        inboundService.loadAllInboundsFromPanels();
//        InboundEntity entity = inboundRepository.findById(1L).orElse(null);
//        if (entity != null)
//            entity.setGeneratable(true);
//        inboundRepository.save(entity);
//        InboundEntity entity1 = inboundRepository.findById(3L).orElse(null);
//        if (entity1 != null)
//            entity1.setGeneratable(true);
//        inboundRepository.save(entity1);

    }

    private void bootstrapPlans() throws Exception {
//        PlanRequest planRequest = PlanRequest.builder()
//                .periodLength(30)
//                .totalFlow(30L)
//                .price(90000000D)
//                .build();
//        planService.create(planRequest);
    }

    private void bootstrapServer() throws Exception {
//        ServerEntity entity = serverRepository.findByUrl("https://test.gixmetir.online:9090/").orElse(null);
//        if (entity != null) return;
//        serverService.create(
//                ServerRequest.builder()
//                        .generatable(true)
//                        .status(true)
//                        .password("w~#4!x}kd_@Ng*}T3r,VBdyZ7J-VRiVZ#}")
//                        .username("husyn.cf")
//                        .url("https://account.gixmetir.online:9090/")
//                        .build()
//        );
    }

    private void bootstrapUser() throws Exception {
        UserRequest request = UserRequest.builder()
                .firstName("ADMIN")
                .lastName("ADMIN")
                .address("Tabriz")
                .email("gixmetir@bk.ru")
                .phoneNumber("0914444444")
                .username("ADMIN@ADMIN")
                .password("ADMIN@admin")
                .role(Role.Admin)
                .isIndefiniteFlow(true)
                .isIndefiniteExpirationTime(true)
                .enabled(true)
                .build();

        userService.create(request);
    }

    private void bootstrapSubscription() throws Exception {
//        subscriptionService.createSubscription(SubscriptionRequest.builder()
//                .periodLength(10)
//                .numberSubscriptionsToGenerate(1)
//                .userId(1L)
//                .title("test")
//                .totalFlow(20L)
//                .build());
    }

    private void bootstrapSetting() {
        if (!globalSettingRepository.findAll().isEmpty()) return;
        GlobalSettingRequest request = GlobalSettingRequest.builder()
                .subscriptionUrl("https://panel.privado-vpn.online:5001/")
                .osSettingRequests(List.of(
                                OsSettingRequest.builder()
                                        .os(OS.ANDROID)
                                        .clients("v2rayng")
                                        .applyFragment(false)
                                        .fragmentInterval("10-20")
                                        .fragmentLength("10-20")
                                        .packets("tlshello")
                                        .generateJson(false)
                                        .generateV2rayLink(true)
                                        .build(),
                                OsSettingRequest.builder()
                                        .os(OS.IOS)
                                        .clients("streisand,v2box")
                                        .applyFragment(true)
                                        .fragmentInterval("10-20")
                                        .fragmentLength("10-20")
                                        .packets("tlshello")
                                        .generateJson(false)
                                        .generateV2rayLink(true)
                                        .build(),
                                OsSettingRequest.builder()
                                        .os(OS.WINDOWS)
                                        .clients("v2rayn,nekoray")
                                        .applyFragment(false)
                                        .fragmentInterval("10-20")
                                        .fragmentLength("10-20")
                                        .packets("tlshello")
                                        .generateJson(false)
                                        .generateV2rayLink(true)
                                        .build(),
                                OsSettingRequest.builder()
                                        .os(OS.LINUX)
                                        .clients("nekoray")
                                        .applyFragment(false)
                                        .fragmentInterval("10-20")
                                        .packets("tlshello")
                                        .fragmentLength("10-20")
                                        .generateJson(false)
                                        .generateV2rayLink(true)
                                        .build()
                        )
                )
                .build();
        globalSettingService.save(request);
    }
}
