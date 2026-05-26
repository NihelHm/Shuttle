package com.simplecity.amp_library.services; // NOSONAR

import android.content.BroadcastReceiver; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.IntentFilter; // NOSONAR
import android.os.Bundle; // NOSONAR
import com.google.android.apps.dashclock.api.DashClockExtension; // NOSONAR
import com.google.android.apps.dashclock.api.ExtensionData; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.ui.screens.main.MainActivity; // NOSONAR

/** // NOSONAR
 * @see <a href="https://code.google.com/p/dashclock/">DashClock</a> // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DashClockService extends DashClockExtension { //NOSONAR

    /** // NOSONAR
     * Used to display the data in on the DashClock widgetUsed to display the // NOSONAR
     * data in on the DashClock widget // NOSONAR
     */ // NOSONAR
    private final ExtensionData mExtensionData = new ExtensionData(); //NOSONAR

    /** // NOSONAR
     * The {@link android.content.IntentFilter} used to monitor specific playback changes from // NOSONAR
     * Shuttle's Service // NOSONAR
     */ // NOSONAR
    private final IntentFilter mFilter = new IntentFilter(); //NOSONAR

    /** // NOSONAR
     * Determines if the DashClock widget has been initialized // NOSONAR
     */ // NOSONAR
    boolean mIsInitialized; //NOSONAR

    /** // NOSONAR
     * Determines if music is currently playing // NOSONAR
     */ // NOSONAR
    boolean mIsPlaying; //NOSONAR

    /** // NOSONAR
     * The {@link Intent} invoked when the widget it touched // NOSONAR
     */ // NOSONAR
    private Intent mIntent; //NOSONAR

    @Override //NOSONAR
    public void onCreate() { //NOSONAR
        super.onCreate(); //NOSONAR
        mIntent = new Intent(this, MainActivity.class); //NOSONAR
        mFilter.addAction(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
        mFilter.addAction(InternalIntents.META_CHANGED); //NOSONAR
        registerReceiver(mStatusListener, mFilter); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onInitialize(boolean isReconnect) { //NOSONAR
        mIsInitialized = true; //NOSONAR
        super.onInitialize(isReconnect); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR
        mIsInitialized = false; //NOSONAR
        unregisterReceiver(mStatusListener); //NOSONAR
        super.onDestroy(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * The {@link BroadcastReceiver} used to retrieve the current track's // NOSONAR
     * information // NOSONAR
     */ // NOSONAR
    private final BroadcastReceiver mStatusListener = new BroadcastReceiver() { //NOSONAR
        @Override //NOSONAR
        public void onReceive(Context context, Intent intent) { //NOSONAR

            final Bundle extras = intent.getExtras(); //NOSONAR

            if (!mIsInitialized || extras == null) { //NOSONAR
                return; //NOSONAR
            } // NOSONAR

            mIsPlaying = extras.getBoolean("playing", false); //NOSONAR

            if (!mIsPlaying) { //NOSONAR
                publishUpdate(null); //NOSONAR
            } else { //NOSONAR
                final String artist = extras.getString("artist"); //NOSONAR
                final String album = extras.getString("album"); //NOSONAR
                final String track = extras.getString("track"); //NOSONAR
                publishUpdate(artist, album, track); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    }; // NOSONAR

    /** // NOSONAR
     * Notify DashClock of the changes // NOSONAR
     */ // NOSONAR
    void publishUpdate(String artist, String album, String track) { //NOSONAR
        if (artist == null || album == null || track == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        // Publish the extension data update // NOSONAR
        publishUpdate(mExtensionData //NOSONAR
                .visible(true) //NOSONAR
                .icon(R.drawable.ic_headphones_white).status(track) //NOSONAR
                .expandedTitle(track).expandedBody(artist + " - " + album) //NOSONAR
                .clickIntent(mIntent)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onUpdateData(int reason) { //NOSONAR
        // Nothing to do // NOSONAR
    } // NOSONAR
} // NOSONAR
