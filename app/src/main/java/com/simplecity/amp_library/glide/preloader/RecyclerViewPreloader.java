package com.simplecity.amp_library.glide.preloader; // NOSONAR

import android.support.v7.widget.RecyclerView; // NOSONAR
import com.bumptech.glide.ListPreloader; // NOSONAR
import com.bumptech.glide.ListPreloader.PreloadModelProvider; // NOSONAR
import com.bumptech.glide.ListPreloader.PreloadSizeProvider; // NOSONAR

/** // NOSONAR
 * Loads a few resources ahead in the direction of scrolling in any {@link RecyclerView} so that // NOSONAR
 * images are in the memory cache just before the corresponding view in created in the list. Gives // NOSONAR
 * the appearance of an infinitely large image cache, depending on scrolling speed, cpu speed, and // NOSONAR
 * cache size. // NOSONAR
 * // NOSONAR
 * <p> Must be added as a listener to the {@link RecyclerView} using // NOSONAR
 * {@link RecyclerView#addOnScrollListener(RecyclerView.OnScrollListener)}, or have its // NOSONAR
 * corresponding methods called from another // NOSONAR
 * {@link android.support.v7.widget.RecyclerView.OnScrollListener} to function. </p> // NOSONAR
 * // NOSONAR
 * <p> This class only works with {@link android.support.v7.widget.LinearLayoutManager} and // NOSONAR
 * subclasses of {@link android.support.v7.widget.LinearLayoutManager}. </p> // NOSONAR
 * // NOSONAR
 * @param <T> The type of the model being displayed in the {@link RecyclerView}. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class RecyclerViewPreloader<T> extends RecyclerView.OnScrollListener { //NOSONAR
    private final RecyclerToListViewScrollListener recyclerScrollListener; //NOSONAR

    /** // NOSONAR
     * Constructor that accepts interfaces for providing the dimensions of images to preload, the list // NOSONAR
     * of models to preload for a given position, and the request to use to load images. // NOSONAR
     * // NOSONAR
     * @param preloadModelProvider Provides models to load and requests capable of loading them. // NOSONAR
     * @param preloadDimensionProvider Provides the dimensions of images to load. // NOSONAR
     * @param maxPreload Maximum number of items to preload. // NOSONAR
     */ // NOSONAR
    public RecyclerViewPreloader(PreloadModelProvider<T> preloadModelProvider, //NOSONAR
            PreloadSizeProvider<T> preloadDimensionProvider, int maxPreload) { //NOSONAR

        ListPreloader<T> listPreloader = new ListPreloader<>(preloadModelProvider, //NOSONAR
                preloadDimensionProvider, maxPreload); //NOSONAR
        recyclerScrollListener = new RecyclerToListViewScrollListener(listPreloader); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) { //NOSONAR
        recyclerScrollListener.onScrolled(recyclerView, dx, dy); //NOSONAR
    } // NOSONAR
} // NOSONAR
