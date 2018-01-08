package ap.student.outlook_mobile_app.mailing.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.HomeActivity;
import ap.student.outlook_mobile_app.DAL.enums.SendMailType;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.mailing.adapter.FolderAdapter;
import ap.student.outlook_mobile_app.mailing.model.Attachment;
import ap.student.outlook_mobile_app.mailing.model.MailFolder;
import ap.student.outlook_mobile_app.mailing.model.Recipient;

public class ReadMailActivity extends AppCompatActivityRest{

    private static final String TAG = "ERROR";
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
    private ImageView closeFolderList;
    private ImageView bgTemp;
    private ImageView attachment_download;
    private String from_name_content;
    private String from_email_content;
    private String subject_content;
    private String body_content;
    private String hasAttachment;
    private String id;
    private String date;
    private String contentType;
    private ListView mListView;
    private List<Attachment> attachmentsList = new ArrayList<>();
    private ArrayList<MailFolder> folderObjectList;
    private ArrayList<MailFolder> folders;
    private ArrayList<String> foldernames;
    private ArrayList<Integer> folderunread;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_READ_STORAGE = 113;
    private boolean CHECK_ATTACHEMENT = false;
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);

        StrictMode.setVmPolicy(builder.build());
        toolbar = (Toolbar) findViewById(R.id.toolbar_read);
        from = (TextView) findViewById(R.id.from_content);
        recipients = (TextView) findViewById(R.id.recepient_content);
        receivedDate = (TextView) findViewById(R.id.date_content);
        subject = (TextView) findViewById(R.id.subject_content);
        body = (WebView) findViewById(R.id.body_content);
        from_email = (TextView) findViewById(R.id.from_email_content);
        minimize = (ImageView) findViewById(R.id.minimize);
        mListView = (ListView) findViewById(R.id.folderlist);
        closeFolderList = (ImageView) findViewById(R.id.close_folder_list);
        bgTemp = (ImageView) findViewById(R.id.bg_temp);
        minimize.setImageResource(R.drawable.ic_minimize_blackvector_24dp);
        attachment_download = (ImageView) findViewById(R.id.attachment_download);
        attachment_download.setImageResource(R.drawable.ic_attach_file_blackvector_24dp);

        // don't show attachment icon if no attachment
        if (getIntent().getStringExtra("HAS_ATTACHMENT").toLowerCase().equals("false")){
            attachment_download.setVisibility(View.GONE);
        } else {
            CHECK_ATTACHEMENT = true;
            try {
                new GraphAPI().getRequest(OutlookObjectCall.UPDATEMAIL, ReadMailActivity.this,"/" + getIntent().getStringExtra("ID") + "/attachments");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Toast.makeText(ReadMailActivity.this, R.string.attachment_error, Toast.LENGTH_SHORT).show();
            }
        }
        //set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<Recipient> allRecipients = new ArrayList<>();
        ArrayList<String> allRecepientsEmails = new ArrayList<>();
        ArrayList<Recipient> ccRecipients = new ArrayList<>();
        ArrayList<Recipient> toRecipients = new ArrayList<>();

        // minimize/maximize the mail info
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

        attachment_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT < 23) {
                   downloadAttachment(attachmentsList);
                   openAttachment(attachmentsList.get(0).getName(), attachmentsList.get(0).getContentType());
                } else {
                    if (checkAndRequestPermissions()) {
                        downloadAttachment(attachmentsList);
                        openAttachment(attachmentsList.get(0).getName(), attachmentsList.get(0).getContentType());
                    }
                }
            }
        });


        // check intents
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

    private void downloadAttachment(List<Attachment> attachmentsList) {
        if (attachmentsList.size() == 0) {
            Toast.makeText(ReadMailActivity.this, R.string.attachment_error, Toast.LENGTH_SHORT).show();
        } else if (attachmentsList.size() == 1) {
            String base64 = attachmentsList.get(0).getContentBytes();
            try {
                if (base64 != null) {
                    byte[] data = Base64.decode(base64, Base64.DEFAULT);
                    File filePath = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS), attachmentsList.get(0).getName());
                    System.out.println(filePath.toString());
                    FileOutputStream os = new FileOutputStream(filePath, true);
                    os.write(data);
                    os.close();
                    Toast.makeText(ReadMailActivity.this, R.string.attachment_saved, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(ReadMailActivity.this, R.string.attachment_saved_failed, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            System.out.println("MORE ATTACHEMENTS");
        }
    }

    private void openAttachment(String filename, String contentType){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +
                 filename);
        builder.detectFileUriExposure();
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, contentType);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            this.startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        switch (outlookObjectCall) {
            case UPDATEMAIL: {
                if (CHECK_ATTACHEMENT) {
                    attachmentsList.clear();
                    JSONObject list = response;
                    try {
                        JSONArray attachmentsArray = list.getJSONArray("value");
                        // MAP ON POJO
                        Type listType = new TypeToken<List<Attachment>>() {
                        }.getType();
                        attachmentsList = new Gson().fromJson(String.valueOf(attachmentsArray), listType);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CHECK_ATTACHEMENT = false;
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
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReadMailActivity.this);
                alertDialogBuilder.setTitle(R.string.alert_delete_title)
                        .setIcon(R.drawable.ic_delete_black_24dp)
                        .setMessage(R.string.alert_delete_message)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                try {
                                    new GraphAPI().deleteRequest(OutlookObjectCall.UPDATEMAIL, ReadMailActivity.this,"/" + getIntent().getStringExtra("ID"));
                                    Toast.makeText(ReadMailActivity.this, R.string.delete_succes, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("POSITION", getIntent().getIntExtra("POSITION", -1));
                                    finish();//finishing activity
                                } catch (IllegalAccessException e) {
                                    Toast.makeText(ReadMailActivity.this, R.string.delete_nosucces, Toast.LENGTH_SHORT).show();
                                    e.getStackTrace();
                                }
                                Intent intent = new Intent();
                                finish();//finishing activity
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.reply:
                Intent intentReply = new Intent(this, NewMailActivity.class);
                intentReply.putExtra("mailType", SendMailType.REPLY.value());
                intentReply.putExtra("ID", getIntent().getStringExtra("ID"));
                startActivity(intentReply);
                break;
            case R.id.reply_all:
                Intent intentReplyAll = new Intent(this, NewMailActivity.class);
                intentReplyAll.putExtra("mailType", SendMailType.REPLYALL.value());
                intentReplyAll.putExtra("ID", getIntent().getStringExtra("ID"));
                startActivity(intentReplyAll);
                break;
            case R.id.forward:
                Intent intentForward = new Intent(this, NewMailActivity.class);
                intentForward.putExtra("mailType", SendMailType.FORWARD.value());
                intentForward.putExtra("ID", getIntent().getStringExtra("ID"));
                startActivity(intentForward);
                break;
            case R.id.action_map:
                folders = new Gson().fromJson(sharedPreferences.getString("AllMailFolders", "[]"), new TypeToken<ArrayList<MailFolder>>(){}.getType());
                mListView.setVisibility(View.VISIBLE);
                closeFolderList.setVisibility(View.VISIBLE);
                bgTemp.setVisibility(View.VISIBLE);
                FolderAdapter adapter = new FolderAdapter(this, folders);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        final MailFolder selectedFolder = folders.get(position);
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReadMailActivity.this);
                        alertDialogBuilder.setTitle(R.string.alert_folder_title)
                                .setIcon(R.drawable.ic_folder_bluevector_24dp)
                                .setMessage(R.string.alert_folder_message)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("DestinationId", selectedFolder.getId());
                                    new GraphAPI().postRequest(OutlookObjectCall.UPDATEMAIL, ReadMailActivity.this, jsonObject, "/" + getIntent().getStringExtra("ID") + "/move");
                                    Toast.makeText(ReadMailActivity.this, R.string.move_succeed, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IllegalAccessException e) {
                                    Toast.makeText(ReadMailActivity.this, R.string.move_failed, Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                startActivity(new Intent(ReadMailActivity.this, MailActivity.class));

                            }
                        })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                                .create()
                                .show();
                    }
                });
                closeFolderList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListView.setVisibility(View.GONE);
                        closeFolderList.setVisibility(View.GONE);
                        bgTemp.setVisibility(View.GONE);
                    }
                });

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

    private boolean checkAndRequestPermissions() {
        int storageWritePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        int storageReadPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storageReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storageWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   downloadAttachment(attachmentsList);
                   openAttachment(attachmentsList.get(0).getName(), attachmentsList.get(0).getContentType());
                } else {
                    Toast.makeText(ReadMailActivity.this, R.string.attachment_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
