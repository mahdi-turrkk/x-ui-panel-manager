package online.gixmetir.xuipanelmanagerbackend.services.xui;

import online.gixmetir.xuipanelmanagerbackend.clients.XuiClient;
import online.gixmetir.xuipanelmanagerbackend.clients.models.*;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.exceptions.CustomException;
import online.gixmetir.xuipanelmanagerbackend.exceptions.UsernameOrPasswordWrongException;
import online.gixmetir.xuipanelmanagerbackend.models.ServerDto;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
            throw new UsernameOrPasswordWrongException("نام کاربری یا رمز عبور اشتباه هست ");
        }
        String header = Objects.requireNonNull(response.getHeaders().get("set-cookie")).toString();
        return Helper.getSessionKey(header);
    }


    public InboundModel[] loadAllInboundsFromXuiPanel(ServerDto serverDto, String sessionKey) throws Exception {
//        String sessionKey = login(serverDto);
        InboundsResponseModel model = xuiClient.getInbounds(URI.create(serverDto.getUrl()), sessionKey);
        if (model.getSuccess()) {
            return model.getObj();
        } else {
            throw new CustomException(model.getMsg());
        }
    }

    public void addClient(List<ClientModel> clients, ServerDto serverDto, Long inboundIdFromPanel) throws Exception {
        String sessionKey = login(serverDto);
        JSONObject jsonObject = convertListOfClientModelToJsonStructure(clients, inboundIdFromPanel);
        ResponseEntity<ResponseModel> response = xuiClient.addClient(URI.create(serverDto.getUrl()), sessionKey, jsonObject.toString());
        if (!Objects.requireNonNull(response.getBody()).getSuccess()) {
            throw new CustomException(response.getBody().getMsg());
        }
    }

    public void updateClients(List<ClientEntity> clients) {
        Map<ServerEntity, List<ClientEntity>> clientsByServer =
                clients.stream()
                        .collect(Collectors.groupingBy(c -> c.getInbound().getServer()));
        clientsByServer.forEach((server, listOfClients) -> {
            ServerDto serverDto = new ServerDto(server);
            String sessionKey = null;
            if ((server.getIsDeleted() == null || !server.getIsDeleted()) && (server.getStatus() != null && server.getStatus())) {
                try {
                    sessionKey = login(serverDto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (ClientEntity clientEntity : listOfClients
                ) {
                    if (clientEntity.getInbound().getGeneratable() != null && clientEntity.getInbound().getGeneratable()) {
                        JSONObject jsonObject = convertListOfClientModelToJsonStructure(List.of(new ClientModel(clientEntity)), clientEntity.getInbound().getIdFromPanel());
                        xuiClient.updateClient(URI.create(serverDto.getUrl()), sessionKey, clientEntity.getUuid(), jsonObject.toString());
                    }
                }
            }
        });
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
            clientObject.put("alterId", 0);
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
        Map<ServerEntity, List<ClientEntity>> clientsByServer =
                clientEntities.stream()
                        .collect(Collectors.groupingBy(c -> c.getInbound().getServer()));
        clientsByServer.forEach((server, listOfClients) -> {
            ServerDto serverDto = new ServerDto(server);
            String sessionKey = null;
            if ((server.getIsDeleted() == null || !server.getIsDeleted()) && server.getStatus()) {
                try {
                    sessionKey = login(serverDto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (ClientEntity clientEntity : listOfClients
                ) {
                    if (clientEntity.getInbound().getGeneratable() != null && clientEntity.getInbound().getGeneratable()) {
                        ResponseEntity<ResponseModel> response = xuiClient.deleteClient(URI.create(serverDto.getUrl()), sessionKey, clientEntity.getInbound().getIdFromPanel(), clientEntity.getUuid());
                        if (!Objects.requireNonNull(response.getBody()).getSuccess())
                            throw new CustomException(response.getBody().getMsg());
                    }
                }
            }
        });
    }

    public void deleteInbound(InboundEntity inboundEntity) throws Exception {
        ServerEntity serverEntity = inboundEntity.getServer();
        if (serverEntity.getIsDeleted() != null && serverEntity.getIsDeleted()) return;
        String sessionKey = login(new ServerDto(serverEntity));
        ResponseEntity<ResponseModel> response = xuiClient.deleteInbound(URI.create(serverEntity.getUrl()), sessionKey, inboundEntity.getIdFromPanel());
        if (!Objects.requireNonNull(response.getBody()).getSuccess())
            throw new CustomException(response.getBody().getMsg());
    }

    public ClientStatsModel clientLog(ClientEntity clientEntity, String sessionKey) throws Exception {
        ResponseEntity<ClientLogResponseModel> response = xuiClient.getClientTraffic(URI.create(clientEntity.getInbound().getServer().getUrl()), sessionKey, clientEntity.getEmail());
        if (Objects.requireNonNull(response.getBody()).getSuccess()) {
            return response.getBody().getObj();
        } else
            throw new CustomException(response.getBody().getMsg());
    }

    public void createInbound(InboundModelRequest inboundModelRequest, ServerDto serverDto) throws Exception {
        String sessionKey = login(serverDto);
        ResponseEntity<InboundResponseModel> response = xuiClient.addInbound(URI.create(serverDto.getUrl()), sessionKey, inboundModelRequest);
        if (!Objects.requireNonNull(response.getBody()).getSuccess())
            throw new CustomException(response.getBody().getMsg());
    }

    public void resetInboundTraffic(long inboundId, ServerDto serverDto) throws Exception {
        String sessionKey = login(serverDto);
        ResponseEntity<ResponseModel> res = xuiClient.resetInboundTraffic(URI.create(serverDto.getUrl()), sessionKey, inboundId);
        if (!Objects.requireNonNull(res.getBody()).getSuccess()) {
            throw new CustomException(res.getBody().getMsg());
        }
    }
}
