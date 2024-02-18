package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class User {
    public String id;
    public Integer alterId;
    public String email;
    public String security;
    public String encryption;
    public String flow;
    public Integer level;
}
