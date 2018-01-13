package ap.student.outlook_mobile_app.DAL;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by alek on 12.01.18.
 */

public class MicrosoftDateFormat {
    public SimpleDateFormat getMicrosoftDateFormat() {
        return microsoftDateFormat;
    }

    private SimpleDateFormat microsoftDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
}
