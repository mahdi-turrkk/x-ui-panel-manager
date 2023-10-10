package online.gixmetir.xuipanelmanagerbackend.models;

import jakarta.persistence.*;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionRenewLogEntity;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRenewDto {
    private Long id;
    private Long subscriptionId;
    private LocalDateTime date;
    private Double totalFlow;
    private Integer periodLength;
    private Double price;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private Boolean markAsPaid;

    public SubscriptionRenewDto(SubscriptionRenewLogEntity subscriptionRenewLogEntity) {
        this.id = subscriptionRenewLogEntity.getId();
        this.subscriptionId = subscriptionRenewLogEntity.getSubscription().getId();
        this.date = subscriptionRenewLogEntity.getDate();
        this.totalFlow = new Helper().byteToGB(subscriptionRenewLogEntity.getTotalFlow());
        this.periodLength = subscriptionRenewLogEntity.getPeriodLength();
        this.price = subscriptionRenewLogEntity.getPrice();
        this.createDate = subscriptionRenewLogEntity.getCreateDate();
        this.lastModifiedDate = subscriptionRenewLogEntity.getLastModifiedDate();
        this.markAsPaid = subscriptionRenewLogEntity.getMarkAsPaid();
    }
}
