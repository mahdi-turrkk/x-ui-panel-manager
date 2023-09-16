package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundDto {
    private Long id;
    private Long serverId;
    private Boolean generatable;
    private Long idFromPanel;
    private String tag;
    private String port;
    private String listen;
    private String protocol;
    private Long up;
    private Long down;
    private Long totalUsed;
    private String remark;
    private Boolean enable;
    private String expireDate;
    private String setting;
    private String streamSettings;
    private String sniffing;

    public InboundDto(InboundEntity entity) {
        id = entity.getId();
        serverId = entity.getServerId();
        generatable = entity.getGeneratable();
        idFromPanel = entity.getIdFromPanel();
        tag = entity.getTag();
        port = entity.getPort();
        listen = entity.getListen();
        protocol = entity.getProtocol();
        up = entity.getUp();
        down = entity.getDown();
        totalUsed = entity.getTotalUsed();
        remark = entity.getRemark();
        enable = entity.getEnable();
        expireDate = entity.getExpireDate();
        setting = entity.getSetting();
        streamSettings = entity.getStreamSettings();
        sniffing = entity.getSniffing();
    }
}
