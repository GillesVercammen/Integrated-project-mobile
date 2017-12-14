package ap.student.outlook_mobile_app.mailing.model;


import java.io.Serializable;

public class Recipient implements Serializable{
    //Microsoft is fucking retarded
    private EmailAddress emailAddress;

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
