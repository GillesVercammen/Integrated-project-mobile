package ap.student.outlook_mobile_app.DAL.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Calendar;

import ap.student.outlook_mobile_app.DAL.MicrosoftDateFormat;

/**
 * Created by alek on 12/1/17.
 */

public class DateTimeTimeZone {
    private String dateTime;
    private String timeZone;

    private transient SimpleDateFormat microsoftDateFormat;

    public DateTimeTimeZone() {
        microsoftDateFormat = new MicrosoftDateFormat().getMicrosoftDateFormat();
    }

    public DateTimeTimeZone(String dateTime, String timeZone) {
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public java.util.Calendar getDateTime() {
        Calendar calendar = java.util.Calendar.getInstance();
        try {
            calendar.setTime(microsoftDateFormat.parse(dateTime.replaceAll("T", " ").substring(0, 19)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = microsoftDateFormat.format(dateTime.getTime());
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
