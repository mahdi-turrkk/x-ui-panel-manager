package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HttpSettings {
    private String path;
    private List<String> host;

}
