package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "athentications")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "user-id", nullable = false)
    private Long userId;
//    @JoinColumn(name = "user-id", insertable = false, updatable = false)
//    @OneToOne(fetch = FetchType.LAZY)
//    private UserEntity userEntity;

}
