package online.gixmetir.xuipanelmanagerbackend.bootstrap;

import online.gixmetir.xuipanelmanagerbackend.models.Role;
import online.gixmetir.xuipanelmanagerbackend.models.UserRequest;
import online.gixmetir.xuipanelmanagerbackend.repositories.AuthenticationRepository;
import online.gixmetir.xuipanelmanagerbackend.services.app.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapUser implements ApplicationRunner {
    private final UserService userService;
    private final AuthenticationRepository authenticationRepository;

    public BootstrapUser(UserService userService, AuthenticationRepository authenticationRepository) {
        this.userService = userService;
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (authenticationRepository.findByUsername("ADMIN").orElse(null) != null) return;
        UserRequest request = UserRequest.builder()
                .firstName("Admin")
                .lastName("Admin")
                .address("Tabriz")
                .email("gixmetir@bk.ru")
                .phoneNumber("09149570548")
                .username("ADMIN")
                .password("ADMIN")
                .role(Role.Admin)
                .enabled(true)
                .build();
        UserRequest request1 = UserRequest.builder()
                .firstName("Customer")
                .lastName("Customer")
                .address("Tabriz")
                .email("gixmetir@bk.ru")
                .phoneNumber("09149570548")
                .username("CUSTOMER")
                .password("CUSTOMER")
                .role(Role.Customer)
                .enabled(true)
                .build();
        userService.create(request);
        userService.create(request1);
    }
}
