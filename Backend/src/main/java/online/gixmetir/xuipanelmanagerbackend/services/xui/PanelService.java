package online.gixmetir.xuipanelmanagerbackend.services.xui;

import online.gixmetir.xuipanelmanagerbackend.clients.XuiClient;
import online.gixmetir.xuipanelmanagerbackend.clients.models.*;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.repositories.ServerRepository;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PanelService {
    private final XuiClient xuiClient;
    private final ServerRepository serverRepository;

    public PanelService(XuiClient xuiClient, ServerRepository serverRepository) {
        this.xuiClient = xuiClient;
        this.serverRepository = serverRepository;
    }

    public String login(LoginModel loginModel) throws Exception {
        ResponseEntity<ResponseModel> response = xuiClient.login(loginModel);
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
        InboundsResponseModel model = xuiClient.getInbounds(sessionKey);
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

        ResponseEntity<ResponseModel> response = xuiClient.updateClient(sessionKey, jsonObject.toString());
        if (response.getBody().getSuccess()) {
            return response;
        } else {
            throw new Exception(response.getBody().getMsg());
        }

    }

    public void deleteClients(List<ClientEntity> clientEntities) throws Exception {
        for (ClientEntity client : clientEntities) {
            ServerEntity serverEntity = client.getInbound().getServer();
            String sessionKey = login(LoginModel.builder()
                    .username(serverEntity.getUsername())
                    .password(serverEntity.getPassword())
                    .build());
            ResponseEntity<ResponseModel> response = xuiClient.deleteClient(sessionKey, client.getInboundId(), client.getUuid());
            if (!response.getBody().getSuccess())
                throw new Exception(response.getBody().getMsg());
        }

    }
}
