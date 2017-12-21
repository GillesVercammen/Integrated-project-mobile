package ap.student.outlook_mobile_app.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.DaysOfTheWeekEnum;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.MonthCalendarCell;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.MonthsInTheYearEnum;
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
    private TableLayout weekCalendarHeader;
    private TableLayout weekCalendarBody;
    private TabHost tabhost;
    private TextView monthCalendarYearTextView;
    private TextView monthCalendarMonthTextView;
    private TextView weekCalendarTextview;
    private TextView weekCalendarCurrentYearTextview;
    private LocalDateTime selectedTime;
    private Button editDayButton;

    private ImageButton nextMonthButton;
    private ImageButton previousMonthButton;
    private ImageButton nextWeekButton;
    private ImageButton previousWeekButton;
    private static final int monthCalendarChildId = 8;
    private int lastId = 0;

    private Map<Integer, MonthCalendarCell> monthCalendarCellMap;
    private int cellSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);
        tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();

        /**
         * Tabs setup
         */
        //Tab 1
        TabHost.TabSpec spec = tabhost.newTabSpec(getResources().getString(R.string.tab_month));
        spec.setContent(R.id.calendar_month);
        spec.setIndicator(getResources().getString(R.string.tab_month));
        tabhost.addTab(spec);

        //Tab 2
        spec = tabhost.newTabSpec(getResources().getString(R.string.tab_week));
        spec.setContent(R.id.calendar_week);
        spec.setIndicator(getResources().getString(R.string.tab_week));
        tabhost.addTab(spec);

        //Tab 3
        spec = tabhost.newTabSpec(getResources().getString(R.string.tab_day));
        spec.setContent(R.id.calendar_day);
        spec.setIndicator(getResources().getString(R.string.tab_day));
        tabhost.addTab(spec);

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
                buildWeekCalendar();
            }
        });

        /**
         * settings for week calendar view
         */
        weekCalendarHeader = (TableLayout) findViewById(R.id.weekCalendarHeaders);
        weekCalendarBody = (TableLayout) findViewById(R.id.weekCalendarBody);
        weekCalendarTextview = (TextView) findViewById(R.id.currentWeekTextView);
        weekCalendarCurrentYearTextview = (TextView) findViewById(R.id.currentYearWeekTextView);
        nextWeekButton = (ImageButton) findViewById(R.id.nextWeekButton);
        previousWeekButton = (ImageButton) findViewById(R.id.previousWeekButton);

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calenderSetWeekButtonClicked(true);
            }
        });

        previousWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calenderSetWeekButtonClicked(false);
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

        calendar = gson.fromJson(sharedPreferences.getString("Calendars", "{}"), Calendar.class);
        event = gson.fromJson(sharedPreferences.getString("Events", "{}"), Event.class);

        try {
            new GraphAPI().getRequest(OutlookObjectCall.READEVENTS, this);
            new GraphAPI().getRequest(OutlookObjectCall.READCALENDAR, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //buildMonthCalendar();
        //buildWeekCalendar();
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {

        switch (outlookObjectCall) {
            case READCALENDAR: {
                calendar = gson.fromJson(response.toString(), Calendar.class);
                editor.putString("Calendars", response.toString());
            }
            break;
            case READEVENTS: {
                event = gson.fromJson(response.toString(), Event.class);
                buildMonthCalendar();
                editor.putString("Events", response.toString());
            }
            break;
        }
        editor.commit();
    }

    private void editDayButtonClicked() {
        startActivity(new Intent(this, EventActivity.class).putExtra("date", monthCalendarCellMap.get(lastId).getDate().toString()).putExtra("calendars", gson.toJson(calendar)));
    }

    private void calendarSetMonthButtonClicked(boolean next) {
        if (next) {
            selectedTime = selectedTime.plusMonths(1);
        } else {
            selectedTime = selectedTime.minusMonths(1);
        }
        buildMonthCalendar();
        buildWeekCalendar();
    }

    private void calenderSetWeekButtonClicked(boolean next) {
        if (next) {
            selectedTime = selectedTime.plusDays(7);
        } else {
            selectedTime = selectedTime.minusDays(7);
        }
        buildWeekCalendar();
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
        selectedTime = LocalDateTime.of(selectedTime.getYear(), selectedTime.getMonth(), selectedCell.getDate().getDayOfMonth(), selectedTime.getHour(), selectedTime.getMinute());
        buildWeekCalendar();
    }

    //TODO : clean up this monster
    private void buildMonthCalendar() {
        cellSize = monthCalendar.getWidth() / 7;

        monthCalendar.removeViews(1, monthCalendar.getChildCount() - 1);
        monthCalendarCellMap.clear();

        monthCalendar.getChildAt(0).setMinimumHeight(cellSize);

        monthCalendarYearTextView.setText(Integer.toString(selectedTime.getYear()));
        monthCalendarMonthTextView.setText(getResources().getString(MonthsInTheYearEnum.values()[selectedTime.getMonthValue()-1].value()));

        int day = selectedTime.with(TemporalAdjusters.firstDayOfMonth()).getDayOfWeek().getValue();
        if (day == 7) day = 0;
        LocalDate lastDayOfMonth = selectedTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate();
        LocalDate index = selectedTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate();

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
                textView.setWidth(cellSize);
                textView.setHeight(cellSize);
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

    private void buildWeekCalendar() {
        weekCalendarCurrentYearTextview.setText(Integer.toString(selectedTime.getYear()));
        LocalDate startDate = selectedTime.minusDays(selectedTime.getDayOfWeek().getValue()).toLocalDate();
        LocalDate endDate = startDate.plusDays(6);

        StringBuilder stringBuilder = new StringBuilder();

        if (startDate.getYear() != endDate.getYear()) {
            weekCalendarCurrentYearTextview.setText(Integer.toString(startDate.getYear()).concat(" - ").concat(Integer.toString(endDate.getYear())));
        }
        if (!startDate.getMonth().equals(endDate.getMonth())) {
            stringBuilder.append(getResources().getString(MonthsInTheYearEnum.values()[startDate.getMonthValue()-1].value()).substring(0, 3))
                    .append(' ').append(startDate.getDayOfMonth()).append(" - ")
                    .append(getResources().getString(MonthsInTheYearEnum.values()[endDate.getMonthValue()-1].value()).substring(0, 3))
                    .append(' ').append(endDate.getDayOfMonth());
        } else {
            stringBuilder.append(getResources().getString(MonthsInTheYearEnum.values()[startDate.getMonthValue()-1].value()))
                    .append(' ').append(startDate.getDayOfMonth())
                    .append(" - ").append(endDate.getDayOfMonth());
        }
        weekCalendarTextview.setText(stringBuilder.toString());

        DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("H:mm ");

        for (int i = 0; i < 7; i++) {
            TableRow row = (TableRow) weekCalendarHeader.getChildAt(i);
            row.setMinimumHeight(cellSize);
            TextView header = (TextView)row.getChildAt(0);
            header.setText(getResources().getString(DaysOfTheWeekEnum.values()[i].value()).concat(" ").concat(String.valueOf(startDate.getDayOfMonth())));

            row = (TableRow) weekCalendarBody.getChildAt(i);
            row.removeAllViews();
            row.setMinimumHeight(cellSize);

            if (checkIfEvent(startDate)) {
                for (Event event : event.getEvents()) {
                    if (event.getStart().getDateTime().getDayOfYear() == startDate.getDayOfYear()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(event.getStart().getDateTime().format(hourFormat)).append(event.getSubject());

                        TextView textView = new TextView(this);
                        textView.setText(stringBuilder.toString());
                        textView.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);

                        row.addView(textView);
                    }
                }
            }
            startDate = startDate.plusDays(1);
        }
    }

    private boolean checkIfEvent(LocalDate index) {
        if (event == null || event.getEvents() == null) {
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
