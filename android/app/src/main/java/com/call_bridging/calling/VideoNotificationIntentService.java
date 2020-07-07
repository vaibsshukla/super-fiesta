package com.call_bridging.calling;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.call_bridging.MainApplication;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class VideoNotificationIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked bSey your subclass's constructor.
     */
    public VideoNotificationIntentService() {
        super("VideoNotificationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Handler rightHandler = new Handler(Looper.getMainLooper());
        rightHandler.post(() -> {
            switch (intent.getAction()) {
                case OpenTokConfig.ConstantStrings.CANCEL:
                    int notificationId = intent.getIntExtra("id", 101);
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationId);
                    break;
                case OpenTokConfig.ConstantStrings.IS_DROPPED:
                    Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    getBaseContext().sendBroadcast(closeIntent);
                    if (isServiceRunningInForeground(getApplicationContext(), VideoCallForegroundService.class)) {
                        Intent serviceIntent = new Intent(getBaseContext(), VideoCallForegroundService.class);
                        serviceIntent.setAction(OpenTokConfig.ConstantStrings.STOP_DECLINE);
                        ContextCompat.startForegroundService(getBaseContext(), serviceIntent);
                    }
                    Intent intent3 = new Intent(OpenTokConfig.ConstantStrings.IS_DECLINED);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent3);
                    break;
                case OpenTokConfig.ConstantStrings.DECLINE:
                    Toast.makeText(getBaseContext(), "Decline", Toast.LENGTH_LONG).show();
                    closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    getBaseContext().sendBroadcast(closeIntent);
                    Intent serviceIntent = new Intent(getBaseContext(), VideoCallForegroundService.class);
                    serviceIntent.setAction(OpenTokConfig.ConstantStrings.STOP_DECLINE);
                    ContextCompat.startForegroundService(getBaseContext(), serviceIntent);
                    sendEvent(OpenTokConfig.ConstantStrings.DECLINE);
                    break;
                case OpenTokConfig.ConstantStrings.ANSWER:
                    Intent intent1 = new Intent(getBaseContext(), VideoCallingActivity.class);
                    intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(intent1);

                    closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    getBaseContext().sendBroadcast(closeIntent);
                    serviceIntent = new Intent(getBaseContext(), VideoCallForegroundService.class);
                    serviceIntent.setAction(OpenTokConfig.ConstantStrings.STOP);
                    ContextCompat.startForegroundService(getBaseContext(), serviceIntent);
                    sendEvent(OpenTokConfig.ConstantStrings.ANSWER);
                    break;
                case OpenTokConfig.ConstantStrings.NOT_ANSWERED:

                    break;
            }
        });
    }

    public void sendEvent(String eventName) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent customEvent = new Intent(OpenTokConfig.ConstantStrings.CALLING_EVENT);
        customEvent.putExtra(OpenTokConfig.ConstantStrings.NOT_ANSWERED, eventName);
        localBroadcastManager.sendBroadcast(customEvent);
    }

    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
}