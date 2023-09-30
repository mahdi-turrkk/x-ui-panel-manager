package online.gixmetir.xuipanelmanagerbackend.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientLogResponseModel {
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("obj")
    private ClientStatsModel obj;
//    public static ClientLogResponseModel fromJson(StringBuilder settings) throws IOException {
////        settings.replace(0, 1, "");
////        settings.replace(settings.length() - 1, settings.length(), "");
//        String jsonObj = settings.toString().replaceAll("\\\\n", "");
//        jsonObj = jsonObj.replaceAll("\\\\", "");
//        System.out.println(jsonObj);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonFactory jsonFactory = objectMapper.getFactory();
//        JsonParser parser = jsonFactory.createJsonParser(jsonObj);
//        JsonNode actualObj = objectMapper.readTree(parser);
//        ClientLogResponseModel inboundSetting = objectMapper.readValue(actualObj.toString(), ClientLogResponseModel.class);
//        return inboundSetting;
//    }

}
