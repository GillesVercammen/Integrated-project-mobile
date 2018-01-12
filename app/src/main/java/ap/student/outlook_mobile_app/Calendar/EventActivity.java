package ap.student.outlook_mobile_app.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.Gson;
import com.microsoft.identity.client.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import ap.student.outlook_mobile_app.BLL.GraphAPI;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.Recurrence;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.ReminderMinutesBeforeStart;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.ShowAs;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.DAL.enums.RecurrencePatternType;
import ap.student.outlook_mobile_app.DAL.enums.RecurrenceRangeType;
import ap.student.outlook_mobile_app.DAL.models.Body;
import ap.student.outlook_mobile_app.DAL.models.Calendar;
import ap.student.outlook_mobile_app.DAL.models.DateTimeTimeZone;
import ap.student.outlook_mobile_app.DAL.models.EmailAddress;
import ap.student.outlook_mobile_app.DAL.models.Event;
import ap.student.outlook_mobile_app.DAL.models.Location;
import ap.student.outlook_mobile_app.DAL.models.PatternedRecurrence;
import ap.student.outlook_mobile_app.DAL.models.Recipient;
import ap.student.outlook_mobile_app.DAL.models.RecurrencePattern;
import ap.student.outlook_mobile_app.DAL.models.RecurrenceRange;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

public class EventActivity extends AppCompatActivityRest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private EditText titleTextInput;
    private EditText locationTextInput;
    private Button setEndTimeButton;
    private CheckBox isAllDayCheckBox;
    private CheckBox isPrivateCheckBox;
    private TextView startTimeTextview;
    private TextView endTimeTextview;
    private DateTimeFormatter dateTimeFormatter;
    private AutoCompleteTextView timeZonePicker;
    private Spinner showAsSpinner;
    private Spinner recurrenceSpinner;
    private Spinner reminderSpinner;
    private Spinner agendaSpinner;
    private Button sendReminderByMailButton;
    private EditText descriptionText;
    private Button confirmButton;
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

    private String id;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event);
        super.onCreate(savedInstanceState);

        showAsMap = new HashMap<>();
        recurrenceMap = new HashMap<>();
        reminderMap = new HashMap<>();
        calendarMap = new HashMap<>();

        setStartTimeButton = (Button) findViewById(R.id.eventSetStartTimeButton);
        setEndTimeButton = (Button) findViewById(R.id.evenSetEndTimeButton);
        addAttendeesButton = (Button) findViewById(R.id.eventAddAttendeesButton);
        confirmButton = (Button) findViewById(R.id.eventConfirmButton);
        showAsSpinner = (Spinner) findViewById(R.id.eventDisplayAsSpinner);
        recurrenceSpinner = (Spinner) findViewById(R.id.eventRepeatSpinner);
        reminderSpinner = (Spinner) findViewById(R.id.eventRemindSpinner);

        titleTextInput = (EditText) findViewById(R.id.titleTextInput);
        descriptionText = (EditText) findViewById(R.id.eventDescriptionText);
        locationTextInput = (EditText) findViewById(R.id.locationTextInput);
        isAllDayCheckBox = (CheckBox) findViewById(R.id.eventAllDayCheckbox);
        isPrivateCheckBox = (CheckBox) findViewById(R.id.eventPrivateCheckbox);

        dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
        startTime = LocalDateTime.of(LocalDate.parse(getIntent().getStringExtra("date")), LocalTime.of(8, 30));
        endTime = startTime.plusMinutes(30);

        startTimeTextview = (TextView) findViewById(R.id.eventStartDateText);
        startTimeTextview.setText(startTime.format(dateTimeFormatter));
        endTimeTextview = (TextView) findViewById(R.id.eventEndDateText);
        endTimeTextview.setText(endTime.format(dateTimeFormatter));

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
        }, startTime.getYear(), startTime.getMonthValue(), startTime.getDayOfMonth());

        timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                onTimePicked(hourOfDay, minute);
            }
        }, startTime.getHour(), startTime.getMinute(), true);

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

        isAllDayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    endTimeTextview.setVisibility(View.GONE);
                    setEndTimeButton.setVisibility(View.GONE);
                } else {
                    endTimeTextview.setVisibility(View.VISIBLE);
                    setEndTimeButton.setVisibility(View.VISIBLE);
                }
            }
        });

        addAttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddAttendeesButtonClicked();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmButtonClicked();
            }
        });

        event = null;
        id = null;
        String eventId = getIntent().getStringExtra("event");
        if (eventId != null && !eventId.isEmpty()) {
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
    }

    private void loadEventSettings() {
        titleTextInput.setText(event.getSubject());
        descriptionText.setText(event.getBody().getContent().split("<body>")[1].split("</body>")[0].replaceAll("<br>", "").substring(2));
        locationTextInput.setText(event.getLocation().getDisplayName());

        Recurrence recurrence = new RecurrenceFinder().findRecurrenceFromPatternedRecurrence(event);
        int index = 0;
        for (Recurrence recurrence1 : recurrenceMap.values()) {
            if (recurrence1 == recurrence) {
                break;
            }
            index++;
        }
        if (index >= recurrenceMap.size()) {
            index = 0;
        }
        recurrenceSpinner.setSelection(index);

        startTimeTextview.setText(event.getStart().getDateTime().format(dateTimeFormatter));
        if (event.isAllDay()) {
            isAllDayCheckBox.setChecked(event.isAllDay());
        } else {
            endTimeTextview.setText(event.getEnd().getDateTime().format(dateTimeFormatter));
        }
        if (event.getSensitivity().equals("private")) {
            isPrivateCheckBox.setChecked(true);
        }

        // TODO : agenda is fucked

        index = 0;
        for (ReminderMinutesBeforeStart r : reminderMap.values()) {
            if (r.getValue() == event.getReminderMinutesBeforeStart()) {
                break;
            }
            index ++;
        }
        if (index >= reminderMap.size()) {
            index = 0;
        }
        reminderSpinner.setSelection(index);

        index = 0;
        for (ShowAs s : showAsMap.values()) {
            if (s.action().equals(event.getShowAs())) {
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

    private PatternedRecurrence setRecurrence(Recurrence recurrence, LocalDate startDate) {
        PatternedRecurrence patternedRecurrence = new PatternedRecurrence();
        RecurrencePattern recurrencePattern = new RecurrencePattern();
        RecurrenceRange recurrenceRange = new RecurrenceRange();


        recurrenceRange.setType(RecurrenceRangeType.NOEND.value());
        recurrenceRange.setStartDate(startDate);
        recurrencePattern.setFirstDayOfWeek(DayOfWeek.SUNDAY.name());

        switch (recurrence) {
            case DAILY: {
                recurrencePattern.setInterval(1);
                recurrencePattern.setType(recurrence.action());
            }
            break;
            case EVERY_FRIDAY: {
                recurrencePattern.setType(RecurrencePatternType.WEEKLY.value());
                recurrencePattern.setDaysOfWeek(new String[] { DayOfWeek.FRIDAY.name() });
                recurrencePattern.setInterval(1);
            }
            break;
            case EVERY_WORKDAY: {
                recurrencePattern.setType(RecurrencePatternType.WEEKLY.value());
                recurrencePattern.setDaysOfWeek(new String[] { DayOfWeek.MONDAY.name(), DayOfWeek.TUESDAY.name(), DayOfWeek.WEDNESDAY.name(), DayOfWeek.THURSDAY.name(), DayOfWeek.FRIDAY.name() });
                recurrencePattern.setInterval(1);
            }
            break;
            case EACH_MONTH_TODAY: {
                recurrencePattern.setType(RecurrencePatternType.ABSOLUTEMONTHLY.value());
                recurrencePattern.setDayOfMonth(startDate.getDayOfMonth());
                recurrencePattern.setInterval(1);
            }
            break;
            case EVERY_SECOND_FRIDAY: {
                recurrencePattern.setType(RecurrencePatternType.WEEKLY.value());
                recurrencePattern.setDaysOfWeek(new String[] { DayOfWeek.FRIDAY.name() });
                recurrencePattern.setInterval(2);
            }
            break;
            case EVERY_YEAR: {
                recurrencePattern.setType(RecurrencePatternType.ABSOLUTEYEARLY.value());
                recurrencePattern.setMonth(startDate.getMonthValue());
                recurrencePattern.setDayOfMonth(startDate.getDayOfMonth());
                recurrencePattern.setInterval(1);
            }
            break;
            case MORE: {
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
        // TODO : add action for attendees
    }

    private void onConfirmButtonClicked() {
        Event event = new Event();
        event.setId(id);
        event.setSubject(titleTextInput.getText().toString());
        event.setBody(new Body(descriptionText.getText().toString().replaceAll("\\n", "<br/>"), "HTML"));
        event.setLocation(new Location(locationTextInput.getText().toString()));

        if (!recurrenceMap.get(recurrenceSpinner.getSelectedItemPosition()).equals(Recurrence.NEVER)) {
            event.setRecurrence(setRecurrence(recurrenceMap.get(recurrenceSpinner.getSelectedItemPosition()), LocalDate.of(startTime.getYear(), startTime.getMonthValue(), startTime.getDayOfMonth())));
        }

        if (isAllDayCheckBox.isChecked()) {
            event.setAllDay(isAllDayCheckBox.isChecked());
            LocalDateTime time = LocalDateTime.of(startTime.getYear(), startTime.getMonthValue(), startTime.getDayOfMonth(), 0, 0);
            event.setStart(new DateTimeTimeZone(time.toString(), TimeZone.getDefault().getDisplayName()));
            event.setEnd(new DateTimeTimeZone(time.plusDays(1).toString(), TimeZone.getDefault().getDisplayName()));
        } else {
            event.setStart(new DateTimeTimeZone(startTime.toString(), TimeZone.getDefault().getDisplayName()));
            event.setEnd(new DateTimeTimeZone(endTime.toString(), TimeZone.getDefault().getDisplayName()));
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

        String calendar = calendarMap.get(agendaSpinner.getSelectedItemPosition()).getId();

        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(event));
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
            startTime = LocalDateTime.of(yearPicked, monthPicked, dayOfMonthPicked, startTime.getHour(), startTime.getMinute());
        } else {
            endTime = LocalDateTime.of(yearPicked, monthPicked, dayOfMonthPicked, endTime.getHour(), endTime.getMinute());
        }

        if (!isAllDayCheckBox.isChecked()) {
            timePicker.show();
        }
    }

    private void onTimePicked(int hourPicked, int minutePicked) {
        if (isStartTime) {
            startTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(),hourPicked, minutePicked);
            startTimeTextview.setText(startTime.format(dateTimeFormatter));
        } else {
            endTime = LocalDateTime.of(endTime.getYear(), endTime.getMonth(), endTime.getDayOfMonth(), hourPicked, minutePicked);
            endTimeTextview.setText(endTime.format(dateTimeFormatter));
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
        if (requestCode == 200) {
            switch (resultCode) {
                case Activity.RESULT_OK: {
                    customRecurrence = new Gson().fromJson(data.getStringExtra("PatternedRecurrence"), PatternedRecurrence.class);
                }
                break;
                case Activity.RESULT_CANCELED: {

                }
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
