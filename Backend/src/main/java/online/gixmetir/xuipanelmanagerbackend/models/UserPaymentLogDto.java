package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.UserPaymentLogEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentLogDto {
    private Long id;
    private Long userId;
    private UserEntity user;
    private LocalDateTime date;
    private LocalDateTime lastModifiedDate;
    private Double payAmount;

    public UserPaymentLogDto(UserPaymentLogEntity userPaymentLogEntity) {
        this.id = userPaymentLogEntity.getId();
        this.userId = userPaymentLogEntity.getUserId();
        this.user = userPaymentLogEntity.getUser();
        this.date = userPaymentLogEntity.getDate();
        this.lastModifiedDate = userPaymentLogEntity.getLastModifiedDate();
        this.payAmount = userPaymentLogEntity.getPayAmount();
    }
}
