package ap.student.outlook_mobile_app.contacts.activity;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.model.Address;
import ap.student.outlook_mobile_app.contacts.model.Contact;

public class ContactDetailActivity extends AppCompatActivityRest {

    private Contact contact;
    private Toolbar toolbar;
    private ViewGroup layout, layout2;
    private EditText displayname, givenname, initials, middlename, nickname, surname, title,
            companyname, jobtitle, department, officelocation, profession, businesshomepage,
            assistantname, manager, homephones, mobilephone, businessphones, spousename,
            emailaddresses, streethome, cityhome, statehome, countryhome, postalcodehome,
            streetbusiness, citybusiness, statebusiness, countrybusiness, postalcodebusiness,
            streetother, cityother, stateother, countryother, postalcodeother, personalNotes,
            birthday, homeaddress, businessaddress, otheraddress;
    private MenuItem saveContact;
    private MenuItem editContact;
    private MenuItem cancelEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contact = (Contact) getIntent().getSerializableExtra("CONTACT");

        toolbar = (Toolbar) findViewById(R.id.toolbar_read);
        layout = (ViewGroup) findViewById(R.id.contact_info);
        //set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(contact.getGivenName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String homeString = contact.getHomeAddress().getCity() + ", " + contact.getHomeAddress().getState() + ", " + contact.getHomeAddress().getPostalCode();
        String businessString = contact.getBusinessAddress().getCity() + ", " + contact.getBusinessAddress().getState() + ", " + contact.getBusinessAddress().getPostalCode();
        String otherString = contact.getOtherAddress().getCity() + ", " + contact.getOtherAddress().getState() + ", " + contact.getOtherAddress().getPostalCode();

        displayname = (EditText) findViewById(R.id.displayname);
        givenname = (EditText) findViewById(R.id.givenname);
        initials = (EditText) findViewById(R.id.initials);
        middlename = (EditText) findViewById(R.id.middlename);
        nickname = (EditText) findViewById(R.id.nickname);
        surname = (EditText) findViewById(R.id.surname);
        title = (EditText) findViewById(R.id.title);
        companyname = (EditText) findViewById(R.id.companyname);
        jobtitle = (EditText) findViewById(R.id.jobtitle);
        department = (EditText) findViewById(R.id.department);
        officelocation = (EditText) findViewById(R.id.officelocation);
        profession = (EditText) findViewById(R.id.profession);
        businesshomepage = (EditText) findViewById(R.id.businesshomepage);
        assistantname = (EditText) findViewById(R.id.assistantname);
        manager = (EditText) findViewById(R.id.manager);
        homephones = (EditText) findViewById(R.id.homephones);
        mobilephone = (EditText) findViewById(R.id.mobilephone);
        businessphones = (EditText) findViewById(R.id.businessphones);
        spousename = (EditText) findViewById(R.id.spousename);
        birthday = (EditText) findViewById(R.id.birthday);
        personalNotes = (EditText) findViewById(R.id.personalNotes);
        streethome = (EditText) findViewById(R.id.streethome);
        homeaddress = (EditText) findViewById(R.id.cityhome);
        countryhome = (EditText) findViewById(R.id.countryorregionhome);
        streetother = (EditText) findViewById(R.id.streetother);
        otheraddress = (EditText) findViewById(R.id.cityother);
        countryother = (EditText) findViewById(R.id.countryorregionother);
        streetbusiness = (EditText) findViewById(R.id.streetbusiness);
        businessaddress = (EditText) findViewById(R.id.citybusiness);
        countrybusiness = (EditText) findViewById(R.id.countryorregionbusiness);


        checkIfInfoExist(contact.getDisplayName(), (EditText) findViewById(R.id.displayname), R.id.displayname, layout2, R.id.layout1);
        checkIfInfoExist(contact.getGivenName(), (EditText) findViewById(R.id.givenname), R.id.givenname, layout2, R.id.layout2);
        checkIfInfoExist(contact.getInitials(), (EditText) findViewById(R.id.initials), R.id.initials, layout2, R.id.layout3);
        checkIfInfoExist(contact.getMiddleName(), (EditText) findViewById(R.id.middlename), R.id.middlename, layout2, R.id.layout4);
        checkIfInfoExist(contact.getNickName(), (EditText) findViewById(R.id.nickname), R.id.nickname, layout2, R.id.layout5);
        checkIfInfoExist(contact.getSurname(), (EditText) findViewById(R.id.surname), R.id.surname, layout2, R.id.layout6);
        checkIfInfoExist(contact.getTitle(), (EditText) findViewById(R.id.title), R.id.title, layout2, R.id.layout7);
        checkIfInfoExist(contact.getCompanyName(), (EditText) findViewById(R.id.companyname), R.id.companyname, layout2, R.id.layout8);
        checkIfInfoExist(contact.getJobTitle(), (EditText) findViewById(R.id.jobtitle), R.id.jobtitle, layout2, R.id.layout9);
        checkIfInfoExist(contact.getDepartment(), (EditText) findViewById(R.id.department), R.id.department, layout2, R.id.layout10);
        checkIfInfoExist(contact.getOfficeLocation(), (EditText) findViewById(R.id.officelocation), R.id.officelocation, layout2, R.id.layout11);
        checkIfInfoExist(contact.getProfession(), (EditText) findViewById(R.id.profession), R.id.profession, layout2, R.id.layout12);
        checkIfInfoExist(contact.getBusinessHomePage(), (EditText) findViewById(R.id.businesshomepage), R.id.businesshomepage, layout2, R.id.layout13);
        checkIfInfoExist(contact.getAssistantName(), (EditText) findViewById(R.id.assistantname), R.id.assistantname, layout2, R.id.layout14);
        checkIfInfoExist(contact.getManager(), (EditText) findViewById(R.id.manager), R.id.manager, layout2, R.id.layout15);
        checkIfArrayExist(contact.getHomePhones().toArray(new String[0]), (EditText) findViewById(R.id.homephones), R.id.homephones, layout2, R.id.layout16);
        checkIfInfoExist(contact.getMobilePhone(), (EditText) findViewById(R.id.mobilephone), R.id.mobilephone, layout2, R.id.layout17);
        checkIfArrayExist(contact.getBusinessPhones().toArray(new String[0]), (EditText) findViewById(R.id.businessphones), R.id.businessphones, layout2, R.id.layout18);
        checkIfInfoExist(contact.getSpouseName(), (EditText) findViewById(R.id.spousename), R.id.spousename, layout2, R.id.layout19);
        try {
            checkIfInfoExist(transformDate(contact.getBirthday()), (EditText) findViewById(R.id.birthday), R.id.birthday, layout2, R.id.layout21);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        checkIfInfoExist(contact.getPersonalNotes(), (EditText) findViewById(R.id.personalNotes), R.id.personalNotes, layout2, R.id.layout22);
        //      checkIfInfoExist(Arrays.toString(contact.getEmailAddresses().toArray(new String[0])), emailaddresses, R.id.emailaddresses);
        checkIfInfoExist(contact.getHomeAddress().getStreet(), (EditText) findViewById(R.id.streethome), R.id.streethome, layout2, R.id.layout23);
        checkIfInfoExist(homeString, (EditText) findViewById(R.id.cityhome), R.id.cityhome, layout2, R.id.layout24);
        checkIfInfoExist(contact.getHomeAddress().getCountryOrRegion(), (EditText) findViewById(R.id.countryorregionhome), R.id.countryorregionhome, layout2, R.id.layout25);
        checkIfInfoExist(contact.getBusinessAddress().getStreet(), (EditText) findViewById(R.id.streetbusiness), R.id.streetbusiness, layout2, R.id.layout26);
        checkIfInfoExist(businessString, (EditText) findViewById(R.id.citybusiness), R.id.citybusiness, layout2, R.id.layout26);
        checkIfInfoExist(contact.getBusinessAddress().getCountryOrRegion(), (EditText) findViewById(R.id.countryorregionbusiness), R.id.countryorregionbusiness, layout2, R.id.layout26);
        checkIfInfoExist(contact.getOtherAddress().getStreet(), (EditText) findViewById(R.id.streetother), R.id.streetother, layout2, R.id.layout26);
        checkIfInfoExist(otherString, (EditText) findViewById(R.id.cityother), R.id.cityother, layout2, R.id.layout26);
        checkIfInfoExist(contact.getOtherAddress().getCountryOrRegion(), (EditText) findViewById(R.id.countryorregionother), R.id.countryorregionother, layout2, R.id.layout26);
    }



    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //INFLATE THE MENU, ADDS ITEMS TO THE BAR IF PRESENT
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        saveContact = (MenuItem) menu.findItem(R.id.action_save);
        editContact = (MenuItem) menu.findItem(R.id.action_edit);
        cancelEdit = (MenuItem) menu.findItem(R.id.action_cancel_edit);
        saveContact.setVisible(false);
        editContact.setVisible(true);
        cancelEdit.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_edit:
                item.setVisible(false);
                saveContact.setVisible(true);
                cancelEdit.setVisible(true);
                setEditable(displayname); setEditable(givenname); setEditable(initials); setEditable(middlename); setEditable(nickname); setEditable(surname);
                setEditable(title); setEditable(companyname); setEditable(jobtitle); setEditable(department); setEditable(officelocation); setEditable(profession);
                setEditable(businesshomepage); setEditable(assistantname); setEditable(manager); setEditable(homephones); setEditable(mobilephone);
                setEditable(businessphones); setEditable(spousename); setEditable(birthday); setEditable(personalNotes);
                setEditable(spousename); setEditable(birthday); setEditable(personalNotes); setEditable(streethome);
                setEditable(homeaddress); setEditable(countryhome); setEditable(streetother); setEditable(otheraddress);
                setEditable(countryother); setEditable(streetbusiness); setEditable(businessaddress); setEditable(countrybusiness);
                break;
            case R.id.action_save:
                item.setVisible(false);
                cancelEdit.setVisible(false);
                editContact.setVisible(true);
                break;
            case R.id.action_cancel_edit:
                item.setVisible(false);
                saveContact.setVisible(false);
                editContact.setVisible(true);
                finish();
                startActivity(getIntent());
        }

        return super.onOptionsItemSelected(item);
    }

    private void setEditable(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setClickable(true);
    }


    private void checkIfInfoExist(String info, EditText editText, int textViewId, ViewGroup layout, int layoutid) {
        if (info != null && !info.equals("") && !info.equals("[]") && !info.equals("null, null, null")){
            layout = (ViewGroup) findViewById(layoutid);
            layout.setVisibility(View.VISIBLE);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.append(info);
        }
    }

    private void checkIfArrayExist(String[] number, EditText editText, int textViewId, ViewGroup layout, int layoutid){
        for (int i = 0; i < number.length; i++){
            layout = (ViewGroup) findViewById(layoutid);
            layout.setVisibility(View.VISIBLE);
            if (i == number.length - 1) {
                editText.append(number[i]);
            } else {
                editText.append(number[i] + ", ");
            }

        }
    }

    private String transformDate(String stringDate) throws ParseException {
        if (stringDate != null && !stringDate.equals("")){
            String JSON_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

            SimpleDateFormat formatter = new SimpleDateFormat(JSON_FORMAT);
            Date date = formatter.parse(stringDate);
            String TO_FORMAT = "dd MMM yyyy";
            String TO_FORMAT_IS_12 = "dd MMM yyyy";

            SimpleDateFormat outputFormat = new SimpleDateFormat(TO_FORMAT);
            return outputFormat.format(date);
        } else {
            return "";
        }
    }

}
