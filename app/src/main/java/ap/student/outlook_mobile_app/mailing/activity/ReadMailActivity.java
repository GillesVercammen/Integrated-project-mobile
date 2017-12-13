package ap.student.outlook_mobile_app.mailing.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.mailing.model.EmailAddress;
import ap.student.outlook_mobile_app.mailing.model.MailFolder;
import ap.student.outlook_mobile_app.mailing.model.Recipient;

public class ReadMailActivity extends AppCompatActivityRest {

    private TextView from;
    private TextView recipients;
    private TextView receivedDate;
    private TextView attachment;
    private TextView from_email;
    private WebView body;
    private TextView subject;
    private Toolbar toolbar;
    private String from_name_content;
    private String from_email_content;
    private String subject_content;
    private String body_content;
    private String hasAttachment;
    private String id;
    private String date;
    private ArrayList<MailFolder> folderObjectList;
    private ArrayList<MailFolder> foldersWithMail;
    private ArrayList<String> foldernames;
    private ArrayList<Integer> folderunread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);

        toolbar = (Toolbar) findViewById(R.id.toolbar_read);
        from = (TextView) findViewById(R.id.from_content);
        recipients = (TextView) findViewById(R.id.recepient_content);
        receivedDate = (TextView) findViewById(R.id.date_content);
        subject = (TextView) findViewById(R.id.subject_content);
        body = (WebView) findViewById(R.id.body_content);
        from_email = (TextView) findViewById(R.id.from_email_content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            new GraphAPI().getRequest(OutlookObjectCall.READMAIL, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        ArrayList<Recipient> allRecipients = new ArrayList<>();
        ArrayList<String> allRecepientsEmails = new ArrayList<>();
        ArrayList<Recipient> ccRecipients = new ArrayList<>();
        ArrayList<Recipient> toRecipients = new ArrayList<>();

        if (!getIntent().getBundleExtra("CC").isEmpty()) {
            Bundle args = getIntent().getBundleExtra("CC");
             ccRecipients = (ArrayList<Recipient>) args.getSerializable("CC_ARRAY");
        }
        if (!getIntent().getBundleExtra("TO").isEmpty()){
            Bundle args = getIntent().getBundleExtra("TO");
            toRecipients = (ArrayList<Recipient>) args.getSerializable("TO_ARRAY");
        }

        if (toRecipients != null) {
            toRecipients.addAll(ccRecipients);
            for (Recipient recipient : toRecipients){
                allRecepientsEmails.add(recipient.getEmailAddress().getAddress());
            }
        } else {
            if (ccRecipients != null) {
                for (Recipient recipient : ccRecipients){
                    allRecepientsEmails.add(recipient.getEmailAddress().getAddress());
                }
            }
        }

        if (!getIntent().getStringExtra("FROM_NAME").isEmpty()) {
            from_name_content = getIntent().getStringExtra("FROM_NAME");
        } else {
            from_name_content = getString(R.string.not_found);
        }
        if (!getIntent().getStringExtra("FROM_EMAIL").isEmpty()) {
            from_email_content = getIntent().getStringExtra("FROM_EMAIL");
        } else {
            from_email_content = getString(R.string.not_found);
        }
        if (!getIntent().getStringExtra("DATE").isEmpty()) {
            date = getIntent().getStringExtra("DATE");
        } else {
            date = getString(R.string.not_found);
        }
        if (!getIntent().getStringExtra("SUBJECT").isEmpty()) {
            subject_content = getIntent().getStringExtra("SUBJECT");
        } else {
            subject_content = getString(R.string.no_subject);
        }
        if (!getIntent().getStringExtra("BODY").isEmpty()) {
            body_content = getIntent().getStringExtra("BODY");
        } else {
            body_content = getString(R.string.not_found);
        }

        from.setText(from_name_content);
        from_email.setText(from_email_content);
        from_email.setSelected(true);
        String to = String.valueOf(allRecepientsEmails);
        recipients.setText(to.substring(1, to.length() - 1));
        recipients.setSelected(true);
        try {
            receivedDate.setText(transformDate(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        subject.setText(subject_content);
        body.setPadding(0,0,0,0);
        body.loadData(body_content, "text/html", "utf-8");


    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        switch (outlookObjectCall) {
            case READMAIL: {
                foldernames = new ArrayList<>();
                folderunread = new ArrayList<>();
                foldersWithMail = new ArrayList<>();
                try {
                    JSONArray folders = response.getJSONArray("value");
                    Type listType = new TypeToken<List<MailFolder>>() {
                    }.getType();
                    folderObjectList = new Gson().fromJson(String.valueOf(folders), listType);
                    foldersWithMail.addAll(folderObjectList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //INFLATE THE MENU, ADDS ITEMS TO THE BAR IF PRESENT
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                try {
                    new GraphAPI().deleteRequest(OutlookObjectCall.UPDATEMAIL,this,"/" + getIntent().getStringExtra("ID"));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.reply:
                // go to new email screen
                break;
            case R.id.action_map:
                JSONObject jsonObject = new JSONObject();
               /* try {
                    jsonObject.put("destinationId", getIntent().getStringExtra("FOLDER_ID"));
                    new GraphAPI().postRequest(OutlookObjectCall.UPDATEMAIL,this, jsonObject, "/" + getIntent().getStringExtra("ID" + "/move"));
                } catch (IllegalAccessException | JSONException e) {
                    e.printStackTrace();
                }*/


        }
        if (item.getItemId() == android.R.id.home) {

        }
            return super.onOptionsItemSelected(item);
    }

    private String transformDate(String stringDate) throws ParseException {

        String JSON_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        SimpleDateFormat formatter = new SimpleDateFormat(JSON_FORMAT);
        Date date = formatter.parse(stringDate);
        String TO_FORMAT_IS_24 = "dd MMM yyyy HH:mm";
        String TO_FORMAT_IS_12 = "dd MMM yyyy hh:mm a";

        boolean is24 = DateFormat.is24HourFormat(this);
        if (is24) {
            SimpleDateFormat outputFormat = new SimpleDateFormat(TO_FORMAT_IS_24);
            return outputFormat.format(date);
        } else {
            SimpleDateFormat outputFormat = new SimpleDateFormat(TO_FORMAT_IS_12);
            return outputFormat.format(date);
        }
    }
}
