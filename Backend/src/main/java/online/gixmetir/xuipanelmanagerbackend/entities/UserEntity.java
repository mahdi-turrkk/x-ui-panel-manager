package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.models.Role;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "enabled")
    private Boolean enabled;
    @Column(name = "authentication_id")
    private Long authenticationId;
    @Column(name = "is_indefinite_flow")
    private Boolean isIndefiniteFlow;
    @Column(name = "total_flow")
    private Long totalFlow;
    @Column(name = "total_used")
    private Long totalUsed;
    @Column(name = "is_indefinit_expiration_time")
    private Boolean isIndefiniteExpirationTime;
//    @Column(name = "period_length")
//    private Integer periodLength;
    @Column(name = "expiration_date_time")
    private LocalDateTime expirationDateTime;
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;
    @Column(name = "price_per_gb")
    private Double pricePerGb;
    @Column(name = "payed_amount")
    private Long payedAmount;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authentication_id", insertable = false, updatable = false)
    private AuthenticationEntity authentication;
    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(name = "last_updated-date")
    private LocalDateTime lastUpdatedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name())); // Replace "ROLE_ADMIN" with your desired authority
        return authorities;
    }

    @Override
    public String getPassword() {
        return authentication.getPassword();
    }

    @Override
    public String getUsername() {
        return authentication.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
