package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundResponseModel {
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("obj")
    private InboundModel obj;
}
