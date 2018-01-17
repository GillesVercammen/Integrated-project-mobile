package ap.student.outlook_mobile_app.Interfaces;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.microsoft.identity.client.AuthenticationResult;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.BLL.ConnectivityManagerFactory;
import ap.student.outlook_mobile_app.BLL.UserAuth;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.MainActivity;
import ap.student.outlook_mobile_app.R;

/**
 * Created by alek on 11/27/17.
 */

public abstract class AppCompatActivityRest extends AppCompatActivity implements IActivity {
    protected static final String USER = "User";
    protected static final String MAILFOLDERS = "MAILFOLDERS";

    //protected Toolbar actionBar = null;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected ConnectivityManagerFactory connectivityManager;

    public abstract void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager = ConnectivityManagerFactory.connectivityManager(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        /*actionBar = (Toolbar) findViewById(R.id.action_bar);
        if (actionBar != null) {
            setSupportActionBar(actionBar);
        }*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Basically a router method
     * @param item
     * @return
     *//*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean closeActivity = false;

        switch (item.getItemId()) {
            /*case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*//*

            case R.id.action_logout: {
                closeActivity = actionLogout();
            }
            break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        if (closeActivity && !this.getClass().equals(MainActivity.class)) {
            this.finish();
        }

        return true;
    }*/

    protected boolean actionLogout() {
        new UserAuth(this).logout();
        startActivity(new Intent(this, MainActivity.class));
        // just finishAffinity() for newer version, don't know why I did this backwards compatible.
        ActivityCompat.finishAffinity(this);
        return false;
    }

    protected void actionLogin() {
        new UserAuth(this).login();
    }

    protected void actionLogin(boolean interactive) {
        if (interactive) {
            new UserAuth(this).interActiveLogin();
        }
        else { actionLogin(); }
    }

    public void loginSuccessfull() {
        System.out.println("Login successfull");
    }

    protected void handleInteractiveRequestRedirect(int requestCode, int resultCode, Intent data) {
        new UserAuth(this).handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }
}
