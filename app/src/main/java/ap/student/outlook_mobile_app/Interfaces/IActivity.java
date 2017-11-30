package ap.student.outlook_mobile_app.Interfaces;

import org.json.JSONObject;

/**
 * Created by alek on 11/27/17.
 */

public interface IActivity {
    public void setResponse(JSONObject response);
    public void processResponse();
}
