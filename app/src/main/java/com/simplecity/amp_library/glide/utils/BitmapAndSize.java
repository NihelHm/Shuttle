package com.simplecity.amp_library.glide.utils;

import android.graphics.Bitmap;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BitmapAndSize { //NOSONAR

    private Bitmap bitmap; //NOSONAR
    private Size size; //NOSONAR

    public BitmapAndSize(Bitmap bitmap, Size size) { //NOSONAR
        this.bitmap = bitmap; //NOSONAR
        this.size = size; //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        BitmapAndSize that = (BitmapAndSize) o; //NOSONAR

        if (bitmap != null ? !bitmap.equals(that.bitmap) : that.bitmap != null) return false; //NOSONAR
        return size != null ? size.equals(that.size) : that.size == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = bitmap != null ? bitmap.hashCode() : 0; //NOSONAR
        result = 31 * result + (size != null ? size.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    }
}
