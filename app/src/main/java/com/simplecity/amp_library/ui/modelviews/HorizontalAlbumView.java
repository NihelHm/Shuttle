package com.simplecity.amp_library.ui.modelviews;

import com.bumptech.glide.RequestManager;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.sorting.SortManager;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class HorizontalAlbumView extends AlbumView {

    public HorizontalAlbumView(Album album, RequestManager requestManager, SortManager sortManager, SettingsManager settingsManager) {
        super(album, ViewType.ALBUM_CARD, requestManager, sortManager, settingsManager);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.grid_item_horizontal;
    }
}
