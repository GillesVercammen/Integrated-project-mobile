package ap.student.outlook_mobile_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class MailActivity extends AppCompatActivityRest {

    private ListView mListView;
    String[] listItems = {"een", "twee", "drie", "vier", "vijf"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mail);
        super.onCreate(savedInstanceState);

        getAllMails();

    }

    public void getAllMails() {
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READMAIL, this);
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

                    ArrayList<String> fromObject_list = new ArrayList<>();

                    for (int i = 0; i < mails.length(); i++) {
                        fromObject_list.add(String.valueOf(mails.getJSONObject(i).getJSONObject("from").getJSONObject("emailAddress").getString("address")));
                    }

                    mListView = (ListView) findViewById(R.id.mail_list);
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fromObject_list);
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
