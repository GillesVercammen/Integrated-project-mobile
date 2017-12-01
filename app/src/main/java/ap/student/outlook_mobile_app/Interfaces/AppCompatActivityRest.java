package ap.student.outlook_mobile_app.Interfaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.R;

/**
 * Created by alek on 11/27/17.
 */

public abstract class AppCompatActivityRest extends AppCompatActivity implements IActivity {
    protected JSONObject response;
    protected OutlookObjectCall outlookObjectCall;
    protected Toolbar actionBar = null;

    public void setOutlookObjectCall(OutlookObjectCall outlookObjectCall) { this.outlookObjectCall = outlookObjectCall; }
    public void setResponse(JSONObject response) {
        this.response = response;
    }
    public abstract void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = (Toolbar) findViewById(R.id.action_bar);
        if (actionBar == null) {
            throw new NullPointerException("'actionBar' couldn't be found. You need to set the activity before calling the super,\n\r and make sure to include the actionbar in said activity.");
        }

        setSupportActionBar(actionBar);
        getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
