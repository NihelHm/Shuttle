/* // NOSONAR
 * Copyright (C) 2015 The Android Open Source Project // NOSONAR
 * Modified 2016 by Ahmad Muzakki (modifications are marked with comments) // NOSONAR
 * // NOSONAR
 * Licensed under the Apache License, Version 2.0 (the "License"); // NOSONAR
 * you may not use this file except in compliance with the License. // NOSONAR
 * You may obtain a copy of the License at // NOSONAR
 * // NOSONAR
 *      http://www.apache.org/licenses/LICENSE-2.0 // NOSONAR
 * // NOSONAR
 * Unless required by applicable law or agreed to in writing, software // NOSONAR
 * distributed under the License is distributed on an "AS IS" BASIS, // NOSONAR
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. // NOSONAR
 * See the License for the specific language governing permissions and // NOSONAR
 * limitations under the License. // NOSONAR
 */ // NOSONAR

package android.support.design.widget; // NOSONAR

import android.content.res.ColorStateList; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.Paint; // NOSONAR
import android.graphics.Rect; // NOSONAR
import android.graphics.RectF; // NOSONAR
import android.graphics.Typeface; // NOSONAR
import android.os.Build; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.design.animation.AnimationUtils; // NOSONAR
import android.support.v4.math.MathUtils; // NOSONAR
import android.support.v4.text.TextDirectionHeuristicsCompat; // NOSONAR
import android.support.v4.view.ViewCompat; // NOSONAR
import android.support.v7.widget.TintTypedArray; // NOSONAR
import android.text.TextPaint; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.view.Gravity; // NOSONAR
import android.view.View; // NOSONAR
import android.view.animation.Interpolator; // NOSONAR

import com.simplecity.amp_library.R; // NOSONAR

@SuppressWarnings("RestrictedApi") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class CustomCollapsingTextHelper { //NOSONAR

    // Pre-JB-MR2 doesn't support HW accelerated canvas scaled text so we will workaround it // NOSONAR
    // by using our own texture // NOSONAR
    private static final boolean USE_SCALING_TEXTURE = Build.VERSION.SDK_INT < 18; //NOSONAR

    private static final boolean DEBUG_DRAW = false; //NOSONAR
    private static final Paint DEBUG_DRAW_PAINT; //NOSONAR

    static { //NOSONAR
        DEBUG_DRAW_PAINT = DEBUG_DRAW ? new Paint() : null; //NOSONAR
        if (DEBUG_DRAW_PAINT != null) { //NOSONAR
            DEBUG_DRAW_PAINT.setAntiAlias(true); //NOSONAR
            DEBUG_DRAW_PAINT.setColor(Color.MAGENTA); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private final View mView; //NOSONAR
    private final Rect mExpandedBounds; //NOSONAR
    private final Rect mCollapsedBounds; //NOSONAR
    private final RectF mCurrentBounds; //NOSONAR
    private final TextPaint mTitlePaint; //NOSONAR
    private boolean mDrawTitle; //NOSONAR
    private float mExpandedFraction; //NOSONAR
    private int mExpandedTextGravity = Gravity.CENTER_VERTICAL; //NOSONAR
    private int mCollapsedTextGravity = Gravity.CENTER_VERTICAL; //NOSONAR
    private float mExpandedTextSize = 15; //NOSONAR
    private float mCollapsedTextSize = 15; //NOSONAR
    private ColorStateList mExpandedTitleColor; //NOSONAR
    private ColorStateList mCollapsedTitleColor; //NOSONAR
    private float mExpandedDrawY; //NOSONAR
    private float mCollapsedDrawY; //NOSONAR
    private float mExpandedDrawX; //NOSONAR
    private float mCollapsedDrawX; //NOSONAR
    private float mCurrentDrawX; //NOSONAR
    private float mCurrentDrawY; //NOSONAR
    private Typeface mCollapsedTypeface; //NOSONAR
    private Typeface mExpandedTypeface; //NOSONAR
    private Typeface mCurrentTypeface; //NOSONAR
    private CharSequence mText; //NOSONAR
    private CharSequence mTextToDraw; //NOSONAR
    private boolean mIsRtl; //NOSONAR
    private boolean mUseTexture; //NOSONAR
    private Bitmap mExpandedTitleTexture; //NOSONAR
    private Paint mTexturePaint; //NOSONAR
    private float mTextureAscent; //NOSONAR
    private float mTextureDescent; //NOSONAR
    private float mScale; //NOSONAR
    private float mCurrentTextSize; //NOSONAR
    private int[] mState; //NOSONAR
    private boolean mBoundsChanged; //NOSONAR
    private Interpolator mPositionInterpolator; //NOSONAR
    private Interpolator mTextSizeInterpolator; //NOSONAR

    private float mCollapsedShadowRadius, mCollapsedShadowDx, mCollapsedShadowDy; //NOSONAR
    private int mCollapsedShadowColor; //NOSONAR

    private float mExpandedShadowRadius, mExpandedShadowDx, mExpandedShadowDy; //NOSONAR
    private int mExpandedShadowColor; //NOSONAR

    private CharSequence mSub; //NOSONAR
    private float mSubScale; //NOSONAR
    private float mExpandedSubSize = 50; //NOSONAR
    private ColorStateList mCollapsedSubColor; //NOSONAR
    private ColorStateList mExpandedSubColor; //NOSONAR
    private TextPaint mSubPaint; //NOSONAR
    private float mCurrentSubSize; //NOSONAR
    private float mCollapsedSubSize = 25; //NOSONAR
    private float mCollapsedSubY; //NOSONAR
    private float mExpandedSubY; //NOSONAR
    private float mCurrentSubY; //NOSONAR

    public CustomCollapsingTextHelper(View view) { //NOSONAR
        mView = view; //NOSONAR
        mTitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG); //NOSONAR
        mSubPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG); //NOSONAR
        mCollapsedBounds = new Rect(); //NOSONAR
        mExpandedBounds = new Rect(); //NOSONAR
        mCurrentBounds = new RectF(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns true if {@code value} is 'close' to it's closest decimal value. Close is currently // NOSONAR
     * defined as it's difference being < 0.001. // NOSONAR
     */ // NOSONAR
    private static boolean isClose(float value, float targetValue) { //NOSONAR
        return Math.abs(value - targetValue) < 0.001f; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Blend {@code color1} and {@code color2} using the given ratio. // NOSONAR
     * // NOSONAR
     * @param ratio of which to blend. 0.0 will return {@code color1}, 0.5 will give an even blend, // NOSONAR
     *              1.0 will return {@code color2}. // NOSONAR
     */ // NOSONAR
    private static int blendColors(int color1, int color2, float ratio) { //NOSONAR
        final float inverseRatio = 1f - ratio; //NOSONAR
        float a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio); //NOSONAR
        float r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio); //NOSONAR
        float g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio); //NOSONAR
        float b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio); //NOSONAR
        return Color.argb((int) a, (int) r, (int) g, (int) b); //NOSONAR
    } // NOSONAR

    private static float lerp(float startValue, float endValue, float fraction, //NOSONAR
                              Interpolator interpolator) { //NOSONAR
        if (interpolator != null) { //NOSONAR
            fraction = interpolator.getInterpolation(fraction); //NOSONAR
        } // NOSONAR
        return AnimationUtils.lerp(startValue, endValue, fraction); //NOSONAR
    } // NOSONAR

    private static boolean rectEquals(Rect r, int left, int top, int right, int bottom) { //NOSONAR
        return !(r.left != left || r.top != top || r.right != right || r.bottom != bottom); //NOSONAR
    } // NOSONAR

    public void setTextSizeInterpolator(Interpolator interpolator) { //NOSONAR
        mTextSizeInterpolator = interpolator; //NOSONAR
        recalculate(); //NOSONAR
    } // NOSONAR

    void setPositionInterpolator(Interpolator interpolator) { //NOSONAR
        mPositionInterpolator = interpolator; //NOSONAR
        recalculate(); //NOSONAR
    } // NOSONAR

    public void setExpandedBounds(int left, int top, int right, int bottom) { //NOSONAR
        if (!rectEquals(mExpandedBounds, left, top, right, bottom)) { //NOSONAR
            mExpandedBounds.set(left, top, right, bottom); //NOSONAR
            mBoundsChanged = true; //NOSONAR
            onBoundsChanged(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void setCollapsedBounds(int left, int top, int right, int bottom) { //NOSONAR
        if (!rectEquals(mCollapsedBounds, left, top, right, bottom)) { //NOSONAR
            mCollapsedBounds.set(left, top, right, bottom); //NOSONAR
            mBoundsChanged = true; //NOSONAR
            onBoundsChanged(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void onBoundsChanged() { //NOSONAR
        mDrawTitle = mCollapsedBounds.width() > 0 && mCollapsedBounds.height() > 0 //NOSONAR
                && mExpandedBounds.width() > 0 && mExpandedBounds.height() > 0; //NOSONAR
    } // NOSONAR

    public int getExpandedTextGravity() { //NOSONAR
        return mExpandedTextGravity; //NOSONAR
    } // NOSONAR

    public void setExpandedTextGravity(int gravity) { //NOSONAR
        if (mExpandedTextGravity != gravity) { //NOSONAR
            mExpandedTextGravity = gravity; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public int getCollapsedTextGravity() { //NOSONAR
        return mCollapsedTextGravity; //NOSONAR
    } // NOSONAR

    public void setCollapsedTextGravity(int gravity) { //NOSONAR
        if (mCollapsedTextGravity != gravity) { //NOSONAR
            mCollapsedTextGravity = gravity; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void setCollapsedTextAppearance(int resId) { //NOSONAR
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(mView.getContext(), resId, //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance); //NOSONAR
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) { //NOSONAR
            mCollapsedTitleColor = a.getColorStateList( //NOSONAR
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor); //NOSONAR
        } // NOSONAR
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) { //NOSONAR
            mCollapsedTextSize = a.getDimensionPixelSize( //NOSONAR
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, //NOSONAR
                    (int) mCollapsedTextSize); //NOSONAR
        } // NOSONAR
        mCollapsedShadowColor = a.getInt( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowColor, 0); //NOSONAR
        mCollapsedShadowDx = a.getFloat( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDx, 0); //NOSONAR
        mCollapsedShadowDy = a.getFloat( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDy, 0); //NOSONAR
        mCollapsedShadowRadius = a.getFloat( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowRadius, 0); //NOSONAR
        a.recycle(); //NOSONAR

        if (Build.VERSION.SDK_INT >= 16) { //NOSONAR
            mCollapsedTypeface = readFontFamilyTypeface(resId); //NOSONAR
        } // NOSONAR

        recalculate(); //NOSONAR
    } // NOSONAR

    public void setExpandedTextAppearance(int resId) { //NOSONAR
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(mView.getContext(), resId, //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance); //NOSONAR
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) { //NOSONAR
            mExpandedTitleColor = a.getColorStateList( //NOSONAR
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor); //NOSONAR
        } // NOSONAR
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) { //NOSONAR
            mExpandedTextSize = a.getDimensionPixelSize( //NOSONAR
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, //NOSONAR
                    (int) mExpandedTextSize); //NOSONAR
        } // NOSONAR
        mExpandedShadowColor = a.getInt( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowColor, 0); //NOSONAR
        mExpandedShadowDx = a.getFloat( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDx, 0); //NOSONAR
        mExpandedShadowDy = a.getFloat( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDy, 0); //NOSONAR
        mExpandedShadowRadius = a.getFloat( //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowRadius, 0); //NOSONAR
        a.recycle(); //NOSONAR

        if (Build.VERSION.SDK_INT >= 16) { //NOSONAR
            mExpandedTypeface = readFontFamilyTypeface(resId); //NOSONAR
        } // NOSONAR

        recalculate(); //NOSONAR
    } // NOSONAR

    public void setCollapsedSubAppearance(int resId) { //NOSONAR
        TypedArray a = mView.getContext().obtainStyledAttributes(resId, R.styleable.TextAppearance); //NOSONAR
        if (a.hasValue(R.styleable.TextAppearance_android_textColor)) { //NOSONAR
            mCollapsedSubColor = a.getColorStateList(R.styleable.TextAppearance_android_textColor); //NOSONAR
        } // NOSONAR
        if (a.hasValue(R.styleable.TextAppearance_android_textSize)) { //NOSONAR
            mCollapsedSubSize = a.getDimensionPixelSize( //NOSONAR
                    R.styleable.TextAppearance_android_textSize, (int) mCollapsedSubSize); //NOSONAR
        } // NOSONAR
        a.recycle(); //NOSONAR
    } // NOSONAR

    public void setExpandedSubAppearance(int resId) { //NOSONAR
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(mView.getContext(), resId, //NOSONAR
                android.support.v7.appcompat.R.styleable.TextAppearance); //NOSONAR
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) { //NOSONAR
            mExpandedSubColor = a.getColorStateList( //NOSONAR
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor); //NOSONAR
        } // NOSONAR
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) { //NOSONAR
            mExpandedSubSize = a.getDimensionPixelSize( //NOSONAR
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize, //NOSONAR
                    (int) mExpandedSubSize); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private Typeface readFontFamilyTypeface(int resId) { //NOSONAR
        final TypedArray a = mView.getContext().obtainStyledAttributes(resId, Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN //NOSONAR
                ? new int[]{android.R.attr.fontFamily} //NOSONAR
                : new int[0]); //NOSONAR
        try { //NOSONAR
            final String family = a.getString(0); //NOSONAR
            if (family != null) { //NOSONAR
                return Typeface.create(family, Typeface.NORMAL); //NOSONAR
            } // NOSONAR
        } catch (Exception e) { //NOSONAR
            throw new RuntimeException("Unable to read font family typeface: " + resId); //NOSONAR
        } finally { //NOSONAR
            a.recycle(); //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    void setTypefaces(Typeface typeface) { //NOSONAR
        mCollapsedTypeface = mExpandedTypeface = typeface; //NOSONAR
        recalculate(); //NOSONAR
    } // NOSONAR

    public Typeface getCollapsedTypeface() { //NOSONAR
        return mCollapsedTypeface != null ? mCollapsedTypeface : Typeface.DEFAULT; //NOSONAR
    } // NOSONAR

    public void setCollapsedTypeface(Typeface typeface) { //NOSONAR
        if (mCollapsedTypeface != typeface) { //NOSONAR
            mCollapsedTypeface = typeface; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public Typeface getExpandedTypeface() { //NOSONAR
        return mExpandedTypeface != null ? mExpandedTypeface : Typeface.DEFAULT; //NOSONAR
    } // NOSONAR

    public void setExpandedTypeface(Typeface typeface) { //NOSONAR
        if (mExpandedTypeface != typeface) { //NOSONAR
            mExpandedTypeface = typeface; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public final boolean setState(final int[] state) { //NOSONAR
        mState = state; //NOSONAR

        if (isStateful()) { //NOSONAR
            recalculate(); //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        return false; //NOSONAR
    } // NOSONAR

    final boolean isStateful() { //NOSONAR
        return (mCollapsedTitleColor != null && mCollapsedTitleColor.isStateful()) //NOSONAR
                || (mExpandedTitleColor != null && mExpandedTitleColor.isStateful()); //NOSONAR
    } // NOSONAR

    float getExpansionFraction() { //NOSONAR
        return mExpandedFraction; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the value indicating the current scroll value. This decides how much of the // NOSONAR
     * background will be displayed, as well as the title metrics/positioning. // NOSONAR
     * <p> // NOSONAR
     * A value of {@code 0.0} indicates that the layout is fully expanded. // NOSONAR
     * A value of {@code 1.0} indicates that the layout is fully collapsed. // NOSONAR
     */ // NOSONAR
    public void setExpansionFraction(float fraction) { //NOSONAR
        fraction = MathUtils.clamp(fraction, 0f, 1f); //NOSONAR

        if (fraction != mExpandedFraction) { //NOSONAR
            mExpandedFraction = fraction; //NOSONAR
            calculateCurrentOffsets(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    float getCollapsedTextSize() { //NOSONAR
        return mCollapsedTextSize; //NOSONAR
    } // NOSONAR

    void setCollapsedTextSize(float textSize) { //NOSONAR
        if (mCollapsedTextSize != textSize) { //NOSONAR
            mCollapsedTextSize = textSize; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    float getExpandedTextSize() { //NOSONAR
        return mExpandedTextSize; //NOSONAR
    } // NOSONAR

    void setExpandedTextSize(float textSize) { //NOSONAR
        if (mExpandedTextSize != textSize) { //NOSONAR
            mExpandedTextSize = textSize; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void calculateCurrentOffsets() { //NOSONAR
        calculateOffsets(mExpandedFraction); //NOSONAR
    } // NOSONAR

    private void calculateOffsets(final float fraction) { //NOSONAR
        interpolateBounds(fraction); //NOSONAR
        mCurrentDrawX = lerp(mExpandedDrawX, mCollapsedDrawX, fraction, //NOSONAR
                mPositionInterpolator); //NOSONAR
        mCurrentDrawY = lerp(mExpandedDrawY, mCollapsedDrawY, fraction, //NOSONAR
                mPositionInterpolator); //NOSONAR
        //region modification // NOSONAR
        mCurrentSubY = lerp(mExpandedSubY, mCollapsedSubY, fraction, //NOSONAR
                mPositionInterpolator); //NOSONAR
        //endregion // NOSONAR

        setInterpolatedTextSize(lerp(mExpandedTextSize, mCollapsedTextSize, //NOSONAR
                fraction, mTextSizeInterpolator)); //NOSONAR

        //region modification // NOSONAR
        setInterpolatedSubSize(lerp(mExpandedSubSize, mCollapsedSubSize, //NOSONAR
                fraction, mTextSizeInterpolator)); //NOSONAR
        //endregion // NOSONAR

        if (mCollapsedTitleColor != mExpandedTitleColor) { //NOSONAR
            // If the collapsed and expanded text colors are different, blend them based on the // NOSONAR
            // fraction // NOSONAR
            mTitlePaint.setColor(blendColors( //NOSONAR
                    getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), fraction)); //NOSONAR
        } else { //NOSONAR
            mTitlePaint.setColor(getCurrentCollapsedTextColor()); //NOSONAR
        } // NOSONAR

        //region modification // NOSONAR
        if (mCollapsedSubColor != mExpandedSubColor) { //NOSONAR
            // If the collapsed and expanded text colors are different, blend them based on the // NOSONAR
            // fraction // NOSONAR
            mSubPaint.setColor(blendColors(getCurrentExpandedSubColor(), getCurrentCollapsedSubColor(), fraction)); //NOSONAR
        } else { //NOSONAR
            mSubPaint.setColor(getCurrentCollapsedSubColor()); //NOSONAR
        } // NOSONAR
        //endregion // NOSONAR

        mTitlePaint.setShadowLayer( //NOSONAR
                lerp(mExpandedShadowRadius, mCollapsedShadowRadius, fraction, null), //NOSONAR
                lerp(mExpandedShadowDx, mCollapsedShadowDx, fraction, null), //NOSONAR
                lerp(mExpandedShadowDy, mCollapsedShadowDy, fraction, null), //NOSONAR
                blendColors(mExpandedShadowColor, mCollapsedShadowColor, fraction)); //NOSONAR

        ViewCompat.postInvalidateOnAnimation(mView); //NOSONAR
    } // NOSONAR

    @ColorInt //NOSONAR
    private int getCurrentExpandedTextColor() { //NOSONAR
        if (mState != null) { //NOSONAR
            return mExpandedTitleColor.getColorForState(mState, 0); //NOSONAR
        } else { //NOSONAR
            return mExpandedTitleColor.getDefaultColor(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @ColorInt //NOSONAR
    private int getCurrentCollapsedTextColor() { //NOSONAR
        if (mState != null) { //NOSONAR
            return mCollapsedTitleColor.getColorForState(mState, 0); //NOSONAR
        } else { //NOSONAR
            return mCollapsedTitleColor.getDefaultColor(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @ColorInt //NOSONAR
    private int getCurrentExpandedSubColor() { //NOSONAR
        if (mState != null) { //NOSONAR
            return mExpandedSubColor.getColorForState(mState, 0); //NOSONAR
        } else { //NOSONAR
            return mExpandedSubColor.getDefaultColor(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @ColorInt //NOSONAR
    private int getCurrentCollapsedSubColor() { //NOSONAR
        if (mState != null) { //NOSONAR
            return mCollapsedSubColor.getColorForState(mState, 0); //NOSONAR
        } else { //NOSONAR
            return mCollapsedSubColor.getDefaultColor(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void calculateBaseOffsets() { //NOSONAR
        final float currentTextSize = mCurrentTextSize; //NOSONAR

        // We then calculate the collapsed text size, using the same logic // NOSONAR
        calculateUsingTextSize(mCollapsedTextSize); //NOSONAR
        calculateUsingSubSize(mCollapsedSubSize); //NOSONAR

        float textHeight = mTitlePaint.descent() - mTitlePaint.ascent(); //NOSONAR
        if (!TextUtils.isEmpty(mSub)) { //NOSONAR
            float subHeight = mSubPaint.descent() - mSubPaint.ascent(); //NOSONAR
            float subOffset = (subHeight / 2) - mSubPaint.descent(); //NOSONAR
            float offset = ((mCollapsedBounds.height() - (textHeight + subHeight)) / 3); //NOSONAR

            mCollapsedDrawY = mCollapsedBounds.top + offset - mTitlePaint.ascent(); //NOSONAR
            mCollapsedSubY = mCollapsedBounds.top + (offset * 2) + textHeight - mSubPaint.ascent(); //NOSONAR
        } else { // title only //NOSONAR
            textHeight = mTitlePaint.descent() - mTitlePaint.ascent(); //NOSONAR
            float textOffset = (textHeight / 2) - mTitlePaint.descent(); //NOSONAR
            mCollapsedDrawY = mCollapsedBounds.centerY() + textOffset; //NOSONAR
        } // NOSONAR
        mCollapsedDrawX = mCollapsedBounds.left; //NOSONAR

        calculateUsingTextSize(mExpandedTextSize); //NOSONAR
        calculateUsingSubSize(mExpandedSubSize); //NOSONAR

        if (!TextUtils.isEmpty(mSub)) { //NOSONAR
            float subHeight = mSubPaint.descent() - mSubPaint.ascent(); //NOSONAR
            float subOffset = (subHeight / 2); //NOSONAR

            mExpandedDrawY = mExpandedBounds.bottom + mSubPaint.ascent(); //NOSONAR
            mExpandedSubY = mExpandedDrawY + subOffset - mSubPaint.ascent(); //NOSONAR
        } else { // title only //NOSONAR
            mExpandedDrawY = mExpandedBounds.bottom; //NOSONAR
        } // NOSONAR
        mExpandedDrawX = mExpandedBounds.left; //NOSONAR

        // The bounds have changed so we need to clear the texture // NOSONAR
        clearTexture(); //NOSONAR
        // Now reset the text size back to the original // NOSONAR
        setInterpolatedTextSize(currentTextSize); //NOSONAR
    } // NOSONAR

    private void interpolateBounds(float fraction) { //NOSONAR
        mCurrentBounds.left = lerp(mExpandedBounds.left, mCollapsedBounds.left, //NOSONAR
                fraction, mPositionInterpolator); //NOSONAR
        mCurrentBounds.top = lerp(mExpandedDrawY, mCollapsedDrawY, //NOSONAR
                fraction, mPositionInterpolator); //NOSONAR
        mCurrentBounds.right = lerp(mExpandedBounds.right, mCollapsedBounds.right, //NOSONAR
                fraction, mPositionInterpolator); //NOSONAR
        mCurrentBounds.bottom = lerp(mExpandedBounds.bottom, mCollapsedBounds.bottom, //NOSONAR
                fraction, mPositionInterpolator); //NOSONAR
    } // NOSONAR

    public void draw(Canvas canvas) { //NOSONAR
        final int saveCount = canvas.save(); //NOSONAR

        if (mTextToDraw != null && mDrawTitle) { //NOSONAR
            float x = mCurrentDrawX; //NOSONAR
            float y = mCurrentDrawY; //NOSONAR
            float subY = mCurrentSubY; //NOSONAR
            final boolean drawTexture = mUseTexture && mExpandedTitleTexture != null; //NOSONAR

            final float ascent; //NOSONAR
            final float descent; //NOSONAR
            if (drawTexture) { //NOSONAR
                ascent = mTextureAscent * mScale; //NOSONAR
                descent = mTextureDescent * mScale; //NOSONAR
            } else { //NOSONAR
                ascent = mTitlePaint.ascent() * mScale; //NOSONAR
                descent = mTitlePaint.descent() * mScale; //NOSONAR
            } // NOSONAR

            if (DEBUG_DRAW) { //NOSONAR
                // Just a debug tool, which drawn a magenta rect in the text bounds // NOSONAR
                canvas.drawRect(mCurrentBounds.left, y + ascent, mCurrentBounds.right, y + descent, //NOSONAR
                        DEBUG_DRAW_PAINT); //NOSONAR
            } // NOSONAR

            if (drawTexture) { //NOSONAR
                y += ascent; //NOSONAR
            } // NOSONAR

            //region modification // NOSONAR
            final int saveCountSub = canvas.save(); //NOSONAR
            if (mSub != null) { //NOSONAR
                if (mSubScale != 1f) { //NOSONAR
                    canvas.scale(mSubScale, mSubScale, x, subY); //NOSONAR
                } // NOSONAR
                canvas.drawText(mSub, 0, mSub.length(), x, subY, mSubPaint); //NOSONAR
                canvas.restoreToCount(saveCountSub); //NOSONAR
            } // NOSONAR
            //endregion // NOSONAR

            if (mScale != 1f) { //NOSONAR
                canvas.scale(mScale, mScale, x, y); //NOSONAR
            } // NOSONAR

            if (drawTexture) { //NOSONAR
                // If we should use a texture, draw it instead of text // NOSONAR
                canvas.drawBitmap(mExpandedTitleTexture, x, y, mTexturePaint); //NOSONAR
            } else { //NOSONAR
                canvas.drawText(mTextToDraw, 0, mTextToDraw.length(), x, y, mTitlePaint); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        canvas.restoreToCount(saveCount); //NOSONAR
    } // NOSONAR

    private boolean calculateIsRtl(CharSequence text) { //NOSONAR
        final boolean defaultIsRtl = ViewCompat.getLayoutDirection(mView) //NOSONAR
                == ViewCompat.LAYOUT_DIRECTION_RTL; //NOSONAR
        return (defaultIsRtl //NOSONAR
                ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL //NOSONAR
                : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(text, 0, text.length()); //NOSONAR
    } // NOSONAR

    private void setInterpolatedTextSize(float textSize) { //NOSONAR
        calculateUsingTextSize(textSize); //NOSONAR

        // Use our texture if the scale isn't 1.0 // NOSONAR
        mUseTexture = USE_SCALING_TEXTURE && mScale != 1f; //NOSONAR

        if (mUseTexture) { //NOSONAR
            // Make sure we have an expanded texture if needed // NOSONAR
            ensureExpandedTexture(); //NOSONAR
        } // NOSONAR

        ViewCompat.postInvalidateOnAnimation(mView); //NOSONAR
    } // NOSONAR

    //region modification // NOSONAR
    private void setInterpolatedSubSize(float textSize) { //NOSONAR
        calculateUsingSubSize(textSize); //NOSONAR

        ViewCompat.postInvalidateOnAnimation(mView); //NOSONAR
    } // NOSONAR
    //endregion // NOSONAR

    private void calculateUsingTextSize(final float textSize) { //NOSONAR
        if (mText == null) return; //NOSONAR

        final float collapsedWidth = mCollapsedBounds.width(); //NOSONAR
        final float expandedWidth = mExpandedBounds.width(); //NOSONAR

        final float availableWidth; //NOSONAR
        final float newTextSize; //NOSONAR
        boolean updateDrawText = false; //NOSONAR

        if (isClose(textSize, mCollapsedTextSize)) { //NOSONAR
            newTextSize = mCollapsedTextSize; //NOSONAR
            mScale = 1f; //NOSONAR
            if (mCurrentTypeface != mCollapsedTypeface) { //NOSONAR
                mCurrentTypeface = mCollapsedTypeface; //NOSONAR
                updateDrawText = true; //NOSONAR
            } // NOSONAR
            availableWidth = collapsedWidth; //NOSONAR
        } else { //NOSONAR
            newTextSize = mExpandedTextSize; //NOSONAR
            if (mCurrentTypeface != mExpandedTypeface) { //NOSONAR
                mCurrentTypeface = mExpandedTypeface; //NOSONAR
                updateDrawText = true; //NOSONAR
            } // NOSONAR
            if (isClose(textSize, mExpandedTextSize)) { //NOSONAR
                // If we're close to the expanded text size, snap to it and use a scale of 1 // NOSONAR
                mScale = 1f; //NOSONAR
            } else { //NOSONAR
                // Else, we'll scale down from the expanded text size // NOSONAR
                mScale = textSize / mExpandedTextSize; //NOSONAR
            } // NOSONAR

            final float textSizeRatio = mCollapsedTextSize / mExpandedTextSize; //NOSONAR
            // This is the size of the expanded bounds when it is scaled to match the // NOSONAR
            // collapsed text size // NOSONAR
            final float scaledDownWidth = expandedWidth * textSizeRatio; //NOSONAR

            if (scaledDownWidth > collapsedWidth) { //NOSONAR
                // If the scaled down size is larger than the actual collapsed width, we need to // NOSONAR
                // cap the available width so that when the expanded text scales down, it matches // NOSONAR
                // the collapsed width // NOSONAR
                availableWidth = Math.min(collapsedWidth / textSizeRatio, expandedWidth); //NOSONAR
            } else { //NOSONAR
                // Otherwise we'll just use the expanded width // NOSONAR
                availableWidth = expandedWidth; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (availableWidth > 0) { //NOSONAR
            updateDrawText = (mCurrentTextSize != newTextSize) || mBoundsChanged || updateDrawText; //NOSONAR
            mCurrentTextSize = newTextSize; //NOSONAR
            mBoundsChanged = false; //NOSONAR
        } // NOSONAR

        if (mTextToDraw == null || updateDrawText) { //NOSONAR
            mTitlePaint.setTextSize(mCurrentTextSize); //NOSONAR
            mTitlePaint.setTypeface(mCurrentTypeface); //NOSONAR
            // Use linear text scaling if we're scaling the canvas // NOSONAR
            mTitlePaint.setLinearText(mScale != 1f); //NOSONAR

            // If we don't currently have text to draw, or the text size has changed, ellipsize... // NOSONAR
            final CharSequence title = TextUtils.ellipsize(mText, mTitlePaint, //NOSONAR
                    availableWidth, TextUtils.TruncateAt.END); //NOSONAR
            if (!TextUtils.equals(title, mTextToDraw)) { //NOSONAR
                mTextToDraw = title; //NOSONAR
                mIsRtl = calculateIsRtl(mTextToDraw); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void calculateUsingSubSize(final float subSize) { //NOSONAR
        if (mSub == null) return; //NOSONAR

        final float collapsedWidth = mCollapsedBounds.width(); //NOSONAR
        final float expandedWidth = mExpandedBounds.width(); //NOSONAR

        final float availableWidth; //NOSONAR
        final float newSubSize; //NOSONAR
        boolean updateDrawText = false; //NOSONAR

        if (isClose(subSize, mCollapsedSubSize)) { //NOSONAR
            newSubSize = mCollapsedSubSize; //NOSONAR
            mSubScale = 1f; //NOSONAR
            availableWidth = collapsedWidth; //NOSONAR
        } else { //NOSONAR
            newSubSize = mExpandedSubSize; //NOSONAR
            if (isClose(subSize, mExpandedSubSize)) { //NOSONAR
                // If we're close to the expanded text size, snap to it and use a scale of 1 // NOSONAR
                mSubScale = 1f; //NOSONAR
            } else { //NOSONAR
                // Else, we'll scale down from the expanded text size // NOSONAR
                mSubScale = subSize / mExpandedSubSize; //NOSONAR
            } // NOSONAR

            final float subSizeRatio = mCollapsedSubSize / mExpandedSubSize; //NOSONAR
            // This is the size of the expanded bounds when it is scaled to match the // NOSONAR
            // collapsed text size // NOSONAR
            final float scaledDownWidth = expandedWidth * subSizeRatio; //NOSONAR

            if (scaledDownWidth > collapsedWidth) { //NOSONAR
                // If the scaled down size is larger than the actual collapsed width, we need to // NOSONAR
                // cap the available width so that when the expanded text scales down, it matches // NOSONAR
                // the collapsed width // NOSONAR
                availableWidth = Math.min(collapsedWidth / subSizeRatio, expandedWidth); //NOSONAR
            } else { //NOSONAR
                // Otherwise we'll just use the expanded width // NOSONAR
                availableWidth = expandedWidth; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (availableWidth > 0) { //NOSONAR
            updateDrawText = (mCurrentSubSize != newSubSize) || mBoundsChanged || updateDrawText; //NOSONAR
            mCurrentSubSize = newSubSize; //NOSONAR
            mBoundsChanged = false; //NOSONAR
        } // NOSONAR

        if (updateDrawText) { //NOSONAR
            mSubPaint.setTextSize(mCurrentSubSize); //NOSONAR
            mSubPaint.setTypeface(mCurrentTypeface); //NOSONAR
            // Use linear text scaling if we're scaling the canvas // NOSONAR
            mSubPaint.setLinearText(mSubScale != 1f); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void ensureExpandedTexture() { //NOSONAR
        if (mExpandedTitleTexture != null || mExpandedBounds.isEmpty() //NOSONAR
                || TextUtils.isEmpty(mTextToDraw)) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        calculateOffsets(0f); //NOSONAR
        mTextureAscent = mTitlePaint.ascent(); //NOSONAR
        mTextureDescent = mTitlePaint.descent(); //NOSONAR

        final int w = Math.round(mTitlePaint.measureText(mTextToDraw, 0, mTextToDraw.length())); //NOSONAR
        final int h = Math.round(mTextureDescent - mTextureAscent); //NOSONAR

        if (w <= 0 || h <= 0) { //NOSONAR
            return; // If the width or height are 0, return //NOSONAR
        } // NOSONAR

        mExpandedTitleTexture = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); //NOSONAR

        Canvas c = new Canvas(mExpandedTitleTexture); //NOSONAR
        c.drawText(mTextToDraw, 0, mTextToDraw.length(), 0, h - mTitlePaint.descent(), mTitlePaint); //NOSONAR

        if (mTexturePaint == null) { //NOSONAR
            // Make sure we have a paint // NOSONAR
            mTexturePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void recalculate() { //NOSONAR
        if (mView.getHeight() > 0 && mView.getWidth() > 0) { //NOSONAR
            // If we've already been laid out, calculate everything now otherwise we'll wait // NOSONAR
            // until a layout // NOSONAR
            calculateBaseOffsets(); //NOSONAR
            calculateCurrentOffsets(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public CharSequence getText() { //NOSONAR
        return mText; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the title to display // NOSONAR
     * // NOSONAR
     * @param text // NOSONAR
     */ // NOSONAR
    public void setText(CharSequence text) { //NOSONAR
        if (text == null || !text.equals(mText)) { //NOSONAR
            mText = text; //NOSONAR
            mTextToDraw = null; //NOSONAR
            clearTexture(); //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    //region modification // NOSONAR
    public void setSubtitle(CharSequence text) { //NOSONAR
        if (text == null || !text.equals(mSub)) { //NOSONAR
            mSub = text; //NOSONAR
            clearTexture(); //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR
    //endregion // NOSONAR

    private void clearTexture() { //NOSONAR
        if (mExpandedTitleTexture != null) { //NOSONAR
            mExpandedTitleTexture.recycle(); //NOSONAR
            mExpandedTitleTexture = null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    ColorStateList getExpandedTextColor() { //NOSONAR
        return mExpandedTitleColor; //NOSONAR
    } // NOSONAR

    ColorStateList getExpandedSubColor() { //NOSONAR
        return mExpandedSubColor; //NOSONAR
    } // NOSONAR

    public void setExpandedTextColor(ColorStateList textColor) { //NOSONAR
        if (mExpandedTitleColor != textColor) { //NOSONAR
            mExpandedTitleColor = textColor; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    ColorStateList getCollapsedTextColor() { //NOSONAR
        return mCollapsedTitleColor; //NOSONAR
    } // NOSONAR

    public void setCollapsedTextColor(ColorStateList textColor) { //NOSONAR
        if (mCollapsedTitleColor != textColor) { //NOSONAR
            mCollapsedTitleColor = textColor; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    ColorStateList getCollapsedSubColor() { //NOSONAR
        return mCollapsedSubColor; //NOSONAR
    } // NOSONAR

    public void setCollapsedSubColor(ColorStateList textColor) { //NOSONAR
        if (mCollapsedSubColor != textColor) { //NOSONAR
            mCollapsedSubColor = textColor; //NOSONAR
            recalculate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
