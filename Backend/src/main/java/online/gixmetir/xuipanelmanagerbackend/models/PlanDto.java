package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.PlanEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanDto {
    private Long id;
    private Double price;
    private Long totalFlow;
    private Integer periodLength;

    public PlanDto(PlanEntity entity) {
        this.id = entity.getId();
        this.price = entity.getPrice();
        this.totalFlow = entity.getTotalFlow();
        this.periodLength = entity.getPeriodLength();
    }
}
