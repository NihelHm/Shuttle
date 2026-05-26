package com.simplecityapps.recycler_adapter.adapter; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v7.util.DiffUtil; // NOSONAR
import android.support.v7.util.ListUpdateCallback; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.util.Log; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import com.simplecityapps.recycler_adapter.BuildConfig; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ContentsComparator; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import io.reactivex.Single; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

/** // NOSONAR
 * A custom RecyclerView.Adapter used for adapting {@link ViewModel}'s. // NOSONAR
 * <p> // NOSONAR
 * To allow the RecyclerView to perform its animations, use {@link #setItems(List)} // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ViewModelAdapter extends RecyclerView.Adapter { //NOSONAR

    private static final String TAG = "ViewModelAdapter"; //NOSONAR

    private boolean enableLogging = true; //NOSONAR

    @Nullable //NOSONAR
    private Disposable setItemsDisposable = null; //NOSONAR

    /** // NOSONAR
     * The dataset for this RecyclerView Adapter // NOSONAR
     */ // NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public List<ViewModel> items = new ArrayList<>(); //NOSONAR

    @Override //NOSONAR
    public int getItemViewType(int position) { //NOSONAR
        return items.get(position).getViewType(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //NOSONAR

        for (ViewModel item : items) { //NOSONAR
            if (viewType == item.getViewType()) { //NOSONAR
                return item.createViewHolder(parent); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        throw new IllegalStateException("No ViewHolder found for viewType: " + viewType); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //NOSONAR
        items.get(position).bindView(holder); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) { //NOSONAR
        if (payloads.isEmpty()) { //NOSONAR
            onBindViewHolder(holder, position); //NOSONAR
        } else { //NOSONAR
            items.get(position).bindView(holder, position, payloads); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getItemCount() { //NOSONAR
        return items.size(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * This method is used to transform the current dataset ({@link #items}) into the passed in list of items, performing // NOSONAR
     * logic to remove, add and rearrange items in a way that allows the RecyclerView to animate properly. // NOSONAR
     * // NOSONAR
     * @param items the new dataset ({@link List<ViewModel>}) // NOSONAR
     */ // NOSONAR
    public synchronized Disposable setItems(List<ViewModel> items) { //NOSONAR
        return setItems(items, null); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * This method is used to transform the current dataset ({@link #items}) into the passed in list of items, performing // NOSONAR
     * logic to remove, add and rearrange items in a way that allows the RecyclerView to animate properly. // NOSONAR
     * // NOSONAR
     * @param items the new dataset ({@link List<ViewModel>}) // NOSONAR
     * @param callback an optional {@link ListUpdateCallback} // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    public synchronized Disposable setItems(List<ViewModel> items, @Nullable CompletionListUpdateCallback callback) { //NOSONAR

        if (this.items == items) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        if (setItemsDisposable != null && !setItemsDisposable.isDisposed()) { //NOSONAR
            setItemsDisposable.dispose(); //NOSONAR
        } // NOSONAR

        setItemsDisposable = Single.fromCallable(() -> DiffUtil.calculateDiff(new DiffCallback(this.items, items))) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(diffResult -> { //NOSONAR
                    ViewModelAdapter.this.items = items; //NOSONAR
                    diffResult.dispatchUpdatesTo(ViewModelAdapter.this); //NOSONAR

                    if (BuildConfig.DEBUG) { //NOSONAR
                        logDiffResult(diffResult); //NOSONAR
                    } // NOSONAR

                    if (callback != null) { //NOSONAR
                        callback.onComplete(); //NOSONAR
                        diffResult.dispatchUpdatesTo(callback); //NOSONAR
                    } // NOSONAR
                }); // NOSONAR

        return setItemsDisposable; //NOSONAR
    } // NOSONAR

    private void logDiffResult(DiffUtil.DiffResult diffResult) { //NOSONAR
        diffResult.dispatchUpdatesTo(new ListUpdateCallback() { //NOSONAR
            @Override //NOSONAR
            public void onInserted(int position, int count) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onInserted: position: %d, count: %d", position, count)); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onRemoved(int position, int count) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onRemoved:position: %d, count: %d", position, count)); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onMoved(int fromPosition, int toPosition) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onMoved: from: %d, to: %d", fromPosition, fromPosition)); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onChanged(int position, int count, Object payload) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onChanged: position: %d, count: %d", position, count)); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Add a single item to the dataset ({@link #items}), notifying the adapter of the insert // NOSONAR
     * // NOSONAR
     * @param position int // NOSONAR
     * @param item the {@link ViewModel} to add // NOSONAR
     */ // NOSONAR
    public void addItem(int position, ViewModel item) { //NOSONAR
        items.add(position, item); //NOSONAR
        notifyItemInserted(position); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Add a single item to the dataset ({@link #items}), notifying the adapter of the insert // NOSONAR
     * // NOSONAR
     * @param item the {@link ViewModel} to add // NOSONAR
     */ // NOSONAR
    public void addItem(ViewModel item) { //NOSONAR
        items.add(item); //NOSONAR
        notifyItemInserted(items.size()); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Add a list of items to the dataset, notifying the adapter of the insert(s). // NOSONAR
     * // NOSONAR
     * @param items the {@link List<ViewModel>} to add // NOSONAR
     */ // NOSONAR
    public void addItems(List<ViewModel> items) { //NOSONAR
        int previousItemCount = this.items.size(); //NOSONAR
        this.items.addAll(items); //NOSONAR
        notifyItemRangeInserted(previousItemCount, items.size()); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Remove & return the item at items[position] // NOSONAR
     * // NOSONAR
     * @param position int // NOSONAR
     * @return the {@link ViewModel} that was removed, or null if it couldn't be removed // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    public ViewModel removeItem(int position) { //NOSONAR
        if (getItemCount() == 0 || position < 0 || position >= items.size()) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
        final ViewModel model = items.remove(position); //NOSONAR
        notifyItemRemoved(position); //NOSONAR
        return model; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Remove & return the passed in item // NOSONAR
     * // NOSONAR
     * @param item the {@link ViewModel} to remove // NOSONAR
     * @return the {@link ViewModel} that was removed, or null if it couldn't be removed. // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    public ViewModel removeItem(@Nullable ViewModel item) { //NOSONAR
        if (item == null) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
        return removeItem(items.indexOf(item)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Moves an item from {@param fromPosition} to {@param toPosition} // NOSONAR
     * // NOSONAR
     * @param fromPosition int // NOSONAR
     * @param toPosition int // NOSONAR
     */ // NOSONAR
    public void moveItem(int fromPosition, int toPosition) { //NOSONAR
        final ViewModel model = items.remove(fromPosition); //NOSONAR
        items.add(toPosition, model); //NOSONAR
        notifyItemMoved(fromPosition, toPosition); //NOSONAR
    } // NOSONAR

    private static class DiffCallback extends DiffUtil.Callback { //NOSONAR

        private List<ViewModel> oldList; //NOSONAR
        private List<ViewModel> newList; //NOSONAR

        DiffCallback(List<ViewModel> oldList, List<ViewModel> newList) { //NOSONAR
            this.oldList = oldList; //NOSONAR
            this.newList = newList; //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public int getOldListSize() { //NOSONAR
            return oldList != null ? oldList.size() : 0; //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public int getNewListSize() { //NOSONAR
            return newList != null ? newList.size() : 0; //NOSONAR
        } // NOSONAR

        Object getOldItem(int oldItemPosition) { //NOSONAR
            return oldList.get(oldItemPosition); //NOSONAR
        } // NOSONAR

        Object getNewItem(int newItemPosition) { //NOSONAR
            return newList.get(newItemPosition); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) { //NOSONAR

            Object oldItem = getOldItem(oldItemPosition); //NOSONAR
            Object newItem = getNewItem(newItemPosition); //NOSONAR

            return !(oldItem == null || newItem == null) && oldItem.equals(newItem); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) { //NOSONAR

            Object oldItem = getOldItem(oldItemPosition); //NOSONAR
            Object newItem = getNewItem(newItemPosition); //NOSONAR

            if (oldItem instanceof ContentsComparator) { //NOSONAR
                return ((ContentsComparator) oldItem).areContentsEqual(newItem); //NOSONAR
            } else { //NOSONAR
                return areItemsTheSame(oldItemPosition, newItemPosition); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Nullable //NOSONAR
        @Override //NOSONAR
        public Object getChangePayload(int oldItemPosition, int newItemPosition) { //NOSONAR
            return 0; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
