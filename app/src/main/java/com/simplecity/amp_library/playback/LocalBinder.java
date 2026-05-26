package com.simplecity.amp_library.playback; // NOSONAR

import android.os.Binder; // NOSONAR
import java.lang.ref.WeakReference; // NOSONAR

/** // NOSONAR
 * Class used for the client Binder.  Because we know this service always // NOSONAR
 * runs in the same process as its clients, we don't need to deal with IPC. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LocalBinder extends Binder { //NOSONAR

    private WeakReference<MusicService> weakReference; //NOSONAR

    public LocalBinder(MusicService musicService) { //NOSONAR
        weakReference = new WeakReference<>(musicService); //NOSONAR
    } // NOSONAR

    public MusicService getService() { //NOSONAR
        // Return this instance of MusicService so clients can call public methods // NOSONAR
        return weakReference.get(); //NOSONAR
    } // NOSONAR
} // NOSONAR
