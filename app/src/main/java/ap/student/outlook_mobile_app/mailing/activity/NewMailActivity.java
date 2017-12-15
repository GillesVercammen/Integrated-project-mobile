package ap.student.outlook_mobile_app.mailing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.models.Body;
import ap.student.outlook_mobile_app.DAL.models.Contact;
import ap.student.outlook_mobile_app.DAL.models.EmailAddress;
import ap.student.outlook_mobile_app.DAL.models.Message;
import ap.student.outlook_mobile_app.DAL.models.ToRecipients;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

/**
 * Created by Alexander on 12/7/2017.
 */

public class NewMailActivity extends AppCompatActivityRest {

    private static final String TAG = "NewMailActivity";
    private AutoCompleteTextView recipientTextField;
    private EditText subjectTextField;
    private EditText messageTextField;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        subjectTextField = (EditText) findViewById(R.id.subjectTextField);
        messageTextField = (EditText) findViewById(R.id.messageTextField);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //https://developer.android.com/reference/android/widget/AutoCompleteTextView.html
        //getContacts through API, map to objects, getEmails (String), put Strings in list and connect them to AutoCompleteTextView
        getContacts();

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_dropdown_item_1line, contactList);
        //AutoCompleteTextView textView = (AutoCompleteTextView)
        //        findViewById(R.id.recipientTextField);
        //textView.setAdapter(adapter);

    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

        switch (outlookObjectCall) {
            case READCONTACTS: {
                System.out.println("Reading contacts");

                JSONObject list = response;
                try {
                    JSONArray contactList = list.getJSONArray("value");
                    // MAP ON POJO
                    Type listType = new TypeToken<List<Contact>>() {
                    }.getType();
                    contacts = new Gson().fromJson(String.valueOf(contactList), listType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //IF RESPONSE IS EMPTY SHOW: NO CONTACTS FOUND
                if (contacts.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No contacts found!", Toast.LENGTH_SHORT).show();
                }

                int counter = 0;

                for (int i=0; i<contacts.size(); i++)
                {
                    if (contacts.get(i).getEmailAddresses().size() > 0) {
                        counter++;
                    }
                }

                int j = 0;

                String[] contactList = new String[counter];

                for (int i=0; i<contacts.size(); i++)
                {
                    if (contacts.get(i).getEmailAddresses().size() > 0) {
                        //System.out.println(contacts.get(i).getEmailAddresses().get(0).getAddress());
                        contactList[j] = contacts.get(i).getEmailAddresses().get(0).getAddress();
                        j++;
                    }
                }

                for (String c : contactList
                     ) {
                    System.out.println(c);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_dropdown_item_1line, contactList);
                AutoCompleteTextView textView = (AutoCompleteTextView)
                        findViewById(R.id.recipientTextField);
                textView.setAdapter(adapter);

            }
            break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send_mail) {
            Toast.makeText(getApplicationContext(), "Sending mail...", Toast.LENGTH_SHORT).show();
            try {
                sendMail();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMail() throws JSONException {

        //take input from user and parse to String
        String recipientsString = recipientTextField.getText().toString();
        String subject = subjectTextField.getText().toString();
        String messageString = messageTextField.getText().toString();

        //make object from our input fields
        Body body = new Body(messageString, "Text");
        List<ToRecipients> toRecipientsList = new ArrayList<>();
        EmailAddress emailAddress = new EmailAddress(recipientsString);
        ToRecipients toRecipients = new ToRecipients(emailAddress);
        toRecipientsList.add(toRecipients);

        //this is our full Message object to send
        Message message = new Message(subject, body, toRecipientsList);

        //convert Message object to JSON
        JSONObject JSON = new JSONObject(new Gson().toJson(message));

        //wrap JSONobject in another JSONobject to make sure format is correct {"message": message}
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", JSON);

        //do our call
        try {
            new GraphAPI().postRequest(OutlookObjectCall.SENDMAIL, this, jsonMessage);
            Toast.makeText(getApplicationContext(), "Mail sent!", Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong! Please review your e-mail.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        this.finish();
    }

    public void getContacts(){
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READCONTACTS, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

}
