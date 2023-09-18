package online.gixmetir.xuipanelmanagerbackend.entities;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels.StreamSettings;

import java.io.IOException;

@Entity
@Table(name = "inbounds")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InboundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server-id", nullable = false)
    private Long serverId;
    @ManyToOne
    @JoinColumn(name = "server-id", insertable = false, updatable = false)
    private ServerEntity server;
    @Column(name = "generatable")
    private Boolean generatable;
    @Column(name = "id-from-panel")
    private Long idFromPanel;
    @Column(name = "tag")
    private String tag;
    @Column(name = "port")
    private String port;
    @Column(name = "listen")
    private String listen;
    @Column(name = "protocol")
    private String protocol;
    @Column(name = "up")
    private Long up;
    @Column(name = "down")
    private Long down;
    @Column(name = "total-used")
    private Long totalUsed;
    @Column(name = "remark")
    private String remark;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "expire-date")
    private String expireDate;
    @Column(name = "setting")
    private String setting;
    @Column(name = "stream-settings")
    private String streamSettings;
    @Column(name = "sniffing")
    private String sniffing;


    public StreamSettings getStreamSettingsObj() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser parser = jsonFactory.createParser(streamSettings);
        JsonNode actualObj = objectMapper.readTree(parser);
        return objectMapper.readValue(actualObj.toString(), StreamSettings.class);
    }
}
