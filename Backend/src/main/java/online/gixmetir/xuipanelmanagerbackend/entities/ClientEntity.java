package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "clients")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "inbound-id", nullable = false)
    private Long inboundId;
    @ManyToOne
    @JoinColumn(name = "inbound-id", insertable = false, updatable = false)
    private InboundEntity inbound;
    @Column(name = "subscription-id", nullable = false)
    private Long subscriptionId;
    @ManyToOne
    @JoinColumn(name = "subscription-id", insertable = false, updatable = false)
    private SubscriptionEntity subscription;

    @Column(name = "limit-ip")
    private Integer limitIp;
    @Column(name = "expire-date")
    private LocalDate expireDate;
    @Column(name = "total-gb")
    private Long totalGb;
    @Column(name = "email")
    private String email;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "alter-id")
    private Integer alterId;
    @Column(name = "security")
    private String security;
    @Column(name = "encryption")
    private String encryption;
    @Column(name = "flow")
    private String flow;
    @Column(name = "up")
    private Long up;
    @Column(name = "down")
    private Long down;
    @Column(name = "total-used")
    private long totalUsed;
    @Column(name = "send_to_user")
    private Boolean sendToUser;


}
