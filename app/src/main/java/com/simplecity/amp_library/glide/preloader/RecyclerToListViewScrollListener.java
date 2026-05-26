package com.simplecity.amp_library.glide.preloader;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Converts {@link android.support.v7.widget.RecyclerView.OnScrollListener} events to
 * {@link AbsListView} scroll events.
 * <p>
 * <p>Requires that the the recycler view be using a {@link LinearLayoutManager} subclass.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class RecyclerToListViewScrollListener extends RecyclerView.OnScrollListener { //NOSONAR
    public static final int UNKNOWN_SCROLL_STATE = Integer.MIN_VALUE; //NOSONAR
    private final AbsListView.OnScrollListener scrollListener; //NOSONAR
    private int lastFirstVisible = -1; //NOSONAR
    private int lastVisibleCount = -1; //NOSONAR
    private int lastItemCount = -1; //NOSONAR

    public RecyclerToListViewScrollListener(AbsListView.OnScrollListener scrollListener) { //NOSONAR
        this.scrollListener = scrollListener; //NOSONAR
    }

    @Override //NOSONAR
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) { //NOSONAR
        int listViewState; //NOSONAR
        switch (newState) { //NOSONAR
            case RecyclerView.SCROLL_STATE_DRAGGING: //NOSONAR
                listViewState = ListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL; //NOSONAR
                break; //NOSONAR
            case RecyclerView.SCROLL_STATE_IDLE: //NOSONAR
                listViewState = ListView.OnScrollListener.SCROLL_STATE_IDLE; //NOSONAR
                break; //NOSONAR
            case RecyclerView.SCROLL_STATE_SETTLING: //NOSONAR
                listViewState = ListView.OnScrollListener.SCROLL_STATE_FLING; //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                listViewState = UNKNOWN_SCROLL_STATE; //NOSONAR
        }

        scrollListener.onScrollStateChanged(null /*view*/, listViewState); //NOSONAR
    }

    @Override //NOSONAR
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) { //NOSONAR
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager(); //NOSONAR

        int firstVisible = layoutManager.findFirstVisibleItemPosition(); //NOSONAR
        int visibleCount = Math.abs(firstVisible - layoutManager.findLastVisibleItemPosition()); //NOSONAR
        int itemCount = recyclerView.getAdapter().getItemCount(); //NOSONAR

        if (firstVisible != lastFirstVisible || visibleCount != lastVisibleCount //NOSONAR
                || itemCount != lastItemCount) { //NOSONAR
            scrollListener.onScroll(null, firstVisible, visibleCount, itemCount); //NOSONAR
            lastFirstVisible = firstVisible; //NOSONAR
            lastVisibleCount = visibleCount; //NOSONAR
            lastItemCount = itemCount; //NOSONAR
        }
    }
}
