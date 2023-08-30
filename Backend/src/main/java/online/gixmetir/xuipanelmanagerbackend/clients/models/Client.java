package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @JsonProperty("email")
    private String email;
    @JsonProperty("enable")
    private boolean enable;
    @JsonProperty("expiryTime")
    private long expiryTime;
    @JsonProperty("flow")
    private String flow;
    @JsonProperty("id")
    private String id;

    @JsonProperty("limitIp")
    private int limitIp;
    @JsonProperty("subId")
    private String subId;

    @JsonProperty("tgId")
    private String tgId;

    @JsonProperty("totalGB")
    private long totalGb;
/*
*       "email": "hossein-tls-tcp",
      "enable": true,
      "expiryTime": 0,
      "flow": "",
      "id": "f1e1321a-5765-456c-8657-007cbbe8d6a1",
      "limitIp": 0,
      "subId": "me",
      "tgId": "",
      "totalGB": 0*/

}
