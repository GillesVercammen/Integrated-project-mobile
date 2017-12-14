package ap.student.outlook_mobile_app.Calendar.CalendarElements;

/**
 * Created by alek on 12/8/17.
 */

public enum ShowAs {
    FREE("Free"),
    TENTATIVE("Tentative"),
    BUSY("Busy"),
    OOF("Oof"),
    WORKING_ELSEWHERE("WorkingElseWhere"),
    UNKNOWN("Unknown");

    private String action;

    ShowAs (String action) { this.action = action; }

    public String action() {
        return action;
    }
}
