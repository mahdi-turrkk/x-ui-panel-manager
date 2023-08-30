package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.eloyzone.jalalicalendar.DateConverter;
import com.github.eloyzone.jalalicalendar.JalaliDate;
import com.github.eloyzone.jalalicalendar.JalaliDateFormatter;
import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientStats {
    @JsonProperty("id")
    private String id;
    @JsonProperty("inboundId")
    private String inboundId;
    @JsonProperty("enable")
    private Boolean enable;
    @JsonProperty("email")
    private String email;
    @JsonProperty("up")
    private String up;
    @JsonProperty("down")
    private String down;
    @JsonProperty("expiryTime")
    private String expiryTime;
    @JsonProperty("total")
    private String total;

    @Override
    public String toString() {
        String clientName = "کاربر: " + email;
        DecimalFormat decimalFormat = new DecimalFormat("#0.000");
        Helper helper = new Helper();

        String enableStr = "فعال/ غیر فعال: ";
        enableStr += enable ? "✅" + "فعال" + "✅" : "⚠\uFE0F" + "غیر فعال" + "⚠\uFE0F";
        long uploadAmount = Long.parseLong(up);
        long downloadAmount = Long.parseLong(down);
        long totalAmount = Long.parseLong(total);
        double upGb = helper.byteToGB(uploadAmount);
        double downGb = helper.byteToGB(downloadAmount);
        String upload = "حجم آپلود شده: " + decimalFormat.format(upGb) + " GB";
        String download = "حجم دانلود شده: " + decimalFormat.format(downGb) + " GB";
        String totalUsageGb = "حجم کل مصرف شده: " + decimalFormat.format(helper.byteToGB(uploadAmount + downloadAmount)) + " GB";
        String totalGb = "حجم کل : " + decimalFormat.format(helper.byteToGB(totalAmount)) + " GB";

        long expMilliSecond = Long.parseLong(expiryTime);
        String exp = "اعتبار تا تاریخ : ";
        String extraDescription = "\nتوضیحات: \n";
        if (totalAmount <= uploadAmount + downloadAmount) {
            extraDescription += "حجم اختصاص یافته تمام شده است" + "\n";
        }
        if (expMilliSecond <= 0) {
            exp += "نامعلوم";
        } else {
            DateConverter dateConverter = new DateConverter();
            Instant instant = Instant.ofEpochMilli(expMilliSecond);
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            JalaliDate jalaliDate = dateConverter.gregorianToJalali(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());
            String result = jalaliDate.format(new JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_PERSIAN));
            if (localDate.compareTo(LocalDate.now(ZoneId.systemDefault())) < 1) {
                extraDescription += "منقضی شده در تاریخ " + result + "\n";
            }
            exp += result;

        }

        return clientName + "\n" + enableStr + "\n" + upload + "\n" + download + "\n" + totalUsageGb + "\n" + totalGb + "\n" + exp + "\n" + extraDescription + "\n\t";

    }
}
