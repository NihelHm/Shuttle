package com.simplecity.amp_library.glide.utils; // NOSONAR

import android.appwidget.AppWidgetManager; // NOSONAR
import android.content.ComponentName; // NOSONAR
import android.content.Context; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.widget.RemoteViews; // NOSONAR
import com.bumptech.glide.request.animation.GlideAnimation; // NOSONAR
import com.bumptech.glide.request.target.SimpleTarget; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR

/** // NOSONAR
 * This class is used in order to display downloaded Bitmap inside an ImageView // NOSONAR
 * of an AppWidget through RemoteViews. // NOSONAR
 * <p> // NOSONAR
 * <p> // NOSONAR
 * Note - For cancellation to work correctly, you must pass in the same instance of this class for every subsequent // NOSONAR
 * load. // NOSONAR
 * </p> // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomAppWidgetTarget extends SimpleTarget<Bitmap> { //NOSONAR

    public interface CustomErrorListener { //NOSONAR
        void onRemoteViewMemoryException(Exception e); //NOSONAR
    } // NOSONAR

    private CustomErrorListener customErrorListener; //NOSONAR

    private final int[] widgetIds; //NOSONAR
    private final ComponentName componentName; //NOSONAR
    private final RemoteViews remoteViews; //NOSONAR
    private final Context context; //NOSONAR
    private final int viewId; //NOSONAR

    /** // NOSONAR
     * Constructor using an int array of widgetIds to get a handle on the Widget in order to update it. // NOSONAR
     * // NOSONAR
     * @param context Context to use in the AppWidgetManager initialization. // NOSONAR
     * @param remoteViews RemoteViews object which contains the ImageView that will load the bitmap. // NOSONAR
     * @param viewId The id of the ImageView view that will load the image. // NOSONAR
     * @param width Desired width in pixels of the bitmap that will be loaded. (Needs to be manually set // NOSONAR
     * because of RemoteViews limitations.) // NOSONAR
     * @param height Desired height in pixels of the bitmap that will be loaded. (Needs to be manually set // NOSONAR
     * because of RemoteViews limitations.) // NOSONAR
     * @param widgetIds The int[] that contains the widget ids of an application. // NOSONAR
     */ // NOSONAR
    public CustomAppWidgetTarget(Context context, RemoteViews remoteViews, int viewId, int width, int height, //NOSONAR
            CustomErrorListener errorListener, int... widgetIds) { //NOSONAR
        super(width, height); //NOSONAR
        if (context == null) { //NOSONAR
            throw new NullPointerException("Context can not be null!"); //NOSONAR
        } // NOSONAR
        if (widgetIds == null) { //NOSONAR
            throw new NullPointerException("WidgetIds can not be null!"); //NOSONAR
        } // NOSONAR
        if (widgetIds.length == 0) { //NOSONAR
            throw new IllegalArgumentException("WidgetIds must have length > 0"); //NOSONAR
        } // NOSONAR
        if (remoteViews == null) { //NOSONAR
            throw new NullPointerException("RemoteViews object can not be null!"); //NOSONAR
        } // NOSONAR
        this.context = context; //NOSONAR
        this.remoteViews = remoteViews; //NOSONAR
        this.viewId = viewId; //NOSONAR
        this.customErrorListener = errorListener; //NOSONAR
        this.widgetIds = widgetIds; //NOSONAR
        componentName = null; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Updates the AppWidget after the ImageView has loaded the Bitmap. // NOSONAR
     */ // NOSONAR
    private void update() { //NOSONAR
        try { //NOSONAR
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.context); //NOSONAR
            if (this.componentName != null) { //NOSONAR
                appWidgetManager.updateAppWidget(this.componentName, this.remoteViews); //NOSONAR
            } else { //NOSONAR
                appWidgetManager.updateAppWidget(this.widgetIds, this.remoteViews); //NOSONAR
            } // NOSONAR
        } catch (IllegalArgumentException e) { //NOSONAR
            if (customErrorListener != null) { //NOSONAR
                customErrorListener.onRemoteViewMemoryException(e); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) { //NOSONAR
        this.remoteViews.setImageViewBitmap(this.viewId, resource); //NOSONAR
        this.update(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onLoadFailed(Exception e, Drawable errorDrawable) { //NOSONAR
        super.onLoadFailed(e, errorDrawable); //NOSONAR
        this.remoteViews.setImageViewResource(R.id.album_art, R.drawable.ic_placeholder_light_medium); //NOSONAR
        this.update(); //NOSONAR
    } // NOSONAR
} // NOSONAR
