package ap.student.outlook_mobile_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.models.Attendee;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.activity.ContactsActivity;
import ap.student.outlook_mobile_app.mailing.model.EmailAddress;

/**
 * Created by alek on 12.01.18.
 */

public class AttendeesActivity extends ContactsActivity {
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView bottomNavigationItemView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationItemView.setVisibility(View.GONE);

        gson = new Gson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_check_white_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        super.processResponse(outlookObjectCall, response);
        if (outlookObjectCall.equals(OutlookObjectCall.CONTACTS)) {
            String addresses = getIntent().getStringExtra("attendees");
            if (addresses != null) {
                ap.student.outlook_mobile_app.DAL.models.EmailAddress[] emailAddresses = gson.fromJson(addresses, ap.student.outlook_mobile_app.DAL.models.EmailAddress[].class);
                for (ap.student.outlook_mobile_app.DAL.models.EmailAddress emailAddress : emailAddresses) {
                    for (int i = 0; i < contacts.size(); i++) {
                        if (contacts.get(i).getEmailAddresses().size() != 0 && contacts.get(i).getEmailAddresses().get(0).getName().equals(emailAddress.getName())) {
                            onIconClicked(i);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            List<EmailAddress> emailAddressList = new ArrayList<EmailAddress>();
            boolean badContacts = false;

            for (int select : mAdapter.getSelectedItems()) {
                if (contacts.get(select).getEmailAddresses().size() != 0) {
                    emailAddressList.add(contacts.get(select).getEmailAddresses().get(0));
                } else {
                    badContacts = true;
                }
            }
            if (badContacts) {
                Toast.makeText(this, "One or more selected contacts don't have email addresses attached to them. They will be ignored.", Toast.LENGTH_LONG).show();
            }

            String json = gson.toJson(emailAddressList.toArray(new EmailAddress[]{}));
            setResult(RESULT_OK, new Intent().putExtra("attendees", json));
        }
        return super.onOptionsItemSelected(item);
    }
}
