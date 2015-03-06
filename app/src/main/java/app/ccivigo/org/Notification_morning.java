package app.ccivigo.org;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import 	android.app.Service;

public class Notification_morning extends Service {

    @Override
    public void onCreate() {
        Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
        Intent resultIntent = new Intent(this, Frag_Oracion.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        Notification noti_builder = new Notification.Builder(this)
                .setContentTitle("Don't forget to plan your activitites for the day! ")
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //what does this do!?
        noti_builder.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, noti_builder);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}