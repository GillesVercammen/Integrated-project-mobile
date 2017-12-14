package ap.student.outlook_mobile_app.Calendar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.MonthCalendarCell;
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

    private Map<Integer, MonthCalendarCell> monthCalendarCellMap;

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

        monthCalendarCellMap = new HashMap<>();

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
        editDayButton = (Button) findViewById(R.id.createNewEventButton);

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
    public void onResume() {
        super.onResume();
        buildMonthCalendar();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

        switch (outlookObjectCall) {
            case READCALENDAR: {
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
        startActivity(new Intent(this, EventActivity.class).putExtra("dateTime", monthCalendarCellMap.get(lastId).getDateTime().toString()));
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
        MonthCalendarCell calendarCell = monthCalendarCellMap.get(lastId);
        if (calendarCell.isHasEvent()) {
            calendarCell.getTextView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else {
            calendarCell.getTextView().setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
        }

        MonthCalendarCell selectedCell = monthCalendarCellMap.get(id);
        if (selectedCell.isHasEvent()) {
            selectedCell.getTextView().setBackgroundResource(R.drawable.monthcalendar_select_event);
        } else {
            selectedCell.getTextView().setBackgroundResource(R.drawable.monthcalendar_select);
        }

        lastId = id;
        selectedTime = LocalDateTime.of(selectedTime.getYear(), selectedTime.getMonth(), selectedCell.getDateTime().getDayOfMonth(), selectedTime.getHour(), selectedTime.getMinute());
    }

    //TODO : clean up this monster
    private void buildMonthCalendar() {
        int width = monthCalendar.getWidth() / 7;

        monthCalendar.removeViews(1, monthCalendar.getChildCount() - 1);
        monthCalendarCellMap.clear();

        monthCalendar.getChildAt(0).setMinimumHeight(width);

        monthCalendarYearTextView.setText(Integer.toString(selectedTime.getYear()));
        monthCalendarMonthTextView.setText(selectedTime.getMonth().name());

        int day = selectedTime.with(TemporalAdjusters.firstDayOfMonth()).getDayOfWeek().getValue();
        if (day == 7) day = 0;
        LocalDateTime lastDayOfMonth = selectedTime.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime index = selectedTime.with(TemporalAdjusters.firstDayOfMonth()).withHour(8).withMinute(0).withSecond(0).withNano(0);

        System.out.println(index);

        while (index.isBefore(lastDayOfMonth) || index.equals(lastDayOfMonth)) {
            TableRow tableRow = new TableRow(this);
            while ((index.isBefore(lastDayOfMonth) || index.equals(lastDayOfMonth)) && day < 7) {

                boolean hasEvent = false;
                boolean isSelected = false;

                final TextView textView = new TextView(this);
                textView.setText(Integer.toString(index.getDayOfMonth()));
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_Button_Borderless_Colored);
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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

                if (index.getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }

                if (index.getDayOfYear() == selectedTime.getDayOfYear()) {
                    if (checkIfEvent(index)) {
                        textView.setBackgroundResource(R.drawable.monthcalendar_select_event);
                        hasEvent = true;
                    } else {
                        textView.setBackgroundResource(R.drawable.monthcalendar_select);
                    }
                    isSelected = true;
                    lastId = textView.getId();
                }
                else if (checkIfEvent(index)) {
                    textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    hasEvent = true;
                }

                tableRow.addView(textView);

                monthCalendarCellMap.put(textView.getId(), new MonthCalendarCell(textView.getId(), textView, index, hasEvent, isSelected));

                day++;
                index = index.plusDays(1);
            }

            if (index.getDayOfMonth() <= 7 && index.getMonth().equals(lastDayOfMonth.getMonth())) {
                tableRow.setGravity(Gravity.END);
            } else {
                tableRow.setGravity(Gravity.START);
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
