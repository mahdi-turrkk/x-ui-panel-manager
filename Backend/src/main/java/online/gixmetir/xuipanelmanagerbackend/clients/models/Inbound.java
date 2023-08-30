package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inbound {
    @JsonProperty("id")
    private int id;
    @JsonProperty("up")
    private String up;
    @JsonProperty("down")
    private String down;
    @JsonProperty("total")
    private String total;
    @JsonProperty("enable")
    private Boolean enable;
    @JsonProperty("expiryTime")
    private String expiryTime;
    @JsonProperty("clientStats")
    private ClientStats[] clientStats;

    @JsonProperty("listen")
    private String listen;
    @JsonProperty("port")
    private String port;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("settings")
    private String settings;
    @JsonProperty("remark")
    private String remark;

    @JsonProperty("sniffing")
    private String sniffing;
    @JsonProperty("streamSettings")
    private String streamSettings;
    @JsonProperty("tag")
    private String tag;


  /*
    vless ://f1e1321a-5765-456c-8657-007cbbe8d6a1@xxx.gixmetir.online:80?type=tcp&security=tls&fp=chrome&alpn=http%2F1.1%2Ch2&allowInsecure=1&sni=telewebion.com#TLS-TCP-hossein-tls-tcp
  */
}
