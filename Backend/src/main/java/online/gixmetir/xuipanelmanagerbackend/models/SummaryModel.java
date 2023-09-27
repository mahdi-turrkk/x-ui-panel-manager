package online.gixmetir.xuipanelmanagerbackend.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryModel {
    private double totalUpload;
    private double totalDownload;
    private double totalSold;
    private double totalLastMonthSold;
}
