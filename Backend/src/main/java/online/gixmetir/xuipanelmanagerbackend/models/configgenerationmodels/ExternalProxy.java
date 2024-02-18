package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

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
