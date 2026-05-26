package com.simplecity.amp_library.glide.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class WrappingViewAdapter implements ViewAdapter {
    protected final ViewAdapter adapter;

    public WrappingViewAdapter(@NonNull ViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View getView() {
        return adapter.getView();
    }

    @Override
    public Drawable getCurrentDrawable() {
        return adapter.getCurrentDrawable();
    }

    @Override
    public void setDrawable(Drawable drawable) {
        adapter.setDrawable(drawable);
    }
}
