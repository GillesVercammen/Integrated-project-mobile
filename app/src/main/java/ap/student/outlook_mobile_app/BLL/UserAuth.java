package ap.student.outlook_mobile_app.BLL;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.util.List;

import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

/**
 * Created by alek on 12/4/17.
 */

public class UserAuth {
    private static final String TAG = UserAuth.class.getSimpleName();

    private final static String CLIENT_ID = "aef5a5da-9aed-4176-a978-765aa2907719";
    private final static String SCOPES [] = {"https://graph.microsoft.com/User.Read", "https://graph.microsoft.com/Mail.Read", "https://graph.microsoft.com/Mail.Send", "https://graph.microsoft.com/Calendars.ReadWrite", "https://graph.microsoft.com/Calendars.ReadWrite"};
    private AppCompatActivityRest activity;
    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private Authentication authentication;

    public UserAuth(AppCompatActivityRest activity) {
        this.activity = activity;
        /* Configure your sample app and save state for this activity */
        sampleApp = new PublicClientApplication(
                activity.getApplicationContext(),
                CLIENT_ID);

        this.authentication = Authentication.getAuthentication();
    }

    public void login() {
        /* Attempt to get a user and acquireTokenSilent
    * If this fails we do an interactive request
    */
        List<User> users = null;

        try {
            users = sampleApp.getUsers();

            if (users != null && users.size() == 1) {
          /* We have 1 user */

                sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), authentication.getAuthCallback(this.activity));
            } else {
          /* We have no user */

          /* Let's do an interactive request */
                sampleApp.acquireToken(this.activity, SCOPES, authentication.getAuthCallback(this.activity));
            }
        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
    }

    public void logout() {
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
            }
            else {
            /* We have multiple users */
                for (int i = 0; i < users.size(); i++) {
                    sampleApp.remove(users.get(i));
                }
            }

            Toast.makeText(activity.getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

        } catch (MsalClientException e) {
            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
    }

    public void interActiveLogin() {
        sampleApp.acquireToken(this.activity, SCOPES, authentication.getAuthCallback(this.activity));
    }

    public void handleInteractiveRequestRedirect(int requestCode, int resultCode, Intent data) {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }
}
