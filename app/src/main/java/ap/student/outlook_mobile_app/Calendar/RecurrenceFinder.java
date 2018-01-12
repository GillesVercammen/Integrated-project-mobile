package ap.student.outlook_mobile_app.Calendar;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import ap.student.outlook_mobile_app.Calendar.CalendarElements.Recurrence;
import ap.student.outlook_mobile_app.DAL.enums.RecurrencePatternType;
import ap.student.outlook_mobile_app.DAL.models.Event;

/**
 * Created by alek on 12.01.18.
 */
public class RecurrenceFinder {
    Recurrence recurrence;

    public RecurrenceFinder () {
        recurrence = Recurrence.MORE;
    }

    public Recurrence findRecurrenceFromPatternedRecurrence(Event event) {
        // Time for horrible code
        RecurrencePatternType patternType = null;
        for (RecurrencePatternType recurrencePatternType : RecurrencePatternType.values()) {
            if (recurrencePatternType.value().equals(event.getRecurrence().getPattern().getType())) {
                patternType = recurrencePatternType;
                break;
            }
        }

        if (patternType == null) {
            recurrence = Recurrence.NEVER;
        }
        else {
            switch (patternType) {
                case DAILY: {
                    recurrence = Recurrence.DAILY;
                }
                break;
                case WEEKLY: {
                    List<String> daysofweek = Arrays.asList(event.getRecurrence().getPattern().getDaysOfWeek());
                    if (daysofweek.size() == 5) {
                        if (!daysofweek.contains(DayOfWeek.SATURDAY) && !daysofweek.contains(DayOfWeek.SUNDAY)) {
                            recurrence = Recurrence.EVERY_WORKDAY;
                        }
                    } else if (daysofweek.size() == 1) {
                        if (daysofweek.contains(DayOfWeek.FRIDAY)) {
                            if (event.getRecurrence().getPattern().getInterval() == 1) {
                                recurrence = Recurrence.EVERY_FRIDAY;
                            } else if (event.getRecurrence().getPattern().getInterval() == 2) {
                                recurrence = Recurrence.EVERY_SECOND_FRIDAY;
                            }
                        }
                    }
                }
                break;
                case ABSOLUTEMONTHLY: {
                    if (event.getRecurrence().getPattern().getDayOfMonth() == event.getStart().getDateTime().getDayOfMonth() && event.getRecurrence().getPattern().getInterval() == 1) {
                        recurrence = Recurrence.EACH_MONTH_TODAY;
                    }
                }
                break;
                case ABSOLUTEYEARLY: {
                    if (event.getRecurrence().getPattern().getInterval() == 1 && event.getRecurrence().getPattern().getDayOfMonth() == event.getStart().getDateTime().getDayOfMonth() && event.getRecurrence().getPattern().getMonth() == event.getStart().getDateTime().getMonthValue()) {
                        recurrence = Recurrence.EVERY_YEAR;
                    }
                }
                break;
            }
        }

        return recurrence;
    }
}
