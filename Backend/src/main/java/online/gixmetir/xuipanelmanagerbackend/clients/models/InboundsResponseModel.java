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
public class InboundsResponseModel {
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("obj")
    private Inbound[] obj;
    public static InboundsResponseModel fromJson(StringBuilder settings) throws IOException {
//        settings.replace(0, 1, "");
//        settings.replace(settings.length() - 1, settings.length(), "");
        String jsonObj = settings.toString().replaceAll("\\\\n", "");
        jsonObj = jsonObj.replaceAll("\\\\", "");
        System.out.println(jsonObj);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser parser = jsonFactory.createJsonParser(jsonObj);
        JsonNode actualObj = objectMapper.readTree(parser);
        InboundsResponseModel inboundSetting = objectMapper.readValue(actualObj.toString(), InboundsResponseModel.class);
        return inboundSetting;
    }

}
