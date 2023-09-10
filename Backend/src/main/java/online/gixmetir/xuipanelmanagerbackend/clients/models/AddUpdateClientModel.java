package online.gixmetir.xuipanelmanagerbackend.clients.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUpdateClientModel {
    public Long id;
    public InboundSettingModel
            settings;

}
