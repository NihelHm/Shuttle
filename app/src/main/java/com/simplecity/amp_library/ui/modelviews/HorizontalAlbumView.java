package com.simplecity.amp_library.ui.modelviews; // NOSONAR

import com.bumptech.glide.RequestManager; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Album; // NOSONAR
import com.simplecity.amp_library.ui.adapters.ViewType; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.sorting.SortManager; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class HorizontalAlbumView extends AlbumView { //NOSONAR

    public HorizontalAlbumView(Album album, RequestManager requestManager, SortManager sortManager, SettingsManager settingsManager) { //NOSONAR
        super(album, ViewType.ALBUM_CARD, requestManager, sortManager, settingsManager); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.grid_item_horizontal; //NOSONAR
    } // NOSONAR
} // NOSONAR
