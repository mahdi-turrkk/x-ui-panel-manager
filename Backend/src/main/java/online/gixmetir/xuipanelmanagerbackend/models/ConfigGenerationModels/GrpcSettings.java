package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrpcSettings {
    private String serviceName;
    private boolean multiMode;
}
