package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.clients.XuiLoginClient;
import online.gixmetir.xuipanelmanagerbackend.clients.models.InboundsResponseModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ResponseModel;
import online.gixmetir.xuipanelmanagerbackend.services.xui.PanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inbounds")
public class InboundController {
    private final XuiLoginClient xuiLoginClient;
    private final PanelService panelService;

    @Autowired
    public InboundController(XuiLoginClient xuiLoginClient, PanelService panelService) {
        this.xuiLoginClient = xuiLoginClient;
        this.panelService = panelService;
    }

    @GetMapping("/login-to-x-ui")
    public InboundsResponseModel loginToXui() throws Exception {
        String sessionKey = panelService.login(LoginModel.builder()
                .username("husyn.cf")
                .password("w~#4!x}kd_@Ng*}T3r,VBdyZ7J-VRiVZ#}")
                .build());


        return xuiLoginClient.getInbounds(sessionKey);
    }
}
