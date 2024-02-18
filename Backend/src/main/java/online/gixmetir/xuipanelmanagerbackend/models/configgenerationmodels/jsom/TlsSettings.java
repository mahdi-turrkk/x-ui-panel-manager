package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class TlsSettings {
    public boolean allowInsecure;
    public String serverName;
    public List<String> alpn;
    public String fingerprint;
    public boolean show;
    public String publicKey;
    public String shortId;
    public String spiderX;
}
