package ap.student.outlook_mobile_app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Collections;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class MailActivity extends AppCompatActivityRest  {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mail);
        super.onCreate(savedInstanceState);

        //set actionbar
        setActionBarMail("POSTVAK IN", getIntent().getStringExtra("USER_EMAIL"));

        //top x mails verkrijgen
        getAllMails(15);

    }

    private void setActionBarMail(String title, String subtitle) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
    }

    public void getAllMails(int aantalMails) {
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READMAIL, this, "?$top=" + aantalMails);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

        switch (outlookObjectCall) {
            case READUSER: {
                System.out.println("reading user");
            } break;
            case READMAIL: {
                JSONObject list = response;
                try {
                    JSONArray mails = list.getJSONArray("value");

                    ArrayList<JSONObject> json_emails = new ArrayList<>();

                    for (int i = 0; i < mails.length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", mails.getJSONObject(i).getString("id"));
                        jsonObject.put("hasAttachments", mails.getJSONObject(i).getString("hasAttachments"));
                        jsonObject.put("subject", mails.getJSONObject(i).getString("subject"));
                        jsonObject.put("receivedDateTime", mails.getJSONObject(i).getString("receivedDateTime"));
                        jsonObject.put("from", mails.getJSONObject(i).getJSONObject("from"));
                        json_emails.add(jsonObject);
                    }


                    mListView = (ListView) findViewById(R.id.mail_list);
                    MyCustomAdapter adapter = new MyCustomAdapter(this, json_emails);
                    mListView.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case SENDMAIL: {
                System.out.println("Just send a mail." );
            }
        }
    }

}
