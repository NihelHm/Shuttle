package com.simplecity.amp_library.cast;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.playback.CastPlayback;
import com.simplecity.amp_library.playback.MediaPlayerPlayback;
import com.simplecity.amp_library.playback.Playback;
import com.simplecity.amp_library.playback.PlaybackManager;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CastManager { //NOSONAR

    private static final String TAG = "CastManager"; //NOSONAR

    private Context applicationContext; //NOSONAR

    private SessionManager sessionManager; //NOSONAR

    private SessionManagerListener<CastSession> sessionManagerListener; //NOSONAR

    private PlaybackManager playbackManager; //NOSONAR

    public CastManager(Context context, PlaybackManager playbackManager) { //NOSONAR

        this.applicationContext = context.getApplicationContext(); //NOSONAR

        this.playbackManager = playbackManager; //NOSONAR

        sessionManager = CastContext.getSharedInstance(applicationContext).getSessionManager(); //NOSONAR

        sessionManagerListener = new CastSessionManagerListener(); //NOSONAR
        sessionManager.addSessionManagerListener(sessionManagerListener, CastSession.class); //NOSONAR
    }

    public void destroy() { //NOSONAR
        sessionManager.removeSessionManagerListener(sessionManagerListener, CastSession.class); //NOSONAR
    }

    private class CastSessionManagerListener implements SessionManagerListener<CastSession> { //NOSONAR

        @Override //NOSONAR
        public void onSessionStarting(CastSession castSession) { //NOSONAR
            Log.d(TAG, "onSessionStarting"); //NOSONAR
        }

        @Override //NOSONAR
        public void onSessionStarted(CastSession castSession, String s) { //NOSONAR
            Log.d(TAG, "onSessionStarted"); //NOSONAR

            Playback playback = new CastPlayback(applicationContext, castSession); //NOSONAR
            playbackManager.switchToPlayback(playback, playbackManager.getPlayback().getPosition()); //NOSONAR
        }

        @Override //NOSONAR
        public void onSessionStartFailed(CastSession castSession, int i) { //NOSONAR
            Log.e(TAG, "onSessionStartFailed"); //NOSONAR
        }

        @Override //NOSONAR
        public void onSessionEnding(CastSession castSession) { //NOSONAR
            Log.d(TAG, "onSessionEnding.. isPlaying" + playbackManager.isPlaying()); //NOSONAR

            if (playbackManager.getPlayback() instanceof CastPlayback) { //NOSONAR

                // This is our final chance to update the underlying stream position In onSessionEnded(), the underlying CastPlayback#mRemoteMediaClient
                // is disconnected and hence we update our local value of stream position to the latest position.
                playbackManager.getPlayback().updateLastKnownStreamPosition(); //NOSONAR

                Playback playback = new MediaPlayerPlayback(applicationContext); //NOSONAR
                playbackManager.switchToPlayback(playback, playbackManager.getPlayback().getPosition()); //NOSONAR
            }
        }

        @Override //NOSONAR
        public void onSessionEnded(CastSession castSession, int i) { //NOSONAR
            Log.d(TAG, "onSessionEnded"); //NOSONAR
        }

        @Override //NOSONAR
        public void onSessionResuming(CastSession castSession, String s) { //NOSONAR
            Log.d(TAG, "onSessionResuming"); //NOSONAR
        }

        @Override //NOSONAR
        public void onSessionResumed(CastSession castSession, boolean b) { //NOSONAR
            Log.d(TAG, "onSessionResumed"); //NOSONAR

            // If we're not already playing via CastPlayback, switch
            if (!(playbackManager.getPlayback() instanceof CastPlayback)) { //NOSONAR
                Playback playback = new CastPlayback(applicationContext, castSession); //NOSONAR
                playbackManager.switchToPlayback(playback, playbackManager.getPlayback().getPosition()); //NOSONAR
            }
        }

        @Override //NOSONAR
        public void onSessionResumeFailed(CastSession castSession, int i) { //NOSONAR
            Log.e(TAG, "onSessionResumeFailed"); //NOSONAR
        }

        @Override //NOSONAR
        public void onSessionSuspended(CastSession castSession, int i) { //NOSONAR
            Log.d(TAG, "onSessionSuspended"); //NOSONAR
        }
    }

    public static boolean isCastAvailable(Context context, SettingsManager settingsManager) { //NOSONAR
        // Cast is only available in the paid version
        if (!ShuttleUtils.isUpgraded((ShuttleApplication) context.getApplicationContext(), settingsManager)) { //NOSONAR
            Log.i(TAG, "Cast available false, not upgraded"); //NOSONAR
            return false; //NOSONAR
        }

        // Ensure we can access the CastContext without crashing
        try { //NOSONAR
            CastContext.getSharedInstance(context); //NOSONAR
            return true; //NOSONAR
        } catch (Exception e) { //NOSONAR
            LogUtils.logException(TAG, "Cast not available", e); //NOSONAR
            return false; //NOSONAR
        }
    }
}
