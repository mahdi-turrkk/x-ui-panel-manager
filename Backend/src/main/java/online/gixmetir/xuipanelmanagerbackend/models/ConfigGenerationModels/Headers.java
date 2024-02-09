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
    @JsonProperty("host")
    private String host2;

    public void setHost2(String host2) {
        this.host2 = host2;
        this.host = host2;
    }
}
