package ap.student.outlook_mobile_app.DAL.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import ap.student.outlook_mobile_app.DAL.MicrosoftDateFormat;

/**
 * Created by alek on 12/1/17.
 */

public class RecurrenceRange {
    private String endDate;
    private int numberOfOccurrences;
    private String recurrenceTimeZone;
    private String startDate;
    private String type;

    private SimpleDateFormat microsoftDateFormat;

    public RecurrenceRange() {
        microsoftDateFormat = new MicrosoftDateFormat().getMicrosoftDateFormat();
    }

    public LocalDate getEndDate() {
        return LocalDate.parse(endDate);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate.toString();
    }

    public int getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    public void setNumberOfOccurrences(int numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public String getRecurrenceTimeZone() {
        return recurrenceTimeZone;
    }

    public void setRecurrenceTimeZone(String recurrenceTimeZone) {
        this.recurrenceTimeZone = recurrenceTimeZone;
    }

    public java.util.Calendar getStartDate() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        try {
            calendar.setTime(microsoftDateFormat.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = microsoftDateFormat.format(startDate.getTime());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
