package com.shqiperon.profitness;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews collapsedView = new RemoteViews(context.getPackageName(), R.layout.notification_collapsed);

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.softbeep);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "pfNotification")
                .setSmallIcon(R.drawable.ic_fitness_notification)
                .setContentTitle("Haven't seen you in a while.")
                .setContentText("Come back now and start your healthy lifestyle")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomContentView(collapsedView)
                .setSound(soundUri)
                //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setAutoCancel(true);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, SplashScreenActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();

        long[] vibrate = { 0, 100, 200, 300 };
        notification.vibrate = vibrate;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }
}
