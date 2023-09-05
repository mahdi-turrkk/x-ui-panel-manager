package online.gixmetir.xuipanelmanagerbackend.services.xui;

import online.gixmetir.xuipanelmanagerbackend.clients.XuiLoginClient;
import online.gixmetir.xuipanelmanagerbackend.clients.models.*;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.repositories.InboundRepository;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.services.app.ServerService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PanelService {
    private final XuiLoginClient xuiLoginClient;
    private final ServerService serverService;
    private final InboundRepository inboundRepository;
    private final ServerRepository serverRepository;

    public PanelService(XuiLoginClient xuiLoginClient, ServerService serverService, InboundRepository inboundRepository, ServerRepository serverRepository) {
        this.xuiLoginClient = xuiLoginClient;
        this.serverService = serverService;
        this.inboundRepository = inboundRepository;
        this.serverRepository = serverRepository;
    }

    public String login(LoginModel loginModel) throws Exception {
        ResponseEntity<ResponseModel> response = xuiLoginClient.login(loginModel);
        if (!response.getBody().getSuccess()) {
            throw new Exception("نام کاربری یا رمز عبور اشتباه هست ");
        }
        String header = response.getHeaders().get("set-cookie").toString();
        return Helper.getSessionKey(header);
    }


    public InboundModel[] loadAllInboundsFromXuiPanel(ServerDto serverDto) throws Exception {
        List<ServerEntity> servers = serverRepository.findAll();


        String sessionKey = login(LoginModel.builder()
                .username(serverDto.getUsername())
                .password(serverDto.getPassword())
                .build());

        ArrayList<ClientModel> clients = new ArrayList<>();
        InboundsResponseModel model = xuiLoginClient.getInbounds(sessionKey);
        if (model.getSuccess()) {
            return model.getObj();
        } else {
            throw new Exception(model.getMsg());
        }
//        /*
//
    }


    public ResponseEntity<ResponseModel> addClient(List<ClientModel> clients, ServerDto serverDto, Long inboundId) throws Exception {
        String sessionKey = login(LoginModel.builder()
                .username(serverDto.getUsername())
                .password(serverDto.getPassword())
                .build());
        Helper helper = new Helper();

        JSONObject jsonObject = new JSONObject();
        JSONArray clientsArray = new JSONArray();
        for (ClientModel model : clients) {
            JSONObject clientObject = new JSONObject();
            clientObject.put("id", model.getId());
            clientObject.put("email", model.getEmail());
            clientObject.put("flow", model.getFlow());
            clientObject.put("limitIp", model.getLimitIp());
            clientObject.put("alterId", "");
            clientObject.put("totalGB", model.getTotalGb());
            clientObject.put("expiryTime", model.getExpiryTime());
            clientObject.put("enable", model.isEnable());
            clientObject.put("tgId", model.getTgId());
            clientObject.put("subId", model.getSubId());
            clientsArray.put(clientObject);
        }

        jsonObject.put("settings", new JSONObject().put("clients", clientsArray).toString());
        jsonObject.put("id", inboundId);

        ResponseEntity<ResponseModel> response = xuiLoginClient.addClient(sessionKey, jsonObject.toString());
        if (response.getBody().getSuccess()) {
            return response;
        } else {
            throw new Exception(response.getBody().getMsg());
        }

    }
}
