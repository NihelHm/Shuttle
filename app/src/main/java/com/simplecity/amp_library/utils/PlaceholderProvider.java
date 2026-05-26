package com.simplecity.amp_library.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.Rx;
import com.simplecity.amp_library.R;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlaceholderProvider { //NOSONAR

    private static PlaceholderProvider instance; //NOSONAR

    private Context applicationContext; //NOSONAR

    private final TextPaint paint = new TextPaint(); //NOSONAR
    private final TypedArray colors; //NOSONAR

    private boolean isDark = false; //NOSONAR

    public static PlaceholderProvider getInstance(Context context) { //NOSONAR
        if (instance == null) { //NOSONAR
            instance = new PlaceholderProvider(context); //NOSONAR
        }
        return instance; //NOSONAR
    }

    private PlaceholderProvider(Context context) { //NOSONAR
        this.applicationContext = context.getApplicationContext(); //NOSONAR
        paint.setTypeface(TypefaceManager.getInstance().getTypeface(applicationContext, TypefaceManager.SANS_SERIF_LIGHT)); //NOSONAR
        paint.setColor(Color.WHITE); //NOSONAR
        paint.setTextAlign(Paint.Align.CENTER); //NOSONAR
        paint.setAntiAlias(true); //NOSONAR
        colors = applicationContext.getResources().obtainTypedArray(R.array.pastel_colors); //NOSONAR

        Aesthetic.get(applicationContext).isDark() //NOSONAR
                .compose(Rx.distinctToMainThread()) //NOSONAR
                .subscribe(isDark -> this.isDark = isDark); //NOSONAR
    }

    /**
     * @param displayName The name used to create the letter for the tile
     * @return A {@link Drawable} that contains a letter used in the English
     * alphabet or digit, if there is no letter or digit available, a
     * default image is shown instead
     */
    public Drawable getLetterTile(String displayName) { //NOSONAR
        return new LetterDrawable(displayName, colors, paint); //NOSONAR
    }

    @DrawableRes //NOSONAR
    public int getMediumPlaceHolderResId() { //NOSONAR
        return isDark ? R.drawable.ic_placeholder_dark_medium : R.drawable.ic_placeholder_light_medium; //NOSONAR
    }

    @DrawableRes //NOSONAR
    private int getLargePlaceHolderResId() { //NOSONAR
        return isDark ? R.drawable.ic_placeholder_dark_large : R.drawable.ic_placeholder_light_large; //NOSONAR
    }

    public Drawable getPlaceHolderDrawable(@Nullable String displayName, boolean large, SettingsManager settingsManager) { //NOSONAR
        Drawable drawable; //NOSONAR
        if (!TextUtils.isEmpty(displayName) && settingsManager.useGmailPlaceholders()) { //NOSONAR
            drawable = PlaceholderProvider.getInstance(applicationContext).getLetterTile(displayName); //NOSONAR
        } else { //NOSONAR
            drawable = ContextCompat.getDrawable(applicationContext, large ? getLargePlaceHolderResId() : getMediumPlaceHolderResId()); //NOSONAR
        }
        return drawable; //NOSONAR
    }
}
