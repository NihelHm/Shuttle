package com.simplecity.amp_library.playback;

import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

/**
 * A handler to allow the notification to be stopped on a delay. If a subsequent startNotification comes in,
 * the stopNotification call can be cancelled. Prevents the notification from momentarily disappearing on track change.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
class NotificationStateHandler extends Handler {

    static final int STOP_FOREGROUND = 0;
    static final int START_FOREGROUND = 1;

    private final WeakReference<MusicService> mService;

    NotificationStateHandler(MusicService musicService) {
        mService = new WeakReference<>(musicService);
    }

    @Override
    public void handleMessage(Message msg) {
        final MusicService service = mService.get();
        if (service == null) {
            return;
        }

        switch (msg.what) {
            case START_FOREGROUND:
                //The foreground notification has been started. Don't allow any previously delayed stop_foreground messages to be fired
                removeMessages(NotificationStateHandler.STOP_FOREGROUND);
                break;
            case STOP_FOREGROUND:
                //Stop the foreground notification.
                service.stopForegroundImpl(false, false);
                break;
        }
    }
}
