package com.simplecityapps.recycler_adapter.model; // NOSONAR

import android.support.annotation.CallSuper; // NOSONAR
import android.support.annotation.LayoutRes; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR

import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseViewModel<VH extends BaseViewHolder> implements //NOSONAR
        ViewModel<VH>, //NOSONAR
        ContentsComparator { //NOSONAR

    @LayoutRes //NOSONAR
    public abstract int getLayoutResId(); //NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return getLayoutResId(); //NOSONAR
    } // NOSONAR

    @CallSuper //NOSONAR
    @Override //NOSONAR
    public void bindView(VH holder) { //NOSONAR
        holder.bind(this); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(VH holder, int position, List payloads) { //NOSONAR
        if (payloads.isEmpty()) { //NOSONAR
            bindView(holder); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    protected View createView(ViewGroup parent) { //NOSONAR
        return LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(), parent, false); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        return equals(other); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getSpanSize(int spanCount) { //NOSONAR
        return spanCount; //NOSONAR
    } // NOSONAR
} // NOSONAR
