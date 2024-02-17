package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.models.OsSettingDto;
import online.gixmetir.xuipanelmanagerbackend.models.OsSettingRequest;
import online.gixmetir.xuipanelmanagerbackend.services.app.OsSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/os-setting")
@CrossOrigin("*")
public class OsSettingController {

    private final OsSettingService service;

    public OsSettingController(OsSettingService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<OsSettingDto> save(@RequestBody OsSettingRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/update")
    public ResponseEntity<OsSettingDto> update(@RequestParam Long id, @RequestBody OsSettingRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

}
