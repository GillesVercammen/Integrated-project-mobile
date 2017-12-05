package ap.student.outlook_mobile_app.DAL;

/**
 * Created by alek on 11/28/17.
 */


public enum OutlookObjectCall {
    READUSER(""),
    READMAIL("/messages"),
    READCALENDARS("/calendars"),
    READEVENTS("/events"),
    SENDMAIL("/sendMail");
    private String action;

    OutlookObjectCall(String action) {
        this.action = action;
    }

    public String action() {
        return action;
    }
}
