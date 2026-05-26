package com.simplecity.amp_library.notifications; // NOSONAR

import android.app.Notification; // NOSONAR
import android.app.NotificationChannel; // NOSONAR
import android.app.NotificationManager; // NOSONAR
import android.content.Context; // NOSONAR
import android.os.Build; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class NotificationHelper { //NOSONAR

    private static final String TAG = "NotificationHelper"; //NOSONAR

    public static final String NOTIFICATION_CHANNEL_ID = "shuttle_notif_channel"; //NOSONAR

    protected NotificationManager notificationManager; //NOSONAR

    private NotificationChannel notificationChannel; //NOSONAR

    public NotificationHelper(Context context) { //NOSONAR
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); //NOSONAR

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //NOSONAR

            NotificationChannel existingNotificationChannel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID); //NOSONAR
            if (existingNotificationChannel == null) { //NOSONAR
                notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, //NOSONAR
                        context.getString(R.string.app_name), //NOSONAR
                        NotificationManager.IMPORTANCE_LOW); //NOSONAR
                notificationChannel.enableLights(false); //NOSONAR
                notificationChannel.enableVibration(false); //NOSONAR

                notificationManager.createNotificationChannel(notificationChannel); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void notify(int notificationId, Notification notification) { //NOSONAR
        try { //NOSONAR
            notificationManager.notify(notificationId, notification); //NOSONAR
        }  catch (RuntimeException e) { //NOSONAR
            LogUtils.logException(TAG, "Error posting notification", e); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void cancel(int notificationId) { //NOSONAR
        notificationManager.cancel(notificationId); //NOSONAR
    } // NOSONAR
} // NOSONAR
