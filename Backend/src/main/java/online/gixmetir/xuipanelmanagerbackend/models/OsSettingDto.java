package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.gixmetir.xuipanelmanagerbackend.entities.GlobalSettingEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.OsSettingEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OsSettingDto {
    private Long id;
    private OS os;
    private String clients;
    private Boolean applyFragment;
    private String fragmentInterval;
    private String fragmentLength;
    private Boolean generateJson;
    private Boolean generateV2rayLink;
    private String packets;
    private Long globalSettingId;


    public OsSettingDto(OsSettingEntity osSettingEntity) {
        this.id = osSettingEntity.getId();
        this.os = osSettingEntity.getOs();
        this.clients = osSettingEntity.getClients();
        this.applyFragment = osSettingEntity.getApplyFragment();
        this.fragmentInterval = osSettingEntity.getFragmentInterval();
        this.fragmentLength = osSettingEntity.getFragmentLength();
        this.generateJson = osSettingEntity.getGenerateJson();
        this.generateV2rayLink = osSettingEntity.getGenerateV2rayLink();
        this.globalSettingId = osSettingEntity.getGlobalSettingId();
        this.packets = osSettingEntity.getPackets();
    }
}
