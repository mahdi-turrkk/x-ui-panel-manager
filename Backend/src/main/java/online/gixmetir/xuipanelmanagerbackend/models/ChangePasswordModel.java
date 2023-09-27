package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordModel {
    private Long userId;
    private String newPassword;
    private String oldPassword;
}
