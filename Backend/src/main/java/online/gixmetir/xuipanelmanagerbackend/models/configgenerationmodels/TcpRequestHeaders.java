package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcpRequestHeaders {
    private String host;
    private String acceptEncoding = "gzip, deflate";
    private String connection = "keep-alive";
    private String pragma = "no-cache";
}
