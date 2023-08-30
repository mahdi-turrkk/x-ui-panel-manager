package online.gixmetir.xuipanelmanagerbackend.services.xui;

import online.gixmetir.xuipanelmanagerbackend.clients.XuiLoginClient;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.clients.models.ResponseModel;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PanelService {
    private final XuiLoginClient xuiLoginClient;

    public PanelService(XuiLoginClient xuiLoginClient) {
        this.xuiLoginClient = xuiLoginClient;
    }

    public String login(LoginModel loginModel) throws Exception {
        ResponseEntity<ResponseModel> response = xuiLoginClient.login(loginModel);
        if (!response.getBody().getSuccess()) {
            throw new Exception("نام کاربری یا رمز عبور اشتباه هست ");
        }
        String header = response.getHeaders().get("set-cookie").toString();
        return Helper.getSessionKey(header);
    }
}
