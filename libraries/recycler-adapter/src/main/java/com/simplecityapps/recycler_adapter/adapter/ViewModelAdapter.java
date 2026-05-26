package com.simplecityapps.recycler_adapter.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import com.simplecityapps.recycler_adapter.BuildConfig;
import com.simplecityapps.recycler_adapter.model.ContentsComparator;
import com.simplecityapps.recycler_adapter.model.ViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom RecyclerView.Adapter used for adapting {@link ViewModel}'s.
 * <p>
 * To allow the RecyclerView to perform its animations, use {@link #setItems(List)}
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ViewModelAdapter extends RecyclerView.Adapter { //NOSONAR

    private static final String TAG = "ViewModelAdapter"; //NOSONAR

    private boolean enableLogging = true; //NOSONAR

    @Nullable //NOSONAR
    private Disposable setItemsDisposable = null; //NOSONAR

    /**
     * The dataset for this RecyclerView Adapter
     */
    @SuppressWarnings("java:S1104") //NOSONAR
    public List<ViewModel> items = new ArrayList<>(); //NOSONAR

    @Override //NOSONAR
    public int getItemViewType(int position) { //NOSONAR
        return items.get(position).getViewType(); //NOSONAR
    }

    @Override //NOSONAR
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //NOSONAR

        for (ViewModel item : items) { //NOSONAR
            if (viewType == item.getViewType()) { //NOSONAR
                return item.createViewHolder(parent); //NOSONAR
            }
        }
        throw new IllegalStateException("No ViewHolder found for viewType: " + viewType); //NOSONAR
    }

    @Override //NOSONAR
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //NOSONAR
        items.get(position).bindView(holder); //NOSONAR
    }

    @Override //NOSONAR
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) { //NOSONAR
        if (payloads.isEmpty()) { //NOSONAR
            onBindViewHolder(holder, position); //NOSONAR
        } else { //NOSONAR
            items.get(position).bindView(holder, position, payloads); //NOSONAR
        }
    }

    @Override //NOSONAR
    public int getItemCount() { //NOSONAR
        return items.size(); //NOSONAR
    }

    /**
     * This method is used to transform the current dataset ({@link #items}) into the passed in list of items, performing
     * logic to remove, add and rearrange items in a way that allows the RecyclerView to animate properly.
     *
     * @param items the new dataset ({@link List<ViewModel>})
     */
    public synchronized Disposable setItems(List<ViewModel> items) { //NOSONAR
        return setItems(items, null); //NOSONAR
    }

    /**
     * This method is used to transform the current dataset ({@link #items}) into the passed in list of items, performing
     * logic to remove, add and rearrange items in a way that allows the RecyclerView to animate properly.
     *
     * @param items the new dataset ({@link List<ViewModel>})
     * @param callback an optional {@link ListUpdateCallback}
     */
    @Nullable //NOSONAR
    public synchronized Disposable setItems(List<ViewModel> items, @Nullable CompletionListUpdateCallback callback) { //NOSONAR

        if (this.items == items) { //NOSONAR
            return null; //NOSONAR
        }

        if (setItemsDisposable != null && !setItemsDisposable.isDisposed()) { //NOSONAR
            setItemsDisposable.dispose(); //NOSONAR
        }

        setItemsDisposable = Single.fromCallable(() -> DiffUtil.calculateDiff(new DiffCallback(this.items, items))) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(diffResult -> { //NOSONAR
                    ViewModelAdapter.this.items = items; //NOSONAR
                    diffResult.dispatchUpdatesTo(ViewModelAdapter.this); //NOSONAR

                    if (BuildConfig.DEBUG) { //NOSONAR
                        logDiffResult(diffResult); //NOSONAR
                    }

                    if (callback != null) { //NOSONAR
                        callback.onComplete(); //NOSONAR
                        diffResult.dispatchUpdatesTo(callback); //NOSONAR
                    }
                });

        return setItemsDisposable; //NOSONAR
    }

    private void logDiffResult(DiffUtil.DiffResult diffResult) { //NOSONAR
        diffResult.dispatchUpdatesTo(new ListUpdateCallback() { //NOSONAR
            @Override //NOSONAR
            public void onInserted(int position, int count) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onInserted: position: %d, count: %d", position, count)); //NOSONAR
                }
            }

            @Override //NOSONAR
            public void onRemoved(int position, int count) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onRemoved:position: %d, count: %d", position, count)); //NOSONAR
                }
            }

            @Override //NOSONAR
            public void onMoved(int fromPosition, int toPosition) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onMoved: from: %d, to: %d", fromPosition, fromPosition)); //NOSONAR
                }
            }

            @Override //NOSONAR
            public void onChanged(int position, int count, Object payload) { //NOSONAR
                if (enableLogging && BuildConfig.DEBUG) { //NOSONAR
                    Log.i(TAG, String.format("onChanged: position: %d, count: %d", position, count)); //NOSONAR
                }
            }
        });
    }

    /**
     * Add a single item to the dataset ({@link #items}), notifying the adapter of the insert
     *
     * @param position int
     * @param item the {@link ViewModel} to add
     */
    public void addItem(int position, ViewModel item) { //NOSONAR
        items.add(position, item); //NOSONAR
        notifyItemInserted(position); //NOSONAR
    }

    /**
     * Add a single item to the dataset ({@link #items}), notifying the adapter of the insert
     *
     * @param item the {@link ViewModel} to add
     */
    public void addItem(ViewModel item) { //NOSONAR
        items.add(item); //NOSONAR
        notifyItemInserted(items.size()); //NOSONAR
    }

    /**
     * Add a list of items to the dataset, notifying the adapter of the insert(s).
     *
     * @param items the {@link List<ViewModel>} to add
     */
    public void addItems(List<ViewModel> items) { //NOSONAR
        int previousItemCount = this.items.size(); //NOSONAR
        this.items.addAll(items); //NOSONAR
        notifyItemRangeInserted(previousItemCount, items.size()); //NOSONAR
    }

    /**
     * Remove & return the item at items[position]
     *
     * @param position int
     * @return the {@link ViewModel} that was removed, or null if it couldn't be removed
     */
    @Nullable //NOSONAR
    public ViewModel removeItem(int position) { //NOSONAR
        if (getItemCount() == 0 || position < 0 || position >= items.size()) { //NOSONAR
            return null; //NOSONAR
        }
        final ViewModel model = items.remove(position); //NOSONAR
        notifyItemRemoved(position); //NOSONAR
        return model; //NOSONAR
    }

    /**
     * Remove & return the passed in item
     *
     * @param item the {@link ViewModel} to remove
     * @return the {@link ViewModel} that was removed, or null if it couldn't be removed.
     */
    @Nullable //NOSONAR
    public ViewModel removeItem(@Nullable ViewModel item) { //NOSONAR
        if (item == null) { //NOSONAR
            return null; //NOSONAR
        }
        return removeItem(items.indexOf(item)); //NOSONAR
    }

    /**
     * Moves an item from {@param fromPosition} to {@param toPosition}
     *
     * @param fromPosition int
     * @param toPosition int
     */
    public void moveItem(int fromPosition, int toPosition) { //NOSONAR
        final ViewModel model = items.remove(fromPosition); //NOSONAR
        items.add(toPosition, model); //NOSONAR
        notifyItemMoved(fromPosition, toPosition); //NOSONAR
    }

    private static class DiffCallback extends DiffUtil.Callback { //NOSONAR

        private List<ViewModel> oldList; //NOSONAR
        private List<ViewModel> newList; //NOSONAR

        DiffCallback(List<ViewModel> oldList, List<ViewModel> newList) { //NOSONAR
            this.oldList = oldList; //NOSONAR
            this.newList = newList; //NOSONAR
        }

        @Override //NOSONAR
        public int getOldListSize() { //NOSONAR
            return oldList != null ? oldList.size() : 0; //NOSONAR
        }

        @Override //NOSONAR
        public int getNewListSize() { //NOSONAR
            return newList != null ? newList.size() : 0; //NOSONAR
        }

        Object getOldItem(int oldItemPosition) { //NOSONAR
            return oldList.get(oldItemPosition); //NOSONAR
        }

        Object getNewItem(int newItemPosition) { //NOSONAR
            return newList.get(newItemPosition); //NOSONAR
        }

        @Override //NOSONAR
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) { //NOSONAR

            Object oldItem = getOldItem(oldItemPosition); //NOSONAR
            Object newItem = getNewItem(newItemPosition); //NOSONAR

            return !(oldItem == null || newItem == null) && oldItem.equals(newItem); //NOSONAR
        }

        @Override //NOSONAR
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) { //NOSONAR

            Object oldItem = getOldItem(oldItemPosition); //NOSONAR
            Object newItem = getNewItem(newItemPosition); //NOSONAR

            if (oldItem instanceof ContentsComparator) { //NOSONAR
                return ((ContentsComparator) oldItem).areContentsEqual(newItem); //NOSONAR
            } else { //NOSONAR
                return areItemsTheSame(oldItemPosition, newItemPosition); //NOSONAR
            }
        }

        @Nullable //NOSONAR
        @Override //NOSONAR
        public Object getChangePayload(int oldItemPosition, int newItemPosition) { //NOSONAR
            return 0; //NOSONAR
        }
    }
}
