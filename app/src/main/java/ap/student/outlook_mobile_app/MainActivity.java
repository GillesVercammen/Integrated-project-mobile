package ap.student.outlook_mobile_app;

import android.content.Intent;
import android.os.Bundle;
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
    public void loginSuccessfull() {
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
        callGraphAPI();
        updateSuccessUI();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject graphResponse) {

        switch (outlookObjectCall) {
            case READUSER: {
                System.out.println(graphResponse);
            } break;
            case READMAIL: {
                System.out.println(graphResponse);
            }
            break;
        }
    }

    /* Callback method for acquireTokenSilent calls
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */

    /* Set the UI for successful token acquisition data */
    public void updateSuccessUI() {
        callGraphButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                Authentication.getAuthentication().getAuthResult().getUser().getName());
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        actionLogin(true);
    }

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    public void callGraphAPI() {
        try {
            new GraphAPI().getRequest(OutlookObjectCall.READUSER, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /* Clears a user's tokens from the cache.
    * Logically similar to "sign out" but only signs out of this app.
    */
    @Override
    protected boolean actionLogout() {
        super.actionLogout();
        updateSignedOutUI();
        return false;
    }

    /* Set the UI for signed-out user */
    private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
    }
}
