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
public class SubscriptionDto {
    private Long id;

    private Double totalFlow;
    private Long totalUsed;
    private LocalDateTime expireDate;
    private LocalDateTime startDate;
    private Integer periodLength;
    private Boolean status;
    private String title;
    private String link;

    public SubscriptionDto(SubscriptionEntity subscriptionEntity) {
        this.id = subscriptionEntity.getId();
        this.totalFlow = new Helper().byteToGB(subscriptionEntity.getTotalFlow());
        this.totalUsed = subscriptionEntity.getTotalUsed();
        this.expireDate = subscriptionEntity.getExpireDate();
        this.startDate = subscriptionEntity.getStartDate();
        this.periodLength = subscriptionEntity.getPeriodLength();
        this.status = subscriptionEntity.getStatus();
        this.title = subscriptionEntity.getTitle();
        this.link = new Helper().generateLink(subscriptionEntity.getUuid());
    }

}
