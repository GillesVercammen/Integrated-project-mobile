package ap.student.outlook_mobile_app.DAL.models;

import java.time.LocalDateTime;

/**
 * Created by alek on 11/30/17.
 */

public class ResponseStatus {
    private String response;
    private LocalDateTime time;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
