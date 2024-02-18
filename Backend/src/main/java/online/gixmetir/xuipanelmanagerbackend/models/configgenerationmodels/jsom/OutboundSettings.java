package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
public class OutboundSettings {
    public List<Vnext> vnext;
    public String domainStrategy;
    public FragmentModel fragment;
    public Response response;
    public List<OutboundServer> servers;

}
