package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.PlanEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanRequest {
    private Double price;
    private Long totalFlow;
    private Integer periodLength;


    public PlanEntity toEntity() {
        return PlanEntity.builder()
                .price(price)
                .totalFlow(totalFlow)
                .periodLength(periodLength)
                .build();
    }

    public PlanEntity toEntity(PlanEntity entity) {
        entity.setPrice(price);
        entity.setPeriodLength(periodLength);
        entity.setTotalFlow(totalFlow);
        return entity;
    }
}
