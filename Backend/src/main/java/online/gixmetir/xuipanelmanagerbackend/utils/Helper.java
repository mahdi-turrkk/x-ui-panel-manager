package online.gixmetir.xuipanelmanagerbackend.utils;

import com.google.gson.Gson;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.exceptions.UsernameOrPasswordWrongException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static String getSessionKey(String header) {
        String clearnString = header.substring(1, header.length() - 1);
        String[] items = clearnString.split(";");
        return items[0];
    }

    public double byteToGB(Long byteNumber) {
        if (byteNumber == null) return 0;
        return (double) byteNumber / 1024 / 1024 / 1024;
    }


    public long GBToByte(Long i) {
        if (i == null) return 0;
        return i * 1024 * 1024 * 1024;
    }

    public String generateLink(String uuid) {

        String SUBSCRIPTION_URL = "https://panel.privado-vpn.online:5001";
        return SUBSCRIPTION_URL + "/api/v1/subscriptions/client/" + uuid;
    }

    public String extractUuidFromSubscriptionLink(String link) throws Exception {

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
            throw new IllegalArgumentException("link is invalid");
        }
    }

    public String extractUuidFromConfig(String config) throws Exception {
        if (config.contains("vless") || config.contains("Vless")) {
            // Define a regular expression pattern to match UUID
            // Define the regular expression pattern to match the UUID
            Pattern pattern = Pattern.compile("vless://([a-f0-9-]+)@");

            // Create a Matcher to find the pattern in the URI
            Matcher matcher = pattern.matcher(config);

            // Check if the pattern is found
            if (matcher.find()) {
                // Extract and print the UUID
                String uuid = matcher.group(1);
                return uuid;
            } else {
                System.out.println("UUID not found in the VLESS URI.");
            }
            throw new IllegalArgumentException("config is invalid");
        } else if (config.contains("vmess") || config.contains("Vmess")) {
            String encoded = config.split("://")[1];
            String decoded = new String(Base64.getDecoder().decode(encoded));
            Gson gson = new Gson();
            HashMap map = gson.fromJson(decoded, HashMap.class);

            if (map.get("id") != null) return map.get("id").toString();
            throw new IllegalArgumentException("config is invalid");
        }
        return null;

    }

    public UserEntity getUserFromContext() throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw new UsernameOrPasswordWrongException("anonymous user requested");
        }
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
