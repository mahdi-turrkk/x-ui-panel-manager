package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels;

import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetInboundsResponse extends ResponseBase {
    private List<InboundEntity> obj;
}
