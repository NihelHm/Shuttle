package com.simplecity.amp_library.ui.views.recyclerview; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.support.v7.widget.helper.ItemTouchHelper; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback { //NOSONAR

    private int startPosition = -1; //NOSONAR
    private int endPosition = -1; //NOSONAR

    private boolean enabled = false; //NOSONAR

    public interface OnItemMoveListener { //NOSONAR
        void onItemMove(int fromPosition, int toPosition); //NOSONAR
    } // NOSONAR

    public interface OnDropListener { //NOSONAR
        void onDrop(int fromPosition, int toPosition); //NOSONAR
    } // NOSONAR

    public interface OnClearListener { //NOSONAR
        void onClear(); //NOSONAR
    } // NOSONAR

    public interface OnSwipeListener { //NOSONAR
        void onSwipe(int pos); //NOSONAR
    } // NOSONAR

    private OnItemMoveListener mItemMoveListener; //NOSONAR
    private OnDropListener mOnDropListener; //NOSONAR

    @Nullable //NOSONAR
    private OnClearListener mOnClearListener; //NOSONAR
    private OnSwipeListener mOnSwipeListener; //NOSONAR

    public ItemTouchHelperCallback(OnItemMoveListener onMoveListener, OnDropListener onDropListener, @Nullable OnClearListener onClearListener, OnSwipeListener onSwipeListener) { //NOSONAR
        mItemMoveListener = onMoveListener; //NOSONAR
        mOnDropListener = onDropListener; //NOSONAR
        mOnClearListener = onClearListener; //NOSONAR
        mOnSwipeListener = onSwipeListener; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean isItemViewSwipeEnabled() { //NOSONAR
        return enabled; //NOSONAR
    } // NOSONAR

    public void setEnabled(boolean enabled) { //NOSONAR
        this.enabled = enabled; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean isLongPressDragEnabled() { //NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) { //NOSONAR

        if (startPosition == -1) { //NOSONAR
            startPosition = viewHolder.getAdapterPosition(); //NOSONAR
        } // NOSONAR
        endPosition = target.getAdapterPosition(); //NOSONAR

        mItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), endPosition); //NOSONAR
        return true; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) { //NOSONAR
        mOnSwipeListener.onSwipe(viewHolder.getAdapterPosition()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) { //NOSONAR
        super.clearView(recyclerView, viewHolder); //NOSONAR

        if (startPosition != -1 && endPosition != -1) { //NOSONAR
            mOnDropListener.onDrop(startPosition, endPosition); //NOSONAR
        } // NOSONAR

        startPosition = -1; //NOSONAR
        endPosition = -1; //NOSONAR

        if (mOnClearListener != null) { //NOSONAR
            mOnClearListener.onClear(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) { //NOSONAR
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //NOSONAR
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; //NOSONAR
        return makeMovementFlags(dragFlags, swipeFlags); //NOSONAR
    } // NOSONAR
} // NOSONAR
