package ap.student.outlook_mobile_app.DAL.models;

/**
 * Created by alek on 11/30/17.
 */

public class Attendee {
    private ResponseStatus status;
    private String type;
    private EmailAddress emailAddress;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
