package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.models.PricingDto;
import online.gixmetir.xuipanelmanagerbackend.models.PricingRequest;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.models.ServerRequest;
import online.gixmetir.xuipanelmanagerbackend.services.app.PricingService;
import online.gixmetir.xuipanelmanagerbackend.services.app.ServerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pricing")

public class PricingController {
    private final PricingService service;


    public PricingController(PricingService service) {
        this.service = service;
    }

    @GetMapping("/get-all")
    public Page<PricingDto> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping("/create")
    public ResponseEntity<PricingDto> create(@RequestBody PricingRequest request) throws Exception {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/update")
    public ResponseEntity<PricingDto> update(@RequestParam Long id, @RequestBody PricingRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        service.delete(id);
    }
}
