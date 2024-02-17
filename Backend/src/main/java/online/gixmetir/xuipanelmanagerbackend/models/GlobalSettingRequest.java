package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.GlobalSettingEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalSettingRequest {
    private Long id;
    private String subscriptionUrl;
    private List<OsSettingRequest> osSettingRequests;

    public GlobalSettingEntity toEntity() {
        return GlobalSettingEntity.builder()
                .subscriptionUrl(this.subscriptionUrl)
                .build();
    }

    public GlobalSettingEntity toEntity(GlobalSettingEntity globalSettingEntity) {
        globalSettingEntity.setSubscriptionUrl(this.subscriptionUrl);
        return globalSettingEntity;
    }
}
