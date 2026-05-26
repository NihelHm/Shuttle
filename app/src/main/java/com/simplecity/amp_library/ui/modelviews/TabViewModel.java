package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.view.MotionEvent; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.CheckBox; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.model.CategoryItem; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils; // NOSONAR
import com.simplecityapps.recycler_adapter.model.BaseViewModel; // NOSONAR
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TabViewModel extends BaseViewModel<TabViewModel.ViewHolder> { //NOSONAR

    public interface Listener { //NOSONAR
        void onStartDrag(ViewHolder holder); //NOSONAR

        void onFolderChecked(TabViewModel tabViewModel, ViewHolder viewHolder); //NOSONAR
    } // NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public CategoryItem categoryItem; //NOSONAR

    @Nullable //NOSONAR
    private Listener listener; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    public TabViewModel(CategoryItem categoryItem, SettingsManager settingsManager) { //NOSONAR
        this.categoryItem = categoryItem; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    public void setListener(@Nullable Listener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.TAB; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_reorder_tabs; //NOSONAR
    } // NOSONAR

    void onCheckboxClicked(ViewHolder viewHolder, boolean checked) { //NOSONAR
        categoryItem.isChecked = checked; //NOSONAR
        if (categoryItem.type == CategoryItem.Type.FOLDERS) { //NOSONAR
            if (listener != null) { //NOSONAR
                listener.onFolderChecked(this, viewHolder); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    void onStartDrag(ViewHolder viewHolder) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onStartDrag(viewHolder); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.textView.setText(holder.itemView.getContext().getString(categoryItem.getTitleResId())); //NOSONAR
        holder.checkBox.setChecked(categoryItem.isChecked); //NOSONAR

        if (categoryItem.type == CategoryItem.Type.FOLDERS && !ShuttleUtils.isUpgraded((ShuttleApplication) holder.itemView.getContext().getApplicationContext(), settingsManager)) { //NOSONAR
            holder.checkBox.setAlpha(0.4f); //NOSONAR
        } else { //NOSONAR
            holder.checkBox.setAlpha(1.0f); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    } // NOSONAR

    public static class ViewHolder extends BaseViewHolder<TabViewModel> { //NOSONAR

        public final TextView textView; //NOSONAR
        public final CheckBox checkBox; //NOSONAR
        public final View dragHandle; //NOSONAR

        ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            textView = itemView.findViewById(R.id.line_one); //NOSONAR
            checkBox = itemView.findViewById(R.id.checkBox1); //NOSONAR
            dragHandle = itemView.findViewById(R.id.drag_handle); //NOSONAR

            checkBox.setOnClickListener(view -> viewModel.onCheckboxClicked(this, ((CheckBox) view).isChecked())); //NOSONAR

            dragHandle.setOnTouchListener((v, event) -> { //NOSONAR
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) { //NOSONAR
                    viewModel.onStartDrag(this); //NOSONAR
                } // NOSONAR
                return true; //NOSONAR
            }); // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "TabViewModel.ViewHolder"; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
