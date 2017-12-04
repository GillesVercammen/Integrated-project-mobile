package ap.student.outlook_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;

import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class HomeActivity extends AppCompatActivityRest {
    private ImageButton mailButton;
    private ImageButton calendarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        mailButton = (ImageButton) findViewById(R.id.mailButton);
        calendarButton = (ImageButton) findViewById(R.id.calendarButton);

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMailButtonClicked();
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalendarButtonClicked();
            }
        });
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

    }

    private void onMailButtonClicked() {
        startActivity(new Intent(this, MailActivity.class));
    }

    private void onCalendarButtonClicked() {
        startActivity(new Intent(this, CalendarActivity.class));
    }
}
