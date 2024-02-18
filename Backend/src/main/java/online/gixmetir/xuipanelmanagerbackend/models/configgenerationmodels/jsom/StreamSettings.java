package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StreamSettings {
    public String network;
    public String security;
    public TlsSettings tlsSettings;
    public WsSettings wsSettings;
    public GrpcSettings grpcSettings;
    public SocketOptions sockopt;

}
