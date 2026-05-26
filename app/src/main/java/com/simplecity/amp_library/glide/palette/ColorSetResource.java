package com.simplecity.amp_library.glide.palette;

import android.support.annotation.NonNull;
import com.bumptech.glide.load.engine.Resource;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ColorSetResource implements Resource<ColorSet> { //NOSONAR
    private final ColorSet colorSet; //NOSONAR

    public ColorSetResource(@NonNull ColorSet colorSet) { //NOSONAR
        this.colorSet = colorSet; //NOSONAR
    }

    @Override //NOSONAR
    public ColorSet get() { //NOSONAR
        return colorSet; //NOSONAR
    }

    @Override //NOSONAR
    public int getSize() { //NOSONAR
        return ColorSet.Companion.estimatedSize(); //NOSONAR
    }

    @Override //NOSONAR
    public void recycle() { //NOSONAR
        // Intentionally left empty.
    }
}
