package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Mux {
    public Boolean enabled;
    public Integer concurrency;
    public Integer xudpConcurrency;
    public String xudpProxyUDP443;
}
