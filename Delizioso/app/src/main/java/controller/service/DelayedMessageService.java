package controller.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.bignerdranch.android.delizioso.R;
import com.bignerdranch.android.delizioso.view.activities.FeedbackActivity;


public class DelayedMessageService extends IntentService {
    public static final String EXTRA_MESSAGE = "message";
    public static final String TIME_OF_WAITING = "time";
    public static final int NOTIFICATION_ID = 5453;
    private static final String CHANNEL_ID = "123123";

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double time = intent.getDoubleExtra(TIME_OF_WAITING, 0);
        synchronized (this){
            try{
                wait((long)time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(final String text){
        createNotificationChannel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_original)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{0, 1000})
                .setAutoCancel(true);

        Intent actionIntent = new Intent(this, FeedbackActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                this,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        builder.setContentIntent(actionPendingIntent);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
