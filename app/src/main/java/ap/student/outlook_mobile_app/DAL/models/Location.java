package ap.student.outlook_mobile_app.DAL.models;

/**
 * Created by alek on 12/1/17.
 */

public class Location {
    private PhysicalAddress address;

    public Location() {
    }

    public Location(PhysicalAddress address, String displayName, String locationEmailAddress) {

        this.address = address;
        this.displayName = displayName;
        this.locationEmailAddress = locationEmailAddress;
    }

    public Location(String displayName) {

        this.displayName = displayName;
    }

    private String displayName;
    private String locationEmailAddress;

    public PhysicalAddress getAddress() {
        return address;
    }

    public void setAddress(PhysicalAddress address) {
        this.address = address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLocationEmailAddress() {
        return locationEmailAddress;
    }

    public void setLocationEmailAddress(String locationEmailAddress) {
        this.locationEmailAddress = locationEmailAddress;
    }
}
