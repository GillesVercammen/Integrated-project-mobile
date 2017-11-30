package ap.student.outlook_mobile_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ap.student.outlook_mobile_app.BLL.Authentication;
import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class MainActivity extends AppCompatActivityRest {

    private final static String CLIENT_ID = "0cebe3b4-4e2d-4c24-8b4b-0ef2510470a5";
    private final static String SCOPES [] = {"https://graph.microsoft.com/User.Read", "https://graph.microsoft.com/Mail.Read", "https://graph.microsoft.com/Mail.Send"};
    private JSONObject graphResponse;
    private Authentication authentication;

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authentication = Authentication.getAuthentication();
        callGraphButton = (Button) findViewById(R.id.callGraph);
        signOutButton = (Button) findViewById(R.id.clearCache);

        callGraphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCallGraphClicked();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

  /* Configure your sample app and save state for this activity */
        sampleApp = new PublicClientApplication(
                this.getApplicationContext(),
                CLIENT_ID);

  /* Attempt to get a user and acquireTokenSilent
   * If this fails we do an interactive request
   */
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users != null && users.size() == 1) {
          /* We have 1 user */

                sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), authentication.getAuthSilentCallback(this));
            } else {
          /* We have no user */

          /* Let's do an interactive request */
                sampleApp.acquireToken(this, SCOPES, authentication.getAuthInteractiveCallback(this));
            }
        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }

    }

    public void setAuthResult(AuthenticationResult authResult) {
        this.authResult = authResult;
    }

    @Override
    public void setResponse(JSONObject response) {
        graphResponse = response;
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall) {

        switch (outlookObjectCall) {
            case READUSER: {
                /* Sets the Graph response */
                TextView graphText = (TextView) findViewById(R.id.graphData);
                graphText.setText(graphResponse.toString());

                /*
                JSONObject body = new JSONObject();
                try {
                    body.put("message", new JSONObject().put("subject", "Meet for lunch")
                            .put("body", new JSONObject().put("contentType", "Text")
                            .put("content", "The new cafetaria is open"))
                            .put("toRecipients", new JSONArray().put(
                                    new JSONObject().put("emailAddress", new JSONObject().put("address", "spam.yourownmail.com")))));
                    System.out.println(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    new GraphAPI().postRequest(OutlookObjectCall.SENDMAIL, this, body, "");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }*/
            } break;
            case READMAIL: {
                System.out.println(this.graphResponse);
            }
            break;
            case SENDMAIL: {
                System.out.println("Just send a mail.");
            }
        }
    }

    // getActivity() - returns activity so we can acquireToken within a callback
    public Activity getActivity() {
        return this;
    }

    /* Callback method for acquireTokenSilent calls
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */

    /* Set the UI for successful token acquisition data */
    public void updateSuccessUI() {
        callGraphButton.setVisibility(View.INVISIBLE);
        signOutButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                authResult.getUser().getName());
        findViewById(R.id.graphData).setVisibility(View.VISIBLE);
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        sampleApp.acquireToken(getActivity(), SCOPES, authentication.getAuthInteractiveCallback(this));
    }

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
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
    private void onSignOutClicked() {

    /* Attempt to get a user and remove their cookies from cache */
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users == null) {
            /* We have no users */

            } else if (users.size() == 1) {
            /* We have 1 user */
            /* Remove from token cache */
                sampleApp.remove(users.get(0));
                updateSignedOutUI();

            }
            else {
            /* We have multiple users */
                for (int i = 0; i < users.size(); i++) {
                    sampleApp.remove(users.get(i));
                }
            }

            Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
    }

    /* Set the UI for signed-out user */
    private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
        findViewById(R.id.graphData).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.graphData)).setText("No Data");
    }

}