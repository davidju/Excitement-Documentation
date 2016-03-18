package com.example.judav_000.pgr02excitementdocumentation;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/* Mobile class that listens for message from wearable indicating excitement. */
public class ListenerServiceFromWear extends WearableListenerService {

    private String messagePath1 = "excite_level_one";
    private String messagePath2 = "excite_level_two";

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        // Create wearable notification for excitement.
        if (messageEvent.getPath().equals(messagePath1) || messageEvent.getPath().equals(messagePath2)) {

            final Intent intent = new Intent(this, Camera.class);
            intent.addFlags(Notification.FLAG_AUTO_CANCEL);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            // Choose message to display based on excitement level.
            String contentTitle = "", contentText = "";
            if (messageEvent.getPath().equals(messagePath1)) {
                contentTitle = "Hey there...";
                contentText = "You seem excited!";
            } else {
                contentTitle = "Woah there!";
                contentText = "How are you so hyper!";
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ListenerServiceFromWear.this)
                    .setSmallIcon(R.drawable.cast_ic_notification_2)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .addAction(R.drawable.camera, "Take picture?", pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.joy_excite));
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ListenerServiceFromWear.this);
            // Giving notification a unique ID so it can be dismissed when action in pending intent is complete.
            notificationManager.notify(100, notificationBuilder.build());

        }
    }
}
