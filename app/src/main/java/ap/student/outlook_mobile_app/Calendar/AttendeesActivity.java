package ap.student.outlook_mobile_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.activity.ContactsActivity;
import ap.student.outlook_mobile_app.DAL.models.Contact;

/**
 * Created by alek on 12.01.18.
 */

public class AttendeesActivity extends ContactsActivity {
    private Gson gson;
    private Contact contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView bottomNavigationItemView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationItemView.setVisibility(View.GONE);

        gson = new Gson();

        if (getIntent().getStringExtra("attendees") == null) {
            contacts = new Contact();
        } else {
            contacts = gson.fromJson(getIntent().getStringExtra("attendees"), Contact.class);
        }

        if (contacts.getContacts() != null) {
            for (Contact contact : contacts.getContacts()) {

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(201, new Intent().putExtra("contacts", gson.toJson(contacts)));
        }
        return super.onOptionsItemSelected(item);
    }
}
