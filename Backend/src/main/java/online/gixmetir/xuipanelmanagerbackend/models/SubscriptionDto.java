package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long id;

    private Long totalFlow;
    private Long totalUsed;
    private LocalDate expireDate;
    private LocalDate startDate;
    private Integer periodLength;
    private Boolean status;
    private String title;
    private String link;

    public SubscriptionDto(SubscriptionEntity subscriptionEntity) {
        this.id = subscriptionEntity.getId();
        this.totalFlow = subscriptionEntity.getTotalFlow();
        this.totalUsed = subscriptionEntity.getTotalUsed();
        this.expireDate = subscriptionEntity.getExpireDate();
        this.startDate = subscriptionEntity.getStartDate();
        this.periodLength = subscriptionEntity.getPeriodLength();
        this.status = subscriptionEntity.getStatus();
        this.title = subscriptionEntity.getTitle();
        this.link = new Helper().generateLink(subscriptionEntity.getUuid());
    }

}
