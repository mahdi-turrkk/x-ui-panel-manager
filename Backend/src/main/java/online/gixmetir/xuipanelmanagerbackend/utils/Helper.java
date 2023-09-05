package online.gixmetir.xuipanelmanagerbackend.utils;

import java.nio.charset.Charset;
import java.util.Random;

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
}
