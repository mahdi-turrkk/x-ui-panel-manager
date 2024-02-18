package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Inbound {
    public String tag;
    public int port;
    public String listen;
    public String protocol;
    public Sniffing sniffing;
    public Settings settings;

}

