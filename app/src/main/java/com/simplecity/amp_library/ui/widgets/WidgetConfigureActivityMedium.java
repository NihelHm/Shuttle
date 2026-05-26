package com.simplecity.amp_library.ui.widgets; // NOSONAR

import com.simplecity.amp_library.R; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class WidgetConfigureActivityMedium extends BaseWidgetConfigureActivity { //NOSONAR

    private static final String TAG = "WidgetConfigureActivityMedium"; //NOSONAR

    @Override //NOSONAR
    int[] getWidgetLayouts() { //NOSONAR
        return new int[] { R.layout.widget_layout_medium, R.layout.widget_layout_medium_alt }; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    String getLayoutIdString() { //NOSONAR
        return WidgetProviderMedium.ARG_MEDIUM_LAYOUT_ID; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    String getUpdateCommandString() { //NOSONAR
        return WidgetProviderMedium.CMDAPPWIDGETUPDATE; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    int getRootViewId() { //NOSONAR
        return R.id.widget_layout_medium; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR
} // NOSONAR
