package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {
    private Long totalFlow;
    private Integer periodLength;
    private String title;
    private Integer numberSubscriptionsToGenerate = 0;
    private Long userId;

    public SubscriptionEntity toEntity() {
        return SubscriptionEntity.builder()
                .totalFlow(new Helper().GBToByte(this.totalFlow))
                .periodLength(this.periodLength)
                .title(this.title)
                .userId(this.userId)
                .build();
    }

    public SubscriptionEntity toEntity(SubscriptionEntity entity) {
        entity.setTotalFlow(this.totalFlow * 1024 * 1024 * 1024);
        entity.setPeriodLength(this.periodLength);
        entity.setTitle(this.title);
        return entity;
    }
}
