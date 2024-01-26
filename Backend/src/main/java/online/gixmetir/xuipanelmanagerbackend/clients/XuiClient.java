package online.gixmetir.xuipanelmanagerbackend.clients;

import online.gixmetir.xuipanelmanagerbackend.clients.models.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@FeignClient(name = "panel", url = "http://127.0.0.1:8080")
public interface XuiClient {
    @PostMapping("/login")
    ResponseEntity<ResponseModel> login(URI url, @RequestBody LoginModel loginModel);

    @GetMapping(value = "/panel/api/inbounds/list", consumes = "application/json;charset=utf-8")
    InboundsResponseModel getInbounds(URI url, @RequestHeader("cookie") String header);

    @PostMapping(value = "/panel/api/inbounds/add", consumes = "application/json;charset=utf-8")
    ResponseEntity<InboundResponseModel> addInbound(URI url, @RequestHeader("cookie") String header, @RequestBody InboundModelRequest obj);

    @PostMapping(value = "/panel/api/inbounds/addClient", consumes = "application/json;charset=utf-8")
    ResponseEntity<ResponseModel> addClient(URI url, @RequestHeader("cookie") String header, @RequestBody String json);

    @PostMapping(value = "/panel/api/inbounds/updateClient/{uuid}", consumes = "application/json;charset=utf-8")
    ResponseEntity<ResponseModel> updateClient(URI url, @RequestHeader("cookie") String header, @PathVariable(value = "uuid") String uuid, @RequestBody String json);

    @PostMapping(value = "/panel/api/inbounds/{inboundId}/delClient/{clientUuid}", consumes = "application/json;charset=utf-8")
    ResponseEntity<ResponseModel> deleteClient(URI url, @RequestHeader("cookie") String header, @PathVariable(value = "inboundId") Long inboundId, @PathVariable(value = "clientUuid") String clientUuid);

    @GetMapping(value = "/panel/api/inbounds/getClientTraffics/{email}", consumes = "application/json;charset=utf-8")
    ResponseEntity<ClientLogResponseModel> getClientTraffic(URI url, @RequestHeader("cookie") String header, @PathVariable(value = "email") String email);

    @PostMapping(value = "/panel/api/inbounds/del/{inboundId}", consumes = "application/json;charset=utf-8")
    ResponseEntity<ResponseModel> deleteInbound(URI uri, @RequestHeader("cookie") String sessionKey, @PathVariable(value = "inboundId") Long idFromPanel);

    @PostMapping(value = "panel/api/inbounds/resetAllClientTraffics/{inboundId}", consumes = "application/json;charset=utf-8")
    ResponseEntity<ResponseModel> resetInboundTraffic(URI uri,@RequestHeader("cookie") String sessionKey, @PathVariable(value = "inboundId") Long inboundId);
}
