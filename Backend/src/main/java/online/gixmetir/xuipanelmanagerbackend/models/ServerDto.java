package online.gixmetir.xuipanelmanagerbackend.models;

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
    private Boolean status;
    private Boolean isDeleted;

    public ServerDto(ServerEntity entity) {
        this.id = entity.getId();
        this.url = entity.getUrl();
        this.password = entity.getPassword();
        this.username = entity.getUsername();
        this.status = entity.getStatus();
        this.isDeleted = entity.getIsDeleted();
    }
}
