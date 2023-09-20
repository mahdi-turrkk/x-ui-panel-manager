package online.gixmetir.xuipanelmanagerbackend.utils;

import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static String getSessionKey(String header) {
        String clearnString = header.substring(1, header.length() - 1);
        String[] items = clearnString.split(";");
        return items[0];
    }

    public double byteToGB(Long byteNumber) {
        return (double) byteNumber / 1024 / 1024 / 1024;
    }


    public long GBToByte(Long i) {
        return i * 1024 * 1024 * 1024;
    }

    public String generateLink(String uuid) {

        String SUBSCRIPTION_URL = "https://panel.gixmetir.online:5001";
        return SUBSCRIPTION_URL + "/api/v1/subscriptions/client/" + uuid;
    }

    public String extractUuidFromLink(String link) throws Exception {

        // Define a regular expression pattern to match UUID
        Pattern pattern = Pattern.compile("/([0-9a-fA-F\\-]+)$");

        // Create a Matcher object
        Matcher matcher = pattern.matcher(link);

        // Find the UUID in the URL
        if (matcher.find()) {
            String uuid = matcher.group(1);
            System.out.println("Extracted UUID: " + uuid);
            return uuid;
        } else {
            throw new Exception("link is invalid");
        }
    }

    public UserEntity getUserFromContext() throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw new Exception("anonymous user requested");
        }
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
