package ap.student.outlook_mobile_app;

import android.os.Bundle;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.models.Calendar;
import ap.student.outlook_mobile_app.DAL.models.Event;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

public class CalendarActivity extends AppCompatActivityRest {
    private Calendar calendar;
    private Event event;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);

        gson = new Gson();

        try {
            new GraphAPI().getRequest(OutlookObjectCall.READCALENDARS, this);
            new GraphAPI().getRequest(OutlookObjectCall.READEVENTS, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

            switch (outlookObjectCall) {
                case READCALENDARS: {
                    calendar = gson.fromJson(response.toString(), Calendar.class);
                    System.out.println(calendar.getCalendars()[0].getOwner().getName());
                }
                break;
                case READEVENTS: {
                    System.out.println(response);
                    event = gson.fromJson(response.toString(), Event.class);
                    System.out.println(event.getEvents()[0].getOrganizer().getEmailAddress().getName());
                    System.out.println(event.getEvents()[0].getCreatedDateTime().toString());
                }
                break;
            }
    }
}
