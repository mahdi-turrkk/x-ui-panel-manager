package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionFilter;
import online.gixmetir.xuipanelmanagerbackend.filters.SubscriptionRenewLogFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.services.app.SubscriptionService;
import online.gixmetir.xuipanelmanagerbackend.services.app.SyncService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")

public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SyncService syncWithPanels;

    public SubscriptionController(SubscriptionService subscriptionService, SyncService syncWithPanels) {
        this.subscriptionService = subscriptionService;
        this.syncWithPanels = syncWithPanels;
    }

    @PostMapping("/create")
    public List<SubscriptionDto> create(@RequestBody SubscriptionRequest request) throws Exception {
        return subscriptionService.createSubscription(request);
    }

    @PutMapping("/update")
    public SubscriptionDto update(@RequestParam Long id, @RequestBody SubscriptionRequest request, @RequestParam SubscriptionUpdateType updateType) throws Exception {
        return subscriptionService.update(id, request, updateType);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) throws Exception {
        subscriptionService.delete(id);
    }

    @GetMapping("/get-all")
    public Page<SubscriptionDto> getAll(SubscriptionFilter filter, Pageable pageable, boolean selfSubs) throws Exception {
        return subscriptionService.getAll(filter, pageable, selfSubs);
    }

    @GetMapping("/get-all-renew-list")
    public Page<SubscriptionRenewDto> getAllRenewList(SubscriptionRenewLogFilter filter, Pageable pageable) {
        return subscriptionService.getAllSubscriptionLogList(filter, pageable);
    }

    @GetMapping("/client/{uuid}")
    public ResponseEntity<String> getSubscriptionData(@PathVariable String uuid) throws Exception {
        return subscriptionService.getSubscriptionData(uuid);
    }

    @PutMapping("/change-status")
    public SubscriptionDto changeStatus(@RequestParam Boolean newStatus, @RequestParam Long id) throws Exception {
        return subscriptionService.changeStatus(newStatus, id);
    }

    @PutMapping("/change-pay-status-for-subscription-renew-log")
    public SubscriptionRenewDto changePayStatusForSubscription(@RequestParam Boolean newPayStatus, @RequestParam Long renewId) {
        return subscriptionService.changePayStatusForSubscriptionRenewLog(newPayStatus, renewId);
    }

    @PutMapping("/change-pay-status-for-subscription")
    public SubscriptionDto changePayStatusForSubscriptionRenew(@RequestParam Boolean newPayStatus, @RequestParam Long id) {
        return subscriptionService.changePayStatusForSubscription(newPayStatus, id);
    }

    @GetMapping("/report")
    public SubscriptionDto report(@RequestParam String subLink) throws Exception {
        return subscriptionService.report(subLink);
    }



    @GetMapping("/sync")
    public void sync() throws Exception {
        syncWithPanels.syncWithPanels();
        syncWithPanels.expiration();
    }

    @GetMapping("/summary")
    public SummaryModel summary() {
        return subscriptionService.getSummary();
    }
}
