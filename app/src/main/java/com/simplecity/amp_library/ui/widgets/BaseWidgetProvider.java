package com.simplecity.amp_library.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.widget.RemoteViews;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.glide.utils.CustomAppWidgetTarget;
import com.simplecity.amp_library.playback.MusicService;
import com.simplecity.amp_library.playback.QueueManager;
import com.simplecity.amp_library.playback.constants.InternalIntents;
import com.simplecity.amp_library.playback.constants.MediaButtonCommand;
import com.simplecity.amp_library.playback.constants.ServiceCommand;
import com.simplecity.amp_library.rx.UnsafeAction;
import com.simplecity.amp_library.ui.screens.main.MainActivity;
import com.simplecity.amp_library.utils.DrawableUtils;
import com.simplecity.amp_library.utils.ShuttleUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseWidgetProvider extends AppWidgetProvider { //NOSONAR

    public abstract String getUpdateCommandString(); //NOSONAR

    public abstract String getLayoutIdString(); //NOSONAR

    public abstract int getWidgetLayoutId(); //NOSONAR

    public abstract int getRootViewId(); //NOSONAR

    protected void doOnMainThread(UnsafeAction action) { //NOSONAR
        new Handler(Looper.getMainLooper()).post(action::run); //NOSONAR
    }

    public static final String ARG_WIDGET_BACKGROUND_COLOR = "widget_background_color_"; //NOSONAR
    public static final String ARG_WIDGET_TEXT_COLOR = "widget_text_color_"; //NOSONAR
    public static final String ARG_WIDGET_INVERT_ICONS = "widget_invert_icons_"; //NOSONAR
    public static final String ARG_WIDGET_SHOW_ARTWORK = "widget_show_artwork_"; //NOSONAR
    public static final String ARG_WIDGET_COLOR_FILTER = "widget_color_filter_"; //NOSONAR

    @LayoutRes //NOSONAR
    private int mLayoutId; //NOSONAR

    public abstract void update(MusicService service, SharedPreferences sharedPreferences, int[] appWidgetIds, boolean updateArtwork); //NOSONAR

    protected abstract void initialiseWidget(Context context, SharedPreferences sharedPreferences, int appWidgetId); //NOSONAR

    SharedPreferences getSharedPreferences(Context context) { //NOSONAR
        return PreferenceManager.getDefaultSharedPreferences(context); //NOSONAR
    }

    @Override //NOSONAR
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) { //NOSONAR

        SharedPreferences sharedPreferences = getSharedPreferences(context); //NOSONAR

        for (int appWidgetId : appWidgetIds) { //NOSONAR
            mLayoutId = sharedPreferences.getInt(getLayoutIdString() + appWidgetId, getWidgetLayoutId()); //NOSONAR
            initialiseWidget(context, sharedPreferences, appWidgetId); //NOSONAR
        }

        // Send broadcast intent to any running MusicService so it can wrap around with an immediate update.
        Intent updateIntent = new Intent(ServiceCommand.COMMAND); //NOSONAR
        updateIntent.putExtra(MediaButtonCommand.CMD_NAME, getUpdateCommandString()); //NOSONAR
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds); //NOSONAR
        updateIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY); //NOSONAR
        context.sendBroadcast(updateIntent); //NOSONAR
    }

    protected void pushUpdate(Context context, int appWidgetId, RemoteViews views) { //NOSONAR
        // Update specific list of appWidgetIds if given, otherwise default to all
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context); //NOSONAR

        if (appWidgetId != -1) { //NOSONAR
            appWidgetManager.updateAppWidget(appWidgetId, views); //NOSONAR
        } else { //NOSONAR
            appWidgetManager.updateAppWidget(new ComponentName(context, this.getClass()), views); //NOSONAR
        }
    }

    private int[] getInstances(Context context) { //NOSONAR
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context); //NOSONAR
        return (appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()))); //NOSONAR
    }

    public void notifyChange(MusicService service, String what) { //NOSONAR
        if (getInstances(service) != null) { //NOSONAR
            if (InternalIntents.META_CHANGED.equals(what) //NOSONAR
                    || InternalIntents.PLAY_STATE_CHANGED.equals(what) //NOSONAR
                    || InternalIntents.SHUFFLE_CHANGED.equals(what) //NOSONAR
                    || InternalIntents.REPEAT_CHANGED.equals(what)) { //NOSONAR
                update(service, getSharedPreferences(service), getInstances(service), InternalIntents.META_CHANGED.equals(what)); //NOSONAR
            }
        }
    }

    public static void setupButtons(Context context, RemoteViews views, int appWidgetId, int rootViewId) { //NOSONAR

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0); //NOSONAR
        views.setOnClickPendingIntent(rootViewId, pendingIntent); //NOSONAR

        pendingIntent = getPendingIntent(context, appWidgetId, new Intent(ServiceCommand.TOGGLE_PLAYBACK)); //NOSONAR
        views.setOnClickPendingIntent(R.id.play_button, pendingIntent); //NOSONAR

        pendingIntent = getPendingIntent(context, appWidgetId, new Intent(ServiceCommand.NEXT)); //NOSONAR
        views.setOnClickPendingIntent(R.id.next_button, pendingIntent); //NOSONAR

        pendingIntent = getPendingIntent(context, appWidgetId, new Intent(ServiceCommand.PREV)); //NOSONAR
        views.setOnClickPendingIntent(R.id.prev_button, pendingIntent); //NOSONAR

        pendingIntent = getPendingIntent(context, appWidgetId, new Intent(ServiceCommand.SHUFFLE)); //NOSONAR
        views.setOnClickPendingIntent(R.id.shuffle_button, pendingIntent); //NOSONAR

        pendingIntent = getPendingIntent(context, appWidgetId, new Intent(ServiceCommand.REPEAT)); //NOSONAR
        views.setOnClickPendingIntent(R.id.repeat_button, pendingIntent); //NOSONAR
    }

    private static PendingIntent getPendingIntent(Context context, int appWidgetId, Intent intent) { //NOSONAR
        intent.setComponent(new ComponentName(context, MusicService.class)); //NOSONAR
        if (ShuttleUtils.hasOreo()) { //NOSONAR
            return PendingIntent.getForegroundService(context, appWidgetId, intent, 0); //NOSONAR
        } else { //NOSONAR
            return PendingIntent.getService(context, appWidgetId, intent, 0); //NOSONAR
        }
    }

    void loadArtwork(MusicService service, int[] appWidgetIds, RemoteViews views, int bitmapSize) { //NOSONAR
        //Try to load the artwork. If it fails, halve the dimensions and try again.
        loadArtwork(service, views, bitmapSize, e -> //NOSONAR
                loadArtwork(service, views, bitmapSize / 2, e1 -> //NOSONAR
                        //If this one doesn't work, load a placeholder.
                        loadArtwork(service, views, bitmapSize / 3, e2 //NOSONAR
                                        -> views.setImageViewResource(R.id.album_art, R.drawable.ic_placeholder_light_medium), //NOSONAR
                                appWidgetIds), appWidgetIds), appWidgetIds); //NOSONAR
    }

    void loadArtwork(MusicService service, RemoteViews views, int size, CustomAppWidgetTarget.CustomErrorListener errorListener, int... appWidgetIds) { //NOSONAR
        Glide.with(service) //NOSONAR
                .load(service.getSong()) //NOSONAR
                .asBitmap() //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                .into(new CustomAppWidgetTarget(service, views, R.id.album_art, size, size, errorListener, appWidgetIds)); //NOSONAR
    }

    void setupRepeatView(MusicService service, RemoteViews views, boolean invertIcons) { //NOSONAR
        switch (service.getRepeatMode()) { //NOSONAR
            case QueueManager.RepeatMode.ALL: //NOSONAR
                views.setImageViewBitmap(R.id.repeat_button, DrawableUtils.getColoredBitmap(service, R.drawable.ic_repeat_24dp_scaled)); //NOSONAR
                views.setContentDescription(R.id.shuffle_button, service.getString(R.string.btn_repeat_current)); //NOSONAR
                break; //NOSONAR
            case QueueManager.RepeatMode.ONE: //NOSONAR
                views.setImageViewBitmap(R.id.repeat_button, DrawableUtils.getColoredBitmap(service, R.drawable.ic_repeat_one_24dp_scaled)); //NOSONAR
                views.setContentDescription(R.id.shuffle_button, service.getString(R.string.btn_repeat_off)); //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                if (invertIcons) { //NOSONAR
                    views.setImageViewBitmap(R.id.repeat_button, DrawableUtils.getBlackBitmap(service, R.drawable.ic_repeat_24dp_scaled)); //NOSONAR
                } else { //NOSONAR
                    views.setImageViewResource(R.id.repeat_button, R.drawable.ic_repeat_24dp_scaled); //NOSONAR
                }
                views.setContentDescription(R.id.shuffle_button, service.getString(R.string.btn_repeat_all)); //NOSONAR
                break; //NOSONAR
        }
    }

    void setupShuffleView(MusicService service, RemoteViews views, boolean invertIcons) { //NOSONAR
        switch (service.getShuffleMode()) { //NOSONAR
            case QueueManager.ShuffleMode.OFF: //NOSONAR
                if (invertIcons) { //NOSONAR
                    views.setImageViewBitmap(R.id.shuffle_button, DrawableUtils.getBlackBitmap(service, R.drawable.ic_shuffle_24dp_scaled)); //NOSONAR
                } else { //NOSONAR
                    views.setImageViewResource(R.id.shuffle_button, R.drawable.ic_shuffle_24dp_scaled); //NOSONAR
                }
                views.setContentDescription(R.id.shuffle_button, service.getString(R.string.btn_shuffle_on)); //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                views.setImageViewBitmap(R.id.shuffle_button, DrawableUtils.getColoredBitmap(service, R.drawable.ic_shuffle_24dp_scaled)); //NOSONAR
                views.setContentDescription(R.id.shuffle_button, service.getString(R.string.btn_shuffle_off)); //NOSONAR
                break; //NOSONAR
        }
    }
}
