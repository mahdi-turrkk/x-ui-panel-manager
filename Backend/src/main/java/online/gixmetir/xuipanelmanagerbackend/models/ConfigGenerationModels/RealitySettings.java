package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealitySettings {
    private String[] shortIds;
    private String[] serverNames;
    private RealitySettingsSettings settings;
}
