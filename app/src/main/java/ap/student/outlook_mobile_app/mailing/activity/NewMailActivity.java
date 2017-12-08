package ap.student.outlook_mobile_app.mailing.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

/**
 * Created by Alexander on 12/7/2017.
 */

public class NewMailActivity extends AppCompatActivityRest {

    private EditText recipientTextField;
    private EditText subjectTextField;
    private EditText messageTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        recipientTextField = (EditText) findViewById(R.id.recipientTextField);
        subjectTextField = (EditText) findViewById(R.id.subjectTextField);
        messageTextField = (EditText) findViewById(R.id.messageTextField);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_mail, menu);
        return true;
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
//
    }

    public void sendMail(){
        //get input fields, parse them to JSON, do call, show toaster for correct mesage

        String recipient = recipientTextField.toString();
        String subject = subjectTextField.toString();
        String message = messageTextField.toString();

        JSONObject JSON = new JSONObject();

        //TextView subtitleTextView =
        //        (TextView) rowView.findViewById(R.id.mail_list_subtitle);
        //if (!jsonObject.getString("subject").isEmpty()) {
        //    subtitleTextView.setText(jsonObject.getString("subject"));
        //} else {
        //    subtitleTextView.setText(R.string.no_subject);
        //}
        //if(jsonObject.getString("isRead").toLowerCase().equals("false")){
        //    subtitleTextView.setTypeface(subtitleTextView.getTypeface(), Typeface.BOLD);
        //}

        //try {
        //    new GraphAPI().postRequest(OutlookObjectCall.SENDMAIL, this, "/inbox/messages?$top=");
//
        //} catch (IllegalAccessException e) {
        //    e.printStackTrace();
        //}


    }

}
