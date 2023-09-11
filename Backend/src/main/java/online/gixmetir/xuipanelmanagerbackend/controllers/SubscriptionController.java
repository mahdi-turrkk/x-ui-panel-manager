package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionFilter;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionDto;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionRequest;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionUpdateType;
import online.gixmetir.xuipanelmanagerbackend.services.app.SubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/create")
    public List<SubscriptionDto> create(@RequestBody SubscriptionRequest request) throws Exception {
        return subscriptionService.createSubscription(request);
    }

    @PutMapping("/update")
    public SubscriptionDto update(@RequestParam Long id, @RequestBody SubscriptionRequest request, @RequestParam SubscriptionUpdateType updateType) {
        return subscriptionService.update(id, request,updateType);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) throws Exception {
        subscriptionService.delete(id);
    }

    @GetMapping("/get-all")
    public Page<SubscriptionDto> getAll(SubscriptionFilter filter,Pageable pageable) {
        return subscriptionService.getAll(filter,pageable);

    }
}
