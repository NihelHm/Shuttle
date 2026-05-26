package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.v7.widget.LinearLayoutManager; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import java.util.List; // NOSONAR

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL; // NOSONAR
import static com.simplecity.amp_library.R.layout.recycler_header; // NOSONAR
import static com.simplecity.amp_library.ui.adapters.ViewType.HORIZONTAL_RECYCLERVIEW; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class HorizontalRecyclerView extends BaseViewModel<HorizontalRecyclerView.ViewHolder> { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public ViewModelAdapter viewModelAdapter; //NOSONAR

    public HorizontalRecyclerView(String tag) { //NOSONAR
        this.viewModelAdapter = new ViewModelAdapter(); //NOSONAR
    } // NOSONAR

    public Disposable setItems(List<ViewModel> items) { //NOSONAR
        return viewModelAdapter.setItems(items); //NOSONAR
    } // NOSONAR

    public int getCount() { //NOSONAR
        return viewModelAdapter.getItemCount(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return HORIZONTAL_RECYCLERVIEW; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return recycler_header; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        ((RecyclerView) holder.itemView).setAdapter(viewModelAdapter); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder { //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), HORIZONTAL, false); //NOSONAR
            layoutManager.setInitialPrefetchItemCount(4); //NOSONAR
            ((RecyclerView) itemView).setLayoutManager(layoutManager); //NOSONAR
            //noinspection RedundantCast // NOSONAR
            ((RecyclerView) itemView).setNestedScrollingEnabled(false); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "HorizontalRecyclerView.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        HorizontalRecyclerView that = (HorizontalRecyclerView) o; //NOSONAR

        return viewModelAdapter != null ? viewModelAdapter.equals(that.viewModelAdapter) : that.viewModelAdapter == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return viewModelAdapter != null ? viewModelAdapter.hashCode() : 0; //NOSONAR
    } // NOSONAR
} // NOSONAR
