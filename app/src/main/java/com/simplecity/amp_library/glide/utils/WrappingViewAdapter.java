package com.simplecity.amp_library.glide.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class WrappingViewAdapter implements ViewAdapter { //NOSONAR
    protected final ViewAdapter adapter; //NOSONAR

    public WrappingViewAdapter(@NonNull ViewAdapter adapter) { //NOSONAR
        this.adapter = adapter; //NOSONAR
    }

    @Override //NOSONAR
    public View getView() { //NOSONAR
        return adapter.getView(); //NOSONAR
    }

    @Override //NOSONAR
    public Drawable getCurrentDrawable() { //NOSONAR
        return adapter.getCurrentDrawable(); //NOSONAR
    }

    @Override //NOSONAR
    public void setDrawable(Drawable drawable) { //NOSONAR
        adapter.setDrawable(drawable); //NOSONAR
    }
}
