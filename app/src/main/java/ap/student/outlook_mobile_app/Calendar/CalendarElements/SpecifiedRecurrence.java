package ap.student.outlook_mobile_app.Calendar.CalendarElements;

import ap.student.outlook_mobile_app.R;

/**
 * Created by alek on 12/15/17.
 */

public enum SpecifiedRecurrence {
    DAILY(R.layout.specified_daily),
    WEEKLY(R.layout.specified_weekly),
    MONTHLY_DAY(R.layout.specified_monthly_day),
    MONTHLY_WEEK(R.layout.specified_daily),
    YEARLY_DAY(R.layout.specified_daily),
    YEARLY_WEEK(R.layout.specified_daily);

    int value;
    SpecifiedRecurrence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
