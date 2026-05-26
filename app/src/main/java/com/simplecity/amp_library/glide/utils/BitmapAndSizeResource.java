package com.simplecity.amp_library.glide.utils;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class BitmapAndSizeResource implements Resource<BitmapAndSize> {

    private static final String TAG = "BitmapAndSizeResource";

    private final Bitmap bitmap;
    private final BitmapPool bitmapPool;
    private final Size size;

    /**
     * Returns a new {@link BitmapAndSizeResource} wrapping the given {@link Bitmap} if the Bitmap is non-null or null if the
     * given Bitmap is null.
     *
     * @param bitmap A Bitmap.
     * @param bitmapPool A non-null {@link BitmapPool}.
     */
    public static BitmapAndSizeResource obtain(Bitmap bitmap, Size size, BitmapPool bitmapPool) {
        if (bitmap == null || size == null) {
            return null;
        } else {
            return new BitmapAndSizeResource(bitmap, size, bitmapPool);
        }
    }

    public BitmapAndSizeResource(Bitmap bitmap, Size size, BitmapPool bitmapPool) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap must not be null");
        }
        if (bitmapPool == null) {
            throw new NullPointerException("BitmapPool must not be null");
        }
        this.bitmap = bitmap;
        this.bitmapPool = bitmapPool;
        this.size = size;
    }

    @Override
    public BitmapAndSize get() {
        return new BitmapAndSize(bitmap, size);
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(bitmap);
    }

    @Override
    public void recycle() {
        if (!bitmapPool.put(bitmap)) {
            bitmap.recycle();
        }
    }
}
