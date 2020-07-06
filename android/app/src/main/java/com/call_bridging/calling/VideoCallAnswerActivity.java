package com.call_bridging.calling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.call_bridging.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.call_bridging.calling.VideoCallForegroundService.RINGTIME;


public class VideoCallAnswerActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btnDecline, btnAnswer;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_answering);
        btnDecline = findViewById(R.id.btnDecline);
        btnAnswer = findViewById(R.id.btnAnswer);
        btnAnswer.setOnClickListener(this);
        btnDecline.setOnClickListener(this);
        LocalBroadcastManager.getInstance(VideoCallAnswerActivity.this).registerReceiver(mMessageReceiver,
                new IntentFilter(OpenTokConfig.ConstantStrings.IS_DECLINED));
        handler.postDelayed(() -> {
            finish();
        }, RINGTIME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDecline:
                Intent leftIntent = new Intent(this, VideoNotificationIntentService.class);
                leftIntent.setAction(OpenTokConfig.ConstantStrings.DECLINE);
                startService(leftIntent);
                finish();
                break;
            case R.id.btnAnswer:
                Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                getBaseContext().sendBroadcast(closeIntent);
                Intent serviceIntent = new Intent(getBaseContext(), VideoCallForegroundService.class);
                serviceIntent.setAction(OpenTokConfig.ConstantStrings.STOP);
                ContextCompat.startForegroundService(getBaseContext(), serviceIntent);

                Intent intent1 = new Intent(getBaseContext(), VideoCallingActivity.class);
                intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent1);
                finish();
                break;
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(VideoCallAnswerActivity.this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
