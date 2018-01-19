package ap.student.outlook_mobile_app.contacts.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Locale;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.model.Address;
import ap.student.outlook_mobile_app.contacts.model.Contact;
import ap.student.outlook_mobile_app.mailing.model.EmailAddress;

public class EditContactActivity extends AppCompatActivityRest {

    private ViewGroup givenNameLayout, surNameLayout,middleNameLayout, nickNameLayout, initialsLayout, titleLayout, outerLayout;
    private EditText editGivenName, editSurName, editMiddleName, editNickName, editInitials, editTitle;

    private ViewGroup birthdayLayout, spousenameLayout, mobilephoneLayout;
    private EditText editBirthday, editSpousename, editMobilephone;

    private ViewGroup emailaddressLayout;
    private EditText editEmail;

    private ViewGroup homephoneLayout;
    private EditText editHomephone;

    private ViewGroup businessphoneLayout;
    private EditText editBusinessphone;


    private ViewGroup companyNameLayout, departmentLayout, officeLocationLayout, businesshomepageLayout, jobTitleLayout, professionLayout, assistantnameLayout, managerLayout;
    private EditText editCompanyName, editDepartment, editOfficeLocation, editBusinesshomepage, editJobTitle, editProfession, editAssistantname, editManager;

    private ViewGroup streethomeLayout, cityhomeLayout, postalcodehomeLayout, statehomeLayout, countryorregionhomeLayout;
    private EditText editStreethome, editCityhome, editPostalcodehome, editStatehome, editCountryorregionhome;

    private ViewGroup streetbusinessLayout, citybusinessLayout, postalcodebusinessLayout, statebusinessLayout, countryorregionbusinessLayout;
    private EditText editStreetbusiness, editCitybusiness, editPostalcodebusiness, editStatebusiness, editCountryorregionbusiness;

    private ViewGroup streetotherLayout, cityotherLayout, postalcodeotherLayout, stateotherLayout, countryorregionotherLayout;
    private EditText editStreetother, editCityother, editPostalcodeother, editStateother, editCountryorregionother;

    private ViewGroup personalNoteLayout;
    private EditText editPersonalnote;

    private ImageView open_close_user, open_close_general, open_close_business, open_close_home, open_close_businessaddress, open_close_otheraddress, open_close_email, addMoreEmail, removeEmail,
            addMoreHomephone, removeHomephone, addMoreBusinessphone, removeBusinessphone, open_close_homephones, open_close_businessphones, open_close_personalnote;

    private boolean userOpen = true;
    private boolean generalOpen = false;
    private boolean businessOpen = false;
    private boolean homeOpen = false;
    private boolean businessAddressOpen = false;
    private boolean otherOpen = false;
    private boolean emailOpen = false;
    private boolean homePhoneOpen = false;
    private boolean businessPhoneOpen = false;
    private boolean personalnoteOpen = false;
    public int numberOfeditTexts = 0;
    public int numberOfeditTextsHomePhone = 0;
    public int numberOfEditTextsBusinessPhone = 0;
    private Contact contact;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String toolbarTitle = getString(R.string.edit_contact);
        setActionBarMail(toolbarTitle, toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        outerLayout = (LinearLayout) findViewById( R.id.outerLayout);

        editGivenName = (EditText) findViewById(R.id.editGivenName);
        editSurName = (EditText) findViewById(R.id.editSurName);
        editMiddleName = (EditText) findViewById(R.id.editMiddleName);
        editNickName = (EditText) findViewById(R.id.editNickName);
        editInitials = (EditText) findViewById(R.id.editInitials);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editGivenName = (EditText) findViewById(R.id.editGivenName);

        editBirthday = (EditText) findViewById(R.id.editBirthday);
        editSpousename = (EditText) findViewById(R.id.editSpousename);
        editMobilephone =(EditText) findViewById(R.id.editMobilephone);

        editEmail = (EditText) findViewById(R.id.editEmail);

        editHomephone = (EditText) findViewById(R.id.editHomephone);

        editBusinessphone = (EditText) findViewById(R.id.editBusinessphone);

        editCompanyName = (EditText) findViewById(R.id.editCompanyName);
        editDepartment = (EditText) findViewById(R.id.editDepartment);
        editOfficeLocation = (EditText) findViewById(R.id.editOfficeLocation);
        editBusinesshomepage = (EditText) findViewById(R.id.editBusinesshomepage);
        editJobTitle = (EditText) findViewById(R.id.editJobTitle);
        editProfession = (EditText) findViewById(R.id.editProfession);
        editAssistantname = (EditText) findViewById(R.id.editAssistantname);
        editManager = (EditText) findViewById(R.id.editManager);

        editStreethome = (EditText) findViewById(R.id.editStreethome);
        editCityhome = (EditText) findViewById(R.id.editCityhome);
        editPostalcodehome = (EditText) findViewById(R.id.editPostalcodehome);
        editStatehome = (EditText) findViewById(R.id.editStatehome);
        editCountryorregionhome = (EditText) findViewById(R.id.editCountryorregionhome);

        editStreetbusiness = (EditText) findViewById(R.id.editStreetbusiness);
        editCitybusiness = (EditText) findViewById(R.id.editCitybusiness);
        editPostalcodebusiness = (EditText) findViewById(R.id.editPostalcodebusiness);
        editStatebusiness = (EditText) findViewById(R.id.editStatebusiness);
        editCountryorregionbusiness = (EditText) findViewById(R.id.editCountryorregionbusiness);

        editStreetother = (EditText) findViewById(R.id.editStreetother);
        editCityother = (EditText) findViewById(R.id.editCityother);
        editPostalcodeother = (EditText) findViewById(R.id.editPostalcodeother);
        editStateother = (EditText) findViewById(R.id.editStateother);
        editCountryorregionother = (EditText) findViewById(R.id.editCountryorregionother);

        editPersonalnote = (EditText) findViewById(R.id.editPersonalNote);

        removeEmail = (ImageView) findViewById(R.id.remove_email);
        removeHomephone = (ImageView) findViewById(R.id.remove_homephone);
        removeBusinessphone = (ImageView) findViewById(R.id.remove_businessphone);


        contact = (Contact) getIntent().getSerializableExtra("CONTACT");

        setAllInfo();
        setOnclickListeners();

        addMoreEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditText();
            }
        });

        addMoreHomephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditTextHomePhones();
            }
        });

        addMoreBusinessphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditTextHBusinessPhones();
            }
        });

        removeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idEditText = numberOfeditTexts ;

                EditText et = (EditText) findViewById(R.string.emailEditText + idEditText);
                if (et.getText().toString().equals("")){
                    et.setId(0);
                    et.setVisibility(View.GONE);
                    numberOfeditTexts--;
                } else {
                    Toast.makeText(EditContactActivity.this, R.string.delete_email_first, Toast.LENGTH_SHORT).show();
                }
                if (numberOfeditTexts == 0) {
                    removeEmail.setVisibility(View.GONE);
                }

            }
        });

        removeHomephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idEditText = numberOfeditTextsHomePhone;
                EditText et = (EditText)findViewById(R.string.homephoneEditText + idEditText);

                if(et.getText().toString().equals("")){
                    et.setId(0);
                    et.setVisibility(View.GONE);
                    numberOfeditTextsHomePhone--;
                } else {
                    Toast.makeText(EditContactActivity.this, R.string.delete_phone_first, Toast.LENGTH_SHORT).show();
                }
                if (numberOfeditTextsHomePhone == 0) {
                    removeHomephone.setVisibility(View.GONE);
                }

            }
        });

        removeBusinessphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idEditText = numberOfEditTextsBusinessPhone;
                EditText et = (EditText) findViewById(R.string.businessphoneEditText + idEditText);

                if(et.getText().toString().equals("")){
                    et.setId(0);
                    et.setVisibility(View.GONE);
                    numberOfEditTextsBusinessPhone--;
                } else {
                    Toast.makeText(EditContactActivity.this, R.string.delete_phone_first, Toast.LENGTH_SHORT).show();
                }
                if (numberOfEditTextsBusinessPhone == 0) {
                    removeBusinessphone.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setAllInfo() {
        checkIfInfoExist(contact.getGivenName(), editGivenName);
        checkIfInfoExist(contact.getSurname(), editSurName);
        checkIfInfoExist(contact.getMiddleName(), editMiddleName);
        checkIfInfoExist(contact.getNickName(), editNickName);
        checkIfInfoExist(contact.getInitials(), editInitials);
        checkIfInfoExist(contact.getTitle(), editTitle);
        checkIfInfoExist(contact.getBirthday(), editBirthday);
        checkIfInfoExist(contact.getMobilePhone(), editMobilephone);
        checkIfInfoExist(contact.getSpouseName(), editSpousename);
        if (contact.getEmailAddresses().size() > 0) {
            checkIfEmailsExist(contact.getEmailAddresses());
        }
        if(contact.getHomePhones().size() > 0) {
            checkIfHomePhoneExist(contact.getHomePhones());
        }
        if(contact.getBusinessPhones().size() > 0) {
            checkIfBusinessPhoneExist(contact.getBusinessPhones());
        }
        checkIfInfoExist(contact.getCompanyName(), editCompanyName);
        checkIfInfoExist(contact.getDepartment(), editDepartment);
        checkIfInfoExist(contact.getOfficeLocation(), editOfficeLocation);
        checkIfInfoExist(contact.getBusinessHomePage(), editBusinesshomepage);
        checkIfInfoExist(contact.getJobTitle(), editJobTitle);
        checkIfInfoExist(contact.getProfession(), editProfession);
        checkIfInfoExist(contact.getAssistantName(), editAssistantname);
        checkIfInfoExist(contact.getManager(), editManager);
        if (contact.getHomeAddress() != null){
             checkIfHomeAddressExist(contact.getHomeAddress());
         }
        if (contact.getBusinessAddress() != null){
            checkIfHBusinessAddressExist(contact.getBusinessAddress());
        }
        if (contact.getOtherAddress() != null){
            checkIfOtherAddressExist(contact.getOtherAddress());
        }
        checkIfInfoExist(contact.getPersonalNotes(), editPersonalnote);
    }

    private void checkIfHomeAddressExist(Address address) {
        if (address.getStreet() != null && !address.getStreet().equals("") ){
            editStreethome.setText(address.getStreet());
        }
        if (address.getPostalCode() != null && !address.getPostalCode().equals("")){
            editPostalcodehome.setText(address.getPostalCode());
        }
        if (address.getCity() != null && !address.getCity().equals("")){
            editCityhome.setText(address.getCity());
        }
        if (address.getState() != null && !address.getState().equals("")){
            editStatehome.setText(address.getState());
        }
        if (address.getCountryOrRegion() != null && !address.getCountryOrRegion().equals("")){
            editCountryorregionhome.setText(address.getCountryOrRegion());
        }
    }

    private void checkIfHBusinessAddressExist(Address address) {
        if (address.getStreet() != null && !address.getStreet().equals("") ){
            editStreetbusiness.setText(address.getStreet());
        }
        if (address.getPostalCode() != null && !address.getPostalCode().equals("")){
            editPostalcodebusiness.setText(address.getPostalCode());
        }
        if (address.getCity() != null && !address.getCity().equals("")){
            editCitybusiness.setText(address.getCity());
        }
        if (address.getState() != null && !address.getState().equals("")){
            editStatebusiness.setText(address.getState());
        }
        if (address.getCountryOrRegion() != null && !address.getCountryOrRegion().equals("")){
            editCountryorregionbusiness.setText(address.getCountryOrRegion());
        }
    }

    private void checkIfOtherAddressExist(Address address) {
        if (address.getStreet() != null && !address.getStreet().equals("") ){
            editStreetother.setText(address.getStreet());
        }
        if (address.getPostalCode() != null && !address.getPostalCode().equals("")){
            editPostalcodeother.setText(address.getPostalCode());
        }
        if (address.getCity() != null && !address.getCity().equals("")){
            editCityother.setText(address.getCity());
        }
        if (address.getState() != null && !address.getState().equals("")){
            editStateother.setText(address.getState());
        }
        if (address.getCountryOrRegion() != null && !address.getCountryOrRegion().equals("")){
            editCountryorregionother.setText(address.getCountryOrRegion());
        }
    }

    private void checkIfHomePhoneExist(ArrayList<String> phones){
        int counter = 0;
        for (String phone : phones){
            counter++;
            if (counter == 1) {
                editHomephone.setText(phone);
            } else {
                addEditTextHomePhones();
                EditText et = findViewById(R.string.homephoneEditText + (counter - 1));
                et.setVisibility(View.GONE);
                et.setText(phone);
            }
        }
    }

    private void checkIfBusinessPhoneExist(ArrayList<String> phones){
        int counter = 0;
        for (String phone : phones){
            counter++;
            if (counter == 1) {
                editBusinessphone.setText(phone);
            } else {
                addEditTextHBusinessPhones();
                EditText et = findViewById(R.string.businessphoneEditText + (counter - 1));
                et.setVisibility(View.GONE);
                et.setText(phone);
            }
        }
    }

    private void checkIfEmailsExist(ArrayList<EmailAddress> emailAddresses){
        int counter = 0;
        for (EmailAddress emailAddress : emailAddresses){
            counter++;
            if (counter == 1) {
                editEmail.setText(emailAddress.getAddress());
            } else {
                addEditText();
                EditText et = findViewById(R.string.emailEditText + (counter - 1));
                et.setVisibility(View.GONE);
                et.setText(emailAddress.getAddress());
            }
        }
    }
    private void checkIfInfoExist(String contactinfo, EditText editText) {
        if (contactinfo != null && !contactinfo.isEmpty() && !contactinfo.equals("") && !contactinfo.equals("[]")){
            if (!(editText.getId() == R.id.editBirthday)) {
                editText.setText(contactinfo);
            } else {
                try {
                    String newInfo = setDate(contactinfo);
                    editText.setText(newInfo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String setDate(String stringDate) throws ParseException {
        String JSON_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(JSON_FORMAT);
        Date date = formatter.parse(stringDate);
        String TO_FORMAT = "dd/MM/yyyy";
        String TO_FORMAT_IS_12 = "dd MMM yyyy";
        SimpleDateFormat outputFormat = new SimpleDateFormat(TO_FORMAT);
        return outputFormat.format(date);
    }

    public String getDate(String stringDate) throws ParseException {
        System.out.println(stringDate);
        String JSON_FORMAT = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(JSON_FORMAT);
        Date date = formatter.parse(stringDate);
        String TO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String TO_FORMAT_IS_12 = "dd MMM yyyy";
        SimpleDateFormat outputFormat = new SimpleDateFormat(TO_FORMAT);
        return outputFormat.format(date);
    }


    private void setActionBarMail(String title, Toolbar toolbar) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        // THIS LINE REMOVES ANNOYING LEFT MARGIN
        toolbar.setTitleMarginStart(30);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //INFLATE THE MENU, ADDS ITEMS TO THE BAR IF PRESENT
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditContactActivity.this);
                alertDialogBuilder.setTitle(R.string.alert_add_contact)
                        .setIcon(R.drawable.ic_delete_black_24dp)
                        .setMessage(R.string.alert_edit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(!(editGivenName.getText().toString().equals("") && editSurName.getText().toString().equals(""))){
                                    JSONObject jsonObject = new JSONObject();
                                    JSONObject result;
                                    try {
                                        result = getAllInfo(jsonObject);
                                        new GraphAPI().patchRequest(OutlookObjectCall.CONTACTS, EditContactActivity.this, result, "/" + contact.getId());
                                        Intent intent = new Intent(EditContactActivity.this, ContactsActivity.class);
                                        startActivity(intent);
                                    } catch (IllegalAccessException | ParseException | JSONException e) {
                                        Toast.makeText(EditContactActivity.this, R.string.error_edit_contact, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(EditContactActivity.this, R.string.fill_name, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEditText(){
        LinearLayout ll = (LinearLayout)
                findViewById(R.id.emailaddressesLayout);

        // add edittext
        EditText et = new EditText(this);
        // moet positief zijn
        et.setId(R.string.emailEditText + (numberOfeditTexts + 1));
        et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        ll.addView(et);

        numberOfeditTexts++;

        if (numberOfeditTexts > 0){
            removeEmail = (ImageView) findViewById(R.id.remove_email);
            removeEmail.setVisibility(View.VISIBLE);
        } else {
            removeEmail = (ImageView) findViewById(R.id.remove_email);
            removeEmail.setVisibility(View.GONE);
        }
    }

    private void addEditTextHomePhones(){
        //MAX 2 NUMMERS!!!
        if (numberOfeditTextsHomePhone < 1) {
            LinearLayout ll = (LinearLayout)
                    findViewById(R.id.homephonesLayout);

            // add edittext
            EditText et = new EditText(this);
            // moet positief zijn
            et.setId(R.string.homephoneEditText + (numberOfeditTextsHomePhone + 1));
            et.setInputType(InputType.TYPE_CLASS_PHONE);
            ll.addView(et);

            numberOfeditTextsHomePhone++;

            if (numberOfeditTextsHomePhone > 0) {
                removeHomephone = (ImageView) findViewById(R.id.remove_homephone);
                removeHomephone.setVisibility(View.VISIBLE);
            } else {
                removeHomephone = (ImageView) findViewById(R.id.remove_homephone);
                removeHomephone.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, R.string.max2, Toast.LENGTH_SHORT).show();
        }
    }

    private void addEditTextHBusinessPhones(){
        if (numberOfEditTextsBusinessPhone < 1  ) {
            LinearLayout ll = (LinearLayout)
                    findViewById(R.id.businessphonesLayout);

            // add edittext
            EditText et = new EditText(this);
            // moet positief zijn
            et.setId(R.string.businessphoneEditText +(numberOfEditTextsBusinessPhone + 1));
            et.setInputType(InputType.TYPE_CLASS_PHONE);
            ll.addView(et);

            numberOfEditTextsBusinessPhone++;

            if (numberOfEditTextsBusinessPhone > 0){
                removeBusinessphone = (ImageView) findViewById(R.id.remove_businessphone);
                removeBusinessphone.setVisibility(View.VISIBLE);
            } else {
                removeBusinessphone = (ImageView) findViewById(R.id.remove_businessphone);
                removeBusinessphone.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, R.string.max2, Toast.LENGTH_SHORT).show();
        }

    }

    private void setOnclickListeners() {
        open_close_user = (ImageView) findViewById(R.id.open_close_user);
        open_close_general = (ImageView) findViewById(R.id.open_close_general);
        open_close_business = (ImageView) findViewById(R.id.open_close_business);
        open_close_home = (ImageView) findViewById(R.id.open_close_home);
        open_close_businessaddress = (ImageView) findViewById(R.id.open_close_businessaddress);
        open_close_otheraddress = (ImageView) findViewById(R.id.open_close_otheraddress);
        open_close_email = (ImageView) findViewById(R.id.open_close_email);
        open_close_homephones = (ImageView) findViewById(R.id.open_close_homephone);
        open_close_businessphones = (ImageView) findViewById(R.id.open_close_businessphone);
        open_close_personalnote = (ImageView) findViewById(R.id.open_close_personalNote);


        // If time rewrite to 1 method
        userClicklistener();
        generalClicklistener();
        emailClickListener();
        homephonesClickListener();
        businessphonesClickListener();
        businessClicklistener();
        homeClicklistener();
        businessaddressClicklistener();
        otheraddressClicklistener();
        personalNoteClickListener();

    }

    private void emailClickListener() {
        emailaddressLayout = (ViewGroup) findViewById(R.id.emailLayout);
        addMoreEmail = (ImageView) findViewById(R.id.add_more_email);

        open_close_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailOpen) {
                    emailaddressLayout.setVisibility(View.GONE);
                    addMoreEmail.setVisibility(View.GONE);
                    open_close_email.setImageResource(R.drawable.ic_add_black_24dp);
                    for (int i = 1; i <= numberOfeditTexts; i++){
                        EditText et = (EditText) findViewById(R.string.emailEditText + i);
                        et.setVisibility(View.GONE);
                       // et.setId(0);
                    }
                    emailOpen = false;
                } else {
                    emailaddressLayout.setVisibility(View.VISIBLE);
                    addMoreEmail.setVisibility(View.VISIBLE);
                    open_close_email.setImageResource(R.drawable.ic_remove_black_24dp);
                    for (int i = 1; i <= numberOfeditTexts; i++){
                        EditText et = (EditText) findViewById(R.string.emailEditText + i);
                        et.setVisibility(View.VISIBLE);
                        // et.setId(0);
                    }
                    emailOpen = true;
                }
            }
        });
    }

    private void homephonesClickListener() {
        homephoneLayout = (ViewGroup) findViewById(R.id.homephoneLayout);
        addMoreHomephone = (ImageView) findViewById(R.id.add_more_homephone);

        open_close_homephones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(homePhoneOpen) {
                    homephoneLayout.setVisibility(View.GONE);
                    addMoreHomephone.setVisibility(View.GONE);
                    open_close_homephones.setImageResource(R.drawable.ic_add_black_24dp);
                    for(int i = 1; i <= numberOfeditTextsHomePhone; i++){
                        EditText et = (EditText) findViewById(R.string.homephoneEditText + i);
                        et.setVisibility(View.GONE);
                      //  et.setId(0);
                    }
                    homePhoneOpen = false;
                } else {
                    homephoneLayout.setVisibility(View.VISIBLE);
                    addMoreEmail.setVisibility(View.VISIBLE);
                    open_close_homephones.setImageResource(R.drawable.ic_remove_black_24dp);
                    for (int i = 1; i <= numberOfeditTextsHomePhone; i++){
                        EditText et = (EditText) findViewById(R.string.homephoneEditText + i);
                        et.setVisibility(View.VISIBLE);
                        // et.setId(0);
                    }
                    homePhoneOpen = true;
                }
            }
        });
    }

    private void personalNoteClickListener(){
        personalNoteLayout = (ViewGroup) findViewById(R.id.personalNoteLayout2);

        open_close_personalnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personalnoteOpen){
                    personalNoteLayout.setVisibility(View.GONE);
                    open_close_personalnote.setImageResource(R.drawable.ic_add_black_24dp);
                    personalnoteOpen = false;
                } else {
                    personalNoteLayout.setVisibility(View.VISIBLE);
                    open_close_personalnote.setImageResource(R.drawable.ic_remove_black_24dp);
                    personalnoteOpen= true;
                }
            }
        });
    }

    private void businessphonesClickListener() {
        businessphoneLayout = (ViewGroup) findViewById(R.id.businessphoneLayout);
        addMoreBusinessphone = (ImageView) findViewById(R.id.add_more_businessephone);

        open_close_businessphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(businessPhoneOpen) {
                    businessphoneLayout.setVisibility(View.GONE);
                    addMoreBusinessphone.setVisibility(View.GONE);
                    open_close_businessphones.setImageResource(R.drawable.ic_add_black_24dp);
                    for(int i = 1; i <= numberOfEditTextsBusinessPhone; i++){
                        EditText et = (EditText) findViewById(R.string.businessphoneEditText + i);
                        et.setVisibility(View.GONE);
                     //   et.setId(0);
                    }
                    businessPhoneOpen = false;
                } else {
                    businessphoneLayout.setVisibility(View.VISIBLE);
                    addMoreBusinessphone.setVisibility(View.VISIBLE);
                    open_close_businessphones.setImageResource(R.drawable.ic_remove_black_24dp);
                    for (int i = 1; i <= numberOfEditTextsBusinessPhone; i++){
                        EditText et = (EditText) findViewById(R.string.businessphoneEditText + i);
                        et.setVisibility(View.VISIBLE);
                        // et.setId(0);
                    }
                    businessPhoneOpen = true;
                }
            }
        });
    }

    private void otheraddressClicklistener() {
        streetotherLayout = (ViewGroup) findViewById(R.id.streetotherLayout);
        cityotherLayout = (ViewGroup) findViewById(R.id.cityotherLayout);
        postalcodeotherLayout = (ViewGroup) findViewById(R.id.postalcodeotherLayout);
        stateotherLayout = (ViewGroup) findViewById(R.id.stateotherLayout);
        countryorregionotherLayout = (ViewGroup) findViewById(R.id.countryorregionotherLayout);

        open_close_otheraddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherOpen) {
                    streetotherLayout.setVisibility(View.GONE);
                    cityotherLayout.setVisibility(View.GONE);
                    postalcodeotherLayout.setVisibility(View.GONE);
                    stateotherLayout.setVisibility(View.GONE);
                    countryorregionotherLayout.setVisibility(View.GONE);
                    open_close_otheraddress.setImageResource(R.drawable.ic_add_black_24dp);
                    otherOpen = false;
                } else {
                    streetotherLayout.setVisibility(View.VISIBLE);
                    cityotherLayout.setVisibility(View.VISIBLE);
                    postalcodeotherLayout.setVisibility(View.VISIBLE);
                    stateotherLayout.setVisibility(View.VISIBLE);
                    countryorregionotherLayout.setVisibility(View.VISIBLE);
                    open_close_otheraddress.setImageResource(R.drawable.ic_remove_black_24dp);
                    otherOpen = true;
                }
            }
        });
    }

    private void businessaddressClicklistener() {
        streetbusinessLayout = (ViewGroup) findViewById(R.id.streetbusinessLayout);
        citybusinessLayout = (ViewGroup) findViewById(R.id.citybusinessLayout);
        postalcodebusinessLayout = (ViewGroup) findViewById(R.id.postalcodebusinessLayout);
        statebusinessLayout = (ViewGroup) findViewById(R.id.statebusinessLayout);
        countryorregionbusinessLayout = (ViewGroup) findViewById(R.id.countryorregionbusinessLayout);

        open_close_businessaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessAddressOpen) {
                    streetbusinessLayout.setVisibility(View.GONE);
                    citybusinessLayout.setVisibility(View.GONE);
                    postalcodebusinessLayout.setVisibility(View.GONE);
                    statebusinessLayout.setVisibility(View.GONE);
                    countryorregionbusinessLayout.setVisibility(View.GONE);
                    open_close_businessaddress.setImageResource(R.drawable.ic_add_black_24dp);
                    businessAddressOpen = false;
                } else {
                    streetbusinessLayout.setVisibility(View.VISIBLE);
                    citybusinessLayout.setVisibility(View.VISIBLE);
                    postalcodebusinessLayout.setVisibility(View.VISIBLE);
                    statebusinessLayout.setVisibility(View.VISIBLE);
                    countryorregionbusinessLayout.setVisibility(View.VISIBLE);
                    open_close_businessaddress.setImageResource(R.drawable.ic_remove_black_24dp);
                    businessAddressOpen = true;
                }
            }
        });

    }

    private void homeClicklistener() {
        streethomeLayout = (ViewGroup) findViewById(R.id.streethomeLayout);
        cityhomeLayout = (ViewGroup) findViewById(R.id.cityhomeLayout);
        postalcodehomeLayout = (ViewGroup) findViewById(R.id.postalcodehomeLayout);
        statehomeLayout = (ViewGroup) findViewById(R.id.statehomeLayout);
        countryorregionhomeLayout = (ViewGroup) findViewById(R.id.countryorregionhomeLayout);
        open_close_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeOpen) {
                    streethomeLayout.setVisibility(View.GONE);
                    cityhomeLayout.setVisibility(View.GONE);
                    postalcodehomeLayout.setVisibility(View.GONE);
                    statehomeLayout.setVisibility(View.GONE);
                    countryorregionhomeLayout.setVisibility(View.GONE);
                    open_close_home.setImageResource(R.drawable.ic_add_black_24dp);
                    homeOpen = false;
                } else {
                    streethomeLayout.setVisibility(View.VISIBLE);
                    cityhomeLayout.setVisibility(View.VISIBLE);
                    postalcodehomeLayout.setVisibility(View.VISIBLE);
                    statehomeLayout.setVisibility(View.VISIBLE);
                    countryorregionhomeLayout.setVisibility(View.VISIBLE);
                    open_close_home.setImageResource(R.drawable.ic_remove_black_24dp);
                    homeOpen = true;
                }
            }
        });
    }

    private void businessClicklistener() {
        companyNameLayout = (ViewGroup) findViewById(R.id.companyNameLayout);
        departmentLayout = (ViewGroup) findViewById(R.id.departmentLayout);
        officeLocationLayout = (ViewGroup) findViewById(R.id.officeLocationLayout);
        businesshomepageLayout = (ViewGroup) findViewById(R.id.businesshomepageLayout);
        jobTitleLayout = (ViewGroup) findViewById(R.id.jobTitleLayout);
        professionLayout = (ViewGroup) findViewById(R.id.professionLayout);
        assistantnameLayout = (ViewGroup) findViewById(R.id.assistantnameLayout);
        managerLayout = (ViewGroup) findViewById(R.id.managerLayout);

        open_close_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (businessOpen){
                    companyNameLayout.setVisibility(View.GONE);
                    departmentLayout.setVisibility(View.GONE);
                    officeLocationLayout.setVisibility(View.GONE);
                    businesshomepageLayout.setVisibility(View.GONE);
                    jobTitleLayout.setVisibility(View.GONE);
                    assistantnameLayout.setVisibility(View.GONE);
                    professionLayout.setVisibility(View.GONE);
                    managerLayout.setVisibility(View.GONE);
                    open_close_business.setImageResource(R.drawable.ic_add_black_24dp);
                    businessOpen = false;
                } else {
                    companyNameLayout.setVisibility(View.VISIBLE);
                    departmentLayout.setVisibility(View.VISIBLE);
                    officeLocationLayout.setVisibility(View.VISIBLE);
                    businesshomepageLayout.setVisibility(View.VISIBLE);
                    jobTitleLayout.setVisibility(View.VISIBLE);
                    assistantnameLayout.setVisibility(View.VISIBLE);
                    professionLayout.setVisibility(View.VISIBLE);
                    managerLayout.setVisibility(View.VISIBLE);
                    open_close_business.setImageResource(R.drawable.ic_remove_black_24dp);
                    businessOpen = true;
                }
            }
        });
    }

    private void generalClicklistener() {
        birthdayLayout = (ViewGroup) findViewById(R.id.birthdayLayout);
        spousenameLayout = (ViewGroup) findViewById(R.id.spousenameLayout);
        mobilephoneLayout = (ViewGroup) findViewById(R.id.mobilephoneLayout);

        open_close_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generalOpen){
                    birthdayLayout.setVisibility(View.GONE);
                    spousenameLayout.setVisibility(View.GONE);
                    mobilephoneLayout.setVisibility(View.GONE);
                    open_close_general.setImageResource(R.drawable.ic_add_black_24dp);
                    generalOpen = false;
                } else {
                    final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }
                    };

                    editBirthday.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //create new styles in style xml to make spinner calendar!!!
                            try {
                                String date = setDate(contact.getBirthday());
                                String[] dates = date.split("/");
                                new DatePickerDialog(EditContactActivity.this, R.style.MySpinnerDatePickerStyle, dateDialog, myCalendar
                                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    birthdayLayout.setVisibility(View.VISIBLE);
                    mobilephoneLayout.setVisibility(View.VISIBLE);
                    spousenameLayout.setVisibility(View.VISIBLE);
                    open_close_general.setImageResource(R.drawable.ic_remove_black_24dp);
                    generalOpen = true;
                    }
                }
            });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    private void userClicklistener() {
        givenNameLayout = (ViewGroup) findViewById(R.id.givenNameLayout);
        surNameLayout = (ViewGroup) findViewById(R.id.surNameLayout);
        middleNameLayout = (ViewGroup) findViewById(R.id.middleNameLayout);
        nickNameLayout = (ViewGroup) findViewById(R.id.nickNameLayout);
        initialsLayout = (ViewGroup) findViewById(R.id.initialsLayout);
        titleLayout = (ViewGroup) findViewById(R.id.titleLayout);
        open_close_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userOpen) {
                    givenNameLayout.setVisibility(View.GONE);
                    surNameLayout.setVisibility(View.GONE);
                    middleNameLayout.setVisibility(View.GONE);
                    nickNameLayout.setVisibility(View.GONE);
                    initialsLayout.setVisibility(View.GONE);
                    titleLayout.setVisibility(View.GONE);
                    open_close_user.setImageResource(R.drawable.ic_add_black_24dp);
                    userOpen = false;
                } else {
                    givenNameLayout.setVisibility(View.VISIBLE);
                    surNameLayout.setVisibility(View.VISIBLE);
                    middleNameLayout.setVisibility(View.VISIBLE);
                    nickNameLayout.setVisibility(View.VISIBLE);
                    initialsLayout.setVisibility(View.VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
                    open_close_user.setImageResource(R.drawable.ic_remove_black_24dp);
                    userOpen = true;
                }
            }
        });
    }

    private JSONObject getAllInfo(JSONObject jsonObject) throws JSONException, ParseException {

        checkIfEditTextFilled(editGivenName, jsonObject, "givenName");
        checkIfEditTextFilled(editSurName, jsonObject, "surname");
        checkIfEditTextFilled(editMiddleName, jsonObject, "givenName");
        checkIfEditTextFilled(editNickName, jsonObject, "nickName");
        checkIfEditTextFilled(editInitials, jsonObject, "initials");
        checkIfEditTextFilled(editTitle, jsonObject, "title");

        checkIfEditTextFilled(editBirthday, jsonObject, "birthday");
        checkIfEditTextFilled(editMobilephone, jsonObject, "mobilePhone");
        checkIfEditTextFilled(editSpousename, jsonObject, "spouseName");

        checkIfEditTextFilled(editEmail, jsonObject, "emailAddresses");

        checkIfEditTextFilled(editHomephone, jsonObject, "homePhones");

        checkIfEditTextFilled(editBusinessphone, jsonObject, "businessPhones");

        checkIfEditTextFilled(editCompanyName, jsonObject, "companyName");
        checkIfEditTextFilled(editDepartment, jsonObject, "department");
        checkIfEditTextFilled(editOfficeLocation, jsonObject, "officeLocation");
        checkIfEditTextFilled(editBusinesshomepage, jsonObject, "businesshomepage");
        checkIfEditTextFilled(editJobTitle, jsonObject, "jobTitle");
        checkIfEditTextFilled(editProfession, jsonObject, "profession");
        checkIfEditTextFilled(editAssistantname, jsonObject, "assistantName");
        checkIfEditTextFilled(editManager, jsonObject, "manager");

        JSONObject jsonObjectHome = new JSONObject();
        checkIfEditTextAddressFilled(editStreethome, jsonObject, "homeAddress", jsonObjectHome, "street");
        checkIfEditTextAddressFilled(editCityhome, jsonObject, "homeAddress", jsonObjectHome, "city");
        checkIfEditTextAddressFilled(editPostalcodehome, jsonObject, "homeAddress", jsonObjectHome, "postalcode");
        checkIfEditTextAddressFilled(editStatehome, jsonObject, "homeAddress", jsonObjectHome, "state");
        checkIfEditTextAddressFilled(editCountryorregionhome, jsonObject, "homeAddress", jsonObjectHome, "countryOrRegion");

        JSONObject jsonObjectBusiness = new JSONObject();
        checkIfEditTextAddressFilled(editStreetbusiness, jsonObject, "businessAddress", jsonObjectBusiness, "street");
        checkIfEditTextAddressFilled(editCityhome, jsonObject, "businessAddress", jsonObjectBusiness, "city");
        checkIfEditTextAddressFilled(editStatehome, jsonObject, "businessAddress", jsonObjectBusiness, "state");
        checkIfEditTextAddressFilled(editPostalcodebusiness, jsonObject, "businessAddress", jsonObjectBusiness, "postalcode");
        checkIfEditTextAddressFilled(editCountryorregionbusiness, jsonObject, "businessAddress", jsonObjectBusiness, "countryOrRegion");

        JSONObject jsonObjectOther = new JSONObject();
        checkIfEditTextAddressFilled(editStreetother, jsonObject, "otherAddress", jsonObjectOther, "street");
        checkIfEditTextAddressFilled(editCityother, jsonObject, "otherAddress", jsonObjectOther, "city");
        checkIfEditTextAddressFilled(editStateother, jsonObject, "otherAddress", jsonObjectOther, "state");
        checkIfEditTextAddressFilled(editPostalcodeother, jsonObject, "otherAddress", jsonObjectOther, "postalcode");
        checkIfEditTextAddressFilled(editCountryorregionother, jsonObject, "otherAddress", jsonObjectOther, "countryOrRegion");

        checkIfEditTextFilled(editPersonalnote, jsonObject, "personalNotes");
        return jsonObject;
    }

    private void checkIfEditTextAddressFilled(EditText editText, JSONObject jsonObject, String jsonString, JSONObject jsonObjectAddress, String jsonStringAddress) throws JSONException {
        if (!editText.getText().toString().equals("")){
            jsonObjectAddress.put(jsonStringAddress, editText.getText().toString());
            jsonObject.put(jsonString, jsonObjectAddress);
        }
    }

    private void checkIfEditTextFilled(EditText editText, JSONObject jsonObject, String jsonString) throws JSONException, ParseException {
        switch (editText.getId()){
            case R.id.editBirthday:
                if (!editText.getText().toString().equals("")){
                    String date = getDate(editText.getText().toString());
                    jsonObject.put(jsonString, date);
                }
                break;
            case R.id.editEmail:
                JSONArray emailJsonArray = new JSONArray();
                JSONObject emailJson = new JSONObject();

                if (!editText.getText().toString().equals("") && isEmailValid(editText.getText().toString())){
                    emailJson.put("name", editText.getText().toString());
                    emailJson.put("address", editText.getText().toString());
                    emailJsonArray.put(emailJson);

                } else {
                    if (!editText.getText().toString().equals("") && !isEmailValid(editText.getText().toString())) {
                        Toast.makeText(EditContactActivity.this, R.string.invalidEmail, Toast.LENGTH_SHORT).show();
                    }
                }
                if (numberOfeditTexts > 0) {
                    for(int i = 1; i <= numberOfeditTexts; i ++){
                        EditText et = (EditText) findViewById(R.string.emailEditText + i);
                        if (!et.getText().toString().equals("") && isEmailValid(et.getText().toString())){
                            JSONObject nextEmailJson = new JSONObject();
                            nextEmailJson.put("name", et.getText().toString());
                            nextEmailJson.put("address", et.getText().toString());
                            emailJsonArray.put(nextEmailJson);
                        } else {
                            if(!et.getText().toString().equals("") && !isEmailValid(et.getText().toString())){
                                Toast.makeText(EditContactActivity.this, R.string.invalidEmail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if (numberOfeditTexts == 0){
                    if (!editText.getText().toString().equals("") && isEmailValid(editText.getText().toString())){
                        jsonObject.put("emailAddresses", emailJsonArray);
                    }
                } else {
                    if (!editText.getText().toString().equals("") && isEmailValid(editText.getText().toString())){
                        jsonObject.put("emailAddresses", emailJsonArray);
                    }
                }

                break;
            case R.id.editHomephone:
                ArrayList<String> homePhoneNumbers = new ArrayList<>();
                if (!editText.getText().toString().equals("")){
                    homePhoneNumbers.add(editText.getText().toString());
                } else {
                    if (!editText.getText().toString().equals("") ) {
                        Toast.makeText(EditContactActivity.this, R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                    }
                }
                if (numberOfeditTextsHomePhone > 0) {
                    for(int i = 1; i <= numberOfeditTextsHomePhone; i ++){
                        EditText et = (EditText) findViewById(R.string.homephoneEditText + i);
                        if (!et.getText().toString().equals("")){
                            homePhoneNumbers.add(et.getText().toString());
                        } else {
                            if(!et.getText().toString().equals("")){
                                Toast.makeText(EditContactActivity.this, R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if (numberOfeditTextsHomePhone == 0){
                    if (!editText.getText().toString().equals("")){
                        jsonObject.put("homePhones", new JSONArray(homePhoneNumbers));
                    }
                } else {
                    if (!editText.getText().toString().equals("")){
                        jsonObject.put("homePhones", new JSONArray(homePhoneNumbers));
                    }
                }
                break;
            case R.id.editBusinessphone:
                ArrayList<String> businessPhoneNumbers = new ArrayList<>();
                if (!editText.getText().toString().equals("")){
                    businessPhoneNumbers.add(editText.getText().toString());
                } else {
                    if (!editText.getText().toString().equals("")) {
                        Toast.makeText(EditContactActivity.this, R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                    }
                }
                if (numberOfEditTextsBusinessPhone > 0) {
                    for(int i = 1; i <= numberOfEditTextsBusinessPhone; i ++){
                        EditText et = (EditText) findViewById(R.string.businessphoneEditText + i);
                        if (!et.getText().toString().equals("")){
                            businessPhoneNumbers.add(et.getText().toString());
                        } else {
                            if(!et.getText().toString().equals("")){
                                Toast.makeText(EditContactActivity.this, R.string.invalidPhone, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if (numberOfEditTextsBusinessPhone == 0){
                    if (!editText.getText().toString().equals("")){
                        jsonObject.put("homePhones", new JSONArray(businessPhoneNumbers));
                    }
                } else {
                    if (!editText.getText().toString().equals("")){
                        jsonObject.put("homePhones", new JSONArray(businessPhoneNumbers));
                    }
                }
                break;
            default:
                if(!editText.getText().toString().equals("")){
                    jsonObject.put(jsonString, editText.getText().toString());
                }
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
