package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.models.AuthDto;
import online.gixmetir.xuipanelmanagerbackend.models.Role;
import online.gixmetir.xuipanelmanagerbackend.services.app.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-role")
    public Role getRole() {
        UserEntity entity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return entity.getRole();
    }

}
