package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sniffing {
    private boolean enabled;
    private List<String> destOverride;
    public boolean routeOnly;

}
