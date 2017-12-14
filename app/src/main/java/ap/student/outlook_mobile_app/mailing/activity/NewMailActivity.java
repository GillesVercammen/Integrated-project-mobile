package ap.student.outlook_mobile_app.mailing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.models.Body;
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
    private EditText recipientTextField;
    private EditText subjectTextField;
    private EditText messageTextField;

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        recipientTextField = (EditText) findViewById(R.id.recipientTextField);
        subjectTextField = (EditText) findViewById(R.id.subjectTextField);
        messageTextField = (EditText) findViewById(R.id.messageTextField);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

}
