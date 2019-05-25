package com.appincubator.digitaldarji.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import com.appincubator.digitaldarji.Activity.MainActivity;
import com.appincubator.digitaldarji.R;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        int NOTIFICATION_ID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "digital_darji_1";
        CharSequence name = "digital_darji";
        String Description = "Digital Darji";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(false);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{0,400,200,400});

            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.nnnn)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setVibrate(new long[]{0,400,200,400})
                .setContentTitle("Digital Darji")
                .setContentText(remoteMessage.getData().get("text"));

        Intent resultIntent = new Intent(NotificationService.this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
