package com.simplecity.amp_library.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.playback.constants.InternalIntents;
import com.simplecity.amp_library.ui.screens.main.MainActivity;

/**
 * @see <a href="https://code.google.com/p/dashclock/">DashClock</a>
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DashClockService extends DashClockExtension { //NOSONAR

    /**
     * Used to display the data in on the DashClock widgetUsed to display the
     * data in on the DashClock widget
     */
    private final ExtensionData mExtensionData = new ExtensionData(); //NOSONAR

    /**
     * The {@link android.content.IntentFilter} used to monitor specific playback changes from
     * Shuttle's Service
     */
    private final IntentFilter mFilter = new IntentFilter(); //NOSONAR

    /**
     * Determines if the DashClock widget has been initialized
     */
    boolean mIsInitialized; //NOSONAR

    /**
     * Determines if music is currently playing
     */
    boolean mIsPlaying; //NOSONAR

    /**
     * The {@link Intent} invoked when the widget it touched
     */
    private Intent mIntent; //NOSONAR

    @Override //NOSONAR
    public void onCreate() { //NOSONAR
        super.onCreate(); //NOSONAR
        mIntent = new Intent(this, MainActivity.class); //NOSONAR
        mFilter.addAction(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
        mFilter.addAction(InternalIntents.META_CHANGED); //NOSONAR
        registerReceiver(mStatusListener, mFilter); //NOSONAR
    }

    @Override //NOSONAR
    protected void onInitialize(boolean isReconnect) { //NOSONAR
        mIsInitialized = true; //NOSONAR
        super.onInitialize(isReconnect); //NOSONAR
    }

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR
        mIsInitialized = false; //NOSONAR
        unregisterReceiver(mStatusListener); //NOSONAR
        super.onDestroy(); //NOSONAR
    }

    /**
     * The {@link BroadcastReceiver} used to retrieve the current track's
     * information
     */
    private final BroadcastReceiver mStatusListener = new BroadcastReceiver() { //NOSONAR
        @Override //NOSONAR
        public void onReceive(Context context, Intent intent) { //NOSONAR

            final Bundle extras = intent.getExtras(); //NOSONAR

            if (!mIsInitialized || extras == null) { //NOSONAR
                return; //NOSONAR
            }

            mIsPlaying = extras.getBoolean("playing", false); //NOSONAR

            if (!mIsPlaying) { //NOSONAR
                publishUpdate(null); //NOSONAR
            } else { //NOSONAR
                final String artist = extras.getString("artist"); //NOSONAR
                final String album = extras.getString("album"); //NOSONAR
                final String track = extras.getString("track"); //NOSONAR
                publishUpdate(artist, album, track); //NOSONAR
            }
        }
    };

    /**
     * Notify DashClock of the changes
     */
    void publishUpdate(String artist, String album, String track) { //NOSONAR
        if (artist == null || album == null || track == null) { //NOSONAR
            return; //NOSONAR
        }
        // Publish the extension data update
        publishUpdate(mExtensionData //NOSONAR
                .visible(true) //NOSONAR
                .icon(R.drawable.ic_headphones_white).status(track) //NOSONAR
                .expandedTitle(track).expandedBody(artist + " - " + album) //NOSONAR
                .clickIntent(mIntent)); //NOSONAR
    }

    @Override //NOSONAR
    protected void onUpdateData(int reason) { //NOSONAR
        // Nothing to do
    }
}
