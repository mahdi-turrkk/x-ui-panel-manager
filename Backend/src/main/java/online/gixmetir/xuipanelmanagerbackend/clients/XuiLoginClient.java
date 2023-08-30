package online.gixmetir.xuipanelmanagerbackend.clients;

import online.gixmetir.xuipanelmanagerbackend.clients.models.InboundsResponseModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "panel", url = "https://test.gixmetir.online:9090/")
public interface XuiLoginClient {
    @PostMapping("/login")
    ResponseEntity<ResponseModel> login(@RequestBody LoginModel loginModel);

    @GetMapping(value = "/panel/api/inbounds/list",consumes = "application/json;charset=utf-8")
    InboundsResponseModel getInbounds(@RequestHeader("cookie") String header);


}
