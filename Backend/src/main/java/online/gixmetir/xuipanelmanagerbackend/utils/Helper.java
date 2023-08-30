package online.gixmetir.xuipanelmanagerbackend.utils;

public class Helper {
    public static String getSessionKey(String header) {
        String clearnString = header.substring(1, header.length() - 1);
        String[] items = clearnString.split(";");
        return items[0];
    }
    public double byteToGB(Long byteNumber) {
        return (double) byteNumber / 1024 / 1024 / 1024;
    }
}
