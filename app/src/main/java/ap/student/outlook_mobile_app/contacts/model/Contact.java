package ap.student.outlook_mobile_app.contacts.model;

import java.io.Serializable;
import java.util.ArrayList;

import ap.student.outlook_mobile_app.mailing.model.EmailAddress;

/**
 * Created by Demmy on 30/12/2017.
 */

public class Contact implements Comparable<Contact>, Serializable {

    private int autoId;
    private int color = -1;
    private String id;
    private String birthday;
    private String fileAs;
    private String displayName;
    private String givenName;
    private String initials;
    private String middleName;
    private String nickName;
    private String surname;
    private String title;
    private String jobTitle;
    private String companyName;
    private String department;
    private String officeLocation;
    private String profession;
    private String businessHomePage;
    private String assistantName;
    private String manager;
    private ArrayList<String> homePhones;
    private String mobilePhone;
    private ArrayList<String> businessPhones;
    private String spouseName;
    private String personalNotes;
    private ArrayList<String> children;
    private ArrayList<EmailAddress> emailAddresses;
    private Address homeAddress;
    private Address businessAddress;
    private Address otherAddress;

    public Contact(String displayName) {
        this.displayName = displayName;
    }

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFileAs() {
        return fileAs;
    }

    public void setFileAs(String fileAs) {
        this.fileAs = fileAs;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBusinessHomePage() {
        return businessHomePage;
    }

    public void setBusinessHomePage(String businessHomePage) {
        this.businessHomePage = businessHomePage;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public ArrayList<String> getHomePhones() {
        return homePhones;
    }

    public void setHomePhones(ArrayList<String> homePhones) {
        this.homePhones = homePhones;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public ArrayList<String> getBusinessPhones() {
        return businessPhones;
    }

    public void setBusinessPhones(ArrayList<String> businessPhones) {
        this.businessPhones = businessPhones;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String personalNotes) {
        this.personalNotes = personalNotes;
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

    public ArrayList<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(ArrayList<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(Address businessAddress) {
        this.businessAddress = businessAddress;
    }

    public Address getOtherAddress() {
        return otherAddress;
    }

    public void setOtherAddress(Address otherAddress) {
        this.otherAddress = otherAddress;
    }

    public int compareTo(Contact other) {
        return displayName.compareTo(other.displayName);
    }

}
