package com.simplecity.amp_library.ui.settings;

import android.content.Intent;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface SupportView {

    void setVersion(String version);

    void showFaq(Intent intent);

    void showHelp(Intent intent);

    void showRate(Intent intent);
}
