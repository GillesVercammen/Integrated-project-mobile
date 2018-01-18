package ap.student.outlook_mobile_app.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.DaysOfWeekEnum;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.Recurrence;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.ReminderMinutesBeforeStart;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.ShowAs;
import ap.student.outlook_mobile_app.DAL.MicrosoftDateFormat;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.enums.RecurrencePatternType;
import ap.student.outlook_mobile_app.DAL.enums.RecurrenceRangeType;
import ap.student.outlook_mobile_app.DAL.models.Attendee;
import ap.student.outlook_mobile_app.DAL.models.Body;
import ap.student.outlook_mobile_app.DAL.models.Calendar;
import ap.student.outlook_mobile_app.DAL.models.DateTimeTimeZone;
import ap.student.outlook_mobile_app.DAL.models.EmailAddress;
import ap.student.outlook_mobile_app.DAL.models.Event;
import ap.student.outlook_mobile_app.DAL.models.Location;
import ap.student.outlook_mobile_app.DAL.models.PatternedRecurrence;
import ap.student.outlook_mobile_app.DAL.models.RecurrencePattern;
import ap.student.outlook_mobile_app.DAL.models.RecurrenceRange;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

public class EventActivity extends AppCompatActivityRest {
    private java.util.Calendar startTime;
    private java.util.Calendar endTime;
    private EditText titleTextInput;
    private EditText locationTextInput;
    private Button setEndTimeButton;
    private CheckBox isAllDayCheckBox;
    private CheckBox isPrivateCheckBox;
    private TextView startTimeTextview;
    private TextView endTimeTextview;
    private SimpleDateFormat dateTimeFormatter;
    private AutoCompleteTextView timeZonePicker;
    private Spinner showAsSpinner;
    private Spinner recurrenceSpinner;
    private Spinner reminderSpinner;
    private Spinner agendaSpinner;
    private Button sendReminderByMailButton;
    private EditText descriptionText;
    private Button setStartTimeButton;
    private Map<Integer, ShowAs> showAsMap;
    private Map<Integer, Recurrence> recurrenceMap;
    private Map<Integer, ReminderMinutesBeforeStart> reminderMap;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private boolean isStartTime;
    private PatternedRecurrence customRecurrence;
    private Calendar calendar;
    private Map<Integer, Calendar> calendarMap;
    private Button addAttendeesButton;
    private EmailAddress organiser;
    private Attendee[] attendees;
    private MenuItem delete;
    private TextView attendeesTextview;

    private String id;
    private Event event;
    private SimpleDateFormat microsoftDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event);
        super.onCreate(savedInstanceState);

        attendees = new Attendee[0];
        showAsMap = new HashMap<>();
        recurrenceMap = new HashMap<>();
        reminderMap = new HashMap<>();
        calendarMap = new HashMap<>();

        setStartTimeButton = (Button) findViewById(R.id.eventSetStartTimeButton);
        setEndTimeButton = (Button) findViewById(R.id.evenSetEndTimeButton);
        addAttendeesButton = (Button) findViewById(R.id.eventAddAttendeesButton);
        showAsSpinner = (Spinner) findViewById(R.id.eventDisplayAsSpinner);
        recurrenceSpinner = (Spinner) findViewById(R.id.eventRepeatSpinner);
        reminderSpinner = (Spinner) findViewById(R.id.eventRemindSpinner);

        titleTextInput = (EditText) findViewById(R.id.titleTextInput);
        descriptionText = (EditText) findViewById(R.id.eventDescriptionText);
        locationTextInput = (EditText) findViewById(R.id.locationTextInput);
        isAllDayCheckBox = (CheckBox) findViewById(R.id.eventAllDayCheckbox);
        isPrivateCheckBox = (CheckBox) findViewById(R.id.eventPrivateCheckbox);

        attendeesTextview = (TextView) findViewById(R.id.attendeesTextview);

        dateTimeFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        startTime = java.util.Calendar.getInstance();
        if (getIntent().getStringExtra("time") != null) {
            startTime.setTime(new Gson().fromJson(getIntent().getStringExtra("time"), java.util.Calendar.class).getTime());
        }
        startTime.set(java.util.Calendar.HOUR, 8);
        startTime.set(java.util.Calendar.MINUTE, 30);
        startTime.set(java.util.Calendar.SECOND, 0);

        endTime = java.util.Calendar.getInstance();
        endTime.setTime(startTime.getTime());
        endTime.add(java.util.Calendar.MINUTE, 30);

        startTimeTextview = (TextView) findViewById(R.id.eventStartDateText);
        startTimeTextview.setText(dateTimeFormatter.format(startTime.getTime()));
        endTimeTextview = (TextView) findViewById(R.id.eventEndDateText);
        endTimeTextview.setText(dateTimeFormatter.format(endTime.getTime()));

        timeZonePicker = (AutoCompleteTextView) findViewById(R.id.eventTimezoneAutocomplete);

        timeZonePicker.setText(TimeZone.getDefault().getDisplayName());

        calendar = new Gson().fromJson(getIntent().getStringExtra("calendars"), Calendar.class);
        agendaSpinner = (Spinner) findViewById(R.id.eventAgendaSpinner);

        populateShowAsSpinner();
        populateRecurrenceSpinner();
        populateReminderSpinner();
        populateAgendaSpinner();

        isStartTime = true;

        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onDatePicked(year, ++month, dayOfMonth);
            }
        }, startTime.get(java.util.Calendar.YEAR), startTime.get(java.util.Calendar.MONTH), startTime.get(java.util.Calendar.DAY_OF_MONTH));

        timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                onTimePicked(hourOfDay, minute);
            }
        }, startTime.get(java.util.Calendar.HOUR), startTime.get(java.util.Calendar.MINUTE), true);

        setStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTime = true;
                datePicker.show();
            }
        });

        setEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTime = false;
                datePicker.show();
            }
        });

        recurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > recurrenceMap.size() || recurrenceMap.get(position).action().equals(Recurrence.MORE.action())) {
                    onMoreSelected();
                    // FIXME : doesn't do shit on reselect
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addAttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddAttendeesButtonClicked();
            }
        });

        String title = getString(R.string.event_title);
        event = null;
        id = null;
        String eventId = getIntent().getStringExtra("event");
        if (eventId != null && !eventId.isEmpty()) {
            title = getString(R.string.event_edit_title);
            Event events = new Gson().fromJson(sharedPreferences.getString("Events", "{}"), Event.class);
            for (Event e : events.getEvents()) {
                if (e.getId().equals(eventId)) {
                    event = e;
                    id = e.getId();
                    loadEventSettings();
                    break;
                }
            }
        }

        String text = attendees.length + " ".concat(getResources().getString(R.string.attendees_textview));
        attendeesTextview.setText(text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBarMail(title, toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        microsoftDateFormat = new MicrosoftDateFormat().getMicrosoftDateFormat();
        microsoftDateFormat.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        if (id == null) {
            menu.findItem(R.id.action_delete).setEnabled(false).setVisible(false);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_event : {
                if (connectivityManager.isConnected()) {
                    onConfirmButtonClicked();
                } else {
                    Toast.makeText(this, "You can't perform this action whilst logged out.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.action_delete : {
                if (connectivityManager.isConnected()) {
                    deleteEvent();
                } else {
                    Toast.makeText(this, "You can't perform this action whilst logged out.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case android.R.id.home : {
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadEventSettings() {
        titleTextInput.setText(event.getSubject());

        String description = event.getBody().getContent();

        attendees = event.getAttendees();

        if (description.length() > 2) {
            descriptionText.setText(description.split("<body>")[1].split("</body>")[0].replaceAll("<br>", "").substring(2));
        }
        locationTextInput.setText(event.getLocation().getDisplayName());

        Recurrence recurrence = new RecurrenceFinder().findRecurrenceFromPatternedRecurrence(event);
        int index = 0;
        while (index <  recurrenceMap.size()) {
            if (recurrenceMap.get(index).equals(recurrence)) {
                break;
            }
            index++;
        }
        if (index >= recurrenceMap.size()) {
            index = 0;
        }
        recurrenceSpinner.setSelection(index);

        startTime.setTime(event.getStart().getDateTime().getTime());
        endTime.setTime(event.getEnd().getDateTime().getTime());
        startTimeTextview.setText(dateTimeFormatter.format(event.getStart().getDateTime().getTime()));

        if (event.isAllDay()) {
            isAllDayCheckBox.setChecked(event.isAllDay());
            endTime.setTime(startTime.getTime());
            endTime.set(java.util.Calendar.MINUTE, 30);
        } else {
            endTimeTextview.setText(dateTimeFormatter.format(event.getEnd().getDateTime().getTime()));
        }
        if (event.getSensitivity().equals("private")) {
            isPrivateCheckBox.setChecked(true);
        }

        // TODO : agenda is fucked

        index = 0;
        while (index < reminderMap.values().size()) {
            if (reminderMap.get(index).getValue() == event.getReminderMinutesBeforeStart()) {
                break;
            }
            index ++;
        }
        if (index >= reminderMap.size()) {
            index = 0;
        }
        reminderSpinner.setSelection(index);

        index = 0;
        while (index < showAsMap.values().size()) {
            if (showAsMap.get(index).action().equals(event.getShowAs())) {
                break;
            }
            index++;
        }
        if (index >= showAsMap.size()) {
            index = 0;
        }
        showAsSpinner.setSelection(index);
    }

    private void onMoreSelected() {
        startActivityForResult(new Intent(this, CustomRecurrenceActivity.class), 200);
    }

    private PatternedRecurrence setRecurrence(Recurrence recurrence, java.util.Calendar startDate, java.util.Calendar endDate) {
        PatternedRecurrence patternedRecurrence = new PatternedRecurrence();
        RecurrencePattern recurrencePattern = new RecurrencePattern();
        RecurrenceRange recurrenceRange = new RecurrenceRange();


        recurrenceRange.setType(RecurrenceRangeType.NOEND.value());
        recurrenceRange.setStartDate(startDate);
        recurrenceRange.setEndDate(endDate);
        recurrencePattern.setFirstDayOfWeek(DaysOfWeekEnum.SUNDAY.name());

        switch (recurrence) {
            case DAILY: {
                recurrencePattern.setInterval(1);
                recurrencePattern.setType(recurrence.action());
            }
            break;
            case EVERY_FRIDAY: {
                recurrencePattern.setType(RecurrencePatternType.WEEKLY.value());
                recurrencePattern.setDaysOfWeek(new String[] { DaysOfWeekEnum.FRIDAY.name() });
                recurrencePattern.setInterval(1);
            }
            break;
            case EVERY_WORKDAY: {
                recurrencePattern.setType(RecurrencePatternType.WEEKLY.value());
                recurrencePattern.setDaysOfWeek(new String[] { DaysOfWeekEnum.MONDAY.name(), DaysOfWeekEnum.TUESDAY.name(), DaysOfWeekEnum.WEDNESDAY.name(), DaysOfWeekEnum.THURSDAY.name(), DaysOfWeekEnum.FRIDAY.name() });
                recurrencePattern.setInterval(1);
            }
            break;
            case EACH_MONTH_TODAY: {
                recurrencePattern.setType(RecurrencePatternType.ABSOLUTEMONTHLY.value());
                recurrencePattern.setDayOfMonth(startDate.get(java.util.Calendar.DAY_OF_MONTH));
                recurrencePattern.setInterval(1);
            }
            break;
            case EVERY_SECOND_FRIDAY: {
                recurrencePattern.setType(RecurrencePatternType.WEEKLY.value());
                recurrencePattern.setDaysOfWeek(new String[] { DaysOfWeekEnum.FRIDAY.name() });
                recurrencePattern.setInterval(2);
            }
            break;
            case EVERY_YEAR: {
                recurrencePattern.setType(RecurrencePatternType.ABSOLUTEYEARLY.value());
                recurrencePattern.setMonth(startDate.get(java.util.Calendar.MONTH));
                recurrencePattern.setDayOfMonth(startDate.get(java.util.Calendar.DAY_OF_MONTH));
                recurrencePattern.setInterval(1);
            }
            break;
            case MORE: {
                RecurrenceRange range = customRecurrence.getRange();
                range.setStartDate(startDate);
                range.setEndDate(endDate);
                customRecurrence.setRange(range);
                return customRecurrence;
            }
            default:
            case NEVER: {
                return null;
            }
        }
        patternedRecurrence.setPattern(recurrencePattern);
        patternedRecurrence.setRange(recurrenceRange);
        return patternedRecurrence;
    }

    private void onAddAttendeesButtonClicked() {
        EmailAddress[] emailAddress = new EmailAddress[attendees.length];
        for (int i = 0; i < attendees.length; i++) {
            emailAddress[i] = attendees[i].getEmailAddress();
        }
        startActivityForResult(new Intent(this, AttendeesActivity.class).putExtra("attendees", new Gson().toJson(emailAddress)), 201);
    }

    private void onConfirmButtonClicked() {
        if (startTime.getTime().after(endTime.getTime())) {
            Toast.makeText(this, "The startime must be before the endtime, automatically changing the endtime.", Toast.LENGTH_SHORT).show();
            endTime.setTime(startTime.getTime());
            endTime.add(java.util.Calendar.HOUR, 1);
        }

        Event event = new Event();
        event.setId(id);
        event.setSubject(titleTextInput.getText().toString());
        event.setBody(new Body(descriptionText.getText().toString().replaceAll("\\n", "<br/>"), "HTML"));
        event.setLocation(new Location(locationTextInput.getText().toString()));

        if (!recurrenceMap.get(recurrenceSpinner.getSelectedItemPosition()).equals(Recurrence.NEVER)) {
            event.setRecurrence(setRecurrence(recurrenceMap.get(recurrenceSpinner.getSelectedItemPosition()), startTime, endTime));
        }

        if (isAllDayCheckBox.isChecked()) {
            event.setAllDay(isAllDayCheckBox.isChecked());
            java.util.Calendar time = java.util.Calendar.getInstance();
            time.set(startTime.get(java.util.Calendar.YEAR), startTime.get(java.util.Calendar.MONTH), startTime.get(java.util.Calendar.DAY_OF_MONTH), 0, 0, 0);
            event.setStart(new DateTimeTimeZone(microsoftDateFormat.format(time.getTime()), TimeZone.getDefault().getID()));
            time.add(java.util.Calendar.DAY_OF_YEAR, 1);
            event.setEnd(new DateTimeTimeZone(microsoftDateFormat.format(time.getTime()), TimeZone.getDefault().getID()));
        } else {
            event.setStart(new DateTimeTimeZone(microsoftDateFormat.format(startTime.getTime()), TimeZone.getDefault().getID()));
            event.setEnd(new DateTimeTimeZone(microsoftDateFormat.format(endTime.getTime()), TimeZone.getDefault().getID()));
        }

        if (isPrivateCheckBox.isChecked()) { event.setSensitivity("private"); }
        else { event.setSensitivity("normal"); }

        event.setReminderMinutesBeforeStart(reminderMap.get(reminderSpinner.getSelectedItemPosition()).getValue());
        if (event.getReminderMinutesBeforeStart() != -1) {
            event.setReminderOn(true);
        }

        event.setShowAs(showAsMap.get(showAsSpinner.getSelectedItemPosition()).action());

        /*event.setAttendees(new Attendee[] {
                new Attendee("Required", new EmailAddress())
        });*/
        event.setAttendees(attendees);

        String calendar = calendarMap.get(agendaSpinner.getSelectedItemPosition()).getId();

        try {
            Gson gson = new Gson();
            String array = gson.toJson(event);
            JSONObject jsonObject = new JSONObject(array);
            //new GraphAPI().postRequest(OutlookObjectCall.POSTEVENT,this, jsonObject);
            if (id == null) {
                new GraphAPI().postRequest(OutlookObjectCall.POSTCALENDAR, this, jsonObject, "/".concat(calendar).concat(OutlookObjectCall.POSTEVENT.action()));
            } else {
                new GraphAPI().patchRequest(OutlookObjectCall.POSTEVENT, this, jsonObject, "/".concat(id));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onDatePicked(int yearPicked, int monthPicked, int dayOfMonthPicked) {
        if (isStartTime) {
            startTime.set(yearPicked, --monthPicked, dayOfMonthPicked);
        } else {
            endTime.set(yearPicked, --monthPicked, dayOfMonthPicked);
        }

        if (!isAllDayCheckBox.isChecked()) {
            timePicker.show();
        } else {
            onTimePicked(0, 0);
        }
    }

    private void onTimePicked(int hourPicked, int minutePicked) {
        if (isStartTime) {
            startTime.set(java.util.Calendar.HOUR, hourPicked);
            startTime.set(java.util.Calendar.MINUTE, minutePicked);
            startTimeTextview.setText(dateTimeFormatter.format(startTime.getTime()));
        } else {
            endTime.set(java.util.Calendar.HOUR, hourPicked);
            endTime.set(java.util.Calendar.MINUTE, minutePicked);
            endTimeTextview.setText(dateTimeFormatter.format(endTime.getTime()));
        }
        if (endTime.getTime().before(startTime.getTime())) {
            Toast.makeText(this, "The current starttime is set after the endtime. While this does not compute you can confirm, but it won't end when you set.", Toast.LENGTH_LONG).show();
        }
    }

    private void populateReminderSpinner() {
        for (ReminderMinutesBeforeStart value : ReminderMinutesBeforeStart.values()) {
            reminderMap.put(reminderMap.size(), value);
        }
        populateSpinner(R.array.reminderArray, reminderSpinner);
    }

    private void populateRecurrenceSpinner() {
        for (Recurrence value : Recurrence.values()) {
            recurrenceMap.put(recurrenceMap.size(), value);
        }
        populateSpinner(R.array.recurrenceArray, recurrenceSpinner);
    }

    private void populateShowAsSpinner() {
        for (ShowAs value : ShowAs.values()) {
            showAsMap.put(showAsMap.size(), value);
        }
        populateSpinner(R.array.eventShowAsArray, showAsSpinner);
    }

    private void populateAgendaSpinner() {
        String[] calendars = new String[calendar.getCalendars().length];
        for (Calendar calendar : calendar.getCalendars()) {
            calendars[calendarMap.size()] = calendar.getName();
            calendarMap.put(calendarMap.size(), calendar);
        }
        agendaSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, calendars));
    }

    private void populateSpinner(int arrayResource, Spinner spinner) {
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(arrayResource))));
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        this.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 200 : {
                if (resultCode == RESULT_OK) {
                    customRecurrence = new Gson().fromJson(data.getStringExtra("PatternedRecurrence"), PatternedRecurrence.class);
                }
            }
            break;
            case 201 : {
                if (resultCode == RESULT_OK) {
                    EmailAddress[] emailAddresses = new Gson().fromJson(data.getStringExtra("attendees"), EmailAddress[].class);
                    Attendee[] attendees = new Attendee[emailAddresses.length];
                    for (int i = 0; i < emailAddresses.length; i++) {
                        Attendee attendee = new Attendee();
                        attendee.setEmailAddress(emailAddresses[i]);
                        attendees[i] = attendee;
                    }
                    this.attendees = attendees;
                    String text = attendees.length + " ".concat(getResources().getString(R.string.attendees_textview));
                    attendeesTextview.setText(text);
                }
            }
        }
    }

    private void deleteEvent() {
        try {
            new GraphAPI().deleteRequest(OutlookObjectCall.READEVENTS, this, "/" + id);
            Thread.sleep(10);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
