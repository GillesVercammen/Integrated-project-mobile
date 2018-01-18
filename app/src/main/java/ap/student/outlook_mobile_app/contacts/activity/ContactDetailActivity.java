package ap.student.outlook_mobile_app.contacts.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.enums.SendMailType;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.model.Address;
import ap.student.outlook_mobile_app.contacts.model.Contact;
import ap.student.outlook_mobile_app.mailing.activity.NewMailActivity;

public class ContactDetailActivity extends AppCompatActivityRest {

    private Contact contact;
    private TextView displayName, nickName, title, birthday, homepage, homephone, businessphone, mobilephone, email, street, city, postalcode, state, country,
                    companyname, profession, jobtitle, officelocation, department, manager, assistantname, personalnote;
    private ViewGroup headerLayout, homephoneLayout, mobilephoneLayout, businessphoneLayout, emailLayout, addressLayout, businessinfoLayout, personalnoteLayout, buttonLayout;
    private Button homeButton, businessButton, otherButton;
    private ImageView homephoneButton, mobilephoneButton, businessphoneButton, emailButton;

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
        companyname = (TextView)findViewById(R.id.companyname);
        profession = (TextView)findViewById(R.id.profession);
        jobtitle = (TextView)findViewById(R.id.jobtitle);
        officelocation = (TextView)findViewById(R.id.officelocation);
        department = (TextView)findViewById(R.id.department);
        manager = (TextView)findViewById(R.id.manager);
        assistantname = (TextView)findViewById(R.id.assistantname);
        personalnote = (TextView)findViewById(R.id.personalNoteDetail);

        homeButton = (Button)findViewById(R.id.Home);
        businessButton = (Button)findViewById(R.id.Business);
        otherButton = (Button)findViewById(R.id.Other);

        headerLayout = (ViewGroup) findViewById(R.id.headerLayout);
        homephoneLayout = (ViewGroup) findViewById(R.id.homephoneLayout);
        mobilephoneLayout = (ViewGroup) findViewById(R.id.mobilephoneLayout);
        businessphoneLayout = (ViewGroup) findViewById(R.id.businessphoneLayout);
        emailLayout = (ViewGroup) findViewById(R.id.emailLayout);
        addressLayout = (ViewGroup) findViewById(R.id.addressLayout);
        businessinfoLayout = (ViewGroup) findViewById(R.id.businessinfoLayout);
        personalnoteLayout= (ViewGroup) findViewById(R.id.personalNoteLayoutDetail);
        buttonLayout = (ViewGroup)findViewById(R.id.buttonLayout);

        homephoneButton = (ImageView) findViewById(R.id.lastImage);
        mobilephoneButton = (ImageView) findViewById(R.id.lastImage2);
        businessphoneButton = (ImageView) findViewById(R.id.lastImage3);
        emailButton = (ImageView) findViewById(R.id.lastImage4);


        checkIfInfoExist(contact.getDisplayName(), displayName, headerLayout);
        checkIfInfoExist(contact.getNickName(), nickName, headerLayout);
        checkIfInfoExist(contact.getTitle(), title, headerLayout);
        checkIfInfoExist(contact.getBirthday(), birthday, headerLayout);
        checkIfInfoExist(contact.getBusinessHomePage(), homepage, headerLayout);
        checkIfInfoExist(contact.getPersonalNotes(), personalnote, personalnoteLayout);

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
                businessinfoLayout.setVisibility(View.GONE);
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
                businessinfoLayout.setVisibility(View.VISIBLE);
                otherButton.setTextColor(getResources().getColor(R.color.white));
                homeButton.setTextColor(getResources().getColor(R.color.white));
                businessButton.setTextColor(getResources().getColor(R.color.md_blue_grey_300));
                if (contact.getBusinessAddress() != null){
                    checkIfAddressExist(contact.getBusinessAddress(), addressLayout);
                } else {
                    addressLayout.setVisibility(View.GONE);
                }
               checkIfInfoExist(contact.getCompanyName(), companyname, businessinfoLayout);
                checkIfInfoExist(contact.getProfession(), profession, businessinfoLayout);
                checkIfInfoExist(contact.getJobTitle(), jobtitle, businessinfoLayout);
                checkIfInfoExist(contact.getOfficeLocation(), officelocation, businessinfoLayout);
                checkIfInfoExist(contact.getDepartment(), department, businessinfoLayout);
                checkIfInfoExist(contact.getManager(), manager, businessinfoLayout);
                checkIfInfoExist(contact.getAssistantName(), assistantname, businessinfoLayout);

            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessinfoLayout.setVisibility(View.GONE);
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

        homephoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getHomePhones().get(0)));
                startActivity(intent);
            }
        });
        mobilephoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getMobilePhone()));
                startActivity(intent);
            }
        });
        businessphoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getBusinessPhones().get(0)));
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectivityManager.isConnected()){
                    Intent sendMailIntent = new Intent(ContactDetailActivity.this, NewMailActivity.class);
                    sendMailIntent.putExtra("mailType", SendMailType.NORMALSEND.value());
                    sendMailIntent.putExtra("TO", contact.getEmailAddresses().get(0).getAddress());
                    startActivity(sendMailIntent);
                } else {
                    Toast.makeText(ContactDetailActivity.this, R.string.offline_error, Toast.LENGTH_SHORT).show();
                }

            }
        });

        fixTabBar();

    }

    private void fixTabBar(){
        boolean home = isEmptyAddress(contact.getHomeAddress(), homeButton);
        boolean business = isEmptyAddress(contact.getBusinessAddress(), businessButton);
        boolean other = isEmptyAddress(contact.getOtherAddress(), otherButton);
        if (home && business && other){
            buttonLayout.setVisibility(View.GONE);
        }

    }

    private boolean isEmptyAddress(Address address, final Button button) {
        int dirtyCount = 0;
        if (!(address.getStreet() != null && !address.getStreet().equals("")) ){
            dirtyCount++;
        }
        if (!(address.getPostalCode() != null && !address.getPostalCode().equals(""))){
            dirtyCount++;
        }
        if (!(address.getCity() != null && !address.getCity().equals(""))){
            dirtyCount++;
        }
        if (!(address.getState() != null && !address.getState().equals(""))){
            dirtyCount++;
        }
        if (!(address.getCountryOrRegion() != null && !address.getCountryOrRegion().equals(""))){
           dirtyCount++;
        }
        if (dirtyCount == 5) {
            button.setClickable(false);
            button.setAlpha(.5f);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ContactDetailActivity.this, R.string.no_address, Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } else {
            return false;
        }

    }


    private void checkIfAddressExist(Address address, ViewGroup layout) {
        layout.setVisibility(View.VISIBLE);
        int dirtyCount = 0;
        if (address.getStreet() != null && !address.getStreet().equals("") ){
            street.setVisibility(View.VISIBLE);
            street.setText("");
            street.setText(address.getStreet());
        } else {
            dirtyCount++;
            street.setVisibility(View.GONE);
        }
        if (address.getPostalCode() != null && !address.getPostalCode().equals("")){
            postalcode.setVisibility(View.VISIBLE);
            postalcode.setText("");
            postalcode.setText(address.getPostalCode() + ", ");
        } else {
            dirtyCount++;
            postalcode.setVisibility(View.GONE);
        }
        if (address.getCity() != null && !address.getCity().equals("")){
            city.setVisibility(View.VISIBLE);
            city.setText("");
            city.setText(address.getCity());
        } else {
            dirtyCount++;
            city.setVisibility(View.GONE);
        }
        if (address.getState() != null && !address.getState().equals("")){
            state.setVisibility(View.VISIBLE);
            state.setText("");
            state.setText(address.getState());
        } else {
            dirtyCount++;
            state.setVisibility(View.GONE);
        }
        if (address.getCountryOrRegion() != null && !address.getCountryOrRegion().equals("")){
            country.setVisibility(View.VISIBLE);
            country.setText("");
            country.setText(address.getCountryOrRegion());
        } else {
            dirtyCount++;
            country.setVisibility(View.GONE);
        }
        if (dirtyCount == 5) {
            layout.setVisibility(View.GONE);
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
                if (connectivityManager.isConnected()){
                    Intent intent = new Intent(ContactDetailActivity.this, EditContactActivity.class);
                    intent.putExtra("CONTACT", contact);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.offline_error, Toast.LENGTH_SHORT).show();
                }

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
