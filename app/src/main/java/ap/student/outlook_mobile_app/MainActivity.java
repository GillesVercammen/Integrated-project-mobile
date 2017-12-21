package ap.student.outlook_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.identity.client.User;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.BLL.Authentication;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class MainActivity extends AppCompatActivityRest {
    private Button offlineButton;
    private Button loginButton;
    private RelativeLayout loginView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * need to set layout before calling super
         * I hope this won't cause any problems later
          */
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        offlineButton = (Button) findViewById(R.id.offlineButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginView = (RelativeLayout) findViewById(R.id.loadingPanel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionLogin(true);
            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onOfflineButtonClicked();
            }
        });

        if (connectivityManager.isConnected()) {
            actionLogin();
        } else {
            setLoginButton();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this is bad practice
        return true;
    }

    @Override
    public void loginSuccessfull() {
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
        editor.putString(USER, new Gson().toJson(Authentication.getAuthentication().getAuthResult().getUser()));
        editor.commit();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject graphResponse) {
        switch (outlookObjectCall) {
            case LOGINERROR: {
                setLoginButton();
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
        startActivity(new Intent(this, HomeActivity.class));
    }

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handleInteractiveRequestRedirect(requestCode, resultCode, data);
        setLoginButton();
    }

    @Override
    protected boolean actionLogout() {
        super.actionLogout();
        this.finish();
        return false;
    }

    private void setLoginButton() {
        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }
}
