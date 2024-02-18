package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private String username;
    private String password;

}
