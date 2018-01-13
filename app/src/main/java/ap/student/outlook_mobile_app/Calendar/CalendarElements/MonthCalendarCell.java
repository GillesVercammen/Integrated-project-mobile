package ap.student.outlook_mobile_app.Calendar.CalendarElements;

import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Created by alek on 12/8/17.
 */

public class MonthCalendarCell {
    private int id;
    private boolean hasEvent = false;
    private Calendar date;
    private boolean isSelected = false;
    private TextView textView;

    public MonthCalendarCell(int id, TextView textView, Calendar date) {
        this.id = id;
        this.textView = textView;
        this.date = date;
    }

    public MonthCalendarCell(int id, TextView textView, Calendar date, boolean hasEvent) {
        this.id = id;
        this.textView = textView;
        this.date = date;
        this.hasEvent = hasEvent;
    }

    public MonthCalendarCell(int id, TextView textView, Calendar date, boolean hasEvent, boolean isSelected) {
        this.id = id;
        this.textView = textView;
        this.date = date;
        this.hasEvent = hasEvent;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHasEvent() {
        return hasEvent;
    }

    public void setHasEvent(boolean hasEvent) {
        this.hasEvent = hasEvent;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
