package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.models.ServerRequest;
import online.gixmetir.xuipanelmanagerbackend.services.app.ServerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/servers")

public class ServerController {
    private final ServerService service;


    public ServerController(ServerService service) {
        this.service = service;
    }

    @GetMapping("/get-all")
    public Page<ServerDto> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/sync-subscriptions-with-servers")
    public void syncSubscriptionsWithServers() throws Exception {

        service.syncSubscriptionsWithServers();
    }

    @PostMapping("/create")
    public ResponseEntity<ServerDto> create(@RequestBody ServerRequest request) throws Exception {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/update")
    public ResponseEntity<ServerDto> update(@RequestParam Long id, @RequestBody ServerRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        service.delete(id);
    }

    @PutMapping("/change-status")
    public ServerDto changeStatus(@RequestParam Boolean newStatus, @RequestParam Long id) {
        return service.changeStatus(newStatus, id);
    }
}
