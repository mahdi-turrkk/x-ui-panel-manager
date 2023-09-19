package online.gixmetir.xuipanelmanagerbackend.services.xui;

import online.gixmetir.xuipanelmanagerbackend.clients.XuiClient;
import online.gixmetir.xuipanelmanagerbackend.clients.models.*;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
public class PanelService {
    private final XuiClient xuiClient;

    public PanelService(XuiClient xuiClient) {
        this.xuiClient = xuiClient;
    }

    public String login(ServerDto serverDto) throws Exception {
        LoginModel loginModel = LoginModel.builder()
                .username(serverDto.getUsername())
                .password(serverDto.getPassword())
                .build();
        ResponseEntity<ResponseModel> response = xuiClient.login(URI.create(serverDto.getUrl()), loginModel);
        if (!Objects.requireNonNull(response.getBody()).getSuccess()) {
            throw new Exception("نام کاربری یا رمز عبور اشتباه هست ");
        }
        String header = Objects.requireNonNull(response.getHeaders().get("set-cookie")).toString();
        return Helper.getSessionKey(header);
    }


    public InboundModel[] loadAllInboundsFromXuiPanel(ServerDto serverDto) throws Exception {
        String sessionKey = login(serverDto);
        InboundsResponseModel model = xuiClient.getInbounds(URI.create(serverDto.getUrl()), sessionKey);
        if (model.getSuccess()) {
            return model.getObj();
        } else {
            throw new Exception(model.getMsg());
        }
    }

    public void addClient(List<ClientModel> clients, ServerDto serverDto, Long inboundIdFromPanel) throws Exception {
        String sessionKey = login(serverDto);
        JSONObject jsonObject = convertListOfClientModelToJsonStructure(clients, inboundIdFromPanel);
        ResponseEntity<ResponseModel> response = xuiClient.addClient(URI.create(serverDto.getUrl()), sessionKey, jsonObject.toString());
        if (!Objects.requireNonNull(response.getBody()).getSuccess()) {
            throw new Exception(response.getBody().getMsg());
        }
    }

    public void updateClients(List<ClientEntity> clients) throws Exception {
        for (ClientEntity entity : clients) {
            ServerDto serverDto = new ServerDto(entity.getInbound().getServer());
            String sessionKey = login(serverDto);
            JSONObject jsonObject = convertListOfClientModelToJsonStructure(List.of(new ClientModel(entity)), entity.getInbound().getIdFromPanel());
            xuiClient.updateClient(URI.create(serverDto.getUrl()), sessionKey, entity.getUuid(), jsonObject.toString());
        }
    }

    private static JSONObject convertListOfClientModelToJsonStructure(List<ClientModel> clients, Long inboundIdFromPanel) {
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
        jsonObject.put("id", inboundIdFromPanel);
        return jsonObject;
    }

    public void deleteClients(List<ClientEntity> clientEntities) throws Exception {
        for (ClientEntity client : clientEntities) {
            ServerEntity serverEntity = client.getInbound().getServer();
            String sessionKey = login(new ServerDto(serverEntity));
            ResponseEntity<ResponseModel> response = xuiClient.deleteClient(URI.create(serverEntity.getUrl()), sessionKey, client.getInboundId(), client.getUuid());
            if (!Objects.requireNonNull(response.getBody()).getSuccess())
                throw new Exception(response.getBody().getMsg());
        }

    }

    public ClientStatsModel clientLog(ClientEntity clientEntity, String sessionKey) throws Exception {
        ResponseEntity<ClientLogResponseModel> response = xuiClient.getClientTraffic(URI.create(clientEntity.getInbound().getServer().getUrl()), sessionKey, clientEntity.getEmail());
        if (response.getBody().getSuccess()) {
            return response.getBody().getObj();
        } else
            throw new Exception(response.getBody().getMsg());
    }
}
