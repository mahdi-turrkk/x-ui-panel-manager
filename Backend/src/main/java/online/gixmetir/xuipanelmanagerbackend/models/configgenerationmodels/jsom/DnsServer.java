package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class DnsServer {
    public String address;
    public List<String> domains;
    public Long port;
    public List<String> expectIPs;
}
