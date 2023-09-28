package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;

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
    private Long totalFlow;
    private Boolean isIndefiniteFlow;
    private Boolean isIndefiniteExpirationTime;
    private Integer periodLength;


    public UserEntity toEntity() {

        return UserEntity.builder()
                .address(this.address)
                .email(this.email)
                .role(this.role)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phoneNumber(this.phoneNumber)
                .enabled(this.enabled)
                .totalFlow(new Helper().GBToByte(this.totalFlow))
                .isIndefiniteFlow(this.isIndefiniteFlow)
                .isIndefiniteExpirationTime(this.isIndefiniteExpirationTime)
                .periodLength(this.periodLength)
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
        entity.setTotalFlow(new Helper().GBToByte(this.totalFlow));
        entity.setIsIndefiniteFlow(this.isIndefiniteFlow);
        entity.setIsIndefiniteExpirationTime(this.isIndefiniteExpirationTime);
        entity.setPeriodLength(this.periodLength);
        return entity;
    }
}
