package ap.student.outlook_mobile_app.mailing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView minimize;
    private ImageView maximize;
    private String from_name_content;
    private String from_email_content;
    private String subject_content;
    private String body_content;
    private String hasAttachment;
    private String id;
    private String date;
    private String contentType;
    private ListView mListView;
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
        minimize = (ImageView) findViewById(R.id.minimize);
        mListView = (ListView) findViewById(R.id.folderlist);
        minimize.setImageResource(R.drawable.ic_minimize_blackvector_24dp);

        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from.setVisibility(View.GONE);
                recipients.setVisibility(View.GONE);
                receivedDate.setVisibility(View.GONE);
                subject.setVisibility(View.GONE);
                from_email.setVisibility(View.GONE);
                findViewById(R.id.from).setVisibility(View.GONE);
                findViewById(R.id.from_email).setVisibility(View.GONE);
                findViewById(R.id.recepient).setVisibility(View.GONE);
                findViewById(R.id.subject).setVisibility(View.GONE);
                findViewById(R.id.date).setVisibility(View.GONE);
                minimize.setVisibility(View.GONE);
                maximize = (ImageView) findViewById(R.id.maximize);
                maximize.setImageResource(R.drawable.ic_maximize_blackvector_24dp);
                maximize.setVisibility(View.VISIBLE);

                maximize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        from.setVisibility(View.VISIBLE);
                        recipients.setVisibility(View.VISIBLE);
                        receivedDate.setVisibility(View.VISIBLE);
                        subject.setVisibility(View.VISIBLE);
                        from_email.setVisibility(View.VISIBLE);
                        findViewById(R.id.from).setVisibility(View.VISIBLE);
                        findViewById(R.id.from_email).setVisibility(View.VISIBLE);
                        findViewById(R.id.recepient).setVisibility(View.VISIBLE);
                        findViewById(R.id.subject).setVisibility(View.VISIBLE);
                        findViewById(R.id.date).setVisibility(View.VISIBLE);
                        maximize.setVisibility(View.GONE);
                        minimize.setVisibility(View.VISIBLE);
                    }
                });

            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        if (!getIntent().getStringExtra("CONTENT").isEmpty()) {
            contentType = getIntent().getStringExtra("CONTENT");
        } else {
            body_content = "text/html";
        }



        from.setText(from_name_content);
        from_email.setText(from_email_content.toLowerCase());
        from_email.setSelected(true);
        String to = String.valueOf(allRecepientsEmails);
        recipients.setText(to.toLowerCase().substring(1, to.length() - 1));
        recipients.setSelected(true);
        try {
            receivedDate.setText(transformDate(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        subject.setText(subject_content);
        body.setPadding(0,0,0,0);
        if (contentType.equals("html")) {
            body.loadDataWithBaseURL("", body_content, "text/html", "utf-8","");
            body.getSettings().setLoadWithOverviewMode(true);
        } else {
            body.loadDataWithBaseURL("", body_content, "text", "utf-8","");
        }

    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
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
                Intent intent2 = new Intent();
                setResult(RESULT_OK,intent2);
                finish();
                break;
            case R.id.action_delete:
                try {
                    new GraphAPI().deleteRequest(OutlookObjectCall.UPDATEMAIL,this,"/" + getIntent().getStringExtra("ID"));
                    Toast.makeText(this, R.string.delete_succes, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("POSITION", getIntent().getIntExtra("POSITION", -1));
                    setResult(500,intent);
                    finish();//finishing activity
                } catch (IllegalAccessException e) {
                    Toast.makeText(this, R.string.delete_nosucces, Toast.LENGTH_SHORT).show();
                    e.getStackTrace();
                }
                break;
            case R.id.reply:
                // go to new email screen
                break;
            case R.id.action_map:
                mListView.setVisibility(View.VISIBLE);
                break;
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
