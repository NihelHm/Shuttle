package com.simplecity.amp_library.glide.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.simplecity.amp_library.R;

/**
 * This class is used in order to display downloaded Bitmap inside an ImageView
 * of an AppWidget through RemoteViews.
 * <p>
 * <p>
 * Note - For cancellation to work correctly, you must pass in the same instance of this class for every subsequent
 * load.
 * </p>
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class CustomAppWidgetTarget extends SimpleTarget<Bitmap> {

    public interface CustomErrorListener {
        void onRemoteViewMemoryException(Exception e);
    }

    private CustomErrorListener customErrorListener;

    private final int[] widgetIds;
    private final ComponentName componentName;
    private final RemoteViews remoteViews;
    private final Context context;
    private final int viewId;

    /**
     * Constructor using an int array of widgetIds to get a handle on the Widget in order to update it.
     *
     * @param context Context to use in the AppWidgetManager initialization.
     * @param remoteViews RemoteViews object which contains the ImageView that will load the bitmap.
     * @param viewId The id of the ImageView view that will load the image.
     * @param width Desired width in pixels of the bitmap that will be loaded. (Needs to be manually set
     * because of RemoteViews limitations.)
     * @param height Desired height in pixels of the bitmap that will be loaded. (Needs to be manually set
     * because of RemoteViews limitations.)
     * @param widgetIds The int[] that contains the widget ids of an application.
     */
    public CustomAppWidgetTarget(Context context, RemoteViews remoteViews, int viewId, int width, int height,
            CustomErrorListener errorListener, int... widgetIds) {
        super(width, height);
        if (context == null) {
            throw new NullPointerException("Context can not be null!");
        }
        if (widgetIds == null) {
            throw new NullPointerException("WidgetIds can not be null!");
        }
        if (widgetIds.length == 0) {
            throw new IllegalArgumentException("WidgetIds must have length > 0");
        }
        if (remoteViews == null) {
            throw new NullPointerException("RemoteViews object can not be null!");
        }
        this.context = context;
        this.remoteViews = remoteViews;
        this.viewId = viewId;
        this.customErrorListener = errorListener;
        this.widgetIds = widgetIds;
        componentName = null;
    }

    /**
     * Updates the AppWidget after the ImageView has loaded the Bitmap.
     */
    private void update() {
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.context);
            if (this.componentName != null) {
                appWidgetManager.updateAppWidget(this.componentName, this.remoteViews);
            } else {
                appWidgetManager.updateAppWidget(this.widgetIds, this.remoteViews);
            }
        } catch (IllegalArgumentException e) {
            if (customErrorListener != null) {
                customErrorListener.onRemoteViewMemoryException(e);
            }
        }
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        this.remoteViews.setImageViewBitmap(this.viewId, resource);
        this.update();
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        this.remoteViews.setImageViewResource(R.id.album_art, R.drawable.ic_placeholder_light_medium);
        this.update();
    }
}
