

package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "servers")
public class ServerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url")
    private String url;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "generatable")
    private Boolean generatable;

}