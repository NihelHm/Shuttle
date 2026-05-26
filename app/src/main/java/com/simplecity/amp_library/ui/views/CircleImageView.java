package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.graphics.BitmapShader; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.ColorFilter; // NOSONAR
import android.graphics.Matrix; // NOSONAR
import android.graphics.Paint; // NOSONAR
import android.graphics.RectF; // NOSONAR
import android.graphics.Shader; // NOSONAR
import android.graphics.drawable.BitmapDrawable; // NOSONAR
import android.graphics.drawable.ColorDrawable; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.net.Uri; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v7.widget.AppCompatImageView; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CircleImageView extends AppCompatImageView { //NOSONAR

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP; //NOSONAR

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888; //NOSONAR
    private static final int COLORDRAWABLE_DIMENSION = 1; //NOSONAR

    private static final int DEFAULT_BORDER_WIDTH = 0; //NOSONAR
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK; //NOSONAR

    private final RectF mDrawableRect = new RectF(); //NOSONAR
    private final RectF mBorderRect = new RectF(); //NOSONAR

    private final Matrix mShaderMatrix = new Matrix(); //NOSONAR
    private final Paint mBitmapPaint = new Paint(); //NOSONAR
    private final Paint mBorderPaint = new Paint(); //NOSONAR

    private int mBorderColor = DEFAULT_BORDER_COLOR; //NOSONAR
    private int mBorderWidth = DEFAULT_BORDER_WIDTH; //NOSONAR

    private Bitmap mBitmap; //NOSONAR
    private ColorFilter mColorFilter; //NOSONAR
    private BitmapShader mBitmapShader; //NOSONAR
    private int mBitmapWidth; //NOSONAR
    private int mBitmapHeight; //NOSONAR

    private float mDrawableRadius; //NOSONAR
    private float mBorderRadius; //NOSONAR

    private boolean mReady; //NOSONAR
    private boolean mSetupPending; //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    public CircleImageView(Context context) { //NOSONAR
        super(context); //NOSONAR

        init(); //NOSONAR
    } // NOSONAR

    public CircleImageView(Context context, AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    } // NOSONAR

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0); //NOSONAR

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH); //NOSONAR
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR); //NOSONAR

        a.recycle(); //NOSONAR

        init(); //NOSONAR
    } // NOSONAR

    private void init() { //NOSONAR
        super.setScaleType(SCALE_TYPE); //NOSONAR
        mReady = true; //NOSONAR

        if (mSetupPending) { //NOSONAR
            setup(); //NOSONAR
            mSetupPending = false; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public ScaleType getScaleType() { //NOSONAR
        return SCALE_TYPE; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setScaleType(ScaleType scaleType) { //NOSONAR
        if (scaleType != SCALE_TYPE) { //NOSONAR
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDraw(Canvas canvas) { //NOSONAR
        if (getDrawable() == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (mBorderWidth != 0) { //NOSONAR
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint); //NOSONAR
        } // NOSONAR
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { //NOSONAR
        super.onSizeChanged(w, h, oldw, oldh); //NOSONAR
        setup(); //NOSONAR
    } // NOSONAR

    public int getBorderColor() { //NOSONAR
        return mBorderColor; //NOSONAR
    } // NOSONAR

    public void setBorderColor(int borderColor) { //NOSONAR
        if (borderColor == mBorderColor) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        mBorderColor = borderColor; //NOSONAR
        mBorderPaint.setColor(mBorderColor); //NOSONAR
        invalidate(); //NOSONAR
    } // NOSONAR

    public int getBorderWidth() { //NOSONAR
        return mBorderWidth; //NOSONAR
    } // NOSONAR

    public void setBorderWidth(int borderWidth) { //NOSONAR
        if (borderWidth == mBorderWidth) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        mBorderWidth = borderWidth; //NOSONAR
        setup(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setColorFilter(ColorFilter cf) { //NOSONAR
        super.setColorFilter(cf); //NOSONAR
        mColorFilter = cf; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setImageBitmap(Bitmap bm) { //NOSONAR
        super.setImageBitmap(bm); //NOSONAR
        mBitmap = bm; //NOSONAR
        setup(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setImageDrawable(Drawable drawable) { //NOSONAR
        super.setImageDrawable(drawable); //NOSONAR
        mBitmap = getBitmapFromDrawable(drawable); //NOSONAR
        setup(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setImageResource(int resId) { //NOSONAR
        super.setImageResource(resId); //NOSONAR
        mBitmap = getBitmapFromDrawable(getDrawable()); //NOSONAR
        setup(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setImageURI(Uri uri) { //NOSONAR
        super.setImageURI(uri); //NOSONAR
        mBitmap = getBitmapFromDrawable(getDrawable()); //NOSONAR
        setup(); //NOSONAR
    } // NOSONAR

    private Bitmap getBitmapFromDrawable(Drawable drawable) { //NOSONAR
        if (drawable == null) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR

        if (drawable instanceof BitmapDrawable) { //NOSONAR
            return ((BitmapDrawable) drawable).getBitmap(); //NOSONAR
        } // NOSONAR

        try { //NOSONAR
            Bitmap bitmap; //NOSONAR

            if (drawable instanceof ColorDrawable) { //NOSONAR
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG); //NOSONAR
            } else { //NOSONAR
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG); //NOSONAR
            } // NOSONAR

            Canvas canvas = new Canvas(bitmap); //NOSONAR
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()); //NOSONAR
            drawable.draw(canvas); //NOSONAR
            return bitmap; //NOSONAR
        } catch (OutOfMemoryError e) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void setup() { //NOSONAR
        if (!mReady) { //NOSONAR
            mSetupPending = true; //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        if (mBitmap == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP); //NOSONAR

        mBitmapPaint.setAntiAlias(true); //NOSONAR
        mBitmapPaint.setShader(mBitmapShader); //NOSONAR
        mBitmapPaint.setColorFilter(mColorFilter); //NOSONAR

        mBorderPaint.setStyle(Paint.Style.FILL); //NOSONAR
        mBorderPaint.setAntiAlias(true); //NOSONAR
        mBorderPaint.setColor(mBorderColor); //NOSONAR
        mBorderPaint.setStrokeWidth(mBorderWidth); //NOSONAR

        mBitmapHeight = mBitmap.getHeight(); //NOSONAR
        mBitmapWidth = mBitmap.getWidth(); //NOSONAR

        mBorderRect.set(0, 0, getWidth(), getHeight()); //NOSONAR
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2); //NOSONAR

        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth); //NOSONAR
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2); //NOSONAR

        updateShaderMatrix(); //NOSONAR
        invalidate(); //NOSONAR
    } // NOSONAR

    private void updateShaderMatrix() { //NOSONAR
        float scale; //NOSONAR
        float dx = 0; //NOSONAR
        float dy = 0; //NOSONAR

        mShaderMatrix.set(null); //NOSONAR

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) { //NOSONAR
            scale = mDrawableRect.height() / (float) mBitmapHeight; //NOSONAR
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f; //NOSONAR
        } else { //NOSONAR
            scale = mDrawableRect.width() / (float) mBitmapWidth; //NOSONAR
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f; //NOSONAR
        } // NOSONAR

        mShaderMatrix.setScale(scale, scale); //NOSONAR
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth); //NOSONAR

        mBitmapShader.setLocalMatrix(mShaderMatrix); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        if (!isInEditMode()) { //NOSONAR
            aestheticDisposable = Aesthetic.get(getContext()).isDark() //NOSONAR
                    .subscribe(isDark -> { //NOSONAR
                        if (isDark) { //NOSONAR
                            setBorderColor(ContextCompat.getColor(getContext(), R.color.md_grey_900)); //NOSONAR
                        } else { //NOSONAR
                            setBorderColor(ContextCompat.getColor(getContext(), R.color.md_grey_250)); //NOSONAR
                        } // NOSONAR
                    }); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR

        aestheticDisposable.dispose(); //NOSONAR
    } // NOSONAR
} // NOSONAR
