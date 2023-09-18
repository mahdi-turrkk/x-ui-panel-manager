package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KcpSettings {
    private int mtu;
    private int tti;
    private int uplinkCapacity;
    private int downlinkCapacity;
    private boolean congestion;
    private int readBufferSize;
    private int writeBufferSize;
    private Header header;
    private String seed;

}
