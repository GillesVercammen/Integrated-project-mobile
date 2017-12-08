package ap.student.outlook_mobile_app.Calendar;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import ap.student.outlook_mobile_app.Calendar.CalendarElements.Recurrence;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.ReminderMinutesBeforeStart;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.ShowAs;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;
import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.R;

public class EventActivity extends AppCompatActivityRest {
    private LocalDateTime eventTime;
    private TextView startTimeTextview;
    private TextView endTimeTextview;
    private DateTimeFormatter dateTimeFormatter;
    private AutoCompleteTextView timeZonePicker;
    private Spinner showAsSpinner;
    private Spinner recurrenceSpinner;
    private Spinner reminderSpinner;
    private Button setStartTimeButton;
    private Map<Integer, ShowAs> showAsMap;
    private Map<Integer, Recurrence> recurrenceMap;
    private Map<Integer, ReminderMinutesBeforeStart> reminderMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event);
        super.onCreate(savedInstanceState);

        showAsMap = new HashMap<>();
        recurrenceMap = new HashMap<>();
        reminderMap = new HashMap<>();

        setStartTimeButton = (Button) findViewById(R.id.eventSetStartTimeButton);
        showAsSpinner = (Spinner) findViewById(R.id.eventDisplayAsSpinner);
        recurrenceSpinner = (Spinner) findViewById(R.id.eventRepeatSpinner);
        reminderSpinner = (Spinner) findViewById(R.id.eventRemindSpinner);
        dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-mm-dd HH:mm");
        eventTime = LocalDateTime.parse(getIntent().getStringExtra("dateTime"));

        startTimeTextview = (TextView) findViewById(R.id.eventStartDateText);
        startTimeTextview.setText(eventTime.format(dateTimeFormatter));
        endTimeTextview = (TextView) findViewById(R.id.eventEndDateText);
        endTimeTextview.setText(eventTime.plusMinutes(30).format(dateTimeFormatter));

        timeZonePicker = (AutoCompleteTextView) findViewById(R.id.eventTimezoneAutocomplete);

        timeZonePicker.setText(TimeZone.getDefault().getDisplayName());

        populateShowAsSpinner();
        populateRecurrenceSpinner();
        populateReminderSpinner();

        setStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(showAsSpinner.getSelectedItemPosition());
                System.out.println(showAsMap.get(showAsSpinner.getSelectedItemPosition()).toString());
            }
        });

        recurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (recurrenceMap.get(position).action().equals(Recurrence.MORE.action())) {
                    // meer
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
