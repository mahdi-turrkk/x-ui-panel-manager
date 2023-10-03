package online.gixmetir.xuipanelmanagerbackend.bootstrap;

import online.gixmetir.xuipanelmanagerbackend.models.Role;
import online.gixmetir.xuipanelmanagerbackend.models.UserRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.AuthenticationRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.services.app.InboundService;
import online.gixmetir.xuipanelmanagerbackend.services.app.ServerService;
import online.gixmetir.xuipanelmanagerbackend.services.app.SubscriptionService;
import online.gixmetir.xuipanelmanagerbackend.services.app.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationRunner {
    private final UserService userService;
    private final AuthenticationRepository authenticationRepository;
    private final ServerService serverService;
    private final ServerRepository serverRepository;
    private final InboundService inboundService;
    private final SubscriptionService subscriptionService;
    private final InboundRepository inboundRepository;

    public Bootstrap(UserService userService, AuthenticationRepository authenticationRepository, ServerService serverService, ServerRepository serverRepository, InboundService inboundService, SubscriptionService subscriptionService, InboundRepository inboundRepository) {
        this.userService = userService;
        this.authenticationRepository = authenticationRepository;
        this.serverService = serverService;
        this.serverRepository = serverRepository;
        this.inboundService = inboundService;
        this.subscriptionService = subscriptionService;
        this.inboundRepository = inboundRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (authenticationRepository.findByUsername("HUSYN.CF").orElse(null) != null) return;

        bootstrapServer();
        bootstrapUser();
        bootstrapInbound();
        bootstrapSubscription();

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
                .firstName("Hossein")
                .lastName("Shakeri")
                .address("Tabriz")
                .email("gixmetir@bk.ru")
                .phoneNumber("09149570548")
                .username("HUSYN.CF")
                .password("6230064227HUSYN.CF")
                .role(Role.Admin)
                .isIndefiniteFlow(true)
                .isIndefiniteExpirationTime(true)
                .enabled(true)
                .build();
        UserRequest request1 = UserRequest.builder()
                .firstName("Mahdi")
                .lastName("Cahvoshi")
                .address("Tabriz")
                .email("gixmetir@bk.ru")
                .phoneNumber("09149570548")
                .username("MAHDI")
                .password("MAHDITURK")
                .role(Role.Admin)
                .isIndefiniteFlow(true)
                .isIndefiniteExpirationTime(true)
                .enabled(true)
                .build();
        userService.create(request);
        userService.create(request1);
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
}
