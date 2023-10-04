package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.models.PlanDto;
import online.gixmetir.xuipanelmanagerbackend.models.PlanRequest;
import online.gixmetir.xuipanelmanagerbackend.services.app.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plans")

public class PlanController {
    private final PlanService service;


    public PlanController(PlanService service) {
        this.service = service;
    }

    @GetMapping("/get-all")
    public Page<PlanDto> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping("/create")
    public ResponseEntity<PlanDto> create(@RequestBody PlanRequest request) throws Exception {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/update")
    public ResponseEntity<PlanDto> update(@RequestParam Long id, @RequestBody PlanRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        service.delete(id);
    }
}
