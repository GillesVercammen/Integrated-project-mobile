package ap.student.outlook_mobile_app.Calendar.CalendarElements;

import android.widget.TextView;

import java.time.LocalDateTime;

/**
 * Created by alek on 12/8/17.
 */

public class MonthCalendarCell {
    private int id;
    private boolean hasEvent = false;
    private LocalDateTime dateTime;
    private boolean isSelected = false;
    private TextView textView;

    public MonthCalendarCell(int id, TextView textView, LocalDateTime dateTime) {
        this.id = id;
        this.textView = textView;
        this.dateTime = dateTime;
    }

    public MonthCalendarCell(int id, TextView textView, LocalDateTime dateTime, boolean hasEvent) {
        this.id = id;
        this.textView = textView;
        this.dateTime = dateTime;
        this.hasEvent = hasEvent;
    }

    public MonthCalendarCell(int id, TextView textView, LocalDateTime dateTime, boolean hasEvent, boolean isSelected) {
        this.id = id;
        this.textView = textView;
        this.dateTime = dateTime;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
