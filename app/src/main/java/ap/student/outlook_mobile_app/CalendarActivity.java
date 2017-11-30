package ap.student.outlook_mobile_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class CalendarActivity extends AppCompatActivityRest {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected boolean calendarIntent() {
        return false;
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall) {

    }
}
