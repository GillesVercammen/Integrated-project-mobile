package ap.student.outlook_mobile_app.DAL.models;

import java.time.ZonedDateTime;

/**
 * Created by alek on 11/30/17.
 */

public class ResponseStatus {
    private String response;
    private String time;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ZonedDateTime getTime() {
        return ZonedDateTime.parse(time);
    }

    public void setTime(ZonedDateTime time) {
        this.time = time.toString();
    }
}
