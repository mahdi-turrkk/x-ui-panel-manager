package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.OsSettingEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OsSettingRequest {

    private Long id;
    private OS os;
    private String clients;
    private Boolean applyFragment;
    private String fragmentInterval;
    private String fragmentLength;
    private Boolean generateJson;
    private Boolean generateV2rayLink;
    private Long globalSettingId;
    private String packets;


    public OsSettingEntity toEntity() {
        return OsSettingEntity.builder()
                .os(this.os)
                .clients(this.clients)
                .applyFragment(this.applyFragment)
                .fragmentInterval(this.fragmentInterval)
                .fragmentLength(this.fragmentLength)
                .generateJson(this.generateJson)
                .generateV2rayLink(this.generateV2rayLink)
                .globalSettingId(this.globalSettingId)
                .packets(this.packets)
                .build();
    }

    public OsSettingEntity toEntity(OsSettingEntity osSettingEntity) {
        osSettingEntity.setOs(this.os);
        osSettingEntity.setClients(this.clients);
        osSettingEntity.setApplyFragment(this.applyFragment);
        osSettingEntity.setFragmentInterval(this.fragmentInterval);
        osSettingEntity.setFragmentLength(this.fragmentLength);
        osSettingEntity.setGenerateJson(this.generateJson);
        osSettingEntity.setGenerateV2rayLink(this.generateV2rayLink);
        osSettingEntity.setGlobalSettingId(this.globalSettingId);
        osSettingEntity.setPackets(this.packets);
        return osSettingEntity;
    }
}
