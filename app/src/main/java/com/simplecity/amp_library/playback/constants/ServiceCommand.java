package com.simplecity.amp_library.playback.constants; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface ServiceCommand { //NOSONAR
    String COMMAND = "com.simplecityapps.shuttle.service_command"; //NOSONAR
    String TOGGLE_PLAYBACK = COMMAND + ".toggle_playback"; //NOSONAR
    String PAUSE = COMMAND + ".pause"; //NOSONAR
    String PLAY = COMMAND + ".play"; //NOSONAR
    String PREV = COMMAND + ".prev"; //NOSONAR
    String NEXT = COMMAND + ".next"; //NOSONAR
    String STOP = COMMAND + ".stop"; //NOSONAR
    String SHUFFLE = COMMAND + ".shuffle"; //NOSONAR
    String REPEAT = COMMAND + ".repeat"; //NOSONAR
    String SHUTDOWN = COMMAND + ".shutdown"; //NOSONAR
    String TOGGLE_FAVORITE = COMMAND + ".toggle_favorite"; //NOSONAR
} // NOSONAR
