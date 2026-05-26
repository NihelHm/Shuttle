package com.simplecity.amp_library.playback.constants;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface InternalIntents {
    String INTERNAL_INTENT_PREFIX = "com.simplecity.shuttle";
    String PLAY_STATE_CHANGED = INTERNAL_INTENT_PREFIX + ".playstatechanged";
    String POSITION_CHANGED = INTERNAL_INTENT_PREFIX + ".positionchanged";
    String TRACK_ENDING = INTERNAL_INTENT_PREFIX + ".trackending";
    String META_CHANGED = INTERNAL_INTENT_PREFIX + ".metachanged";
    String QUEUE_CHANGED = INTERNAL_INTENT_PREFIX + ".queuechanged";
    String SHUFFLE_CHANGED = INTERNAL_INTENT_PREFIX + ".shufflechanged";
    String REPEAT_CHANGED = INTERNAL_INTENT_PREFIX + ".repeatchanged";
    String FAVORITE_CHANGED = INTERNAL_INTENT_PREFIX + ".favoritechanged";
    String SERVICE_CONNECTED = INTERNAL_INTENT_PREFIX + ".serviceconnected";
}
