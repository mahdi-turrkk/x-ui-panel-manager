package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.filters.UserFilter;
import online.gixmetir.xuipanelmanagerbackend.filters.UserPaymentLogFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.services.app.UserPaymentLogService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-payment-logs")

public class UserPaymentLogController {
    private final UserPaymentLogService service;

    public UserPaymentLogController(UserPaymentLogService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public void duePayment(@RequestBody UserPaymentLogRequest request) throws Exception {
        service.create(request);
    }

    @GetMapping("/get-all")
    public Page<UserPaymentLogDto> getAll(UserPaymentLogFilter filter, Pageable pageable) {
        return service.getAll(filter, pageable);
    }


    @PutMapping("/update")
    public ResponseEntity<UserPaymentLogDto> update(@RequestParam Long id, @RequestBody UserPaymentLogRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }


    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        service.delete(id);
    }

}
