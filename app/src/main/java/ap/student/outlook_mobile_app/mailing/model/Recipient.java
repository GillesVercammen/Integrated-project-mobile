package ap.student.outlook_mobile_app.mailing.model;

/**
 * Created by alek on 12/1/17.
 */

public class Recipient {
    //Microsoft is fucking retarded
    private EmailAddress emailAddress;

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
