package ap.student.outlook_mobile_app.Calendar;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.DaysOfTheWeekEnum;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.MonthCalendarCell;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.MonthsInTheYearEnum;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.Recurrence;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.models.Calendar;
import ap.student.outlook_mobile_app.DAL.models.Event;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;
import ap.student.outlook_mobile_app.contacts.activity.ContactsActivity;
import ap.student.outlook_mobile_app.mailing.activity.MailActivity;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CalendarActivity extends AppCompatActivityRest {
    private Calendar calendar;
    private Event event;
    private Gson gson;
    private TableLayout monthCalendar;
    private TableLayout weekCalendarHeader;
    private TableLayout weekCalendarBody;
    private TableLayout dayCalendar;
    private TabHost tabhost;
    private TextView monthCalendarYearTextView;
    private TextView monthCalendarMonthTextView;
    private TextView weekCalendarTextview;
    private TextView weekCalendarCurrentYearTextview;
    private TextView dayCalendarTextview;
    private TextView dayCalendarCurrentYearTextview;
    private TextView dayCalendarNoEventsTextview;
    private java.util.Calendar selectedTime;

    private ImageButton nextMonthButton;
    private ImageButton previousMonthButton;
    private ImageButton nextWeekButton;
    private ImageButton previousWeekButton;
    private ImageButton nextDayButton;
    private ImageButton previousDayButon;
    private static final int monthCalendarChildId = 8;
    private int lastId = 0;

    SimpleDateFormat hourFormat;
    private GraphAPI graph;

    private Map<Integer, MonthCalendarCell> monthCalendarCellMap;
    private int cellSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);
        tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_email:
                                finish();
                                break;
                            case R.id.action_calendar:
                                break;
                            case R.id.action_contacts:
                                Intent intent3 = new Intent(CalendarActivity.this, ContactsActivity.class);
                                startActivity(intent3);
                                break;

                        }
                        return true;
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = getString(R.string.calendar_title);
        setActionBarMail(title, toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

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
                buildDayCalendar();
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
                calendarSetWeekButtonClicked(true);
            }
        });

        previousWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarSetWeekButtonClicked(false);
            }
        });

        /**
         * settings for day calendar
         */
        dayCalendar = (TableLayout) findViewById(R.id.dayCalendar);
        dayCalendarTextview = (TextView) findViewById(R.id.currentDayTextView);
        dayCalendarCurrentYearTextview = (TextView) findViewById(R.id.currentYearDayTextView);
        dayCalendarNoEventsTextview = (TextView) findViewById(R.id.noEventsMessage);

        nextDayButton = (ImageButton) findViewById(R.id.nextDayButton);
        previousDayButon = (ImageButton) findViewById(R.id.previousDayButton);

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarSetDayButtonClicked(true);
            }
        });

        previousDayButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarSetDayButtonClicked(false);
            }
        });

        /**
         * global settings
         */
        event = null;
        selectedTime = java.util.Calendar.getInstance();
        hourFormat = new SimpleDateFormat("H.mm ", Locale.getDefault());

        gson = new Gson();

        calendar = gson.fromJson(sharedPreferences.getString("Calendars", "{}"), Calendar.class);
        event = gson.fromJson(sharedPreferences.getString("Events", "{}"), Event.class);

        graph = new GraphAPI();

        try {
            //graph.getRequest(OutlookObjectCall.READEVENTS, this);
            graph.getRequest(OutlookObjectCall.READCALENDAR, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_event : {
                editDayButtonClicked();
            }
            break;
            case android.R.id.home : {
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // SET ACTIONBAR
    private void setActionBarMail(String title, Toolbar toolbar) {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.white));
        // THIS LINE REMOVES ANNOYING LEFT MARGIN
        toolbar.setTitleMarginStart(30);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            graph.getRequest(OutlookObjectCall.READEVENTS, this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

                if (event != null) {
                    List<Event> eventList = Arrays.asList(event.getEvents());
                    Collections.sort(eventList, new Comparator<Event>() {
                        @Override
                        public int compare(Event event, Event event1) {
                            if (event.getStart().getDateTime().getTime().before(event1.getStart().getDateTime().getTime())) {
                                return -1;
                            } else if (event.getStart().getDateTime().getTime().after(event1.getStart().getDateTime().getTime())) {
                                return 1;
                            }
                            return 0;
                        }
                    });
                    event.setEvents(eventList.toArray(new Event[eventList.size()]));
                }

                buildMonthCalendar();
                buildWeekCalendar();
                buildDayCalendar();
                editor.putString("Events", response.toString());
            }
            break;
        }
        editor.commit();
    }

    private void editDayButtonClicked() {
        startActivity(new Intent(this, EventActivity.class).putExtra("date", monthCalendarCellMap.get(lastId).getDate().toString()).putExtra("calendars", gson.toJson(calendar)).putExtra("time", gson.toJson(selectedTime)));
    }

    private void calendarSetMonthButtonClicked(boolean next) {
        if (next) {
            selectedTime.add(java.util.Calendar.MONTH, 1);
        } else {
            selectedTime.add(java.util.Calendar.MONTH, -1);
        }
        buildMonthCalendar();
        buildWeekCalendar();
        buildDayCalendar();
    }

    private void calendarSetWeekButtonClicked(boolean next) {
        if (next) {
            selectedTime.add(java.util.Calendar.DAY_OF_YEAR, 7);
        } else {
            selectedTime.add(java.util.Calendar.DAY_OF_YEAR, -7);
        }
        buildWeekCalendar();
        buildMonthCalendar();
        buildDayCalendar();
    }

    private void calendarSetDayButtonClicked(boolean next) {
        if (next) {
            selectedTime.add(java.util.Calendar.DAY_OF_YEAR, 1);
        } else {
            selectedTime.add(java.util.Calendar.DAY_OF_YEAR, -1);
        }
        buildDayCalendar();
        buildMonthCalendar();
        buildWeekCalendar();
    }

    private void monthCalendarChildClicked(int id) {
        if (lastId == id) {
            goToDay();
            return;
        }
        MonthCalendarCell calendarCell = monthCalendarCellMap.get(lastId);
        if (calendarCell.isHasEvent()) {
            calendarCell.getTextView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
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
        selectedTime.set(selectedTime.get(java.util.Calendar.YEAR), selectedTime.get(java.util.Calendar.MONTH), selectedCell.getDate().get(java.util.Calendar.DAY_OF_MONTH));
        buildWeekCalendar();
        buildDayCalendar();
    }

    private void buildMonthCalendar() {
        cellSize = monthCalendar.getWidth() / 7;

        monthCalendar.removeViews(1, monthCalendar.getChildCount() - 1);
        monthCalendarCellMap.clear();

        monthCalendar.getChildAt(0).setMinimumHeight(cellSize);

        monthCalendarYearTextView.setText(Integer.toString(selectedTime.get(java.util.Calendar.YEAR)));
        monthCalendarMonthTextView.setText(getResources().getString(MonthsInTheYearEnum.values()[selectedTime.get(java.util.Calendar.MONTH)].value()));

        java.util.Calendar firstDayOfMonth = java.util.Calendar.getInstance();
        firstDayOfMonth.setTime(selectedTime.getTime());
        firstDayOfMonth.set(java.util.Calendar.DAY_OF_MONTH, selectedTime.getMinimum(java.util.Calendar.DAY_OF_MONTH));
        int day = firstDayOfMonth.get(java.util.Calendar.DAY_OF_WEEK)-1;
        if (day == -1) day = 6;

        java.util.Calendar index = java.util.Calendar.getInstance();
        index.setTime(selectedTime.getTime());
        index.set(selectedTime.get(java.util.Calendar.YEAR), selectedTime.get(java.util.Calendar.MONTH), selectedTime.getMinimum(java.util.Calendar.DAY_OF_MONTH));

        while (index.get(java.util.Calendar.MONTH) == selectedTime.get(java.util.Calendar.MONTH) && (index.get(java.util.Calendar.DAY_OF_MONTH) <= selectedTime.getMaximum(java.util.Calendar.DAY_OF_MONTH))) {
            TableRow tableRow = new TableRow(this);
            while (index.get(java.util.Calendar.MONTH) == selectedTime.get(java.util.Calendar.MONTH) && index.get(java.util.Calendar.DAY_OF_MONTH) <= selectedTime.getMaximum(java.util.Calendar.DAY_OF_MONTH) && day < 7) {

                boolean hasEvent = false;
                boolean isSelected = false;

                final TextView textView = new TextView(this);
                textView.setText(Integer.toString(index.get(java.util.Calendar.DAY_OF_MONTH)));
                textView.setGravity(Gravity.CENTER);
                textView.setTextAppearance(this, R.style.TextAppearance_AppCompat_Widget_Button_Borderless_Colored);
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                textView.setWidth(cellSize);
                textView.setHeight(cellSize);
                textView.setId(monthCalendarChildId + index.get(java.util.Calendar.DAY_OF_MONTH));
                textView.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        monthCalendarChildClicked(textView.getId());
                    }
                });

                textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        monthCalendarChildClicked(textView.getId());
                        goToDay();
                        return false;
                    }
                });

                if (index.get(java.util.Calendar.DAY_OF_YEAR) == java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)) {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }

                if (getEvents(index) != null) {
                    hasEvent = true;
                }

                if (index.get(java.util.Calendar.DAY_OF_YEAR) == selectedTime.get(java.util.Calendar.DAY_OF_YEAR)) {
                    if (hasEvent) {
                        textView.setBackgroundResource(R.drawable.monthcalendar_select_event);
                        hasEvent = true;
                    } else {
                        textView.setBackgroundResource(R.drawable.monthcalendar_select);
                    }
                    isSelected = true;
                    lastId = textView.getId();
                }
                else if (hasEvent) {
                    textView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    hasEvent = true;
                }

                tableRow.addView(textView);

                monthCalendarCellMap.put(textView.getId(), new MonthCalendarCell(textView.getId(), textView, index, hasEvent, isSelected));

                day++;
                index.add(java.util.Calendar.DAY_OF_YEAR,1);
            }

            if (index.get(java.util.Calendar.DAY_OF_MONTH) <= 7 && index.get(java.util.Calendar.MONTH) == selectedTime.get(java.util.Calendar.MONTH)) {
                tableRow.setGravity(Gravity.END);
            } else {
                tableRow.setGravity(Gravity.START);
            }
            monthCalendar.addView(tableRow);
            day = 0;
        }
    }

    private void buildWeekCalendar() {
        weekCalendarCurrentYearTextview.setText(Integer.toString(selectedTime.get(java.util.Calendar.YEAR)));
        java.util.Calendar startDate = java.util.Calendar.getInstance();
        startDate.setTime(selectedTime.getTime());
        startDate.add(java.util.Calendar.DAY_OF_YEAR, (-selectedTime.get(java.util.Calendar.DAY_OF_WEEK)+1));
        java.util.Calendar endDate = java.util.Calendar.getInstance();
        endDate.setTime(startDate.getTime());
        endDate.add(java.util.Calendar.DAY_OF_YEAR, 6);

        StringBuilder stringBuilder = new StringBuilder();

        if (startDate.get(java.util.Calendar.YEAR) != endDate.get(java.util.Calendar.YEAR)) {
            weekCalendarCurrentYearTextview.setText(Integer.toString(startDate.get(java.util.Calendar.YEAR)).concat(" - ").concat(Integer.toString(endDate.get(java.util.Calendar.YEAR))));
        }
        if (startDate.get(java.util.Calendar.MONTH) != endDate.get(java.util.Calendar.MONTH)) {
            stringBuilder.append(getResources().getString(MonthsInTheYearEnum.values()[startDate.get(java.util.Calendar.MONTH)].value()).substring(0, 3))
                    .append(' ').append(startDate.get(java.util.Calendar.DAY_OF_MONTH)).append(" - ")
                    .append(getResources().getString(MonthsInTheYearEnum.values()[endDate.get(java.util.Calendar.MONTH)].value()).substring(0, 3))
                    .append(' ').append(endDate.get(java.util.Calendar.DAY_OF_MONTH));
        } else {
            stringBuilder.append(getResources().getString(MonthsInTheYearEnum.values()[startDate.get(java.util.Calendar.MONTH)].value()))
                    .append(' ').append(startDate.get(java.util.Calendar.DAY_OF_MONTH))
                    .append(" - ").append(endDate.get(java.util.Calendar.DAY_OF_MONTH));
        }
        weekCalendarTextview.setText(stringBuilder.toString());

        for (int i = 0; i < 7; i++) {
            TableRow row = (TableRow) weekCalendarHeader.getChildAt(i);
            row.setMinimumHeight(cellSize);
            TextView header = (TextView)row.getChildAt(0);
            header.setText(getResources().getString(DaysOfTheWeekEnum.values()[i].value()).concat(" ").concat(String.valueOf(startDate.get(java.util.Calendar.DAY_OF_MONTH))));

            row = (TableRow) weekCalendarBody.getChildAt(i);
            row.removeAllViews();
            row.setMinimumHeight(cellSize);

            Event[] events = getEvents(startDate);

            if (events != null) {
                for (final Event event : events) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(hourFormat.format(event.getStart().getDateTime().getTime())).append(event.getSubject());

                    TextView textView = new TextView(this);
                    textView.setText(stringBuilder.toString());
                    textView.setTextAppearance(this, R.style.TextAppearance_AppCompat_Subhead);
                    textView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    TableRow.LayoutParams params = new TableRow.LayoutParams(FILL_PARENT, WRAP_CONTENT);
                    params.setMargins(4, 4, 4, 4);
                    textView.setLayoutParams(params);
                    textView.setPadding(4, 4, 4, 4);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eventClicked(event.getId());
                        }
                    });

                    row.addView(textView);
                }
            }
            startDate.add(java.util.Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void buildDayCalendar() {
        dayCalendarCurrentYearTextview.setText(String.valueOf(selectedTime.get(java.util.Calendar.YEAR)));

        dayCalendar.removeAllViews();

        int dayOfWeek = selectedTime.get(java.util.Calendar.DAY_OF_WEEK) -1;
        if (dayOfWeek == -1) dayOfWeek = 6;

        dayCalendarTextview.setText(new StringBuilder()
                .append(getResources().getString(DaysOfTheWeekEnum.values()[dayOfWeek].value()))
                .append(' ').append(selectedTime.get(java.util.Calendar.DAY_OF_MONTH)).append(' ')
                .append(getResources().getString(MonthsInTheYearEnum.values()[selectedTime.get(java.util.Calendar.MONTH)].value())).toString());

        Event[] events = getEvents(selectedTime);

        if (events != null) {
            dayCalendarNoEventsTextview.setVisibility(View.GONE);
            dayCalendar.setVisibility(View.VISIBLE);

            for (final Event event : events) {
                TableRow row = new TableRow(this);
                TextView header = new TextView(this);
                if (event.isAllDay()) {
                    header.setText(getResources().getText(R.string.event_all_day_checkbox));
                } else {
                    header.setText(hourFormat.format(event.getStart().getDateTime().getTime()).concat(" - ")
                            .concat(hourFormat.format(event.getEnd().getDateTime().getTime())));
                }
                header.setTextAppearance(this, R.style.TextAppearance_AppCompat);
                header.setPadding(8, 8,8,8);
                header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView content = new TextView(this);
                content.setText(event.getSubject().concat("\r\n".concat(event.getLocation().getDisplayName().concat(" ").concat("\r\n").concat(event.getOrganizer().getEmailAddress().getName()))));
                content.setTextAppearance(this, R.style.TextAppearance_AppCompat);
                content.setPadding(8, 8, 8, 8);

                row.addView(header);
                row.addView(content);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eventClicked(event.getId());
                    }
                });

                row.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                TableLayout.LayoutParams params = new TableLayout.LayoutParams(FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 4, 4, 4);
                row.setLayoutParams(params);
                dayCalendar.addView(row);
            }
        } else {
            dayCalendarNoEventsTextview.setVisibility(View.VISIBLE);
            dayCalendar.setVisibility(View.GONE);
        }
    }

    private Event[] getEvents(java.util.Calendar index) {
        if (event != null && event.getEvents() != null) {
            List<Event> events = new ArrayList<>();
            for (Event event : event.getEvents()) {
                if (event.getStart().getDateTime().get(java.util.Calendar.YEAR) == index.get(java.util.Calendar.YEAR) && event.getStart().getDateTime().get(java.util.Calendar.DAY_OF_YEAR) == index.get(java.util.Calendar.DAY_OF_YEAR)) {
                    events.add(event);
                }
            }
            if (events.size() > 0) {
                return events.toArray(new Event[]{});
            }
        }
        return null;
    }

    private void eventClicked(String id) {
        startActivity(new Intent(this, EventActivity.class).putExtra("event", id).putExtra("date", monthCalendarCellMap.get(lastId).getDate().toString()).putExtra("calendars", gson.toJson(calendar)).putExtra("time", gson.toJson(selectedTime)));
    }

    private void goToDay() {
        tabhost.setCurrentTab(2);
    }
}
