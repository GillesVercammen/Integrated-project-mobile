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
import com.google.gson.Gson;
import com.microsoft.identity.client.User;

import org.json.JSONObject;

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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this is bad practice
        return true;
    }

    @Override
    public void loginSuccessfull() {
        startActivity(new Intent(this, MailActivity.class));
        this.finish();
        editor.putString("User", new Gson().toJson(Authentication.getAuthentication().getAuthResult().getUser()));
        editor.commit();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject graphResponse) {
        switch (outlookObjectCall) {
            case LOGINERROR: {
                Toast.makeText(this, "Can't connect to the internet, you can try again, or continue offline.", Toast.LENGTH_LONG).show();
            }
            break;
            case PERMISSIONSERROR: {
                Toast.makeText(this, "CANNOT LOG IN", Toast.LENGTH_LONG).show();
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
