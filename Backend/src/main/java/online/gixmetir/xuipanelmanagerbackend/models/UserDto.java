package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import org.hibernate.Hibernate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;
    private String username;
    private Boolean enabled;


    public UserDto(UserEntity entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.address = entity.getAddress();
        this.role = entity.getRole();
        this.username = entity.getAuthentication() != null ? entity.getAuthentication().getUsername() : null;
        this.enabled = entity.isEnabled();
    }
}
