package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private Role role;
    private String token;
}
