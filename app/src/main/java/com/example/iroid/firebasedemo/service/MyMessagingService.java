package com.example.iroid.firebasedemo.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.iroid.firebasedemo.R;
import com.example.iroid.firebasedemo.activities.ServiceProvider;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody()
                        ,remoteMessage.getNotification().getClickAction());
    }

    private void showNotification(String title,String message,String action){


        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"pushNotifications")
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(message);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(11,builder.build());
    }


}
