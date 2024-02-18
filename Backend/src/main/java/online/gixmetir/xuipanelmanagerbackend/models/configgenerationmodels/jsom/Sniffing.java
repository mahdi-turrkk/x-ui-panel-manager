package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class Sniffing {
    public List<String> destOverride;
    public boolean enabled;
    public boolean routeOnly;

}
