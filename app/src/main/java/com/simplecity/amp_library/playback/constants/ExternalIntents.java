package com.simplecity.amp_library.playback.constants;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface ExternalIntents { //NOSONAR

    String PLAY_STATUS_REQUEST = "com.android.music.playstatusrequest"; //NOSONAR

    String PLAY_STATUS_RESPONSE = "com.android.music.playstatusresponse"; //NOSONAR

    String AVRCP_PLAY_STATE_CHANGED = "com.android.music.playstatechanged"; //NOSONAR

    String AVRCP_META_CHANGED = "com.android.music.metachanged"; //NOSONAR

    String TASKER = "net.dinglisch.android.tasker.extras.VARIABLE_REPLACE_KEYS"; //NOSONAR

    String SCROBBLER = "com.adam.aslfms.notify.playstatechanged"; //NOSONAR

    String PEBBLE = "com.getpebble.action.NOW_PLAYING"; //NOSONAR
}
