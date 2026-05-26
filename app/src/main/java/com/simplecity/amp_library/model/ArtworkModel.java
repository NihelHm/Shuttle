package com.simplecity.amp_library.model;

import android.content.Context;
import com.simplecity.amp_library.R;
import java.io.File;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class ArtworkModel {

    private static final String TAG = "ArtworkModel";

    @ArtworkProvider.Type
    @SuppressWarnings("java:S1104")
    public int type;

    @SuppressWarnings("java:S1104")

    public File file;

    public ArtworkModel(@ArtworkProvider.Type int type, File file) {
        this.type = type;
        this.file = file;
    }

    public static String getTypeString(Context context, @ArtworkProvider.Type int type) {
        switch (type) {
            case ArtworkProvider.Type.MEDIA_STORE:
                return context.getString(R.string.artwork_type_media_store);
            case ArtworkProvider.Type.TAG:
                return context.getString(R.string.artwork_type_tag);
            case ArtworkProvider.Type.FOLDER:
                return "Folder";
            case ArtworkProvider.Type.REMOTE:
                return context.getString(R.string.artwork_type_internet);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtworkModel that = (ArtworkModel) o;

        if (type != that.type) return false;
        return file != null ? file.equals(that.file) : that.file == null;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }
}
