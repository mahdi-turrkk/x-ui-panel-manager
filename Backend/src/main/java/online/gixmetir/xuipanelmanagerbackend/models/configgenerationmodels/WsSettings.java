package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WsSettings {
    private boolean connectionReuse;
    private boolean acceptProxyProtocol;
    private String path;
    private Headers headers;
}
