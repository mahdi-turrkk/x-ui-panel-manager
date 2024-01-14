package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Headers {
    @JsonProperty("Host")
    private String host;

}
