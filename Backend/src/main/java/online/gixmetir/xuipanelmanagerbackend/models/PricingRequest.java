package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.PricingEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingRequest {
    private Double price;
    private Long totalFlow;
    private Integer periodLength;


    public PricingEntity toEntity() {
        return PricingEntity.builder()
                .price(price)
                .totalFlow(totalFlow)
                .periodLength(periodLength)
                .build();
    }

    public PricingEntity toEntity(PricingEntity entity) {
        entity.setPrice(price);
        entity.setPeriodLength(periodLength);
        entity.setTotalFlow(totalFlow);
        return entity;
    }
}
