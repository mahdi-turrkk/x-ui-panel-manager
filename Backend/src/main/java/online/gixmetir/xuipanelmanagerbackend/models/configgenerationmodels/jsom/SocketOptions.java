package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocketOptions {
    public String dialerProxy;
    public int tcpKeepAliveIdle;
    public int mark;
    public boolean tcpNoDelay;
}
