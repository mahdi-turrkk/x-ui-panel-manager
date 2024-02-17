package online.gixmetir.xuipanelmanagerbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "global_settings")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subscription-url")
    private String subscriptionUrl;
    @OneToMany(mappedBy = "globalSettingEntity")
    private List<OsSettingEntity> osSettingEntityList;
}
