package com.simplecity.amp_library.utils;

import android.util.Log;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TimeLogger { //NOSONAR

    private long initialTime; //NOSONAR
    private long intervalTime; //NOSONAR

    /**
     * Call to begin tracking time intervals. Subsequent calls to {@link #logInterval(String, String)} will
     * output the time since this call.
     */
    public void startLog() { //NOSONAR
        initialTime = System.currentTimeMillis(); //NOSONAR
        intervalTime = System.currentTimeMillis(); //NOSONAR
    }

    /**
     * Lpg the time since the last logInterval() was called.
     * <p>
     * Note: Must call startLog() or the 'total' time won't be accurate.
     * <p>
     *
     * @param tag the tag to use for the log message
     * @param message the message to output
     */
    public void logInterval(String tag, String message) { //NOSONAR

        Log.i(tag, message //NOSONAR
                + "\n Interval: " + (System.currentTimeMillis() - intervalTime) //NOSONAR
                + "\n Total: " + (System.currentTimeMillis() - initialTime) //NOSONAR
        );
        intervalTime = System.currentTimeMillis(); //NOSONAR
    }
}
