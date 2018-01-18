package ap.student.outlook_mobile_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.microsoft.identity.client.User;

import org.json.JSONObject;

import java.util.UUID;

import ap.student.outlook_mobile_app.BLL.Authentication;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.mailing.activity.MailActivity;

public class MainActivity extends AppCompatActivityRest {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * need to set layout before calling super
         * I hope this won't cause any problems later
          */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView outlookImage = (ImageView) findViewById(R.id.outlookImage);
        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        outlookImage.setVisibility(View.VISIBLE);

        outlookImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        welcomeText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firebase id login", "Refreshed token: " + refreshedToken);
            System.out.println("Refreshed token " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (connectivityManager.isConnected()) {
            actionLogin();
        } else {
            startActivity(new Intent(this, MailActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this is bad practice
        return true;
    }

    @Override
    public void loginSuccessfull() {

        //Send the Microsoft authentication token to the nodeJS server so it can make API calls
        String senderID = "371442591215";
        System.out.println("MS token here: " + Authentication.getAuthentication().getAuthResult().getAccessToken());
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(senderID + "@gcm.googleapis.com")
                .setMessageId(UUID.randomUUID().toString())
                .addData("MS_token", Authentication.getAuthentication().getAuthResult().getAccessToken())
                .build());

        startActivity(new Intent(this, MailActivity.class));
        this.finish();
        editor.putString(USER, new Gson().toJson(Authentication.getAuthentication().getAuthResult().getUser()));
        editor.commit();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject graphResponse) {
        switch (outlookObjectCall) {
            case LOGINERROR: {
                Toast.makeText(this, "Can't connect to the internet.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MailActivity.class);
                startActivity(intent);
            }
            break;
            case PERMISSIONSERROR: {
                Toast.makeText(this, "CANNOT LOG IN, PERMISSION NOT GRANTED", Toast.LENGTH_LONG).show();
            }
            break;
        }
    }

    private void onOfflineButtonClicked() {
        Toast.makeText(this, "USING APP OFFLINE", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MailActivity.class));
    }

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    @Override
    protected boolean actionLogout() {
        super.actionLogout();
        this.finish();
        return false;
    }

}
