package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcpSettings {
    private boolean connectionReuse;
    private boolean acceptProxyProtocol;
    private Header header;
}
