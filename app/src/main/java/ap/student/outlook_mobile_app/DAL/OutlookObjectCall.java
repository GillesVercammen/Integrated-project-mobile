package ap.student.outlook_mobile_app.DAL;

/**
 * Created by alek on 11/28/17.
 */


public enum OutlookObjectCall {
    READUSER(""),
    READMAIL("/mailfolders"),
    READFOLDERS("/mailfolders"),
    ADDFOLDERS("/mailfolders"),
    READCALENDAR("/calendars"),
    READEVENTS("/events"),
    READCONTACTS("/contacts"),
    SENDMAIL("/sendMail"),
    UPDATEMAIL("/messages"),
    POSTEVENT("/events"),
    POSTCALENDAR("/calendars"),
    LOGINERROR("/loginError"),
    CONTACTS("/contacts"),
    PERMISSIONSERROR("/permissionsError");
    private String action;

    OutlookObjectCall(String action) {
        this.action = action;
    }

    public String action() {
        return action;
    }
}
