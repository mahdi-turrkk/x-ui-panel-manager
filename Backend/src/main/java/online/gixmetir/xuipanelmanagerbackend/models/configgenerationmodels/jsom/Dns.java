package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class Dns {
    public Map<String, String> hosts;
    public List<Object> servers;

}
