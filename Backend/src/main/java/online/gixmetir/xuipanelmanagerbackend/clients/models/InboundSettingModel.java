package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class InboundSettingModel {
    @JsonProperty("clients")
    private ClientModel[] clients;
    @JsonProperty("decryption")
    private String decryption;
    @JsonProperty("fallbacks")
    private String[] fallbacks;

    public static InboundSettingModel fromJson(String settings) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser parser = jsonFactory.createJsonParser(settings.toString());
        JsonNode actualObj = objectMapper.readTree(parser);
        InboundSettingModel inboundSetting = objectMapper.readValue(actualObj.toString(), InboundSettingModel.class);
        return inboundSetting;
    }

}
