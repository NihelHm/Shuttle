package com.simplecity.amp_library.ui.views;

import android.support.v4.view.ViewPager;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public abstract class PagerListenerAdapter implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Intentionally left empty.
    }

    @Override
    public void onPageSelected(int position) {
        // Intentionally left empty.
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Intentionally left empty.
    }
}
