package ap.student.outlook_mobile_app.mailing.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

/**
 * Created by Alexander on 12/7/2017.
 */

public class NewMailActivity extends AppCompatActivityRest {

    EditText recipientTextField;
    EditText subjectTextField;
    EditText messageTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        recipientTextField = (EditText) findViewById(R.id.recipientTextField);
        subjectTextField = (EditText) findViewById(R.id.subjectTextField);
        messageTextField = (EditText) findViewById(R.id.messageTextField);

        this.sendMail();
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
        //https://msdn.microsoft.com/en-us/office/office365/api/mail-rest-operations#create-and-send-messages

        String recipient = recipientTextField.toString();
        String subject = subjectTextField.toString();
        String message = messageTextField.toString();

        JSONObject JSON = new JSONObject();
        JSONObject messageJson = new JSONObject();
        JSONArray recipientsArray = new JSONArray();
        JSONObject recipientsObject = new JSONObject();
        JSONObject addressObject = new JSONObject();

        try {
            addressObject.put("Address", "est");
            recipientsObject.put("EmailAddress", addressObject);
            recipientsArray.put(recipientsObject);
            System.out.println(recipientsArray);
            Log.d("test", recipientsArray.toString());
            //messageJson.put("key", recipient);
            //messageJson.put("key", subject);
            //messageJson.put("key", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String message;
        //JSONObject json = new JSONObject();
        //json.put("name", "student");
//
        //JSONArray array = new JSONArray();
        //JSONObject item = new JSONObject();
        //item.put("information", "test");
        //item.put("id", 3);
        //item.put("name", "course1");
        //array.put(item);
//
        //json.put("course", array);
//
        //message = json.toString();

//
  //          {
  //              "course":[
  //              {
  //                  "id":3,
  //                      "information":"test",
  //                      "name":"course1"
  //              }
  //     ],
  //              "name":"student"
  //          }



        //try {
        //    new GraphAPI().postRequest(OutlookObjectCall.SENDMAIL, this, "/inbox/messages?$top=");
//
        //} catch (IllegalAccessException e) {
        //    e.printStackTrace();
        //}


    }

}
