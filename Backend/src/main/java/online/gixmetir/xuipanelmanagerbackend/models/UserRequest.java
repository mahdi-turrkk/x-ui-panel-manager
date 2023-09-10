package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;
    private String password;
    private String username;
    private Boolean enabled;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .address(this.address)
                .email(this.email)
                .role(this.role)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phoneNumber(this.phoneNumber)
                .enabled(this.enabled)
                .build();
    }

    public UserEntity toEntity(UserEntity entity) {
        entity.setAddress(this.address);
        entity.setEmail(this.email);
        entity.setRole(this.role);
        entity.setFirstName(this.firstName);
        entity.setLastName(this.lastName);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setEnabled(this.enabled);
        return entity;
    }
}
