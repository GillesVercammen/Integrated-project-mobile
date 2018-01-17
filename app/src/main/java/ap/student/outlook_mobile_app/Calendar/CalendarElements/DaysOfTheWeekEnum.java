package ap.student.outlook_mobile_app.Calendar.CalendarElements;

import android.content.res.Resources;

import com.bumptech.glide.load.engine.Resource;

import ap.student.outlook_mobile_app.R;

/**
 * Created by alek on 12/21/17.
 */

public enum DaysOfTheWeekEnum {
    SUNDAY(R.string.sunday),
    MONDAY(R.string.monday),
    TUESDAY(R.string.tuesday),
    WEDNESDAY(R.string.wednesday),
    THURSDAY(R.string.thursday),
    FRIDAY(R.string.friday),
    SATURDAY(R.string.saturday);

    private int value;

    private DaysOfTheWeekEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
