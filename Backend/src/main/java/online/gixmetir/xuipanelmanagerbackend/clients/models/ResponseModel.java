package online.gixmetir.xuipanelmanagerbackend.clients.models;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel {
    private Boolean success;
    private String msg;
    private String obj;
}
