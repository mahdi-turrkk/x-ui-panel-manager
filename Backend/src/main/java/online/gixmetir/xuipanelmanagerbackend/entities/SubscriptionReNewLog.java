package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions-renew-log")
public class SubscriptionReNewLog {
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
    @Column(name = "total-gb")
    private Long totalGb;
    @Column(name = "period-length")
    private Long periodLength;
}
