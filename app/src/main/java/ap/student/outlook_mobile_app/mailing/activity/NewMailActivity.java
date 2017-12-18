package ap.student.outlook_mobile_app.mailing.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.models.Body;
import ap.student.outlook_mobile_app.DAL.models.Contact;
import ap.student.outlook_mobile_app.DAL.models.EmailAddress;
import ap.student.outlook_mobile_app.DAL.models.Message;
import ap.student.outlook_mobile_app.DAL.models.Recipient;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

/**
 * Created by Alexander on 12/7/2017.
 */

public class NewMailActivity extends AppCompatActivityRest {

    private static final String TAG = "NewMailActivity";
    private AutoCompleteTextView recipientTextField;
    private AutoCompleteTextView ccTextField;
    private AutoCompleteTextView bccTextField;
    private EditText subjectTextField;
    private EditText messageTextField;
    private List<Contact> contacts = new ArrayList<>();
    private LinearLayout ccBar;
    private LinearLayout bccBar;
    private View ccView;
    private View bccView;
    private boolean hasOpenedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        subjectTextField = (EditText) findViewById(R.id.subjectTextField);
        messageTextField = (EditText) findViewById(R.id.messageTextField);
        recipientTextField = (AutoCompleteTextView) findViewById(R.id.recipientTextField);
        ccTextField = (AutoCompleteTextView) findViewById(R.id.ccTextField);
        bccTextField = (AutoCompleteTextView) findViewById(R.id.bccTextField);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView moreRecipients = (ImageView)findViewById(R.id.moreRecipients);

        ccBar = (LinearLayout) findViewById(R.id.ccBar);
        bccBar = (LinearLayout) findViewById(R.id.bccBar);
        ccView = (View) findViewById(R.id.ccView);
        bccView = (View) findViewById(R.id.bccView);

        ccBar.setVisibility(View.GONE);
        bccBar.setVisibility(View.GONE);
        ccView.setVisibility(View.GONE);
        bccView.setVisibility(View.GONE);
        hasOpenedMenu = false;

        moreRecipients.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hasOpenedMenu = !hasOpenedMenu;

                if (hasOpenedMenu) {
                    ccBar.setVisibility(View.VISIBLE);
                    bccBar.setVisibility(View.VISIBLE);
                    ccView.setVisibility(View.VISIBLE);
                    bccView.setVisibility(View.VISIBLE);
                } else if (!hasOpenedMenu) {
                    ccBar.setVisibility(View.GONE);
                    bccBar.setVisibility(View.GONE);
                    ccView.setVisibility(View.GONE);
                    bccView.setVisibility(View.GONE);
                }

            }
        });

        //https://developer.android.com/reference/android/widget/AutoCompleteTextView.html
        getContacts();

    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

        switch (outlookObjectCall) {
            case READCONTACTS: {
                System.out.println(response.toString());
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
                AutoCompleteTextView textView2 = (AutoCompleteTextView)
                        findViewById(R.id.ccTextField);
                AutoCompleteTextView textView3 = (AutoCompleteTextView)
                        findViewById(R.id.bccTextField);

                textView.setAdapter(adapter);
                textView2.setAdapter(adapter);
                textView3.setAdapter(adapter);

            }
            break;

            case SENDMAIL: {
                System.out.println(response.toString());
            }


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

        else if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMail() throws JSONException {

        String subject = subjectTextField.getText().toString();
        String messageString = messageTextField.getText().toString();
        Body body = new Body(messageString, "Text");

        String recipientsString = recipientTextField.getText().toString();
        List<Recipient> toRecipients = splitEmailadresses(recipientsString);
        String ccString = ccTextField.getText().toString();
        String bccString = bccTextField.getText().toString();

        Message message;

        if (!bccString.equals("") && !ccString.equals(""))
        {
            System.out.println("There's CC and BCC!");
            List<Recipient> ccRecipients = splitEmailadresses(ccString);
            List<Recipient> bccRecipients = splitEmailadresses(bccString);
            message = new Message(subject, body, toRecipients, ccRecipients, bccRecipients);
        }
        else if (!ccString.equals("")) {
            System.out.println("There's CC!");
            List<Recipient> ccRecipients = splitEmailadresses(ccString);
            message = new Message(subject, body, toRecipients, ccRecipients);
        }

        else if (!bccString.equals("")) {
            System.out.println("There's BCC!");
            List<Recipient> bccRecipients = splitEmailadresses(bccString);
            message = new Message(body, toRecipients, bccRecipients, subject);
        }
        else {
            System.out.println("Else!");
            message = new Message(subject, body, toRecipients);
        }

        //convert Message object to JSON
        JSONObject JSON = new JSONObject(new Gson().toJson(message));

        //wrap JSONobject in another JSONobject to make sure format is correct {"message": message}
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", JSON);

        System.out.println(jsonMessage);

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

    public List<Recipient> splitEmailadresses(String emailString){

        String[] recipientsSplit = emailString.split(";");
        List<Recipient> recipients = new ArrayList<>();

        for (String s : recipientsSplit) {
            System.out.println(s);
            EmailAddress toEmailAddress = new EmailAddress(s);
            Recipient toRecipient = new Recipient(toEmailAddress);
            recipients.add(toRecipient);
        }

        return recipients;

    }

}
