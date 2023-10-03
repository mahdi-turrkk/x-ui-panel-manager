package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "servers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PricingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "price")
    private Double price;
    @Column(name = "total_flow")
    private Long totalFlow;
    @Column(name = "period_lenght")
    private Integer periodLength;
}
