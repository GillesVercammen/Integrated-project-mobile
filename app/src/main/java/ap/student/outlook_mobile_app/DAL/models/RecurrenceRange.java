package ap.student.outlook_mobile_app.DAL.models;

import java.time.LocalDate;

/**
 * Created by alek on 12/1/17.
 */

public class RecurrenceRange {
    private LocalDate endDate;
    private int numberOfOccurrences;
    private String recurrenceTimeZone;
    private LocalDate startDate;
    private String type;

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
