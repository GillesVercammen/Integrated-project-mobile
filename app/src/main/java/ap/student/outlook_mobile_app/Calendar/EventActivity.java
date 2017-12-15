package ap.student.outlook_mobile_app.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import ap.student.outlook_mobile_app.DAL.models.DateTimeTimeZone;
import ap.student.outlook_mobile_app.DAL.models.Event;
import ap.student.outlook_mobile_app.DAL.models.Location;
import ap.student.outlook_mobile_app.DAL.models.PatternedRecurrence;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event);
        super.onCreate(savedInstanceState);

        showAsMap = new HashMap<>();
        recurrenceMap = new HashMap<>();
        reminderMap = new HashMap<>();

        setStartTimeButton = (Button) findViewById(R.id.eventSetStartTimeButton);
        setEndTimeButton = (Button) findViewById(R.id.evenSetEndTimeButton);
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
        startTime = LocalDateTime.parse(getIntent().getStringExtra("dateTime"));
        endTime = startTime.plusMinutes(30);

        startTimeTextview = (TextView) findViewById(R.id.eventStartDateText);
        startTimeTextview.setText(startTime.format(dateTimeFormatter));
        endTimeTextview = (TextView) findViewById(R.id.eventEndDateText);
        endTimeTextview.setText(endTime.format(dateTimeFormatter));

        timeZonePicker = (AutoCompleteTextView) findViewById(R.id.eventTimezoneAutocomplete);

        timeZonePicker.setText(TimeZone.getDefault().getDisplayName());

        populateShowAsSpinner();
        populateRecurrenceSpinner();
        populateReminderSpinner();

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
                if (recurrenceMap.get(position).action().equals(Recurrence.MORE.action())) {
                    onMoreSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmButtonClicked();
            }
        });
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
                // TODO : more...
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

    private void onConfirmButtonClicked() {
        Event event = new Event();
        event.setSubject(titleTextInput.getText().toString());
        event.setBody(new Body(descriptionText.getText().toString(), "HTML"));
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

        /*event.setAttendees(new Attendee[] {
                new Attendee("Required", new EmailAddress())
        });*/

        List<String[]> list = new ArrayList<>();
        //list.add(new String[] { "Prefer:", " Outlook.Timezone=".concat(TimeZone.getDefault().getDisplayName()) });
        list.add(new String[] {"Content-Type", "application/json"});
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(event));
            new GraphAPI().postRequest(OutlookObjectCall.POSTEVENT,this, jsonObject);
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
        int i = 0;
        for (ReminderMinutesBeforeStart value : ReminderMinutesBeforeStart.values()) {
            reminderMap.put(i, value);
            i++;
        }
        populateSpinner(R.array.reminderArray, reminderSpinner);
    }

    private void populateRecurrenceSpinner() {
        int i = 0;
        for (Recurrence value : Recurrence.values()) {
            recurrenceMap.put(i, value);
            i++;
        }
        populateSpinner(R.array.recurrenceArray, recurrenceSpinner);
    }

    private void populateShowAsSpinner() {
        int i = 0;
        for (ShowAs value : ShowAs.values()) {
            showAsMap.put(i, value);
            i++;
        }
        populateSpinner(R.array.eventShowAsArray, showAsSpinner);
    }

    private void populateSpinner(int arrayResource, Spinner spinner) {
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(arrayResource))));
    }

    @Override
    public void processResponse(OutlookObjectCall outlookObjectCall, JSONObject response) {
        this.finish();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
