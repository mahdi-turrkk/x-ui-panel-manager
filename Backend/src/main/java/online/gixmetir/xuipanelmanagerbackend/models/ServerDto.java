package online.gixmetir.xuipanelmanagerbackend.models;

import jakarta.persistence.Column;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerDto {
    private Long id;
    private String url;
    private String username;
    private String password;
    private Boolean generatable;

    public ServerDto(ServerEntity entity) {
        this.id = entity.getId();
        this.url = entity.getUrl();
        this.generatable = entity.getGeneratable();
        this.password = entity.getPassword();
        this.username = entity.getUsername();
    }
}
