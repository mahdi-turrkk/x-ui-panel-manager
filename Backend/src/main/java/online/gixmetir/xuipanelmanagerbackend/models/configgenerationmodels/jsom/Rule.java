package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class Rule {
    public String id;
    public String type;
    public List<String> inboundTag;
    public String outboundTag;
    public boolean enabled;
    public List<String> domain;
    public List<String> ip;
    public String port;

}
