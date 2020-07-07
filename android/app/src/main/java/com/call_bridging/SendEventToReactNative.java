package com.call_bridging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.call_bridging.calling.OpenTokConfig;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

    public class SendEventToReactNative extends ReactContextBaseJavaModule  {
    private ReactContext mReactContext;

    public SendEventToReactNative(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(reactContext);
        localBroadcastManager.registerReceiver(mReceiver, new IntentFilter(OpenTokConfig.ConstantStrings.CALLING_EVENT));
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String someData = intent.getStringExtra(OpenTokConfig.ConstantStrings.CALL_STATUS);
                if(someData!= null)
                    mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("JS-Event", someData);
            }
        };


    @NonNull
    @Override
    public String getName() {
        return "SendEventToReactNative";
    }

}
