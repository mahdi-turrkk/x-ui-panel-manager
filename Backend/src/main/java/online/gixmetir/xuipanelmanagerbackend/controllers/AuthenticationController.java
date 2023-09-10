package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.models.AuthDto;
import online.gixmetir.xuipanelmanagerbackend.services.app.AuthenticationService;
import online.gixmetir.xuipanelmanagerbackend.services.app.UserService;
import org.hibernate.mapping.Any;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final UserService service;

    public AuthenticationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginModel loginModel) {
        return ResponseEntity.ok(service.login(loginModel));
    }

}
