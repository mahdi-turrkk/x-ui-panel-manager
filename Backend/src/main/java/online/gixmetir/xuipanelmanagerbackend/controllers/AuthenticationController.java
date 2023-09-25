package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.models.AuthDto;
import online.gixmetir.xuipanelmanagerbackend.models.Role;
import online.gixmetir.xuipanelmanagerbackend.services.app.UserService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
@CrossOrigin("*")
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
    public ResponseEntity<Role> getRole() throws Exception {
        UserEntity entity = new Helper().getUserFromContext();
        return ResponseEntity.ok(entity.getRole());
    }

}
