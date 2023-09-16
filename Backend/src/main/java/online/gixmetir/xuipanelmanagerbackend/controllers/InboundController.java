package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.filters.InboundFilter;
import online.gixmetir.xuipanelmanagerbackend.models.InboundDto;
import online.gixmetir.xuipanelmanagerbackend.services.app.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inbounds")

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

    @PutMapping("/change-inbound-generatable")
    public void changeInboundGeneratable(@RequestBody Long inboundId, @RequestBody Boolean generatable) throws Exception {
        inboundService.changeInboundGeneratable(inboundId, generatable);
    }

    @GetMapping("/get-all")
    public Page<InboundDto> getAll(InboundFilter filter, Pageable pageable) {
        return inboundService.getAll(filter, pageable);
    }

}
