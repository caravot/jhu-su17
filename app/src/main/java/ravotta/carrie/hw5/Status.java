package ravotta.carrie.hw5;

public enum Status {
    PENDING("PENDING"),
    DONE("DONE"),
    DUE("DUE");

    private String displayName;

    private Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
