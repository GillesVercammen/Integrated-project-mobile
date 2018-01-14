package ap.student.outlook_mobile_app.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ap.student.outlook_mobile_app.Calendar.CalendarElements.CounterEnum;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.DaysOfWeekEnum;
import ap.student.outlook_mobile_app.Calendar.CalendarElements.SpecifiedRecurrence;
import ap.student.outlook_mobile_app.DAL.enums.RecurrencePatternType;
import ap.student.outlook_mobile_app.DAL.enums.RecurrenceRangeType;
import ap.student.outlook_mobile_app.DAL.models.PatternedRecurrence;
import ap.student.outlook_mobile_app.DAL.models.RecurrencePattern;
import ap.student.outlook_mobile_app.DAL.models.RecurrenceRange;
import ap.student.outlook_mobile_app.R;

public class CustomRecurrenceActivity extends AppCompatActivity {
    private ConstraintLayout specifiedDaily;
    private ConstraintLayout specifiedWeekly;
    private ConstraintLayout specifiedMonthlyDay;
    private ConstraintLayout specifiedMonthlyWeek;
    private ConstraintLayout specifiedYearlyDay;
    private ConstraintLayout specifiedYearlyWeek;

    private Spinner recurrenceSpinner;
    private Map<Integer, SpecifiedRecurrence> recurrenceMap;
    private Button okButton;
    private Button cancelButton;

    private Spinner counterSpinner;
    private Spinner daysOfTheWeekSpinner;
    private Spinner monthsSpinner;
    private Spinner yearlyWeekCounterSpinner;
    private Spinner yearlyWeekDaysOfTheWeekSpinner;
    private Spinner yearlyWeekMonthsSpinner;
    private Map<Integer, CounterEnum> counterEnumMap;
    private Map<Integer, DaysOfWeekEnum> dayOfWeekMap;
    private Map<Integer, Month> monthMap;

    private TextView specifiedDayInterval;
    private CheckBox mondayCheckbox;
    private CheckBox tuesdayCheckbox;
    private CheckBox wednesdayCheckbox;
    private CheckBox thursdayCheckbox;
    private CheckBox fridayCheckbox;
    private CheckBox saturdayCheckbox;
    private CheckBox sundayCheckbox;
    private TextView specifiedWeekInterval;
    private TextView specifiedMonthlyDayInterval;
    private TextView specifiedMonthlyDayInput;
    private TextView specifiedMonthlyInterval;
    private TextView specifiedYearlyDayDayInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recurrence);

        specifiedDaily = (ConstraintLayout) findViewById(R.id.specified_daily);
        specifiedWeekly = (ConstraintLayout) findViewById(R.id.specified_weekly);
        specifiedMonthlyDay = (ConstraintLayout) findViewById(R.id.specified_monthly_day);
        specifiedMonthlyWeek = (ConstraintLayout) findViewById(R.id.specified_monthly_week);
        specifiedYearlyDay = (ConstraintLayout) findViewById(R.id.specified_yearly_day);
        specifiedYearlyWeek = (ConstraintLayout) findViewById(R.id.specified_yearly_week);

        recurrenceMap = new HashMap<>();
        recurrenceSpinner = (Spinner) findViewById(R.id.specifiedRecurrencePatternSpinner);
        okButton = (Button) findViewById(R.id.specifiedRecurrenceOkButton);
        cancelButton = (Button) findViewById(R.id.specifiedRecurrenceCancelButton);

        counterSpinner =  (Spinner) findViewById(R.id.specifiedWeekCountSpinner);
        daysOfTheWeekSpinner = (Spinner) findViewById(R.id.specifiedDaySpinner);
        monthsSpinner = (Spinner) findViewById(R.id.specifiedYearlyMonthSpinner);
        yearlyWeekCounterSpinner = (Spinner) findViewById(R.id.specifiedYearlyWeekintervalSpinner);
        yearlyWeekDaysOfTheWeekSpinner = (Spinner) findViewById(R.id.specifiedYearlyWeekDaySpinner);
        yearlyWeekMonthsSpinner = (Spinner) findViewById(R.id.specifiedYearlyWeekMonthSpinner);
        counterEnumMap = new HashMap<>();
        dayOfWeekMap = new HashMap<>();
        monthMap = new HashMap<>();

        specifiedDayInterval = (TextView) findViewById(R.id.specifiedDailyNumber);
        mondayCheckbox = (CheckBox) findViewById(R.id.checkBoxMonday);
        tuesdayCheckbox = (CheckBox) findViewById(R.id.checkBoxTuesday);
        wednesdayCheckbox = (CheckBox) findViewById(R.id.checkBoxWednesday);
        thursdayCheckbox = (CheckBox) findViewById(R.id.checkBoxThursday);
        fridayCheckbox = (CheckBox) findViewById(R.id.checkBoxFriday);
        saturdayCheckbox = (CheckBox) findViewById(R.id.checkBoxSaturday);
        sundayCheckbox = (CheckBox) findViewById(R.id.checkBoxSunday);
        specifiedWeekInterval = (TextView) findViewById(R.id.specifiedWeeklyNumber);
        specifiedMonthlyDayInput = (TextView) findViewById(R.id.specifiedMonthlyDayDayInput);
        specifiedMonthlyDayInterval = (TextView) findViewById(R.id.specifiedMonthlyIntervalTextinput);
        specifiedMonthlyInterval = (TextView) findViewById(R.id.specifiedMonthlyInterval);
        specifiedYearlyDayDayInput = (TextView) findViewById(R.id.specifiedYearlyDayTextinput);


        populateRecurrenceSpinner();
        initializeLayout();
        recurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setInclude(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClicked();
            }
        });
    }

    private void onOkClicked() {
        setResult(Activity.RESULT_OK, new Intent().putExtra("PatternedRecurrence", new Gson().toJson(createRecurrence())));
        this.finish();
    }

    private void cancel() {
        setResult(Activity.RESULT_CANCELED);
        this.finish();
    }

    private void setInclude(int position) {
        specifiedMonthlyWeek.setVisibility(View.GONE);
        specifiedMonthlyDay.setVisibility(View.GONE);
        specifiedWeekly.setVisibility(View.GONE);
        specifiedDaily.setVisibility(View.GONE);
        specifiedYearlyDay.setVisibility(View.GONE);
        specifiedYearlyWeek.setVisibility(View.GONE);

        SpecifiedRecurrence specifiedRecurrence = recurrenceMap.get(position);
        findViewById(specifiedRecurrence.getValue()).setVisibility(View.VISIBLE);
    }

    private void populateRecurrenceSpinner() {
        int i = 0;
        for (SpecifiedRecurrence recurrence : SpecifiedRecurrence.values()) {
            recurrenceMap.put(i, recurrence);
            i++;
        }
        recurrenceSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.specifiedRecurrenceArray))));
    }

    private void initializeLayout() {
        for (CounterEnum value : CounterEnum.values()) {
            counterEnumMap.put(counterEnumMap.size(), value);
        }
        ArrayAdapter<String> counterAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.specifiedRecurrenceIndexArray)));
        counterSpinner.setAdapter(counterAdapter);
        yearlyWeekCounterSpinner.setAdapter(counterAdapter);

        for (DaysOfWeekEnum day : DaysOfWeekEnum.values()) {
            dayOfWeekMap.put(dayOfWeekMap.size(), day);
        }
        ArrayAdapter<String> weekDayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.DaysOfTheWeekArray)));
        daysOfTheWeekSpinner.setAdapter(weekDayAdapter);
        yearlyWeekDaysOfTheWeekSpinner.setAdapter(weekDayAdapter);

        for (Month month : Month.values()) {
            monthMap.put(monthMap.size(), month);
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.MonthsInTheYearArray)));
        monthsSpinner.setAdapter(monthAdapter);
        yearlyWeekMonthsSpinner.setAdapter(monthAdapter);
    }

    private PatternedRecurrence createRecurrence() {
        PatternedRecurrence patternedRecurrence = new PatternedRecurrence();
        RecurrencePattern recurrencePattern = new RecurrencePattern();
        RecurrenceRange recurrenceRange = new RecurrenceRange();

        recurrenceRange.setType(RecurrenceRangeType.NOEND.value());
        recurrenceRange.setStartDate(Calendar.getInstance());
        recurrencePattern.setFirstDayOfWeek(DaysOfWeekEnum.SUNDAY.name());

        SpecifiedRecurrence recurrence = recurrenceMap.get(recurrenceSpinner.getSelectedItemPosition());

        switch (recurrence) {
            case DAILY: {
                recurrencePattern.setInterval(Integer.parseInt(specifiedDayInterval.getText().toString()));
                recurrencePattern.setType(RecurrencePatternType.DAILY.value());
            }
            break;
            case WEEKLY: {
                List<String> daysOfTheWeek = new ArrayList<>();
                if (mondayCheckbox.isChecked()) daysOfTheWeek.add(DaysOfWeekEnum.MONDAY.name());
                if (tuesdayCheckbox.isChecked()) daysOfTheWeek.add(DaysOfWeekEnum.TUESDAY.name());
                if (wednesdayCheckbox.isChecked()) daysOfTheWeek.add(DaysOfWeekEnum.WEDNESDAY.name());
                if (thursdayCheckbox.isChecked()) daysOfTheWeek.add(DaysOfWeekEnum.THURSDAY.name());
                if (fridayCheckbox.isChecked()) daysOfTheWeek.add(DaysOfWeekEnum.FRIDAY.name());
                if (saturdayCheckbox.isChecked()) daysOfTheWeek.add(DaysOfWeekEnum.SATURDAY.name());
                if (sundayCheckbox.isChecked()) daysOfTheWeek.add(DaysOfWeekEnum.SUNDAY.name());

                recurrencePattern.setDaysOfWeek(daysOfTheWeek.toArray(new String[]{}));
                recurrencePattern.setInterval(Integer.parseInt(specifiedWeekInterval.getText().toString()));
                recurrencePattern.setType(RecurrencePatternType.WEEKLY.value());
            }
            break;
            case MONTHLY_DAY: {
                recurrencePattern.setType(RecurrencePatternType.ABSOLUTEMONTHLY.value());
                recurrencePattern.setDayOfMonth(Integer.parseInt(specifiedMonthlyDayInput.getText().toString()));
                recurrencePattern.setInterval(Integer.parseInt(specifiedMonthlyDayInterval.getText().toString()));
            }
            break;
            case MONTHLY_WEEK: {
                recurrencePattern.setType(RecurrencePatternType.RELATIVEMONTHLY.value());
                recurrencePattern.setInterval(Integer.parseInt(specifiedMonthlyInterval.getText().toString()));
                recurrencePattern.setIndex(counterEnumMap.get(counterSpinner.getSelectedItemPosition()).value());
                recurrencePattern.setDaysOfWeek(new String[]{ dayOfWeekMap.get(daysOfTheWeekSpinner.getSelectedItemPosition()).name() });
            }
            break;
            case YEARLY_DAY: {
                recurrencePattern.setType(RecurrencePatternType.ABSOLUTEYEARLY.value());
                recurrencePattern.setMonth(monthMap.get(monthsSpinner.getSelectedItemPosition()).getValue());
                recurrencePattern.setDayOfMonth(Integer.parseInt(specifiedYearlyDayDayInput.getText().toString()));
                recurrencePattern.setInterval(1);
            }
            break;
            case YEARLY_WEEK: {
                recurrencePattern.setType(RecurrencePatternType.RELATIVEYEARLY.value());
                recurrencePattern.setIndex(counterEnumMap.get(yearlyWeekCounterSpinner.getSelectedItemPosition()).value());
                recurrencePattern.setDaysOfWeek(new String[] { dayOfWeekMap.get(yearlyWeekDaysOfTheWeekSpinner.getSelectedItemPosition()).name() });
                recurrencePattern.setMonth(monthMap.get(yearlyWeekMonthsSpinner.getSelectedItemPosition()).getValue());
                recurrencePattern.setInterval(1);
            }
            break;
            default: {
                throw new NullPointerException("This method requires a PatternedRecurrenceType");
            }
        }
        patternedRecurrence.setRange(recurrenceRange);
        patternedRecurrence.setPattern(recurrencePattern);

        return patternedRecurrence;
    }
}
