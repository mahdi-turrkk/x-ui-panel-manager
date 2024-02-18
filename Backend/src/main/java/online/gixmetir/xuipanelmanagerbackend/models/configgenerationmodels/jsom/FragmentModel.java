package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FragmentModel {
    public String packets;
    public String length;
    public String interval;
}
