package android.ak.c196.notification;

import android.ak.c196.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

       String title = intent.getExtras().getString("title");
       String contentText = intent.getExtras().getString("contentText");
       long notificationId = intent.getExtras().getLong("notificationID");

        createNotification(context,title,contentText, notificationId);
    }

    public void createNotification(Context context, String title, String contentText, long notificationID){

        android.app.Notification.Builder builder = new android.app.Notification.Builder(context);

        android.app.Notification notification = builder.setContentTitle(title)
                .setContentText(contentText)
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.app_icon).build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify((int)notificationID, notification);

    }
}
