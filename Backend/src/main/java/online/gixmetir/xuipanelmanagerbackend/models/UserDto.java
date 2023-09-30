package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;

import java.time.LocalDateTime;

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
    private double totalUsed;
    private double totalFlow;
    private LocalDateTime expirationDateTime;
    private LocalDateTime startDateTime;


    public UserDto(UserEntity entity) {
        Helper helper = new Helper();
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.address = entity.getAddress();
        this.role = entity.getRole();
        this.username = entity.getAuthentication() != null ? entity.getAuthentication().getUsername() : null;
        this.enabled = entity.isEnabled();
        this.totalUsed = helper.byteToGB(entity.getTotalUsed());
        this.totalFlow = helper.byteToGB(entity.getTotalFlow());
        this.expirationDateTime = entity.getExpirationDateTime();
        this.startDateTime = entity.getStartDateTime();
    }
}
