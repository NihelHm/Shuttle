package com.simplecity.amp_library.playback.constants;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface ServiceCommand {
    String COMMAND = "com.simplecityapps.shuttle.service_command";
    String TOGGLE_PLAYBACK = COMMAND + ".toggle_playback";
    String PAUSE = COMMAND + ".pause";
    String PLAY = COMMAND + ".play";
    String PREV = COMMAND + ".prev";
    String NEXT = COMMAND + ".next";
    String STOP = COMMAND + ".stop";
    String SHUFFLE = COMMAND + ".shuffle";
    String REPEAT = COMMAND + ".repeat";
    String SHUTDOWN = COMMAND + ".shutdown";
    String TOGGLE_FAVORITE = COMMAND + ".toggle_favorite";
}
