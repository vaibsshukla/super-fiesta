package com.call_bridging.calling;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.call_bridging.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class VideoCallingActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        WebServiceCoordinator.Listener,
        Session.SessionListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener, View.OnClickListener {

    private static final String LOG_TAG = VideoCallingActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    // Suppressing this warning. mWebServiceCoordinator will get GarbageCollected if it is local.
    @SuppressWarnings("FieldCanBeLocal")
    private WebServiceCoordinator mWebServiceCoordinator;

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private FloatingActionButton fabEndSession;

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
        setContentView(R.layout.activity_video_calling);

        // initialize view objects from your layout
        mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
        fabEndSession = findViewById(R.id.fab);

        fabEndSession.setOnClickListener(this);
        requestPermissions();
    }

    /* Activity lifecycle methods */

    @Override
    protected void onPause() {

        Log.d(LOG_TAG, "onPause");

        super.onPause();

        if (mSession != null) {
            mSession.onPause();
        }

    }

    @Override
    protected void onResume() {

        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setRationale(getString(R.string.rationale_ask_again))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(RC_SETTINGS_SCREEN_PERM)
                    .build()
                    .show();
        }
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {

        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // if there is no server URL set
            if (OpenTokConfig.CHAT_SERVER_URL == null) {
                // use hard coded session values
                if (OpenTokConfig.areHardCodedConfigsValid()) {
                    initializeSession(OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID, OpenTokConfig.TOKEN);
                } else {
                    showConfigError("Configuration Error", OpenTokConfig.hardCodedConfigErrorMessage);
                }
            } else {
                // otherwise initialize WebServiceCoordinator and kick off request for session data
                // session initialization occurs once data is returned, in onSessionConnectionDataReady
                if (OpenTokConfig.isWebServerConfigUrlValid()) {
                    mWebServiceCoordinator = new WebServiceCoordinator(this, this);
                    mWebServiceCoordinator.fetchSessionConnectionData(OpenTokConfig.SESSION_INFO_ENDPOINT);
                } else {
                    showConfigError("Configuration Error", OpenTokConfig.webServerConfigErrorMessage);
                }
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), RC_VIDEO_APP_PERM, perms);
        }
    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
        mSession.setSignalListener((session, signal, s1, connection) -> {
            if ("mentor_disconnect".equalsIgnoreCase(signal)) {
                if (mSession != null) {
                    Log.e("mentor_disconnect", "mentor_disconnect");
                    mSession.disconnect();
                    finish();
                }
            }
        });
    }

    /* Web Service Coordinator delegate methods */

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "ApiKey: " + apiKey + " SessionId: " + sessionId + " Token: " + token);
        initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {

        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
        Toast.makeText(this, "Web Service error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        finish();

    }

    /* Session Listener methods */

    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: " + session.getSessionId());

        // initialize Publisher and set this object to listen to Publisher events
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        // set publisher video style to fill view
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherViewContainer.addView(mPublisher.getView());
        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());

        showOpenTokError(opentokError);
    }

    /* Publisher Listener methods */

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream " + stream.getStreamId());

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream " + stream.getStreamId());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        if (mSession != null) {
            mSession.disconnect();
            finish();
        }
        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    private void showOpenTokError(OpentokError opentokError) {
        Toast.makeText(this, opentokError.getErrorDomain().name() + ": " + opentokError.getMessage() + " Please, see the logcat.", Toast.LENGTH_LONG).show();
        finish();
    }

    private void showConfigError(String alertTitle, final String errorMessage) {
        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(errorMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VideoCallingActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (mSession != null) {
                    mSession.disconnect();
                    finish();
                }
                break;
        }
    }
}
