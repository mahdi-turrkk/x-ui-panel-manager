package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
//inbound setting
public class Settings {
    public Long userLevel;
    public String auth;
    public Boolean udp;
}
