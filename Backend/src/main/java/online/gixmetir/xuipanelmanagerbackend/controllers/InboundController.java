package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.services.app.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inbounds")
public class InboundController {

    private final InboundService inboundService;

    @Autowired
    public InboundController(InboundService inboundService) {
        this.inboundService = inboundService;
    }

    @GetMapping("/load-all-inbounds-from-x-ui-panels")
    public void getAllInboundsFromXuiPanels() throws Exception {
        inboundService.loadAllInboundsFromPanels();
    }

    @PostMapping("/change-inbound-generatable")
    public void addClient(@RequestBody Long inboundId, @RequestBody Boolean generatable) throws Exception {
        inboundService.changeInboundGeneratable(inboundId, generatable);
    }

}
