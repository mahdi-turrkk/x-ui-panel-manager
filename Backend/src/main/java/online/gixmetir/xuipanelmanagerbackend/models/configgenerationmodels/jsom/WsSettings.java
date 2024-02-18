package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WsSettings {
    public String path;
    public Headers headers;
}
