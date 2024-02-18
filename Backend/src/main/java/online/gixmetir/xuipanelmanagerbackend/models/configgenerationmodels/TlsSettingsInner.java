package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TlsSettingsInner {
    private String serverName;
    private String fingerprint;
    private boolean allowInsecure;
    private String[] domains;
}
