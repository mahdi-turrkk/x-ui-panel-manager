package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalProxy {
    private String forceTls;
    private String dest;
    private String port;
    private String remark;
}
