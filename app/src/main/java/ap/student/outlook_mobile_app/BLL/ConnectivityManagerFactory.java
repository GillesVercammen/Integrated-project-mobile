package ap.student.outlook_mobile_app.BLL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by alek on 12/14/17.
 */

public class ConnectivityManagerFactory {
    private static ConnectivityManager connectivityManager = null;
    private static ConnectivityManagerFactory connectivityManagerFactory = null;
    private static NetworkInfo networkInfo;

    private ConnectivityManagerFactory() { }

    public static ConnectivityManagerFactory connectivityManager(Context context) {
        if (connectivityManagerFactory == null) {
            connectivityManagerFactory = new ConnectivityManagerFactory();
            connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        return connectivityManagerFactory;
    }

    public boolean isConnected() {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
