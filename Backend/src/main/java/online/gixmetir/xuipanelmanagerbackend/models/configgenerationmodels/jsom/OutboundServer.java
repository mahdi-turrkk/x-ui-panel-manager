package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutboundServer {
    public String address;
    public int level;
    public String flow;
    public String method;
    public boolean ota;
    public String password;
    public int port;
}
