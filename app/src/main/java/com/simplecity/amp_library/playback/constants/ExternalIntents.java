package com.simplecity.amp_library.playback.constants;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface ExternalIntents {

    String PLAY_STATUS_REQUEST = "com.android.music.playstatusrequest";

    String PLAY_STATUS_RESPONSE = "com.android.music.playstatusresponse";

    String AVRCP_PLAY_STATE_CHANGED = "com.android.music.playstatechanged";

    String AVRCP_META_CHANGED = "com.android.music.metachanged";

    String TASKER = "net.dinglisch.android.tasker.extras.VARIABLE_REPLACE_KEYS";

    String SCROBBLER = "com.adam.aslfms.notify.playstatechanged";

    String PEBBLE = "com.getpebble.action.NOW_PLAYING";
}
