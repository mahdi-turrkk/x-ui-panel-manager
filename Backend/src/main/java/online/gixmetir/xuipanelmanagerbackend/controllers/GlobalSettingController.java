package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.models.GlobalSettingDto;
import online.gixmetir.xuipanelmanagerbackend.models.GlobalSettingRequest;
import online.gixmetir.xuipanelmanagerbackend.services.app.GlobalSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/global-setting")
@CrossOrigin("*")
public class GlobalSettingController {

    private final GlobalSettingService service;

    public GlobalSettingController(GlobalSettingService service) {

        this.service = service;
    }
    @GetMapping("/get")
    public ResponseEntity<GlobalSettingDto> get() {
        return ResponseEntity.ok(service.get());
    }
    @PostMapping("/save")
    public ResponseEntity<GlobalSettingDto> save(@RequestBody GlobalSettingRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalSettingDto> update(@RequestParam Long id, @RequestBody GlobalSettingRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

}
