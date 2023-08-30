package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import online.gixmetir.xuipanelmanagerbackend.models.Role;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String FirstName;
    @Column(name = "last_name")

    private String LastName;
    @Column(name = "email")
    private String Email;
    @Column(name = "phone_number")
    private String PhoneNumber;
    @Column(name = "address")
    private String Address;
    @Column(name = "password")
    private Role role;
}
