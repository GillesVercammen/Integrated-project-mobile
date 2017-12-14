package ap.student.outlook_mobile_app.DAL.enums;

/**
 * Created by alek on 12/14/17.
 */

public enum RecurrenceRangeType {
    ENDDATE("endDate"),
    NOEND("noEnd"),
    NUMBERED("numbered");
    private String value;

    RecurrenceRangeType(String recurrenceType) {
        value = recurrenceType;
    }

    public String value() {
        return value;
    }
}
