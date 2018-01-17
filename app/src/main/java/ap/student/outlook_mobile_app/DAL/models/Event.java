package ap.student.outlook_mobile_app.DAL.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

import ap.student.outlook_mobile_app.DAL.MicrosoftDateFormat;

/**
 * Created by alek on 11/30/17.
 */
public class Event {
    private Event[] value;
    private Attendee[] attendees;
    private Body body;
    private String bodyPreview;
    private String[] categories;
    private String changeKey;
    private String createdDateTime;
    private DateTimeTimeZone end;
    private boolean hasAttachments;
    private String iCalUId;
    private String id;
    private String importance;
    private boolean isAllDay;
    private boolean isCancelled;
    private boolean isOrganizer;
    private boolean isReminderOn;
    private String lastModifiedDateTime;
    private String onlineMeetingUrl;
    private Recipient organizer;
    private String originalEndTimeZone;
    private String dateTimeOffset;
    private String originalStartTimeZone;
    private PatternedRecurrence recurrence;
    private int reminderMinutesBeforeStart;
    private boolean responseRequested;
    private ResponseStatus responseStatus;
    private String sensitivity;
    private String seriesMasterId;
    private String showAs;
    private DateTimeTimeZone start;
    private String subject;
    private String type;
    private String webLink;
    private Location location;

    private transient SimpleDateFormat microsoftDateFormat;

    public Event() {
        microsoftDateFormat = new MicrosoftDateFormat().getMicrosoftDateFormat();
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public void setDateTimeOffset(String dateTimeOffset) {
        this.dateTimeOffset = dateTimeOffset;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Event[] getEvents() {
        return value;
    }

    public void setValue(Event[] events) {
        this.value = events;
    }

    public void setEvents(Event[] events) {
        this.value = events;
    }

    public Attendee[] getAttendees() {
        return attendees;
    }

    public void setAttendees(Attendee[] attendees) {
        this.attendees = attendees;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    /*public ZonedDateTime getCreatedDateTime() {
        return ZonedDateTime.parse(createdDateTime);
    }*/

    public Date getCreatedDateTime() {
        Date date = new Date();
        try {
            date = microsoftDateFormat.parse(createdDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime.toString();
    }

    public DateTimeTimeZone getEnd() {
        return end;
    }

    public void setEnd(DateTimeTimeZone end) {
        this.end = end;
    }

    public boolean isHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public String getiCalUId() {
        return iCalUId;
    }

    public void setiCalUId(String iCalUId) {
        this.iCalUId = iCalUId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    public boolean isReminderOn() {
        return isReminderOn;
    }

    public void setReminderOn(boolean reminderOn) {
        isReminderOn = reminderOn;
    }

    public Date getLastModifiedDateTime() {
        Date date = new Date();
        try {
            date = microsoftDateFormat.parse(createdDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setLastModifiedDateTime(ZonedDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime.toString();
    }

    public String getOnlineMeetingUrl() {
        return onlineMeetingUrl;
    }

    public void setOnlineMeetingUrl(String onlineMeetingUrl) {
        this.onlineMeetingUrl = onlineMeetingUrl;
    }

    public Recipient getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Recipient organizer) {
        this.organizer = organizer;
    }

    public String getOriginalEndTimeZone() {
        return originalEndTimeZone;
    }

    public void setOriginalEndTimeZone(String originalEndTimeZone) {
        this.originalEndTimeZone = originalEndTimeZone;
    }

    public Date getDateTimeOffset() {
        Date date = new Date();
        try {
            date = microsoftDateFormat.parse(dateTimeOffset);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setDateTimeOffset(ZonedDateTime dateTimeOffset) {
        this.dateTimeOffset = dateTimeOffset.toString();
    }

    public String getOriginalStartTimeZone() {
        return originalStartTimeZone;
    }

    public void setOriginalStartTimeZone(String originalStartTimeZone) {
        this.originalStartTimeZone = originalStartTimeZone;
    }

    public PatternedRecurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(PatternedRecurrence patternedRecurrence) {
        this.recurrence = patternedRecurrence;
    }

    public int getReminderMinutesBeforeStart() {
        return reminderMinutesBeforeStart;
    }

    public void setReminderMinutesBeforeStart(int reminderMinutesBeforeStart) {
        this.reminderMinutesBeforeStart = reminderMinutesBeforeStart;
    }

    public boolean isResponseRequested() {
        return responseRequested;
    }

    public void setResponseRequested(boolean responseRequested) {
        this.responseRequested = responseRequested;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getSeriesMasterId() {
        return seriesMasterId;
    }

    public void setSeriesMasterId(String seriesMasterId) {
        this.seriesMasterId = seriesMasterId;
    }

    public String getShowAs() {
        return showAs;
    }

    public void setShowAs(String showAs) {
        this.showAs = showAs;
    }

    public DateTimeTimeZone getStart() {
        return start;
    }

    public void setStart(DateTimeTimeZone start) {
        this.start = start;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
