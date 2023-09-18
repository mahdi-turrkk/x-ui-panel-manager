package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBase {
    private boolean success;
    private String msg;

}
