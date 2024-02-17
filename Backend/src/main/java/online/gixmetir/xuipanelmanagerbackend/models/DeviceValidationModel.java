package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DeviceValidationModel {
    private String fragmentInterval;
    private String fragmentLength;
    private String packets;
    private boolean applyFragment;

    private boolean generateJson;
    private boolean generateV2rayLink;

}


