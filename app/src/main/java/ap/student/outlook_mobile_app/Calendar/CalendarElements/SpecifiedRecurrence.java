package ap.student.outlook_mobile_app.Calendar.CalendarElements;

import ap.student.outlook_mobile_app.R;

/**
 * Created by alek on 12/15/17.
 */

public enum SpecifiedRecurrence {
    DAILY(R.id.specified_daily),
    WEEKLY(R.id.specified_weekly),
    MONTHLY_DAY(R.id.specified_monthly_day),
    MONTHLY_WEEK(R.id.specified_monthly_week),
    YEARLY_DAY(R.id.specified_yearly_day),
    YEARLY_WEEK(R.id.specified_yearly_week);

    int value;
    SpecifiedRecurrence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
