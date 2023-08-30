package online.gixmetir.xuipanelmanagerbackend.clients.models;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {
    private String username;
    private String password;
}
