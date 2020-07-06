package com.call_bridging.calling;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handleSilentPush(remoteMessage);

    }

    private void handleSilentPush(RemoteMessage remoteMessage) {
        Log.d("Received pushed", "Received pushed");
        if (remoteMessage.getData().containsKey(OpenTokConfig.ConstantStrings.TYPE) &&
                remoteMessage.getData().get(OpenTokConfig.ConstantStrings.TYPE).equalsIgnoreCase(OpenTokConfig.ConstantStrings.VIDEO_CALL)) {
            OpenTokConfig.SESSION_ID = remoteMessage.getData().get(OpenTokConfig.ConstantStrings.SESSION_ID);
            OpenTokConfig.TOKEN = remoteMessage.getData().get(OpenTokConfig.ConstantStrings.TOKEN);
            Intent serviceIntent = new Intent(this, VideoCallForegroundService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
            return;
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("FIREBASE Token", "FIREBASE TOKEN - " + s);
    }
}
