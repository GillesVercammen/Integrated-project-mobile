package ap.student.outlook_mobile_app.Interfaces;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;

/**
 * Created by alek on 11/27/17.
 */

public interface IActivity {
    //public void setResponse(JSONObject response);
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response);
}
