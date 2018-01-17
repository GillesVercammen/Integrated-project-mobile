package ap.student.outlook_mobile_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ap.student.outlook_mobile_app.mailing.activity.MailActivity;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "Over here!");
        System.out.println("Over here!");

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        System.out.println("From: " + remoteMessage.getFrom());

        System.out.println("Data is here: " + remoteMessage.getData());

        // prepare intent which is triggered if the
        // notification is selected

        Intent intent = new Intent(this, MailActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_outlook_icon)
                .setContentTitle("New mail from " + remoteMessage.getData().get("name"))
                .setContentText(remoteMessage.getData().get("subject"))
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pop))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        SharedPreferences sharedpreferences = getSharedPreferences("count", Context.MODE_PRIVATE);

        if (sharedpreferences.getInt("count", -1) == -1){
            ShortcutBadger.applyCount(this, 1);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("count", 1);
            editor.apply();
        }

        else {
            ShortcutBadger.applyCount(this, sharedpreferences.getInt("count", -1) + 1);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("count", sharedpreferences.getInt("count", -1) + 1);
            editor.apply();
        }

        notificationManager.notify(0, n);



    }

}
