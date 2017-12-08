package ap.student.outlook_mobile_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.models.Calendar;
import ap.student.outlook_mobile_app.DAL.models.Event;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

public class CalendarActivity extends AppCompatActivityRest {
    private Calendar calendar;
    private Event event;
    private Gson gson;
    private TableLayout monthCalendar;
    private TextView monthCalendarYearTextView;
    private TextView monthCalendarMonthTextView;
    private LocalDateTime selectedTime;
    private Button editDayButton;

    private ImageButton nextMonthButton;
    private ImageButton previousMonthButton;
    private static final int monthCalendarChildId = 8;
    private int lastId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);

        /**
         * Settings for the month calendar view
         */
        nextMonthButton = (ImageButton) findViewById(R.id.nextMonthButton);
        previousMonthButton = (ImageButton) findViewById(R.id.previousMonthButton);
        monthCalendar = (TableLayout) findViewById(R.id.monthCalendar);
        monthCalendarMonthTextView = (TextView) findViewById(R.id.currentMonthTextView);
        monthCalendarYearTextView = (TextView) findViewById(R.id.currentYearTextView);

        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarSetMonthButtonClicked(true);
            }
        });

        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarSetMonthButtonClicked(false);
            }
        });

        ViewTreeObserver vto = monthCalendar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                monthCalendar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                buildMonthCalendar();
            }
        });


        /**
         * global settings
         */
        event = null;
        selectedTime = LocalDateTime.now();
        editDayButton = (Button) findViewById(R.id.editDayButton);

        editDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDayButtonClicked();
            }
        });

        gson = new Gson();

        try {
            //new GraphAPI().getRequest(OutlookObjectCall.READCALENDARS, this);
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
                event = gson.fromJson(response.toString(), Event.class);
                buildMonthCalendar();
            }
            break;
        }
    }

    private void editDayButtonClicked() {
        startActivity(new Intent(this, EventActivity.class));
    }

    private void calendarSetMonthButtonClicked(boolean next) {
        if (next) {
            selectedTime = selectedTime.plusMonths(1);
        } else {
            selectedTime = selectedTime.minusMonths(1);
        }
        buildMonthCalendar();
    }

    private void monthCalendarChildClicked(int id) {
        findViewById(lastId).setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));

        if (checkIfEvent(LocalDateTime.of(selectedTime.getYear(), selectedTime.getMonth(), lastId - monthCalendarChildId, selectedTime.getHour(), selectedTime.getMinute()))) {
            findViewById(lastId).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        selectedTime = LocalDateTime.of(selectedTime.getYear(), selectedTime.getMonth(), id - monthCalendarChildId, selectedTime.getHour(), selectedTime.getMinute());
        findViewById(id).setBackgroundResource(R.drawable.monthcalendar_select);
        lastId = id;
    }

    //TODO : clean up this monster
    private void buildMonthCalendar() {
        int width = monthCalendar.getWidth() / 7;

        monthCalendar.removeViews(1, monthCalendar.getChildCount() - 1);

        monthCalendar.getChildAt(0).setMinimumHeight(width);

        monthCalendarYearTextView.setText(Integer.toString(selectedTime.getYear()));
        monthCalendarMonthTextView.setText(selectedTime.getMonth().name());

        int day = selectedTime.with(TemporalAdjusters.firstDayOfMonth()).getDayOfWeek().getValue();
        if (day == 7) day = 0;
        LocalDateTime lastDayOfMonth = selectedTime.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime index = selectedTime.with(TemporalAdjusters.firstDayOfMonth());


        while (index.isBefore(lastDayOfMonth) || index.equals(lastDayOfMonth)) {
            TableRow tableRow = new TableRow(this);
            while ((index.isBefore(lastDayOfMonth) || index.equals(lastDayOfMonth)) && day < 7) {
                final TextView textView = new TextView(this);
                textView.setText(Integer.toString(index.getDayOfMonth()));
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_Button_Borderless_Colored);
                textView.setWidth(width);
                textView.setHeight(width);
                textView.setId(monthCalendarChildId + index.getDayOfMonth());
                textView.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        monthCalendarChildClicked(textView.getId());
                    }
                });

                if (index.getDayOfYear() == selectedTime.getDayOfYear()) {
                    textView.setBackgroundResource(R.drawable.monthcalendar_select);
                    lastId = textView.getId();
                }
                else if (checkIfEvent(index)) {
                    textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tableRow.addView(textView);
                day++;
                index = index.plusDays(1);
            }
            tableRow.setGravity(Gravity.START);
            if (index.getDayOfMonth() <= 7) {
                tableRow.setGravity(Gravity.END);
            }
            monthCalendar.addView(tableRow);
            day = 0;
        }
    }

    private boolean checkIfEvent(LocalDateTime index) {
        if (event == null) {
            return false;
        }
        for (Event event : event.getEvents()) {
            LocalDateTime eventTime = event.getStart().getDateTime();
            if (eventTime.getYear() == index.getYear() && eventTime.getDayOfYear() == index.getDayOfYear()) {
                return true;
            }
        }
        return false;
    }
}
