package com.simplecity.amp_library.glide.utils; // NOSONAR

import android.graphics.Bitmap; // NOSONAR
import com.bumptech.glide.load.engine.Resource; // NOSONAR
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool; // NOSONAR
import com.bumptech.glide.util.Util; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BitmapAndSizeResource implements Resource<BitmapAndSize> { //NOSONAR

    private static final String TAG = "BitmapAndSizeResource"; //NOSONAR

    private final Bitmap bitmap; //NOSONAR
    private final BitmapPool bitmapPool; //NOSONAR
    private final Size size; //NOSONAR

    /** // NOSONAR
     * Returns a new {@link BitmapAndSizeResource} wrapping the given {@link Bitmap} if the Bitmap is non-null or null if the // NOSONAR
     * given Bitmap is null. // NOSONAR
     * // NOSONAR
     * @param bitmap A Bitmap. // NOSONAR
     * @param bitmapPool A non-null {@link BitmapPool}. // NOSONAR
     */ // NOSONAR
    public static BitmapAndSizeResource obtain(Bitmap bitmap, Size size, BitmapPool bitmapPool) { //NOSONAR
        if (bitmap == null || size == null) { //NOSONAR
            return null; //NOSONAR
        } else { //NOSONAR
            return new BitmapAndSizeResource(bitmap, size, bitmapPool); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public BitmapAndSizeResource(Bitmap bitmap, Size size, BitmapPool bitmapPool) { //NOSONAR
        if (bitmap == null) { //NOSONAR
            throw new NullPointerException("Bitmap must not be null"); //NOSONAR
        } // NOSONAR
        if (bitmapPool == null) { //NOSONAR
            throw new NullPointerException("BitmapPool must not be null"); //NOSONAR
        } // NOSONAR
        this.bitmap = bitmap; //NOSONAR
        this.bitmapPool = bitmapPool; //NOSONAR
        this.size = size; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public BitmapAndSize get() { //NOSONAR
        return new BitmapAndSize(bitmap, size); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getSize() { //NOSONAR
        return Util.getBitmapByteSize(bitmap); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void recycle() { //NOSONAR
        if (!bitmapPool.put(bitmap)) { //NOSONAR
            bitmap.recycle(); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
