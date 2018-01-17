package ap.student.outlook_mobile_app.Calendar.CalendarElements;

import ap.student.outlook_mobile_app.R;

/**
 * Created by alek on 12/21/17.
 */

public enum MonthsInTheYearEnum {
    JANUARY(R.string.january),
    FEBRUARY(R.string.february),
    MARCH(R.string.march),
    APRIL(R.string.april),
    MAY(R.string.may),
    JUNE(R.string.june),
    JULY(R.string.july),
    AUGUST(R.string.august),
    SEPTEMBER(R.string.september),
    OCTOBER(R.string.october),
    NOVEMBER(R.string.november),
    DECEMBER(R.string.december);

    private int value;

    private MonthsInTheYearEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
