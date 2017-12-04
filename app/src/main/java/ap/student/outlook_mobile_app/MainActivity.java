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
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
        //callGraphAPI();
        //updateSuccessUI();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject graphResponse) {

    }

    /* Set the UI for successful token acquisition data */
    public void updateSuccessUI() {
        callGraphButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                Authentication.getAuthentication().getAuthResult().getUser().getName());
    }

    private void onCallGraphClicked() {
        actionLogin(true);
    }

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handleInteractiveRequestRedirect(requestCode, resultCode, data);
        callGraphButton.setVisibility(View.VISIBLE);
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
        updateSignedOutUI();
        return false;
    }

    /* Set the UI for signed-out user */
    private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
    }
}
