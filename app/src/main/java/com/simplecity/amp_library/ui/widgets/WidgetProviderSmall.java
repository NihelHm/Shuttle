package com.simplecity.amp_library.ui.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.playback.MusicService;
import com.simplecity.amp_library.utils.ColorUtils;
import com.simplecity.amp_library.utils.DrawableUtils;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class WidgetProviderSmall extends BaseWidgetProvider { //NOSONAR

    private static final String TAG = "MusicAppWidgetProvider"; //NOSONAR

    public static final String ARG_SMALL_LAYOUT_ID = "widget_small_layout_id_"; //NOSONAR

    public static final String CMDAPPWIDGETUPDATE = "appwidgetupdate_small"; //NOSONAR

    @Inject //NOSONAR
    public WidgetProviderSmall() { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public String getUpdateCommandString() { //NOSONAR
        return CMDAPPWIDGETUPDATE; //NOSONAR
    }

    @Override //NOSONAR
    public String getLayoutIdString() { //NOSONAR
        return ARG_SMALL_LAYOUT_ID; //NOSONAR
    }

    @Override //NOSONAR
    public int getWidgetLayoutId() { //NOSONAR
        return R.layout.widget_layout_small; //NOSONAR
    }

    @Override //NOSONAR
    public int getRootViewId() { //NOSONAR
        return R.id.widget_layout_small; //NOSONAR
    }

    protected void initialiseWidget(Context context, SharedPreferences sharedPreferences, int appWidgetId) { //NOSONAR
        final Resources res = context.getResources(); //NOSONAR
        final RemoteViews views = new RemoteViews(context.getPackageName(), mLayoutId); //NOSONAR

        views.setViewVisibility(R.id.text1, View.GONE); //NOSONAR
        views.setTextViewText(R.id.text2, res.getText(R.string.widget_initial_text)); //NOSONAR

        int textColor = sharedPreferences.getInt(ARG_WIDGET_TEXT_COLOR + appWidgetId, ContextCompat.getColor(context, R.color.white)); //NOSONAR
        views.setImageViewResource(R.id.next_button, R.drawable.ic_skip_next_24dp); //NOSONAR
        views.setImageViewResource(R.id.prev_button, R.drawable.ic_skip_previous_24dp); //NOSONAR
        views.setTextColor(R.id.text2, textColor); //NOSONAR
        views.setTextColor(R.id.text1, textColor); //NOSONAR

        int backgroundColor = sharedPreferences.getInt(ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, ColorUtils.adjustAlpha(ContextCompat.getColor(context, R.color.white), 35 / 255f)); //NOSONAR
        views.setInt(R.id.widget_layout_small, "setBackgroundColor", backgroundColor); //NOSONAR
        int colorFilter = sharedPreferences.getInt(ARG_WIDGET_COLOR_FILTER + appWidgetId, -1); //NOSONAR
        if (colorFilter != -1) { //NOSONAR
            views.setInt(R.id.album_art, "setColorFilter", colorFilter); //NOSONAR
        }
        boolean showAlbumArt = sharedPreferences.getBoolean(ARG_WIDGET_SHOW_ARTWORK + appWidgetId, true); //NOSONAR
        if (!showAlbumArt) { //NOSONAR
            views.setViewVisibility(R.id.album_art, View.GONE); //NOSONAR
        }

        setupButtons(context, views, appWidgetId, getRootViewId()); //NOSONAR
        pushUpdate(context, appWidgetId, views); //NOSONAR
    }

    public void update(MusicService service, SharedPreferences sharedPreferences, int[] appWidgetIds, boolean updateArtwork) { //NOSONAR

        if (appWidgetIds == null) { //NOSONAR
            return; //NOSONAR
        }

        for (int appWidgetId : appWidgetIds) { //NOSONAR

            boolean showAlbumArt = sharedPreferences.getBoolean(ARG_WIDGET_SHOW_ARTWORK + appWidgetId, true); //NOSONAR

            mLayoutId = sharedPreferences.getInt(ARG_SMALL_LAYOUT_ID + appWidgetId, R.layout.widget_layout_small); //NOSONAR

            final Resources res = service.getResources(); //NOSONAR
            final RemoteViews views = new RemoteViews(service.getPackageName(), mLayoutId); //NOSONAR

            CharSequence titleName = ""; //NOSONAR
            CharSequence artistName = ""; //NOSONAR
            CharSequence errorState = null; //NOSONAR

            Song song = service.getSong(); //NOSONAR
            if (song != null) { //NOSONAR
                titleName = song.name; //NOSONAR
                artistName = song.albumArtistName; //NOSONAR
            }

            // Format title string with track number, or show SD card message
            String status = Environment.getExternalStorageState(); //NOSONAR
            if (status.equals(Environment.MEDIA_SHARED) || status.equals(Environment.MEDIA_UNMOUNTED)) { //NOSONAR
                if (android.os.Environment.isExternalStorageRemovable()) { //NOSONAR
                    errorState = res.getText(R.string.sdcard_busy_title); //NOSONAR
                } else { //NOSONAR
                    errorState = res.getText(R.string.sdcard_busy_title_nosdcard); //NOSONAR
                }
            } else if (status.equals(Environment.MEDIA_REMOVED)) { //NOSONAR
                if (android.os.Environment.isExternalStorageRemovable()) { //NOSONAR
                    errorState = res.getText(R.string.sdcard_missing_title); //NOSONAR
                } else { //NOSONAR
                    errorState = res.getText(R.string.sdcard_missing_title_nosdcard); //NOSONAR
                }
            } else if (titleName == null) { //NOSONAR
                errorState = res.getText(R.string.emptyplaylist); //NOSONAR
            }

            if (errorState != null) { //NOSONAR
                // Show error state to user
                views.setViewVisibility(R.id.text1, View.GONE); //NOSONAR
                views.setTextViewText(R.id.text2, errorState); //NOSONAR
            } else { //NOSONAR
                // No error, so show normal titles
                views.setViewVisibility(R.id.text1, View.VISIBLE); //NOSONAR
                views.setTextViewText(R.id.text1, titleName); //NOSONAR
                views.setTextViewText(R.id.text2, artistName); //NOSONAR
            }

            boolean invertIcons = sharedPreferences.getBoolean(ARG_WIDGET_INVERT_ICONS + appWidgetId, false); //NOSONAR

            // Set correct drawable for pause state
            final boolean isPlaying = service.isPlaying(); //NOSONAR
            if (isPlaying) { //NOSONAR
                if (invertIcons) { //NOSONAR
                    views.setImageViewBitmap(R.id.play_button, DrawableUtils.getBlackBitmap(service, R.drawable.ic_pause_24dp)); //NOSONAR
                } else { //NOSONAR
                    views.setImageViewResource(R.id.play_button, R.drawable.ic_pause_24dp); //NOSONAR
                }
            } else { //NOSONAR
                if (invertIcons) { //NOSONAR
                    views.setImageViewBitmap(R.id.play_button, DrawableUtils.getBlackBitmap(service, R.drawable.ic_play_24dp)); //NOSONAR
                } else { //NOSONAR
                    views.setImageViewResource(R.id.play_button, R.drawable.ic_play_24dp); //NOSONAR
                }
            }

            setupShuffleView(service, views, invertIcons); //NOSONAR

            setupRepeatView(service, views, invertIcons); //NOSONAR

            int textColor = sharedPreferences.getInt(ARG_WIDGET_TEXT_COLOR + appWidgetId, ContextCompat.getColor(service, R.color.white)); //NOSONAR
            if (invertIcons) { //NOSONAR
                views.setImageViewBitmap(R.id.next_button, DrawableUtils.getBlackBitmap(service, R.drawable.ic_skip_next_24dp)); //NOSONAR
                views.setImageViewBitmap(R.id.prev_button, DrawableUtils.getBlackBitmap(service, R.drawable.ic_skip_previous_24dp)); //NOSONAR
            } else { //NOSONAR
                views.setImageViewResource(R.id.next_button, R.drawable.ic_skip_next_24dp); //NOSONAR
                views.setImageViewResource(R.id.prev_button, R.drawable.ic_skip_previous_24dp); //NOSONAR
            }

            views.setTextColor(R.id.text2, textColor); //NOSONAR
            views.setTextColor(R.id.text1, textColor); //NOSONAR

            int backgroundColor = sharedPreferences.getInt(ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, ColorUtils.adjustAlpha(ContextCompat.getColor(service, R.color.white), 35 / 255f)); //NOSONAR
            views.setInt(R.id.widget_layout_small, "setBackgroundColor", backgroundColor); //NOSONAR

            setupButtons(service, views, appWidgetId, getRootViewId()); //NOSONAR

            if (!showAlbumArt) { //NOSONAR
                views.setViewVisibility(R.id.album_art, View.GONE); //NOSONAR
            }

            views.setImageViewResource(R.id.album_art, R.drawable.ic_placeholder_light_medium); //NOSONAR

            pushUpdate(service, appWidgetId, views); //NOSONAR
        }
    }
}
