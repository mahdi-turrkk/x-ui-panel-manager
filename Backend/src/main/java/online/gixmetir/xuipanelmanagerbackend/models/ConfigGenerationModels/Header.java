package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Header {
    private String type;
    private TcpRequest request;
    private String response;
}
