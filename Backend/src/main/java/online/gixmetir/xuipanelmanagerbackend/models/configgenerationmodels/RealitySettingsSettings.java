package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealitySettingsSettings {
    private String publicKey;
    private String serverName;
    private String fp;
}
