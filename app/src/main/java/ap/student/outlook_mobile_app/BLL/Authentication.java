package ap.student.outlook_mobile_app.BLL;

import android.util.Log;

import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;

import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.MainActivity;

/**
 * Created by alek on 11/27/17.
 */

public class Authentication {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static Authentication authentication = null;
    private AuthenticationResult authResult;

    private Authentication() { }

    public static Authentication getAuthentication() {
        if (authentication == null) {
            authentication = new Authentication();
        }

        return authentication;
    }

    public AuthenticationResult getAuthResult() {
        return authResult;
    }

    // App callbacks for MSAL
    // ======================
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    // both have the same damn code

    /* Callback method for acquireTokenSilent calls
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    public AuthenticationCallback getAuthCallback(final AppCompatActivityRest activity) {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
            /* Successfully got a token, call Graph now */
                Log.d(TAG, "Successfully authenticated");

            /* Store the authResult */
                authResult = authenticationResult;

                activity.loginSuccessfull();
            }

            @Override
            public void onError(MsalException exception) {
            /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                /* Tokens expired or no session, retry with interactive */
                }
            }

            @Override
            public void onCancel() {
            /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

}
