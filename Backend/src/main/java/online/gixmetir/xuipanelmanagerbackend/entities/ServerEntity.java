

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
public class ServerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", unique = true)
    private String url;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Boolean status;
    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
