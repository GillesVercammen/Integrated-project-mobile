package ap.student.outlook_mobile_app.Calendar.CalendarElements;

/**
 * Created by alek on 12/8/17.
 */

public enum ReminderMinutesBeforeStart {
    NONE(-1),
    ZERO_MINUTES(0),
    FIVE_MINUTES(5),
    FIFTEEN_MINUTES(15),
    THIRTY(30),
    ONE_HOUR(60),
    TWO_HOURS(120),
    THREE_HOURS(180),
    FOUR_HOURS(240),
    EIGHT_HOURS(480),
    TWELVE_HOURS(720),
    ONE_DAY(1440),
    TWO_DAYS(2880),
    THREE_DAYS(4320),
    ONE_WEEK(10080),
    TWO_WEEKS(20160);

    private final int value;
    private ReminderMinutesBeforeStart(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
