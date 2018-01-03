package ap.student.outlook_mobile_app.contacts.model;

/**
 * Created by Demmy on 2/01/2018.
 */

public class Address {
    private String street;
    private String city;
    private String state;
    private String countryorRegion;
    private String postalCode;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountryorRegion() {
        return countryorRegion;
    }

    public void setCountryorRegion(String countryorRegion) {
        this.countryorRegion = countryorRegion;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
