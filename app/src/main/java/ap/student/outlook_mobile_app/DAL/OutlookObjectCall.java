package ap.student.outlook_mobile_app.DAL;

/**
 * Created by alek on 11/28/17.
 */


public enum OutlookObjectCall {
    READUSER(""),
    READMAIL("/mailfolders"),
    READCALENDAR("/calendars"),
    READEVENTS("/events"),
    SENDMAIL("/sendMail"),
    UPDATEMAIL("/messages"),
    POSTEVENT("/events");
    private String action;

    OutlookObjectCall(String action) {
        this.action = action;
    }

    public String action() {
        return action;
    }
}
