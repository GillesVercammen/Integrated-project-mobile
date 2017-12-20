package ap.student.outlook_mobile_app.DAL.models;

/**
 * Created by alek on 12/1/17.
 */

public class Recipient {

    private EmailAddress emailAddress;

    public Recipient() {
    }

    public Recipient(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "emailAddress=" + emailAddress +
                '}';
    }
}
