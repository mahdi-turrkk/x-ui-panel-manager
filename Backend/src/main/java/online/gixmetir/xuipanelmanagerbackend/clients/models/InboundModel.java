package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InboundModel {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("up")
    private String up;
    @JsonProperty("down")
    private String down;
    @JsonProperty("total")
    private String total;
    @JsonProperty("enable")
    private Boolean enable;
    @JsonProperty("expiryTime")
    private String expiryTime;
    @JsonProperty("clientStats")
    private ClientStatsModel[] clientStats;

    @JsonProperty("listen")
    private String listen;
    @JsonProperty("port")
    private String port;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("settings")
    private String settings;
    @JsonProperty("remark")
    private String remark;

    @JsonProperty("sniffing")
    private String sniffing;
    @JsonProperty("streamSettings")
    private String streamSettings;
    @JsonProperty("tag")
    private String tag;

    public InboundEntity toEntity(Long serverId) {
        return InboundEntity.builder()
                .serverId(serverId)
                .generatable(false)
                .idFromPanel(this.id)
                .tag(this.tag)
                .port(this.port)
                .listen(this.listen)
                .protocol(this.protocol)
                .up(Long.parseLong(this.up))
                .down(Long.parseLong(this.down))
                .totalUsed(Long.parseLong(this.total))
                .remark(this.remark)
                .enable(this.enable)
                .expireDate(this.expiryTime)
                .setting(this.settings)
                .streamSettings(this.streamSettings)
                .sniffing(this.sniffing)
                .build();
    }

    public InboundEntity toEntity(InboundEntity entity) {


        entity.setGeneratable(false);
        entity.setIdFromPanel(this.id);
        entity.setTag(this.tag);
        entity.setPort(this.port);
        entity.setListen(this.listen);
        entity.setProtocol(this.protocol);
        entity.setUp(Long.parseLong(this.up));
        entity.setDown(Long.parseLong(this.down));
        entity.setTotalUsed(Long.parseLong(this.total));
        entity.setRemark(this.remark);
        entity.setEnable(this.enable);
        entity.setExpireDate(this.expiryTime);
        entity.setSetting(this.settings);
        entity.setStreamSettings(this.streamSettings);
        entity.setSniffing(this.sniffing);
        return entity;
    }
  /*
    vless ://f1e1321a-5765-456c-8657-007cbbe8d6a1@xxx.gixmetir.online:80?type=tcp&security=tls&fp=chrome&alpn=http%2F1.1%2Ch2&allowInsecure=1&sni=telewebion.com#TLS-TCP-hossein-tls-tcp
  */
}
