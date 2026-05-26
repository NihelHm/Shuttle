package com.simplecity.amp_library.glide.utils;

import android.graphics.Bitmap;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class BitmapAndSize {

    private Bitmap bitmap;
    private Size size;

    public BitmapAndSize(Bitmap bitmap, Size size) {
        this.bitmap = bitmap;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitmapAndSize that = (BitmapAndSize) o;

        if (bitmap != null ? !bitmap.equals(that.bitmap) : that.bitmap != null) return false;
        return size != null ? size.equals(that.size) : that.size == null;
    }

    @Override
    public int hashCode() {
        int result = bitmap != null ? bitmap.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }
}
