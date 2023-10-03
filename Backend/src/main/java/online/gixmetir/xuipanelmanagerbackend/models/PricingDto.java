package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.PricingEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingDto {
    private Long id;
    private Double price;
    private Long totalFlow;
    private Integer periodLength;

    public PricingDto(PricingEntity entity) {
        this.id = entity.getId();
        this.price = entity.getPrice();
        this.totalFlow = entity.getTotalFlow();
        this.periodLength = entity.getPeriodLength();
    }
}
