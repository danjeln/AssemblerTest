package datalog;

public class Datalog {
    private String description;
    private String oldValue;
    private String newValue;

    public Datalog(String description, String oldValue, String newValue) {
        this.description = description;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getDescription() {
        return description;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    @Override
    public String toString() {
        return "Datalog{" +
                "description='" + description + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
