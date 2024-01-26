package online.gixmetir.xuipanelmanagerbackend.clients.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InboundModelRequest {
    private Boolean enable;
    private Long expiryTime;
    private String listen;
    private Long port;
    private String protocol;
    private String settings;
    private String remark;
    private String sniffing;
    private String streamSettings;

    public void toRequest(InboundEntity inboundEntity) {
        this.enable = inboundEntity.getEnable();
        this.expiryTime = Long.valueOf(inboundEntity.getExpireDate());
        this.listen = inboundEntity.getListen();
        this.port = Long.valueOf(inboundEntity.getPort());
        this.protocol = inboundEntity.getProtocol();
        this.settings = inboundEntity.getSetting();
        this.remark = inboundEntity.getRemark();
        this.sniffing = inboundEntity.getSniffing();
        this.streamSettings = inboundEntity.getStreamSettings();
    }
}
