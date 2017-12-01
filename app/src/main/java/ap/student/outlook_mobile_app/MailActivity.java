package ap.student.outlook_mobile_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class MailActivity extends AppCompatActivityRest {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mail);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

    }
}
