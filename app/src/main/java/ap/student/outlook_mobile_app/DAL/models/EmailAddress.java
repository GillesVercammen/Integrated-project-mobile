package ap.student.outlook_mobile_app.DAL.models;

/**
 * Created by alek on 12/1/17.
 */

public class EmailAddress {
    private String address;
    private String name;

    public EmailAddress() {
    }

    public EmailAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
