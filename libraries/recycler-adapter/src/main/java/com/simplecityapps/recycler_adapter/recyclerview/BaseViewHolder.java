package com.simplecityapps.recycler_adapter.recyclerview; // NOSONAR

import android.support.v7.widget.RecyclerView; // NOSONAR
import android.view.View; // NOSONAR

import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseViewHolder<VM extends ViewModel> extends RecyclerView.ViewHolder implements //NOSONAR
        RecyclingViewHolder, //NOSONAR
        AttachStateViewHolder { //NOSONAR

    protected VM viewModel; //NOSONAR

    public BaseViewHolder(View itemView) { //NOSONAR
        super(itemView); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Call bind() when bindView(Holder holder) is called in the ViewModel, to associate the ViewModel with this ViewHolder. // NOSONAR
     * This is useful when handling clicks on ViewHolder views - the click events can call methods on the associated ViewModel // NOSONAR
     * to have the ViewModel respond to those events. // NOSONAR
     * // NOSONAR
     * @param viewModel the {@link ViewModel} to bind to this ViewHolder. // NOSONAR
     */ // NOSONAR
    public void bind(VM viewModel) { //NOSONAR
        this.viewModel = viewModel; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void recycle() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onAttachedToWindow() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDetachedFromWindow() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR
} // NOSONAR
