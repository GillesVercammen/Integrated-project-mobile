package ap.student.outlook_mobile_app.Calendar;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ap.student.outlook_mobile_app.Calendar.CalendarElements.DaysOfTheWeekEnum;
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
        if (event.getRecurrence() == null) {
                return Recurrence.NEVER;
        }
        RecurrencePatternType patternType = null;

        for (RecurrencePatternType recurrencePatternType : RecurrencePatternType.values()) {
            if (recurrencePatternType.value().equals(event.getRecurrence().getPattern().getType())) {
                patternType = recurrencePatternType;
                break;
            }
        }

            switch (patternType) {
                case DAILY: {
                    recurrence = Recurrence.DAILY;
                }
                break;
                case WEEKLY: {
                    List<String> daysofweek = Arrays.asList(event.getRecurrence().getPattern().getDaysOfWeek());
                    if (daysofweek.size() == 5) {
                        if (!daysofweek.contains(DaysOfTheWeekEnum.SATURDAY) && !daysofweek.contains(DaysOfTheWeekEnum.SUNDAY)) {
                            recurrence = Recurrence.EVERY_WORKDAY;
                        }
                    } else if (daysofweek.size() == 1) {
                        if (daysofweek.contains(DaysOfTheWeekEnum.FRIDAY)) {
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
                    if (event.getRecurrence().getPattern().getDayOfMonth() == event.getStart().getDateTime().get(Calendar.DAY_OF_MONTH) && event.getRecurrence().getPattern().getInterval() == 1) {
                        recurrence = Recurrence.EACH_MONTH_TODAY;
                    }
                }
                break;
                case ABSOLUTEYEARLY: {
                    if (event.getRecurrence().getPattern().getInterval() == 1 && event.getRecurrence().getPattern().getDayOfMonth() == event.getStart().getDateTime().get(Calendar.DAY_OF_MONTH) && event.getRecurrence().getPattern().getMonth() == event.getStart().getDateTime().get(Calendar.MONTH)) {
                        recurrence = Recurrence.EVERY_YEAR;
                    }
                }
                break;
            }


        return recurrence;
    }
}
