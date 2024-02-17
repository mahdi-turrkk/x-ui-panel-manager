package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.gixmetir.xuipanelmanagerbackend.entities.GlobalSettingEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSettingDto {
    private Long id;
    private String subscriptionUrl;
    private List<OsSettingDto> osSettingDtos;

    public GlobalSettingDto(GlobalSettingEntity globalSettingEntity) {
        this.id = globalSettingEntity.getId();
        this.subscriptionUrl = globalSettingEntity.getSubscriptionUrl();
        this.osSettingDtos = globalSettingEntity.getOsSettingEntityList().stream().map(OsSettingDto::new).toList();
    }
}
