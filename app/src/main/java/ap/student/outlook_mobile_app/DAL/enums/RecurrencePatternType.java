package ap.student.outlook_mobile_app.DAL.enums;

/**
 * Created by alek on 12/14/17.
 */

public enum RecurrencePatternType {
    DAILY("daily"),
    WEEKLY("weekly"),
    ABSOLUTEMONTHLY("absoluteMonthly"),
    RELATIVEMONTHLY("relativeMonthly"),
    ABSOLUTEYEARLY("absoluteYearly"),
    RELATIVEYEARLY("relativeYearly");
    String value;

    RecurrencePatternType (String value) {
        this.value = value;
    }

    public String value() { return value; }
}
