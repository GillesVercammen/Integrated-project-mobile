package ap.student.outlook_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.BLL.Authentication;
import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class MainActivity extends AppCompatActivityRest {
    Button callGraphButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * need to set layout before calling super
         * I hope this won't cause any problems later
          */
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        callGraphButton = (Button) findViewById(R.id.callGraph);

        callGraphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCallGraphClicked();
            }
        });

        actionLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this is bad practice
        return true;
    }

    @Override
    public void loginSuccessfull() {
        startActivity(new Intent(this, HomeActivity.class)
            .putExtra("USER_NAME", Authentication.getAuthentication().getAuthResult().getUser().getName())
            .putExtra("USER_EMAIL", Authentication.getAuthentication().getAuthResult().getUser().getDisplayableId()));
        this.finish();
        //callGraphAPI();
        updateSuccessUI();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject graphResponse) {
        //bad practice
    }

    /* Set the UI for successful token acquisition data */
    public void updateSuccessUI() {
        String paramString = Authentication.getAuthentication().getAuthResult().getUser().getName();
        String formattedString = getString(R.string.welcome, paramString);
        callGraphButton.setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText(formattedString);
    }

    private void onCallGraphClicked() {
        actionLogin(true);
    }

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handleInteractiveRequestRedirect(requestCode, resultCode, data);
        callGraphButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    public void callGraphAPI() {
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READUSER, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean actionLogout() {
        super.actionLogout();
        this.finish();
        return false;
    }
}
