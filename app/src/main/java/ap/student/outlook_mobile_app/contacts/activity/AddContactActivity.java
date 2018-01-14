package ap.student.outlook_mobile_app.contacts.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ap.student.outlook_mobile_app.R;

public class AddContactActivity extends AppCompatActivity {

    private ViewGroup givenNameLayout, surNameLayout,middleNameLayout, nickNameLayout, initialsLayout, titleLayout;
    private EditText editGivenName, editSurName, editMiddleName, editNickName, editInitials, editTitle;

    private ViewGroup birthdayLayout, spousenameLayout;
    private EditText editBirthday, editSpousename;

    private ViewGroup emailaddressLayout;
    private EditText editEmail;

    private ViewGroup companyNameLayout, departmentLayout, officeLocationLayout, businesshomepageLayout, jobTitleLayout, professionLayout, assistantnameLayout, managerLayout;
    private EditText editCompanyName, editDepartment, editOfficeLocation, editBusinesshomepage, editJobTitle, editProfession, editAssistantname, editManager;

    private ViewGroup streethomeLayout, cityhomeLayout, postalcodehomeLayout, statehomeLayout, countryorregionhomeLayout;
    private EditText editStreethome, editCityhome, editPostalcodehome, editStatehome, editCountryorregionhome;

    private ViewGroup streetbusinessLayout, citybusinessLayout, postalcodebusinessLayout, statebusinessLayout, countryorregionbusinessLayout;
    private EditText editStreetbusiness, editCitybusiness, editPostalcodebusiness, editStatebusiness, editCountryorregionbusiness;

    private ViewGroup streetotherLayout, cityotherLayout, postalcodeotherLayout, stateotherLayout, countryorregionotherLayout;
    private EditText editStreetother, editCityother, editPostalcodeother, editStateother, editCountryorregionother;

    private ImageView open_close_user, open_close_general, open_close_business, open_close_home, open_close_businessaddress, open_close_otheraddress, open_close_email, addMoreEmail, removeEmail;

    private boolean userOpen = true;
    private boolean generalOpen = false;
    private boolean businessOpen = false;
    private boolean homeOpen = false;
    private boolean businessAddressOpen = false;
    private boolean otherOpen = false;
    private boolean emailOpen = false;
    public int numberOfeditTexts = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String toolbarTitle = getString(R.string.add_contact);
        setActionBarMail(toolbarTitle, toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editGivenName = (EditText) findViewById(R.id.editGivenName);
        editSurName = (EditText) findViewById(R.id.editSurName);
        editMiddleName = (EditText) findViewById(R.id.editMiddleName);
        editNickName = (EditText) findViewById(R.id.editNickName);
        editInitials = (EditText) findViewById(R.id.editInitials);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editGivenName = (EditText) findViewById(R.id.editGivenName);

        editBirthday = (EditText) findViewById(R.id.editBirthday);
        editSpousename = (EditText) findViewById(R.id.editSpousename);

        editEmail = (EditText) findViewById(R.id.editEmail);

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
        removeEmail = (ImageView) findViewById(R.id.remove_email);
        
        setOnclickListeners();

        addMoreEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditText();
            }
        });
        removeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idEditText = numberOfeditTexts ;

                EditText et = (EditText) findViewById(idEditText);

                // cheeky fix to "delete" editText
                et.setId(0);
                et.setVisibility(View.GONE);
                numberOfeditTexts--;            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //INFLATE THE MENU, ADDS ITEMS TO THE BAR IF PRESENT
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEditText(){
        LinearLayout ll = (LinearLayout)
                findViewById(R.id.emailaddressesLayout);

        // add edittext
        EditText et = new EditText(this);
        // moet positief zijn
        et.setId(numberOfeditTexts + 1);
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

    private void setOnclickListeners() {
        open_close_user = (ImageView) findViewById(R.id.open_close_user);
        open_close_general = (ImageView) findViewById(R.id.open_close_general);
        open_close_business = (ImageView) findViewById(R.id.open_close_business);
        open_close_home = (ImageView) findViewById(R.id.open_close_home);
        open_close_businessaddress = (ImageView) findViewById(R.id.open_close_businessaddress);
        open_close_otheraddress = (ImageView) findViewById(R.id.open_close_otheraddress);
        open_close_email = (ImageView) findViewById(R.id.open_close_email);

        userClicklistener();
        generalClicklistener();
        emailClickListener();
        businessClicklistener();
        homeClicklistener();
        businessaddressClicklistener();
        otheraddressClicklistener();

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
                        EditText et = (EditText) findViewById(i);
                        et.setVisibility(View.GONE);
                        et.setId(0);
                    }
                    emailOpen = false;
                } else {
                    emailaddressLayout.setVisibility(View.VISIBLE);
                    addMoreEmail.setVisibility(View.VISIBLE);
                    open_close_email.setImageResource(R.drawable.ic_remove_black_24dp);
                    emailOpen = true;
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

        open_close_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generalOpen){
                    birthdayLayout.setVisibility(View.GONE);
                    spousenameLayout.setVisibility(View.GONE);
                    open_close_general.setImageResource(R.drawable.ic_add_black_24dp);
                    generalOpen = false;
                } else {
                    birthdayLayout.setVisibility(View.VISIBLE);
                    spousenameLayout.setVisibility(View.VISIBLE);
                    open_close_general.setImageResource(R.drawable.ic_remove_black_24dp);
                    generalOpen = true;
                }
            }
        });
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

    private void setActionBarMail(String title, Toolbar toolbar) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        // THIS LINE REMOVES ANNOYING LEFT MARGIN
        toolbar.setTitleMarginStart(30);
    }


}
