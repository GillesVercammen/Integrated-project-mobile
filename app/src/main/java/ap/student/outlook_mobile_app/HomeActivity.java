package ap.student.outlook_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.identity.client.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ap.student.outlook_mobile_app.BLL.Authentication;
import ap.student.outlook_mobile_app.Calendar.CalendarActivity;
import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.mailing.activity.MailActivity;
import ap.student.outlook_mobile_app.mailing.model.MailFolder;
import ap.student.outlook_mobile_app.mailing.model.Message;

public class HomeActivity extends AppCompatActivityRest {
    private ImageButton mailButton;
    private ImageButton calendarButton;
    private ArrayList<MailFolder> folderObjectList;
    private ArrayList<MailFolder> foldersWithMail;
    private ArrayList<String> foldernames;
    private ArrayList<Integer> folderunread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        mailButton = (ImageButton) findViewById(R.id.mailButton);
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMailButtonClicked();
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalendarButtonClicked();
            }
        });

        if (connectivityManager.isConnected()) {
            System.out.println("CONNECTED");
        } else {
            System.out.println("NOT CONNECTED");
            foldersWithMail = new Gson().fromJson(sharedPreferences.getString("MailFolders", "[]"), new TypeToken<ArrayList<MailFolder>>(){}.getType());
        }
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        switch (outlookObjectCall) {
            case READMAIL: {
            }
            break;
        }
        editor.commit();
    }

    private void onMailButtonClicked() {
        startActivity(new Intent(this, MailActivity.class));

    }

    private void onCalendarButtonClicked() {
        startActivity(new Intent(this, CalendarActivity.class));
    }
}
