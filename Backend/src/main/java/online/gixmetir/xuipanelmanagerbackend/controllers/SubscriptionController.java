package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionDto;
import online.gixmetir.xuipanelmanagerbackend.models.SubscriptionRequest;
import online.gixmetir.xuipanelmanagerbackend.services.app.SubscriptionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
