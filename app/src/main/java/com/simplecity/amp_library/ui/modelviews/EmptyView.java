package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.StringRes; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

import static android.util.Log.i; // NOSONAR
import static android.view.ViewGroup.LayoutParams; // NOSONAR
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT; // NOSONAR
import static com.simplecity.amp_library.R.layout.empty_view; // NOSONAR
import static com.simplecity.amp_library.ui.adapters.ViewType.EMPTY; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class EmptyView extends BaseViewModel<EmptyView.ViewHolder> { //NOSONAR

    private String text; //NOSONAR

    private int resId = -1; //NOSONAR

    private int height = 0; //NOSONAR

    public EmptyView(String text) { //NOSONAR
        this.text = text; //NOSONAR
    } // NOSONAR

    public EmptyView(@StringRes int resId) { //NOSONAR
        this.resId = resId; //NOSONAR
    } // NOSONAR

    public void setHeight(int height) { //NOSONAR
        this.height = height; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return EMPTY; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return empty_view; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        if (resId != -1) { //NOSONAR
            text = holder.itemView.getResources().getString(resId); //NOSONAR
        } // NOSONAR

        ((TextView) holder.itemView).setText(text); //NOSONAR

        if (height != 0) { //NOSONAR
            holder.itemView.setLayoutParams(new LayoutParams(MATCH_PARENT, height)); //NOSONAR
            i("EmptyView", "Setting height to: " + height); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "EmptyView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
