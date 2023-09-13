package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {
    private Long totalFlow;
    private LocalDate expireDate;
    private LocalDate startDate;
    private Boolean startAfterFirstUse;
    private Integer periodLength;
    private Boolean status;
    private String title;
    private Integer numberSubscriptionsToGenerate;
    private Long userId;

    public SubscriptionEntity toEntity() {
        return SubscriptionEntity.builder()
                .totalFlow(this.totalFlow * 1024 * 1024 * 1024)
                .expireDate(this.expireDate)
                .startDate(this.startDate)
                .startAfterFirstUse(this.startAfterFirstUse)
                .periodLength(this.periodLength)
                .status(this.status)
                .title(this.title)
                .userId(this.userId)
                .build();
    }

    public SubscriptionEntity toEntity(SubscriptionEntity entity) {
        entity.setTotalFlow(this.totalFlow * 1024 * 1024 * 1024);
        entity.setExpireDate(this.expireDate);
        entity.setStartDate(this.startDate);
        entity.setStartAfterFirstUse(this.startAfterFirstUse);
        entity.setPeriodLength(this.periodLength);
        entity.setStatus(this.status);
        entity.setTitle(this.title);
        entity.setUserId(this.userId);
        return entity;
    }
}
