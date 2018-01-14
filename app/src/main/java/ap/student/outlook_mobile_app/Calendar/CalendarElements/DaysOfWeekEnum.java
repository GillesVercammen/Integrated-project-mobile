package ap.student.outlook_mobile_app.Calendar.CalendarElements;

/**
 * Created by alek on 14.01.18.
 */

public enum DaysOfWeekEnum {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String value;

    private DaysOfWeekEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
