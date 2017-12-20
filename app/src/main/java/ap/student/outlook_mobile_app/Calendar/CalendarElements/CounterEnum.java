package ap.student.outlook_mobile_app.Calendar.CalendarElements;

/**
 * Created by alek on 12/15/17.
 */

public enum CounterEnum {
    FIRST("first"),
    SECOND("second"),
    THIRD("third"),
    FOURTH("fourth"),
    LAST("last");
    String value;

    CounterEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
