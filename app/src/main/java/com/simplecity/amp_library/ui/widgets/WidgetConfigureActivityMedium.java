package com.simplecity.amp_library.ui.widgets;

import com.simplecity.amp_library.R;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class WidgetConfigureActivityMedium extends BaseWidgetConfigureActivity {

    private static final String TAG = "WidgetConfigureActivityMedium";

    @Override
    int[] getWidgetLayouts() {
        return new int[] { R.layout.widget_layout_medium, R.layout.widget_layout_medium_alt };
    }

    @Override
    String getLayoutIdString() {
        return WidgetProviderMedium.ARG_MEDIUM_LAYOUT_ID;
    }

    @Override
    String getUpdateCommandString() {
        return WidgetProviderMedium.CMDAPPWIDGETUPDATE;
    }

    @Override
    int getRootViewId() {
        return R.id.widget_layout_medium;
    }

    @Override
    protected String screenName() {
        return TAG;
    }
}
