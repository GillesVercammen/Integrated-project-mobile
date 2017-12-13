package ap.student.outlook_mobile_app.mailing.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);
        setContentView(R.layout.activity_read_mail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        from = (TextView) findViewById(R.id.from_content);
        recipients = (TextView) findViewById(R.id.recepient_content);
        receivedDate = (TextView) findViewById(R.id.date_content);
        subject = (TextView) findViewById(R.id.subject_content);
        body = (WebView) findViewById(R.id.body_content);
        from_email = (TextView) findViewById(R.id.from_email_content);

        Bundle args = getIntent().getBundleExtra("CC");
        Bundle args2 = getIntent().getBundleExtra("TO");

        ArrayList<Recipient> toRecipients = (ArrayList<Recipient>) args.getSerializable("TO_ARRAY");
        ArrayList<Recipient> ccRecipients = (ArrayList<Recipient>) args.getSerializable("CC_ARRAY");
        ArrayList<Recipient> allRecipients = new ArrayList<>();
        ArrayList<String> allRecepientsEmails = new ArrayList<>();

        if (toRecipients != null) {
            toRecipients.addAll(ccRecipients);
            allRecipients = new ArrayList<>(toRecipients);
        } else {
            if (ccRecipients != null) {
                allRecipients = new ArrayList<>(ccRecipients);
            }
        }
        for (Recipient recipient : allRecipients){
            allRecepientsEmails.add(recipient.getEmailAddress().getAddress());
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
