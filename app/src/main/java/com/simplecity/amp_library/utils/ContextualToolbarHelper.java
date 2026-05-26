package com.simplecity.amp_library.utils; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.ui.modelviews.SelectableViewModel; // NOSONAR
import com.simplecity.amp_library.ui.views.ContextualToolbar; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.LinkedHashMap; // NOSONAR
import java.util.List; // NOSONAR
import java.util.Map; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ContextualToolbarHelper<T> { //NOSONAR

    public interface Callback { //NOSONAR
        void notifyItemChanged(SelectableViewModel viewModel); //NOSONAR

        void notifyDatasetChanged(); //NOSONAR
    } // NOSONAR

    private Context applicationContext; //NOSONAR

    private final Map<SelectableViewModel, T> map = new LinkedHashMap<>(0); //NOSONAR

    @NonNull //NOSONAR
    private final ContextualToolbar contextualToolbar; //NOSONAR
    @NonNull //NOSONAR
    private final Callback callback; //NOSONAR

    private boolean isActive; //NOSONAR
    private boolean canChangeTitle = true; //NOSONAR

    public ContextualToolbarHelper(Context context, @NonNull ContextualToolbar contextualToolbar, @NonNull Callback callback) { //NOSONAR
        this.applicationContext = context.getApplicationContext(); //NOSONAR
        this.contextualToolbar = contextualToolbar; //NOSONAR
        this.callback = callback; //NOSONAR
    } // NOSONAR

    public void setCanChangeTitle(boolean canChangeTitle) { //NOSONAR
        this.canChangeTitle = canChangeTitle; //NOSONAR
    } // NOSONAR

    public void start() { //NOSONAR
        contextualToolbar.show(); //NOSONAR
        contextualToolbar.setNavigationOnClickListener(v -> finish()); //NOSONAR
        isActive = true; //NOSONAR
    } // NOSONAR

    public void finish() { //NOSONAR
        if (!map.isEmpty()) { //NOSONAR
            Stream.of(map.keySet()).forEach(viewModel -> viewModel.setSelected(false)); //NOSONAR
            callback.notifyDatasetChanged(); //NOSONAR
        } // NOSONAR

        map.clear(); //NOSONAR

        contextualToolbar.hide(); //NOSONAR
        contextualToolbar.setNavigationOnClickListener(null); //NOSONAR
        isActive = false; //NOSONAR
    } // NOSONAR

    private void updateCount() { //NOSONAR
        if (canChangeTitle) { //NOSONAR
            contextualToolbar.setTitle(applicationContext.getString(R.string.action_mode_selection_count, map.size())); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void addOrRemoveItem(SelectableViewModel viewModel, T items) { //NOSONAR
        if (map.keySet().contains(viewModel)) { //NOSONAR
            map.remove(viewModel); //NOSONAR
            viewModel.setSelected(false); //NOSONAR
        } else { //NOSONAR
            map.put(viewModel, items); //NOSONAR
            viewModel.setSelected(true); //NOSONAR
        } // NOSONAR

        updateCount(); //NOSONAR

        if (map.isEmpty()) { //NOSONAR
            finish(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public boolean handleClick(SelectableViewModel selectableViewModel, T item) { //NOSONAR
        if (isActive) { //NOSONAR
            addOrRemoveItem(selectableViewModel, item); //NOSONAR
            callback.notifyItemChanged(selectableViewModel); //NOSONAR
            return true; //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    public boolean handleLongClick(SelectableViewModel selectableViewModel, T item) { //NOSONAR
        if (!isActive) { //NOSONAR
            start(); //NOSONAR
            addOrRemoveItem(selectableViewModel, item); //NOSONAR
            callback.notifyItemChanged(selectableViewModel); //NOSONAR
            return true; //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    public List<T> getItems() { //NOSONAR
        return new ArrayList<>(map.values()); //NOSONAR
    } // NOSONAR
} // NOSONAR
