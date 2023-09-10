package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions-renew-log")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionReNewLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subscription-id", nullable = false)
    private Long subscriptionId;
    @ManyToOne
    @JoinColumn(name = "subscription-id", insertable = false, updatable = false)
    private SubscriptionEntity subscription;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "total-flow")
    private Long totalFlow;
    @Column(name = "period-length")
    private Integer periodLength;
}
