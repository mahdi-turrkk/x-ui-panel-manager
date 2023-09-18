package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuicSettings {
    private String security;
    private String key;
    private Header header;
}
