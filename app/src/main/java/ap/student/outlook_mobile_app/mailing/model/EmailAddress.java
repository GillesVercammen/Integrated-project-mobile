package ap.student.outlook_mobile_app.mailing.model;

import java.io.Serializable;

public class EmailAddress implements Serializable{
    private String address;
    private String name;

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
