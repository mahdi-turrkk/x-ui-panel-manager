package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uuid")
    private String Uuid;
    @Column(name = "total_flow")
    private Long totalFlow;
    @Column(name = "remaining_flow")
    private Long remainingFlow;
    @Column(name = "total-used")
    private Long totalUsed;
    @Column(name = "upload")
    private Long upload;
    @Column(name = "download")
    private Long download;
    @Column(name = "expire-date")
    private LocalDate expireDate;
    @Column(name = "start-date")
    private LocalDate startDate;
    @Column(name = "start-after-first-use")
    private Boolean startAfterFirstUse;
    @Column(name = "period-length")
    private Integer periodLength;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "title")
    private String title;

}
