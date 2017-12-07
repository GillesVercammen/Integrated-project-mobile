package ap.student.outlook_mobile_app.DAL.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by alek on 12/1/17.
 */

public class DateTimeTimeZone {
    private String dateTime;
    private String timeZone;

    public DateTimeTimeZone(String dateTime, String timeZone) {
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.parse(dateTime);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime.toString();
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
