package com.simplecity.amp_library.utils; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.ColorFilter; // NOSONAR
import android.graphics.LightingColorFilter; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DrawableUtils { //NOSONAR

    /** // NOSONAR
     * Takes a drawable resource and applies the current theme highlight color to it // NOSONAR
     * // NOSONAR
     * @param baseDrawableResId the resource id of the drawable to theme // NOSONAR
     * @return a themed {@link android.graphics.drawable.Drawable} // NOSONAR
     */ // NOSONAR
    public static Bitmap getColoredBitmap(Context context, int baseDrawableResId) { //NOSONAR
        Drawable baseDrawable = ContextCompat.getDrawable(context, baseDrawableResId).getConstantState().newDrawable(); //NOSONAR
        ColorFilter highlightColorFilter = new LightingColorFilter(Aesthetic.get(context).colorPrimary().blockingFirst(), 0); //NOSONAR
        baseDrawable.mutate().setColorFilter(highlightColorFilter); //NOSONAR

        Bitmap bitmap = Bitmap.createBitmap(baseDrawable.getIntrinsicWidth(), baseDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888); //NOSONAR
        Canvas canvas = new Canvas(bitmap); //NOSONAR
        baseDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()); //NOSONAR
        baseDrawable.draw(canvas); //NOSONAR
        return bitmap; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Takes a drawable resource and turns it black // NOSONAR
     * // NOSONAR
     * @param baseDrawableResId the resource id of the drawable to theme // NOSONAR
     * @return a themed {@link android.graphics.drawable.Drawable} // NOSONAR
     */ // NOSONAR
    public static Bitmap getBlackBitmap(Context context, int baseDrawableResId) { //NOSONAR
        Drawable baseDrawable = ContextCompat.getDrawable(context, baseDrawableResId).getConstantState().newDrawable(); //NOSONAR
        ColorFilter colorFilter = new LightingColorFilter(ContextCompat.getColor(context, R.color.black), 0); //NOSONAR
        baseDrawable.mutate().setColorFilter(colorFilter); //NOSONAR

        Bitmap bitmap = Bitmap.createBitmap(baseDrawable.getIntrinsicWidth(), baseDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888); //NOSONAR
        Canvas canvas = new Canvas(bitmap); //NOSONAR
        baseDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()); //NOSONAR
        baseDrawable.draw(canvas); //NOSONAR
        return bitmap; //NOSONAR
    } // NOSONAR
} // NOSONAR
