package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.models.OS;

@Entity
@Table(name = "os_settings")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OsSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "os")
    private OS os;
    @Column(name = "clients", length = 200)
    private String clients;
    @Column(name = "apply-fragment")
    private Boolean applyFragment;
    @Column(name = "fragment-interval")
    private String fragmentInterval;
    @Column(name = "fragment-length")
    private String fragmentLength;
    @Column(name = "packets")
    private String packets;
    @Column(name = "generate-json")
    private Boolean generateJson;
    @Column(name = "generate-v2ray-link")
    private Boolean generateV2rayLink;
    @Column(name = "global-setting-id")
    private Long globalSettingId;

    @ManyToOne
    @JoinColumn(name = "global-setting-id", insertable = false, updatable = false)
    private GlobalSettingEntity globalSettingEntity;
}
