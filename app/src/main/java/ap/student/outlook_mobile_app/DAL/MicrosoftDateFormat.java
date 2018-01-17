package ap.student.outlook_mobile_app.DAL;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alek on 12.01.18.
 */

public class MicrosoftDateFormat {
    private SimpleDateFormat microsoftDateFormat;

    public MicrosoftDateFormat() {
        microsoftDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        microsoftDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public SimpleDateFormat getMicrosoftDateFormat() {
        return microsoftDateFormat;
    }
}
