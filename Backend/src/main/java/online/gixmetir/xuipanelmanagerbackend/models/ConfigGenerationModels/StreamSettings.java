package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class StreamSettings {
    private String network;
    private String security;
    private ExternalProxy[] externalProxy;
    private TlsSettings tlsSettings;
    private TcpSettings tcpSettings;
    private KcpSettings kcpSettings;
    private WsSettings wsSettings;
    private HttpSettings httpSettings;
    private QuicSettings quicSettings;
    private GrpcSettings grpcSettings;
    private TlsSettings xtlsSettings;
    private RealitySettings realitySettings;

}

