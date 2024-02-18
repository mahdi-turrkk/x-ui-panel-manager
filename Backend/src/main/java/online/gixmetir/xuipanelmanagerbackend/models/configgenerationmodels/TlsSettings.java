package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TlsSettings {
    private String serverName;
    private String minVersion;
    private String maxVersion;
    private String cipherSuites;
    private Certificate[] certificates;
    private String fp;
    private String[] alpn;
    private TlsSettingsInner settings;
    private boolean rejectUnknownSni;
//    private String allowInsecure;
//    private String keyFile;
}
