package ap.student.outlook_mobile_app.DAL.models;

import java.util.List;

/**
 * Created by Alexander on 12/11/17.
 */

public class ToRecipients {
    private EmailAddress emailAddress;

    public ToRecipients() {
    }

    public ToRecipients(EmailAddress emailAddress) {
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
        return "ToRecipients{" +
                "emailAddress=" + emailAddress +
                '}';
    }
}
