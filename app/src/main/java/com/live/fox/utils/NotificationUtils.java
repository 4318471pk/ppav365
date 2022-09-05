package com.live.fox.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * 8.0以上的通知写法上有些变化, 此类兼容8.0以上的通知
 */
public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(int iconResource, String title, String content, PendingIntent pendingIntent) {
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(iconResource)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }


    public NotificationCompat.Builder getNotification_25(int iconResource, String title, String content, PendingIntent pendingIntent) {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(iconResource)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


    }

    public void sendNotification(int iconResource, String title, String content, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= 26) {   //大于Android 8.0后
            createNotificationChannel();
            Notification notification = getChannelNotification(iconResource, title, content, pendingIntent).build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(iconResource, title, content, pendingIntent).build();
            getManager().notify(1, notification);
        }
    }
}  