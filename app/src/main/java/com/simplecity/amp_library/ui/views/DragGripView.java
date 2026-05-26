package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.Resources; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Paint; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.Gravity; // NOSONAR
import android.view.View; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DragGripView extends View { //NOSONAR

    Disposable aestheticDisposable; //NOSONAR

    private static final int[] ATTRS = new int[] { //NOSONAR
            android.R.attr.gravity, //NOSONAR
            android.R.attr.color, //NOSONAR
    }; // NOSONAR

    private static final int HORIZ_RIDGES = 2; //NOSONAR

    private int mGravity = Gravity.START; //NOSONAR
    private int mColor = 0x33333333; //NOSONAR

    private Paint mRidgePaint; //NOSONAR

    private float mRidgeSize; //NOSONAR
    private float mRidgeGap; //NOSONAR

    private int mWidth; //NOSONAR
    private int mHeight; //NOSONAR

    public DragGripView(Context context) { //NOSONAR
        this(context, null, 0); //NOSONAR
    } // NOSONAR

    public DragGripView(Context context, AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    } // NOSONAR

    public DragGripView(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR

        final TypedArray a = context.obtainStyledAttributes(attrs, ATTRS); //NOSONAR
        mGravity = a.getInteger(0, mGravity); //NOSONAR
        mColor = a.getColor(1, mColor); //NOSONAR
        a.recycle(); //NOSONAR

        final Resources res = getResources(); //NOSONAR
        mRidgeSize = res.getDimensionPixelSize(R.dimen.drag_grip_ridge_size); //NOSONAR
        mRidgeGap = res.getDimensionPixelSize(R.dimen.drag_grip_ridge_gap); //NOSONAR

        mRidgePaint = new Paint(); //NOSONAR
        mRidgePaint.setColor(mColor); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        setMeasuredDimension(View.resolveSize((int) (HORIZ_RIDGES //NOSONAR
                        * (mRidgeSize + mRidgeGap) - mRidgeGap) // NOSONAR
                        + getPaddingLeft() + getPaddingRight(), widthMeasureSpec), //NOSONAR
                View.resolveSize((int) mRidgeSize, heightMeasureSpec)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDraw(Canvas canvas) { //NOSONAR
        super.onDraw(canvas); //NOSONAR

        float drawWidth = HORIZ_RIDGES * (mRidgeSize + mRidgeGap) - mRidgeGap; //NOSONAR
        float drawLeft; //NOSONAR

        switch (mGravity & Gravity.HORIZONTAL_GRAVITY_MASK) { //NOSONAR
            case Gravity.CENTER_HORIZONTAL: //NOSONAR
                drawLeft = getPaddingLeft() //NOSONAR
                        + ((mWidth - getPaddingLeft() - getPaddingRight()) - drawWidth) //NOSONAR
                        / 2; //NOSONAR
                break; //NOSONAR
            case Gravity.END: //NOSONAR
                drawLeft = getWidth() - getPaddingRight() - drawWidth; //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                drawLeft = getPaddingLeft(); //NOSONAR
        } // NOSONAR

        int vertRidges = (int) ((mHeight - getPaddingTop() - getPaddingBottom() + mRidgeGap) / (mRidgeSize + mRidgeGap)); //NOSONAR
        float drawHeight = vertRidges * (mRidgeSize + mRidgeGap) - mRidgeGap; //NOSONAR
        float drawTop = getPaddingTop() //NOSONAR
                + ((mHeight - getPaddingTop() - getPaddingBottom()) - drawHeight) //NOSONAR
                / 2; //NOSONAR

        for (int y = 0; y < vertRidges; y++) { //NOSONAR
            for (int x = 0; x < HORIZ_RIDGES; x++) { //NOSONAR
                canvas.drawRect(drawLeft + x * (mRidgeSize + mRidgeGap), //NOSONAR
                        drawTop + y * (mRidgeSize + mRidgeGap), drawLeft + x //NOSONAR
                                * (mRidgeSize + mRidgeGap) + mRidgeSize, // NOSONAR
                        drawTop + y * (mRidgeSize + mRidgeGap) + mRidgeSize, //NOSONAR
                        mRidgePaint); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { //NOSONAR
        super.onSizeChanged(w, h, oldw, oldh); //NOSONAR
        mHeight = h; //NOSONAR
        mWidth = w; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        aestheticDisposable = Aesthetic.get(getContext()) //NOSONAR
                .textColorSecondary() //NOSONAR
                .subscribe(color -> { //NOSONAR
                    mColor = color; //NOSONAR
                    mRidgePaint.setColor(color); //NOSONAR
                    invalidate(); //NOSONAR
                }); // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR

        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR
} // NOSONAR
