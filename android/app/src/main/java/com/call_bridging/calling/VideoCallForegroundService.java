package com.call_bridging.calling;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.call_bridging.R;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class VideoCallForegroundService extends Service {
    public static final String CHANNEL_ID = "BasicFirst";
    public static final int NOTIFICATION_ID = 1;
    public static String ACTION_STOP_SERVICE = "Stop";
    public static String ACTION_STOP_SERVICE_DECLINE = "Stop_decline";
    public static long RINGTIME = 20000;
    Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            Objects.requireNonNull(notificationManager).cancel(NOTIFICATION_ID);
            Objects.requireNonNull(v).cancel();
            handler.removeCallbacksAndMessages(null);
            stopSelf();
            return START_NOT_STICKY;
        }
        if (ACTION_STOP_SERVICE_DECLINE.equals(intent.getAction())) {
            Objects.requireNonNull(notificationManager).cancel(NOTIFICATION_ID);
            Objects.requireNonNull(v).cancel();
            stopSelf();
            return START_NOT_STICKY;
        }

        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);
        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
        long[] mVibratePattern = new long[]{0, 400, 800, 600, 800, 800, 800, 1000, 400, 800, 600,
                800, 800, 800, 1000, 400, 800, 800, 600, 800, 800, 800, 1000, 400, 800};

        Intent leftIntent = new Intent(this, VideoNotificationIntentService.class);
        leftIntent.setAction(OpenTokConfig.ConstantStrings.DECLINE);
        expandedView.setOnClickPendingIntent(R.id.btnDecline, PendingIntent.getService(this, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        Intent rightIntent = new Intent(this, VideoNotificationIntentService.class);
        rightIntent.setAction(OpenTokConfig.ConstantStrings.ANSWER);
        expandedView.setOnClickPendingIntent(R.id.btnAnswer, PendingIntent.getService(this, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        Intent fullScreenIntent = new Intent(getBaseContext(), VideoCallAnswerActivity.class);
        fullScreenIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setCustomContentView(collapsedView)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setLights(Color.RED, 3000, 3000)
                .setSound(alarmSound)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setCustomBigContentView(expandedView);
        startForeground(NOTIFICATION_ID, notificationBuilder.build());
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Mycall" + System.currentTimeMillis());
        wl.acquire(15000);

        new Handler().postDelayed(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createWaveform(mVibratePattern, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(RINGTIME);
            }
        }, 1500);

        handler.postDelayed(() -> {
            Objects.requireNonNull(notificationManager).cancel(NOTIFICATION_ID);
            Objects.requireNonNull(v).cancel();
            stopSelf();

            Intent intent1 = new Intent(getBaseContext(), VideoNotificationIntentService.class);
            intent1.setAction(OpenTokConfig.ConstantStrings.NOT_ANSWERED);
            startService(intent1);
        }, RINGTIME);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}