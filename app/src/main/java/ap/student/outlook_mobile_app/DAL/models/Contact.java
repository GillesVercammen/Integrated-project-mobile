package ap.student.outlook_mobile_app.DAL.models;

import java.util.List;

/**
 * Created by Alexander on 12/14/2017.
 */

public class Contact {

    private String id;
    private String displayName;
    private List<EmailAddress> emailAddresses;

    public Contact() {
    }

    public Contact(String id, String displayName, List<EmailAddress> emailAddresses) {
        this.id = id;
        this.displayName = displayName;
        this.emailAddresses = emailAddresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", emailAddresses=" + emailAddresses +
                '}';
    }
}
