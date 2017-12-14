package ap.student.outlook_mobile_app.Calendar.CalendarElements;

/**
 * Created by alek on 12/8/17.
 */

public enum Recurrence {
    NEVER("never"),
    DAILY("daily"),
    EVERY_FRIDAY("every_friday"),
    EVERY_WORKDAY("every_workday"),
    EACH_MONTH_TODAY("each_month_today"),
    EVERY_SECOND_FRIDAY("every_second_friday"),
    EVERY_YEAR("everyYear"),
    MORE("more");

    private String action;

    Recurrence(String action) {
        this.action = action;
    }

    public String action() {
        return action;
    }
}
