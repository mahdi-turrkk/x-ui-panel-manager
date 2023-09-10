package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.filters.UserFilter;
import online.gixmetir.xuipanelmanagerbackend.models.UserDto;
import online.gixmetir.xuipanelmanagerbackend.models.UserRequest;
import online.gixmetir.xuipanelmanagerbackend.services.app.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/get-all")
    public Page<UserDto> getAll(UserFilter filter, Pageable pageable) {
        return service.getAll(filter, pageable);
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> create(@RequestBody UserRequest request) throws Exception {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> update(@RequestParam Long id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        service.delete(id);
    }
}
