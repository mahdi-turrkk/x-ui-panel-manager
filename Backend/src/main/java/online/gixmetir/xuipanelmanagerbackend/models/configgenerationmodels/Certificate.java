package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    private String certificateFile;
    private String keyFile;
    private String ocspStapling;

}
