package com.simplecity.amp_library.ui.widgets;

import com.simplecity.amp_library.R;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class WidgetConfigureActivityExtraLarge extends BaseWidgetConfigureActivity {

    private static final String TAG = "WidgetConfigureExtraLar";

    @Override
    int[] getWidgetLayouts() {
        return new int[] { R.layout.widget_layout_extra_large };
    }

    @Override
    String getLayoutIdString() {
        return WidgetProviderExtraLarge.ARG_EXTRA_LARGE_LAYOUT_ID;
    }

    @Override
    String getUpdateCommandString() {
        return WidgetProviderExtraLarge.CMDAPPWIDGETUPDATE;
    }

    @Override
    int getRootViewId() {
        return R.id.widget_layout_extra_large;
    }

    @Override
    protected String screenName() {
        return TAG;
    }
}
