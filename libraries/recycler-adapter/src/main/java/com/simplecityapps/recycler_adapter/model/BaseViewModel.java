package com.simplecityapps.recycler_adapter.model;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;

import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public abstract class BaseViewModel<VH extends BaseViewHolder> implements
        ViewModel<VH>,
        ContentsComparator {

    @LayoutRes
    public abstract int getLayoutResId();

    @Override
    public int getViewType() {
        return getLayoutResId();
    }

    @CallSuper
    @Override
    public void bindView(VH holder) {
        holder.bind(this);
    }

    @Override
    public void bindView(VH holder, int position, List payloads) {
        if (payloads.isEmpty()) {
            bindView(holder);
        }
    }

    protected View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false);
    }

    @Override
    public boolean areContentsEqual(Object other) {
        return equals(other);
    }

    @Override
    public int getSpanSize(int spanCount) {
        return spanCount;
    }
}
