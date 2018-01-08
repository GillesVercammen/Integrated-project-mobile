package ap.student.outlook_mobile_app.DAL.enums;

/**
 * Created by Alexander on 12/21/2017.
 */

public enum SendMailType {
    SEND("send"),
    FORWARD("forward"),
    REPLY("reply"),
    REPLYALL("replyAll");
    private String value;

    SendMailType(String emailType) {
        value = emailType;
    }

    public String value() {
        return value;
    }
}