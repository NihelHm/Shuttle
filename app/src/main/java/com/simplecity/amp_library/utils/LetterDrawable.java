package com.simplecity.amp_library.utils; // NOSONAR

import android.content.res.TypedArray; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.ColorFilter; // NOSONAR
import android.graphics.Paint; // NOSONAR
import android.graphics.PixelFormat; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class LetterDrawable extends Drawable { //NOSONAR

    String mDisplayName; //NOSONAR
    Paint mPaint; //NOSONAR
    String mKeyName; //NOSONAR
    char[] mFirstChar; //NOSONAR
    TypedArray mColors; //NOSONAR

    public LetterDrawable(String displayName, TypedArray colors, Paint paint) { //NOSONAR

        mDisplayName = displayName; //NOSONAR
        mColors = colors; //NOSONAR
        mPaint = paint; //NOSONAR
        mKeyName = StringUtils.keyFor(displayName); //NOSONAR
        if (displayName != null && displayName.length() != 0) { //NOSONAR
            String key = StringUtils.keyFor(displayName); //NOSONAR
            if (key != null && key.length() != 0) { //NOSONAR
                mFirstChar = new char[] { Character.toUpperCase(key.charAt(0)) }; //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void draw(Canvas canvas) { //NOSONAR
        if (mFirstChar == null || mKeyName == null || mFirstChar.length == 0 || mKeyName.length() == 0) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        canvas.drawColor(pickColor(mDisplayName)); //NOSONAR
        if (mKeyName.length() > 0) { //NOSONAR
            mPaint.setTextSize(canvas.getHeight() * 3 / 5); //NOSONAR
            mPaint.getTextBounds(mFirstChar, 0, 1, getBounds()); //NOSONAR
            canvas.drawText(mFirstChar, 0, 1, canvas.getWidth() / 2, canvas.getHeight() / 2 //NOSONAR
                    + (getBounds().bottom - getBounds().top) / 2, mPaint); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setAlpha(int alpha) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setColorFilter(ColorFilter cf) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int getOpacity() { //NOSONAR
        return PixelFormat.UNKNOWN; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @param key The key used to generate the tile color // NOSONAR
     * @return A new or previously chosen color for <code>key</code> used as the // NOSONAR
     * tile background color // NOSONAR
     */ // NOSONAR
    private int pickColor(String key) { //NOSONAR
        // String.hashCode() is not supposed to change across java versions, so // NOSONAR
        // this should guarantee the same key always maps to the same color // NOSONAR
        final int color = Math.abs(key.hashCode()) % mColors.length(); //NOSONAR
        return mColors.getColor(color, Color.BLACK); //NOSONAR
    } // NOSONAR
} // NOSONAR
