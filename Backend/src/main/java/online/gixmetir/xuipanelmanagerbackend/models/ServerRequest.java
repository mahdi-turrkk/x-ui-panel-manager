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

    public ServerEntity toEntity() {
        return ServerEntity.builder()
                .url(this.url)
                .generatable(this.generatable)
                .username(this.username)
                .password(this.password)
                .build();
    }

    public ServerEntity toEntity(ServerEntity entity) {
        entity.setGeneratable(this.getGeneratable());
        entity.setPassword(this.getPassword());
        entity.setUsername(this.getUsername());
        entity.setUrl(this.getUrl());
        return entity;
    }
}
