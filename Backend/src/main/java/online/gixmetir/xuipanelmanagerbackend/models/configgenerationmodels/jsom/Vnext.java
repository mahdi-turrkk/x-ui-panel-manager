package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class Vnext {
    public String address;
    public int port;
    public List<User> users;


}
