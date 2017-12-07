package ap.student.outlook_mobile_app;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class EventActivity extends AppCompatActivityRest {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
