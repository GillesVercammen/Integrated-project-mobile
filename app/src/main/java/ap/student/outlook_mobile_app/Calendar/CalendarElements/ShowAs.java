package ap.student.outlook_mobile_app.Calendar.CalendarElements;

/**
 * Created by alek on 12/8/17.
 */

public enum ShowAs {
    FREE("free"),
    TENTATIVE("tentative"),
    BUSY("busy"),
    OOF("oof"),
    WORKING_ELSEWHERE("workingElsewhere"),
    UNKNOWN("unknown");

    private String action;

    ShowAs (String action) { this.action = action; }

    public String action() {
        return action;
    }
}
