package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryModel {
    private Long totalUpload;
    private Long totalDownload;
    private Long totalSold;
    private Long totalLastMonthSold;
}
