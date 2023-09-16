package online.gixmetir.xuipanelmanagerbackend.utils;

import java.util.Random;
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

    public String getRandomString() {
        byte[] array = new byte[13]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array);

        System.out.println(generatedString);
        return generatedString;
    }

    public long GBToByte(int i) {
        return (long) i * 1024 * 1024 * 1024;
    }

    public String generateLink(String uuid) {
        return "https://subscription.gixmetir.online:6000/api/v1/subscriptions/" + uuid;
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
}
