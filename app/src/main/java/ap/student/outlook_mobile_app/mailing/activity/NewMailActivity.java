package ap.student.outlook_mobile_app.mailing.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.enums.SendMailType;
import ap.student.outlook_mobile_app.DAL.models.Body;
import ap.student.outlook_mobile_app.DAL.models.Contact;
import ap.student.outlook_mobile_app.DAL.models.EmailAddress;
import ap.student.outlook_mobile_app.DAL.models.Message;
import ap.student.outlook_mobile_app.DAL.models.Recipient;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.mailing.model.Attachment;

/**
 * Created by Alexander on 12/7/2017.
 */

public class NewMailActivity extends AppCompatActivityRest {

    private static final String TAG = "NewMailActivity";
    private Uri URI = null;
    private AutoCompleteTextView recipientTextField;
    private AutoCompleteTextView ccTextField;
    private AutoCompleteTextView bccTextField;
    private EditText subjectTextField;
    private EditText messageTextField;
    private WebView webView;
    private List<Contact> contacts = new ArrayList<>();
    private TextView textViewCC;
    private TextView textViewBCC;
    private View ccView;
    private View bccView;
    private boolean hasOpenedMenu;
    public SendMailType mailTypeEnum = SendMailType.SEND;
    public Body body;
    int columnIndex;
    String attachmentFile;
    private List<Attachment> attachmentsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        subjectTextField = (EditText) findViewById(R.id.subjectTextField);
        messageTextField = (EditText) findViewById(R.id.messageTextField);
        webView = (WebView) findViewById(R.id.webView);
        recipientTextField = (AutoCompleteTextView) findViewById(R.id.recipientTextField);
        ccTextField = (AutoCompleteTextView) findViewById(R.id.ccTextField);
        bccTextField = (AutoCompleteTextView) findViewById(R.id.bccTextField);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView moreRecipients = (ImageView)findViewById(R.id.moreRecipients);

        textViewCC =  findViewById(R.id.textViewCC);
        textViewBCC = findViewById(R.id.textViewBCC);
        ccView = (View) findViewById(R.id.ccView);
        bccView = (View) findViewById(R.id.bccView);

        textViewCC.setVisibility(View.GONE);
        textViewBCC.setVisibility(View.GONE);
        ccView.setVisibility(View.GONE);
        bccView.setVisibility(View.GONE);
        ccTextField.setVisibility(View.GONE);
        bccTextField.setVisibility(View.GONE);
        hasOpenedMenu = false;

        moreRecipients.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hasOpenedMenu = !hasOpenedMenu;

                if (hasOpenedMenu) {
                    textViewCC.setVisibility(View.VISIBLE);
                    textViewBCC.setVisibility(View.VISIBLE);
                    ccView.setVisibility(View.VISIBLE);
                    bccView.setVisibility(View.VISIBLE);
                    ccTextField.setVisibility(View.VISIBLE);
                    bccTextField.setVisibility(View.VISIBLE);
                } else if (!hasOpenedMenu) {
                    textViewCC.setVisibility(View.GONE);
                    textViewBCC.setVisibility(View.GONE);
                    ccView.setVisibility(View.GONE);
                    bccView.setVisibility(View.GONE);
                    ccTextField.setVisibility(View.GONE);
                    bccTextField.setVisibility(View.GONE);
                }

            }
        });

        getContacts();

        System.out.println("mailType should be here:");
        if (getIntent().getExtras() != null) {
            mailTypeEnum = SendMailType.valueOf(getIntent().getExtras().get("mailType").toString().toUpperCase());
            System.out.println(mailTypeEnum);
        }

        switch (mailTypeEnum) {
            case REPLY:
                System.out.println("In reply case!");
                try {
                    new GraphAPI().postRequest(OutlookObjectCall.UPDATEMAIL, this, "/" + getIntent().getStringExtra("ID") + "/createReply");
                } catch (IllegalAccessException e) {
                    Toast.makeText(getApplicationContext(), R.string.email_wrong, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;

            case REPLYALL:

                System.out.println("In reply all case!");
                try {
                    new GraphAPI().postRequest(OutlookObjectCall.UPDATEMAIL, this, "/" + getIntent().getStringExtra("ID") + "/createReplyAll");
                } catch (IllegalAccessException e) {
                    Toast.makeText(getApplicationContext(), R.string.email_wrong, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;

            case FORWARD:

                System.out.println("In forward case!");
                try {
                    new GraphAPI().postRequest(OutlookObjectCall.UPDATEMAIL, this, "/" + getIntent().getStringExtra("ID") + "/createForward");
                } catch (IllegalAccessException e) {
                    Toast.makeText(getApplicationContext(), R.string.email_wrong, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;

            case NORMALSEND:
                String emailaddress = getIntent().getStringExtra("TO");
                recipientTextField.setText(emailaddress);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            //System.out.println(data.getData());
            //String[] filePathColumn = { MediaStore.Images.Media.DATA, MediaStore.Files };
            //Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            //cursor.moveToFirst();
            //columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //attachmentFile = cursor.getString(columnIndex); //attachmentFile is null, columnIndex is 0
            String realPath = getRealPathFromUri(this, selectedImage);
            Log.e("Attachment Path:", selectedImage.toString());
            Log.e("Attachment Real Path:", realPath);
            URI = Uri.parse("file://" + realPath);
            //cursor.close();
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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
                    Toast.makeText(getApplicationContext(), R.string.no_contacts_found, Toast.LENGTH_SHORT).show();
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
            break;

            case UPDATEMAIL:{
                try {
                    System.out.println(response);
                    Gson gson = new Gson();

                    JSONArray toRecipientsJSON = response.getJSONArray("toRecipients");
                    Type listType = new TypeToken<List<Recipient>>() {
                    }.getType();
                    List<Recipient> toRecipients = new Gson().fromJson(String.valueOf(toRecipientsJSON), listType);
                    String recipientsString = convertRecipientsToString(toRecipients);

                    body = gson.fromJson(String.valueOf(response.get("body")), Body.class);
                    System.out.println(body.getContent());
                    String subject = response.get("subject").toString();
                    System.out.println(subject);

                    subjectTextField.setText(subject);
                    messageTextField.setText("");
                    recipientTextField.setText(recipientsString);

                    //Use this to make the webview editable
                    //String bodyContent = body.getContent();
                    //bodyContent = "<div contenteditable=\"true\"" + bodyContent + "</div>";

                    webView.setPadding(0,0,0,0);
                    webView.loadDataWithBaseURL("", body.getContent(), "text/html", "utf-8","");
                    webView.getSettings().setLoadWithOverviewMode(true);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

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

        if (id == R.id.add_attachment) {

            if (Build.VERSION.SDK_INT < 23) {
                Toast.makeText(getApplicationContext(), R.string.add_attachment, Toast.LENGTH_SHORT).show();

                //do stuff
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra("return-data", true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 101);
            } else {

                if (checkAndRequestPermissions()) {
                    Toast.makeText(getApplicationContext(), R.string.add_attachment, Toast.LENGTH_SHORT).show();

                    //do stuff
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 101);
                }
            }


            return true;

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.send_mail) {

            Toast.makeText(getApplicationContext(), R.string.sending_mail, Toast.LENGTH_SHORT).show();
            try {
                sendMail(URI);
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

    public void sendMail(Uri URI) throws JSONException {

        //body + addedMessage

        if (mailTypeEnum == mailTypeEnum.SEND)
        {
            body = new Body(messageTextField.getText().toString(), "Text");
        }

        else {
            String bodyContent = body.getContent();
            bodyContent = messageTextField.getText().toString() + bodyContent;
            body = new Body(bodyContent, body.getContentType());
        }

        String subject = subjectTextField.getText().toString();
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

        JSONObject JSONMessage = new JSONObject(new Gson().toJson(message));

        //also convert URI thing to itemattachments

        if (URI != null){

            //E/Attachment Path:: content://media/external/images/media/92888 , uriString is file://content://media/external/images/media/92888


            File originalFile = new File(URI.getPath());
            String encodedBase64 = null;
            try {
                FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                //originalFile = /media/external/images/media/92888
                //no such file or directory /media/external/images/media/92888
                System.out.println(originalFile.length());
                byte[] bytes = new byte[(int)originalFile.length()];
                fileInputStreamReader.read(bytes);
                encodedBase64 = new String(Base64.encode(bytes, Base64.DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //https://developer.microsoft.com/en-us/graph/docs/api-reference/v1.0/api/user_sendmail
            //"You can include a file attachment in the same sendMail action call."

            //https://developer.microsoft.com/en-us/graph/docs/api-reference/v1.0/resources/message
            //Zie JSON representation onderaan:   "attachments": [{"@odata.type": "microsoft.graph.attachment"}],

            //https://developer.microsoft.com/en-us/graph/docs/api-reference/v1.0/resources/fileattachment
            //Informatie over een file attachment

            Attachment attachment = new Attachment();
            attachment.setName("attachment");
            attachment.setContentBytes(encodedBase64);

            //attachmentsList.add(attachment);

            JSONObject jsonAttachment = new JSONObject(new Gson().toJson(attachment));

            JSONObject jsonAttachmentWrapped = new JSONObject();
            jsonAttachmentWrapped.put("attachment", jsonAttachment);

            JSONArray jsonAttachmentArray = new JSONArray();
            jsonAttachmentArray.put(jsonAttachmentWrapped);

            System.out.println(jsonAttachmentArray.toString());

            JSONMessage.put("attachments", jsonAttachmentArray);

            System.out.println(JSONMessage.toString());

        }

        //convert Message object to JSON

        //wrap JSONobject in another JSONobject to make sure format is correct {"message": message}
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("message", JSONMessage);

        System.out.println("Message here!");
        System.out.println(jsonMessage);

        //do our call
        try {
            new GraphAPI().postRequest(OutlookObjectCall.SENDMAIL, this, jsonMessage);
            Toast.makeText(getApplicationContext(), R.string.mail_sent, Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
                                          Toast.makeText(getApplicationContext(), R.string.email_wrong, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        this.finish();
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

                } else {
                    Toast.makeText(NewMailActivity.this, R.string.attachment_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

    public String convertRecipientsToString(List<Recipient> recipientList){
        String recipientsString = "";

        for (Recipient recipient: recipientList
                ) {
            recipientsString = recipient.getEmailAddress().getAddress() + ";" + recipientsString;
        }
        return recipientsString;
    }

}
