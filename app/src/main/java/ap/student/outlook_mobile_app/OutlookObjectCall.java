package ap.student.outlook_mobile_app;

/**
 * Created by alek on 11/28/17.
 */


public enum OutlookObjectCall {
    READUSER(""),
    READMAIL("/messages"),
    READCALENDAR("/calendar"),
    SENDMAIL("/messages");
    private String action;

    OutlookObjectCall(String action) {
        this.action = action;
    }

    public String action() {
        return action;
    }
}
