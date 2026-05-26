package com.simplecity.amp_library.ui.views;

import android.support.v4.view.ViewPager;

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
