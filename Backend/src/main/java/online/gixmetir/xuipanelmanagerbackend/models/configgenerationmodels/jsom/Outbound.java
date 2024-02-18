package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Outbound {
    public Mux mux;
    public String protocol;
    public OutboundSettings settings;
    public StreamSettings streamSettings;
    public String tag;

}
