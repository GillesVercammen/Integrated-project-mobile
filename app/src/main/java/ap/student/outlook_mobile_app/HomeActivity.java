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
    private User user;

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
            try {
                new GraphAPI().getRequest(OutlookObjectCall.READMAIL, this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            user = Authentication.getAuthentication().getAuthResult().getUser();
        } else {
            user = new Gson().fromJson(sharedPreferences.getString("User",  "{}"), User.class);
            foldersWithMail = new Gson().fromJson(sharedPreferences.getString("MailFolders", "[]"), new TypeToken<ArrayList<MailFolder>>(){}.getType());
        }

        if (user.getName() != null) {
            getSupportActionBar().setTitle("welcome ".concat(user.getName()));
        }
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        switch (outlookObjectCall) {
            case READMAIL: {
                foldernames = new ArrayList<>();
                folderunread = new ArrayList<>();
                JSONObject list = response;
                foldersWithMail = new ArrayList<>();
                try {
                    JSONArray folders = list.getJSONArray("value");
                    Type listType = new TypeToken<List<MailFolder>>() {
                    }.getType();
                    folderObjectList = new Gson().fromJson(String.valueOf(folders), listType);
                    for(int i = 0; i < folderObjectList.size(); i++){
                        // CHECK IF TOTALCOUNT > 0, OTHERWISE IRRELEVANT FOLDER. ALSO EASIER TO ORDER FOLDER
                        // IN BETA GRAPH API ENDPOINT, FIELD wellKnownName exists --> General name to check for (easier)
                        if (folderObjectList.get(i).getTotalItemCount() > 0) {
                            foldersWithMail.add(folderObjectList.get(i));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.putString("MailFolders", new Gson().toJson(foldersWithMail));
            }
            break;
        }
        editor.commit();
    }

    private void onMailButtonClicked() {
        String user_name = "user_name";
        String user_email = "user_email";
            if (user.getName() != null) {
                user_name = user.getName();
                user_email = user.getDisplayableId();
            }
        Bundle args = new Bundle();
        args.putSerializable("FOLDERS", (Serializable) foldersWithMail);
        startActivity(new Intent(this, MailActivity.class)
            .putExtra("USER_NAME", user_name)
            .putExtra("USER_EMAIL", user_email)
                .putExtra("BUNDLE",args));

    }

    private void onCalendarButtonClicked() {
        startActivity(new Intent(this, CalendarActivity.class));
    }
}
