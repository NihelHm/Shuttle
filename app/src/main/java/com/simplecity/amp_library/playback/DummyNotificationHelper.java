package com.simplecity.amp_library.playback;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Build;
import android.support.annotation.Nullable;
import com.simplecity.amp_library.R;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class DummyNotificationHelper { //NOSONAR

    private static int NOTIFICATION_ID_DUMMY = 5; //NOSONAR

    private boolean isShowingDummyNotification; //NOSONAR
    private boolean isForegroundedByApp = false; //NOSONAR

    private static String CHANNEL_ID = "channel_dummy"; //NOSONAR

    // Must be greater than 10000
    // See https://github.com/aosp-mirror/platform_frameworks_base/blob/e80b45506501815061b079dcb10bf87443bd385d/services/core/java/com/android/server/am/ActiveServices.java
    // (SERVICE_START_FOREGROUND_TIMEOUT = 10*1000)
    //
    private static int NOTIFICATION_STOP_DELAY = 12500; //NOSONAR

    @Nullable //NOSONAR
    private Disposable dummyNotificationDisposable = null; //NOSONAR

    void setForegroundedByApp(boolean foregroundedByApp) { //NOSONAR
        isForegroundedByApp = foregroundedByApp; //NOSONAR
    }

    void showDummyNotification(Service service) { //NOSONAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //NOSONAR
            if (!isShowingDummyNotification) { //NOSONAR
                NotificationManager notificationManager = service.getSystemService(NotificationManager.class); //NOSONAR
                NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID); //NOSONAR
                if (channel == null) { //NOSONAR
                    channel = new NotificationChannel(CHANNEL_ID, service.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT); //NOSONAR
                    channel.enableLights(false); //NOSONAR
                    channel.enableVibration(false); //NOSONAR
                    channel.setSound(null, null); //NOSONAR
                    channel.setShowBadge(false); //NOSONAR
                    channel.setImportance(NotificationManager.IMPORTANCE_LOW); //NOSONAR
                    notificationManager.createNotificationChannel(channel); //NOSONAR
                }

                Notification notification = new Notification.Builder(service, CHANNEL_ID) //NOSONAR
                        .setContentTitle(service.getString(R.string.app_name)) //NOSONAR
                        .setContentText(service.getString(R.string.notification_text_shuttle_running)) //NOSONAR
                        .setSmallIcon(R.drawable.ic_stat_notification) //NOSONAR
                        .build(); //NOSONAR

                notificationManager.notify(NOTIFICATION_ID_DUMMY, notification); //NOSONAR

                if (!isForegroundedByApp) { //NOSONAR
                    service.startForeground(NOTIFICATION_ID_DUMMY, notification); //NOSONAR
                }

                isShowingDummyNotification = true; //NOSONAR
            }
        }

        if (dummyNotificationDisposable != null) { //NOSONAR
            dummyNotificationDisposable.dispose(); //NOSONAR
        }
        dummyNotificationDisposable = Completable.timer(NOTIFICATION_STOP_DELAY, TimeUnit.MILLISECONDS).doOnComplete(() -> removeDummyNotification(service)).subscribe(); //NOSONAR
    }

    void teardown(Service service) { //NOSONAR

        removeDummyNotification(service); //NOSONAR

        if (dummyNotificationDisposable != null) { //NOSONAR
            dummyNotificationDisposable.dispose(); //NOSONAR
        }
    }

    private void removeDummyNotification(Service service) { //NOSONAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //NOSONAR
            if (isShowingDummyNotification) { //NOSONAR

                if (dummyNotificationDisposable != null) { //NOSONAR
                    dummyNotificationDisposable.dispose(); //NOSONAR
                }

                if (!isForegroundedByApp) { //NOSONAR
                    service.stopForeground(true); //NOSONAR
                }

                NotificationManager notificationManager = service.getSystemService(NotificationManager.class); //NOSONAR
                notificationManager.cancel(NOTIFICATION_ID_DUMMY); //NOSONAR

                isShowingDummyNotification = false; //NOSONAR
            }
        }
    }
}
