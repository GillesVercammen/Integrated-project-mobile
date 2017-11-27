package ap.student.outlook_mobile_app.Interfaces;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

/**
 * Created by alek on 11/27/17.
 */

public abstract class AppCompatActivityRest extends AppCompatActivity implements IActivity {
    protected JSONObject response;

    public void setResponse(JSONObject response) {
        this.response = response;
    }
    public void processResponse() { return; }
}
