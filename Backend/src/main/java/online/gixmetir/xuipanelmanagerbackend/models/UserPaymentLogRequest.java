package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.UserPaymentLogEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentLogRequest {
    private Long userId;
    private Double payAmount;

    public UserPaymentLogEntity toEntity() {
        return UserPaymentLogEntity.builder()
                .userId(userId)
                .payAmount(payAmount)
                .build();
    }

    public UserPaymentLogEntity toEntity(UserPaymentLogEntity entity) {
        entity.setUserId(userId);
        entity.setPayAmount(payAmount);
        return entity;
    }
}
