package ap.student.outlook_mobile_app.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ap.student.outlook_mobile_app.Calendar.CalendarElements.CounterEnum;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.SpecifiedRecurrence;
import ap.student.outlook_mobile_app.R;

public class CustomRecurrenceActivity extends AppCompatActivity {
    private Spinner recurrenceSpinner;
    private Map<Integer, SpecifiedRecurrence> recurrenceMap;
    private LinearLayout recurrenceLayout;

    private Spinner counterSpinner;
    private Spinner daysOfTheWeekSpinner;
    private Map<Integer, CounterEnum> counterEnumMap;
    private Map<Integer, DayOfWeek> dayOfWeekMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recurrence);

        recurrenceMap = new HashMap<>();
        recurrenceSpinner = (Spinner) findViewById(R.id.specifiedRecurrencePatternSpinner);
        recurrenceLayout = (LinearLayout) findViewById(R.id.specifiedRecurrenceLayout);
        populateRecurrenceSpinner();
        recurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setInclude(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setInclude(int position) {
        recurrenceLayout.removeAllViews();
        SpecifiedRecurrence specifiedRecurrence = recurrenceMap.get(position);
        recurrenceLayout.addView(LayoutInflater.from(this.getBaseContext()).inflate(specifiedRecurrence.getValue(), recurrenceLayout, false));
        initializeLayout(specifiedRecurrence);
    }

    private void populateRecurrenceSpinner() {
        int i = 0;
        for (SpecifiedRecurrence recurrence : SpecifiedRecurrence.values()) {
            recurrenceMap.put(i, recurrence);
            i++;
        }
        recurrenceSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.specifiedRecurrenceArray))));
    }

    private void initializeLayout(SpecifiedRecurrence specifiedRecurrence) {
        switch (specifiedRecurrence) {
            case MONTHLY_WEEK: {
                counterSpinner =  (Spinner) findViewById(R.id.specifiedWeekCountSpinner);
                daysOfTheWeekSpinner = (Spinner) findViewById(R.id.specifiedDaySpinner);
                counterEnumMap = new HashMap<>();
                dayOfWeekMap = new HashMap<>();

                int i = 0;
                for (CounterEnum value : CounterEnum.values()) {
                    counterEnumMap.put(i, value);
                    i++;
                }
                i = 0;
                for (DayOfWeek day : DayOfWeek.values()) {
                    dayOfWeekMap.put(i, day);
                    i++;
                }
                counterSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.specifiedRecurrenceIndexArray))));
                daysOfTheWeekSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.DaysOfTheWeekArray))));
            }
        }
    }
}
