package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;

import java.time.Instant;
import java.time.ZoneId;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {
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
    public ClientEntity toEntity() {
        return ClientEntity.builder()
                .uuid(this.id)
                .limitIp(this.limitIp)
                .expireDate(Instant.ofEpochMilli(this.expiryTime).atZone(ZoneId.of("UTC")).toLocalDate())
                .totalGb(this.totalGb)
                .email(this.email)
                .flow(this.flow)
                .enable(this.enable)
                .build();
    }

}
