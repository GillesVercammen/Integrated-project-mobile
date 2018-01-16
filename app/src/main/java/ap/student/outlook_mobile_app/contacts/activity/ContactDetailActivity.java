package ap.student.outlook_mobile_app.contacts.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.model.Address;
import ap.student.outlook_mobile_app.contacts.model.Contact;

public class ContactDetailActivity extends AppCompatActivityRest {

    private Contact contact;
    private TextView displayName, nickName, title, birthday, homepage, homephone, businessphone, mobilephone, email, street, city, postalcode, state, country;
    private ViewGroup headerLayout, homephoneLayout, mobilephoneLayout, businessphoneLayout, emailLayout, addressLayout;
    private Button homeButton, businessButton, otherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);


        contact = (Contact) getIntent().getSerializableExtra("CONTACT");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        displayName = (TextView)findViewById(R.id.displayName);
        nickName = (TextView)findViewById(R.id.nickName);
        title = (TextView)findViewById(R.id.title);
        birthday = (TextView)findViewById(R.id.birthday);
        homepage = (TextView)findViewById(R.id.homepage);
        homephone = (TextView)findViewById(R.id.topText);
        mobilephone = (TextView)findViewById(R.id.topText2);
        businessphone = (TextView)findViewById(R.id.topText3);
        email = (TextView)findViewById(R.id.topText4);
        street = (TextView)findViewById(R.id.topText5);
        postalcode = (TextView)findViewById(R.id.topMiddleText5);
        city = (TextView)findViewById(R.id.bottomMiddleText5);
        state = (TextView)findViewById(R.id.downText5);
        country = (TextView)findViewById(R.id.lastBottomText);

        homeButton = (Button)findViewById(R.id.Home);
        businessButton = (Button)findViewById(R.id.Business);
        otherButton = (Button)findViewById(R.id.Other);

        headerLayout = (ViewGroup) findViewById(R.id.headerLayout);
        homephoneLayout = (ViewGroup) findViewById(R.id.homephoneLayout);
        mobilephoneLayout = (ViewGroup) findViewById(R.id.mobilephoneLayout);
        businessphoneLayout = (ViewGroup) findViewById(R.id.businessphoneLayout);
        emailLayout = (ViewGroup) findViewById(R.id.emailLayout);
        addressLayout = (ViewGroup) findViewById(R.id.addressLayout);


        checkIfInfoExist(contact.getDisplayName(), displayName, headerLayout);
        checkIfInfoExist(contact.getNickName(), nickName, headerLayout);
        checkIfInfoExist(contact.getTitle(), title, headerLayout);
        checkIfInfoExist(contact.getBirthday(), birthday, headerLayout);
        checkIfInfoExist(contact.getBusinessHomePage(), homepage, headerLayout);
        if (contact.getHomePhones().size() > 0){
            checkIfInfoExist(contact.getHomePhones().get(0), homephone, homephoneLayout);
        } else {
            homephoneLayout.setVisibility(View.GONE);
        }
        checkIfInfoExist(contact.getMobilePhone(), mobilephone, mobilephoneLayout);
        if (contact.getBusinessPhones().size() > 0) {
            checkIfInfoExist(contact.getBusinessPhones().get(0), businessphone, businessphoneLayout);
        } else {
            businessphoneLayout.setVisibility(View.GONE);
        }
        if (contact.getEmailAddresses().size() > 0) {
            checkIfInfoExist(contact.getEmailAddresses().get(0).getAddress(), email, emailLayout);
        } else {
            emailLayout.setVisibility(View.GONE);
        }
        if (contact.getHomeAddress() != null){
            checkIfAddressExist(contact.getHomeAddress(), addressLayout);
        } else {
            addressLayout.setVisibility(View.GONE);
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeButton.setTextColor(getResources().getColor(R.color.md_blue_grey_300));
                otherButton.setTextColor(getResources().getColor(R.color.white));
                businessButton.setTextColor(getResources().getColor(R.color.white));
                if (contact.getHomeAddress() != null){
                    checkIfAddressExist(contact.getHomeAddress(), addressLayout);
                } else {
                    addressLayout.setVisibility(View.GONE);
                }
            }
        });

        businessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherButton.setTextColor(getResources().getColor(R.color.white));
                homeButton.setTextColor(getResources().getColor(R.color.white));
                businessButton.setTextColor(getResources().getColor(R.color.md_blue_grey_300));
                if (contact.getBusinessAddress() != null){
                    checkIfAddressExist(contact.getBusinessAddress(), addressLayout);
                } else {
                    addressLayout.setVisibility(View.GONE);
                }
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessButton.setTextColor(getResources().getColor(R.color.white));
                homeButton.setTextColor(getResources().getColor(R.color.white));
                otherButton.setTextColor(getResources().getColor(R.color.md_blue_grey_300));
                if (contact.getOtherAddress() != null){
                    checkIfAddressExist(contact.getOtherAddress(), addressLayout);
                } else {
                    addressLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    private void checkIfAddressExist(Address address, ViewGroup layout) {
        if (address.getStreet() != null && !address.getStreet().equals("") ){
            street.setText("");
            street.setText(address.getStreet());
        } else {
            street.setVisibility(View.GONE);
        }
        if (address.getPostalCode() != null && !address.getPostalCode().equals("")){
            postalcode.setText("");
            postalcode.setText(address.getPostalCode() + ", ");
        } else {
            postalcode.setVisibility(View.GONE);
        }
        if (address.getCity() != null && !address.getCity().equals("")){
            city.setText("");
            city.setText(address.getCity());
        } else {
            city.setVisibility(View.GONE);
        }
        if (address.getState() != null && !address.getState().equals("")){
            state.setText("");
            state.setText(address.getState());
        } else {
            state.setVisibility(View.GONE);
        }
        if (address.getCountryOrRegion() != null && !address.getCountryOrRegion().equals("")){
            country.setText("");
            country.setText(address.getCountryOrRegion());
        } else {
            country.setVisibility(View.GONE);
        }

    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //INFLATE THE MENU, ADDS ITEMS TO THE BAR IF PRESENT
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // HANDLE ACTIONBAR CLICKS.
        // AUTOMATICLY SPECIFY HOME/BACK BUTTON IF PARENTACTIVTY IS SET IN MANIFEST
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit:
                System.out.println("helloo xD");
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIfInfoExist(String contactinfo, TextView textView, ViewGroup layout) {
        if (contactinfo != null && !contactinfo.isEmpty() && !contactinfo.equals("") && !contactinfo.equals("[]")){
            if (!(textView.getId() == R.id.birthday)) {
                textView.setText(contactinfo);
            } else {
                try {
                    String newInfo = transformDate(contactinfo);
                    textView.setText(newInfo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (layout.getId() != R.id.headerLayout){
                layout.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }

    private String transformDate(String stringDate) throws ParseException {
        String JSON_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        SimpleDateFormat formatter = new SimpleDateFormat(JSON_FORMAT);
        Date date = formatter.parse(stringDate);
        String TO_FORMAT = "dd MMM yyyy";

        SimpleDateFormat outputFormat = new SimpleDateFormat(TO_FORMAT);
        return outputFormat.format(date);
    }

}
