package ap.student.outlook_mobile_app.DAL.models;

/**
 * Created by alek on 12/1/17.
 */

public class PatternedRecurrence {
    private RecurrencePattern pattern;
    private RecurrenceRange range;

    public RecurrencePattern getPattern() {
        return pattern;
    }

    public void setPattern(RecurrencePattern pattern) {
        this.pattern = pattern;
    }

    public RecurrenceRange getRange() {
        return range;
    }

    public void setRange(RecurrenceRange range) {
        this.range = range;
    }
}
