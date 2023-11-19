package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServerRequest {
    private String url;
    private String username;
    private String password;
    private Boolean generatable;
    private Boolean status;
    private Boolean isDeleted;

    public ServerEntity toEntity() {
        return ServerEntity.builder()
                .url(this.url)
                .username(this.username)
                .password(this.password)
                .status(this.status)
                .status(this.isDeleted)
                .build();
    }

    public ServerEntity toEntity(ServerEntity entity) {
        entity.setPassword(this.getPassword());
        entity.setUsername(this.getUsername());
        entity.setUrl(this.getUrl());
        entity.setStatus(this.getStatus());
        entity.setIsDeleted(this.getIsDeleted());
        return entity;
    }
}
