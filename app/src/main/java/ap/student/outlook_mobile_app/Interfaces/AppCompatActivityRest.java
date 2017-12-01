package ap.student.outlook_mobile_app.Interfaces;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.OutlookObjectCall;

/**
 * Created by alek on 11/27/17.
 */

public abstract class AppCompatActivityRest extends AppCompatActivity implements IActivity {
    protected JSONObject response;
    protected OutlookObjectCall outlookObjectCall;

    public void setOutlookObjectCall(OutlookObjectCall outlookObjectCall) { this.outlookObjectCall = outlookObjectCall; }
    public void setResponse(JSONObject response) {
        this.response = response;
    }
    public abstract void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response);
}
