package com.simplecity.amp_library.playback; // NOSONAR

import android.os.Handler; // NOSONAR
import android.os.Message; // NOSONAR
import java.lang.ref.WeakReference; // NOSONAR

/** // NOSONAR
 * A handler to allow the notification to be stopped on a delay. If a subsequent startNotification comes in, // NOSONAR
 * the stopNotification call can be cancelled. Prevents the notification from momentarily disappearing on track change. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class NotificationStateHandler extends Handler { //NOSONAR

    static final int STOP_FOREGROUND = 0; //NOSONAR
    static final int START_FOREGROUND = 1; //NOSONAR

    private final WeakReference<MusicService> mService; //NOSONAR

    NotificationStateHandler(MusicService musicService) { //NOSONAR
        mService = new WeakReference<>(musicService); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void handleMessage(Message msg) { //NOSONAR
        final MusicService service = mService.get(); //NOSONAR
        if (service == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        switch (msg.what) { //NOSONAR
            case START_FOREGROUND: //NOSONAR
                //The foreground notification has been started. Don't allow any previously delayed stop_foreground messages to be fired // NOSONAR
                removeMessages(NotificationStateHandler.STOP_FOREGROUND); //NOSONAR
                break; //NOSONAR
            case STOP_FOREGROUND: //NOSONAR
                //Stop the foreground notification. // NOSONAR
                service.stopForegroundImpl(false, false); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
