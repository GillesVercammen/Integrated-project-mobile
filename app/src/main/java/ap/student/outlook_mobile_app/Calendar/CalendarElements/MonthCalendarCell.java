package ap.student.outlook_mobile_app.Calendar.CalendarElements;

import java.time.LocalDateTime;

/**
 * Created by alek on 12/8/17.
 */

public class MonthCalendarCell {
    private int Id;
    private boolean hasEvent;
    private LocalDateTime dateTime;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
}
