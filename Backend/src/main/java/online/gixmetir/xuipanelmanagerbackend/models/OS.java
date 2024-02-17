package online.gixmetir.xuipanelmanagerbackend.models;

public enum OS {
    IOS(0),
    LINUX(1),
    WINDOWS(2),
    ANDROID(2);
    private final int number;

    // Constructor to initialize the day number
    OS(int number) {
        this.number = number;
    }

    // Getter method to retrieve the day number
    public int getNumber() {
        return number;
    }

}
