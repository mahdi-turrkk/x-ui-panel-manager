package online.gixmetir.xuipanelmanagerbackend.clients;

import online.gixmetir.xuipanelmanagerbackend.clients.models.InboundsResponseModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "panel", url = "https://test.gixmetir.online:9090/")
public interface XuiLoginClient {
    @PostMapping("/login")
    ResponseEntity<ResponseModel> login(@RequestBody LoginModel loginModel);

    @GetMapping(value = "/panel/api/inbounds/list", consumes = "application/json;charset=utf-8")
    InboundsResponseModel getInbounds(@RequestHeader("cookie") String header);

    @PostMapping(value = "/panel/api/inbounds/addClient", consumes = "application/json;charset=utf-8")
    ResponseEntity<ResponseModel> addClient(@RequestHeader("cookie") String header, @RequestBody String json);

    @PostMapping(value = "/panel/api/inbounds/updateClient/{uuid}", consumes = "application/json;charset=utf-8")
    ResponseEntity<ResponseModel> addClient(@RequestHeader("cookie") String header, @PathVariable(value = "uuid") String uuid, @RequestBody String json);

}
