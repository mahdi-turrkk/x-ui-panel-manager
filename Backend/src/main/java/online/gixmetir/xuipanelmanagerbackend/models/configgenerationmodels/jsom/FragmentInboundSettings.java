package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FragmentInboundSettings {
    public String auth;
    public boolean udp;
    public boolean allowTransparent;
}
