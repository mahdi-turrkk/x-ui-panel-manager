package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

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
