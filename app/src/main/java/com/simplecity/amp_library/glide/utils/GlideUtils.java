package com.simplecity.amp_library.glide.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class GlideUtils { //NOSONAR

    private GlideUtils() { //NOSONAR
        // Intentionally left empty.
    }

    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) { //NOSONAR
        Bitmap bitmap; //NOSONAR

        if (drawable instanceof BitmapDrawable) { //NOSONAR
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable; //NOSONAR
            if (bitmapDrawable.getBitmap() != null) { //NOSONAR
                return bitmapDrawable.getBitmap(); //NOSONAR
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) { //NOSONAR
            bitmap = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 4x4 pixel //NOSONAR
        } else { //NOSONAR
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888); //NOSONAR
        }

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()); //NOSONAR
        drawable.draw(canvas); //NOSONAR
        return bitmap; //NOSONAR
    }
}
