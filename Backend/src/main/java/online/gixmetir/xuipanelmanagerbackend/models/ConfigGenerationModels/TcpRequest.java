package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcpRequest {
    private String version = "1.1";
    private String method = "GET";
    private String path = "/";
    private TcpRequestHeaders headers;
}
