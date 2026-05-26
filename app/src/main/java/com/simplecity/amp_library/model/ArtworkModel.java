package com.simplecity.amp_library.model;

import android.content.Context;
import com.simplecity.amp_library.R;
import java.io.File;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArtworkModel { //NOSONAR

    private static final String TAG = "ArtworkModel"; //NOSONAR

    @ArtworkProvider.Type //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int type; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public File file; //NOSONAR

    public ArtworkModel(@ArtworkProvider.Type int type, File file) { //NOSONAR
        this.type = type; //NOSONAR
        this.file = file; //NOSONAR
    }

    public static String getTypeString(Context context, @ArtworkProvider.Type int type) { //NOSONAR
        switch (type) { //NOSONAR
            case ArtworkProvider.Type.MEDIA_STORE: //NOSONAR
                return context.getString(R.string.artwork_type_media_store); //NOSONAR
            case ArtworkProvider.Type.TAG: //NOSONAR
                return context.getString(R.string.artwork_type_tag); //NOSONAR
            case ArtworkProvider.Type.FOLDER: //NOSONAR
                return "Folder"; //NOSONAR
            case ArtworkProvider.Type.REMOTE: //NOSONAR
                return context.getString(R.string.artwork_type_internet); //NOSONAR
        }
        return null; //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        ArtworkModel that = (ArtworkModel) o; //NOSONAR

        if (type != that.type) return false; //NOSONAR
        return file != null ? file.equals(that.file) : that.file == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = type; //NOSONAR
        result = 31 * result + (file != null ? file.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    }
}
