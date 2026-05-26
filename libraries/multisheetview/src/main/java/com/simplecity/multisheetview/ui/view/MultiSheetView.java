package com.simplecity.multisheetview.ui.view; // NOSONAR

import android.animation.ValueAnimator; // NOSONAR
import android.annotation.SuppressLint; // NOSONAR
import android.content.Context; // NOSONAR
import android.support.annotation.IdRes; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.design.widget.BottomSheetBehavior; // NOSONAR
import android.support.design.widget.CoordinatorLayout; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.View; // NOSONAR

import com.simplecity.multisheetview.R; // NOSONAR
import com.simplecity.multisheetview.ui.behavior.CustomBottomSheetBehavior; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MultiSheetView extends CoordinatorLayout { //NOSONAR

    private static final String TAG = "MultiSheetView"; //NOSONAR

    public interface SheetStateChangeListener { //NOSONAR
        void onSheetStateChanged(@Sheet int sheet, @BottomSheetBehavior.State int state); //NOSONAR

        void onSlide(@Sheet int sheet, float slideOffset); //NOSONAR
    } // NOSONAR

    public @interface Sheet { //NOSONAR
        int NONE = 0; //NOSONAR
        int FIRST = 1; //NOSONAR
        int SECOND = 2; //NOSONAR
    } // NOSONAR

    private CustomBottomSheetBehavior bottomSheetBehavior1; //NOSONAR
    private CustomBottomSheetBehavior bottomSheetBehavior2; //NOSONAR

    @Nullable //NOSONAR
    private SheetStateChangeListener sheetStateChangeListener; //NOSONAR

    public MultiSheetView(Context context) { //NOSONAR
        this(context, null); //NOSONAR
    } // NOSONAR

    public MultiSheetView(@NonNull Context context, @Nullable AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    } // NOSONAR

    public MultiSheetView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR

        inflate(getContext(), R.layout.multi_sheet, this); //NOSONAR

        View sheet1 = findViewById(R.id.sheet1); //NOSONAR
        bottomSheetBehavior1 = (CustomBottomSheetBehavior) BottomSheetBehavior.from(sheet1); //NOSONAR
        bottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() { //NOSONAR
            @Override //NOSONAR
            public void onStateChanged(@NonNull View bottomSheet, int newState) { //NOSONAR
                fadeView(Sheet.FIRST, newState); //NOSONAR

                if (sheetStateChangeListener != null) { //NOSONAR
                    sheetStateChangeListener.onSheetStateChanged(Sheet.FIRST, newState); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { //NOSONAR
                fadeView(findViewById(getSheetPeekViewResId(Sheet.FIRST)), slideOffset); //NOSONAR

                if (sheetStateChangeListener != null) { //NOSONAR
                    sheetStateChangeListener.onSlide(Sheet.FIRST, slideOffset); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR

        View sheet2 = findViewById(R.id.sheet2); //NOSONAR
        bottomSheetBehavior2 = (CustomBottomSheetBehavior) BottomSheetBehavior.from(sheet2); //NOSONAR
        bottomSheetBehavior2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() { //NOSONAR
            @Override //NOSONAR
            public void onStateChanged(@NonNull View bottomSheet, int newState) { //NOSONAR
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_DRAGGING) { //NOSONAR
                    bottomSheetBehavior1.setAllowDragging(false); //NOSONAR
                } else { //NOSONAR
                    bottomSheetBehavior1.setAllowDragging(true); //NOSONAR
                } // NOSONAR

                fadeView(Sheet.SECOND, newState); //NOSONAR

                if (sheetStateChangeListener != null) { //NOSONAR
                    sheetStateChangeListener.onSheetStateChanged(Sheet.SECOND, newState); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { //NOSONAR
                bottomSheetBehavior1.setAllowDragging(false); //NOSONAR
                fadeView(findViewById(getSheetPeekViewResId(Sheet.SECOND)), slideOffset); //NOSONAR

                if (sheetStateChangeListener != null) { //NOSONAR
                    sheetStateChangeListener.onSlide(Sheet.SECOND, slideOffset); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR

        //First sheet view click listener // NOSONAR
        findViewById(getSheetPeekViewResId(Sheet.FIRST)).setOnClickListener(v -> //NOSONAR
                expandSheet(Sheet.FIRST)); //NOSONAR

        //Second sheet view click listener // NOSONAR
        findViewById(getSheetPeekViewResId(Sheet.SECOND)).setOnClickListener(v -> //NOSONAR
                expandSheet(Sheet.SECOND)); //NOSONAR

        findViewById(getSheetPeekViewResId(Sheet.SECOND)).setOnTouchListener((v, event) -> { //NOSONAR
            bottomSheetBehavior1.setAllowDragging(false); //NOSONAR
            bottomSheetBehavior2.setAllowDragging(true); //NOSONAR
            return false; //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    public void setSheetStateChangeListener(@Nullable SheetStateChangeListener sheetStateChangeListener) { //NOSONAR
        this.sheetStateChangeListener = sheetStateChangeListener; //NOSONAR
    } // NOSONAR

    public void expandSheet(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED); //NOSONAR
                break; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void collapseSheet(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED); //NOSONAR
                break; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public boolean isHidden() { //NOSONAR
        int peekHeight = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_sheet_peek_1_height); //NOSONAR
        return bottomSheetBehavior1.getPeekHeight() < peekHeight; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the peek height of sheet one to 0. // NOSONAR
     * // NOSONAR
     * @param collapse true if all expanded sheets should be collapsed. // NOSONAR
     * @param animate  true if the change in peek height should be animated // NOSONAR
     */ // NOSONAR
    public void hide(boolean collapse, boolean animate) { //NOSONAR
        if (!isHidden()) { //NOSONAR
            int peekHeight = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_sheet_peek_1_height); //NOSONAR
            if (animate) { //NOSONAR
                ValueAnimator valueAnimator = ValueAnimator.ofInt(peekHeight, 0); //NOSONAR
                valueAnimator.setDuration(200); //NOSONAR
                valueAnimator.addUpdateListener(valueAnimator1 -> bottomSheetBehavior1.setPeekHeight((Integer) valueAnimator1.getAnimatedValue())); //NOSONAR
                valueAnimator.start(); //NOSONAR
            } else { //NOSONAR
                bottomSheetBehavior1.setPeekHeight(0); //NOSONAR
            } // NOSONAR
            ((LayoutParams) findViewById(getMainContainerResId()).getLayoutParams()).bottomMargin = 0; //NOSONAR
            if (collapse) { //NOSONAR
                goToSheet(Sheet.NONE); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Restores the peek height to its default value. // NOSONAR
     * // NOSONAR
     * @param animate true if the change in peek height should be animated // NOSONAR
     */ // NOSONAR
    public void unhide(boolean animate) { //NOSONAR
        if (isHidden()) { //NOSONAR
            int peekHeight = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_sheet_peek_1_height); //NOSONAR
            int currentHeight = bottomSheetBehavior1.getPeekHeight(); //NOSONAR
            float ratio = 1 - (currentHeight / peekHeight); //NOSONAR
            if (animate) { //NOSONAR
                ValueAnimator valueAnimator = ValueAnimator.ofInt(bottomSheetBehavior1.getPeekHeight(), peekHeight); //NOSONAR
                valueAnimator.setDuration((long) (200 * ratio)); //NOSONAR
                valueAnimator.addUpdateListener(valueAnimator1 -> bottomSheetBehavior1.setPeekHeight((Integer) valueAnimator1.getAnimatedValue())); //NOSONAR
                valueAnimator.start(); //NOSONAR
            } else { //NOSONAR
                bottomSheetBehavior1.setPeekHeight(peekHeight); //NOSONAR
            } // NOSONAR
            ((LayoutParams) findViewById(getMainContainerResId()).getLayoutParams()).bottomMargin = peekHeight; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Expand the passed in sheet, collapsing/expanding the other sheet(s) as required. // NOSONAR
     */ // NOSONAR
    public void goToSheet(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.NONE: //NOSONAR
                collapseSheet(Sheet.FIRST); //NOSONAR
                collapseSheet(Sheet.SECOND); //NOSONAR
                break; //NOSONAR
            case Sheet.FIRST: //NOSONAR
                collapseSheet(Sheet.SECOND); //NOSONAR
                expandSheet(Sheet.FIRST); //NOSONAR
                break; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                expandSheet(Sheet.FIRST); //NOSONAR
                expandSheet(Sheet.SECOND); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return the currently expanded Sheet // NOSONAR
     */ // NOSONAR
    @Sheet //NOSONAR
    public int getCurrentSheet() { //NOSONAR
        if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
            return Sheet.SECOND; //NOSONAR
        } else if (bottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
            return Sheet.FIRST; //NOSONAR
        } else { //NOSONAR
            return Sheet.NONE; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void restoreSheet(@Sheet int sheet) { //NOSONAR
        goToSheet(sheet); //NOSONAR
        fadeView(Sheet.FIRST, bottomSheetBehavior1.getState()); //NOSONAR
        fadeView(Sheet.SECOND, bottomSheetBehavior2.getState()); //NOSONAR
    } // NOSONAR

    public boolean consumeBackPress() { //NOSONAR
        switch (getCurrentSheet()) { //NOSONAR
            case Sheet.SECOND: //NOSONAR
                collapseSheet(Sheet.SECOND); //NOSONAR
                return true; //NOSONAR
            case Sheet.FIRST: //NOSONAR
                collapseSheet(Sheet.FIRST); //NOSONAR
                return true; //NOSONAR
        } // NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    @IdRes //NOSONAR
    public int getMainContainerResId() { //NOSONAR
        return R.id.mainContainer; //NOSONAR
    } // NOSONAR

    @SuppressLint("DefaultLocale") //NOSONAR
    @IdRes //NOSONAR
    public int getSheetContainerViewResId(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                return R.id.sheet1Container; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                return R.id.sheet2Container; //NOSONAR
        } // NOSONAR

        throw new IllegalStateException(String.format("No container view resId found for sheet: %d", sheet)); //NOSONAR
    } // NOSONAR

    @SuppressLint("DefaultLocale") //NOSONAR
    @IdRes //NOSONAR
    public int getSheetPeekViewResId(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                return R.id.sheet1PeekView; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                return R.id.sheet2PeekView; //NOSONAR
        } // NOSONAR

        throw new IllegalStateException(String.format("No peek view resId found for sheet: %d", sheet)); //NOSONAR
    } // NOSONAR

    private void fadeView(@Sheet int sheet, @BottomSheetBehavior.State int state) { //NOSONAR
        if (state == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
            fadeView(findViewById(getSheetPeekViewResId(sheet)), 1f); //NOSONAR
        } else if (state == BottomSheetBehavior.STATE_COLLAPSED) { //NOSONAR
            fadeView(findViewById(getSheetPeekViewResId(sheet)), 0f); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void fadeView(View v, float offset) { //NOSONAR
        float alpha = 1 - offset; //NOSONAR
        v.setAlpha(alpha); //NOSONAR
        v.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * A helper method to return the first MultiSheetView parent of the passed in View, // NOSONAR
     * or null if none can be found. // NOSONAR
     * // NOSONAR
     * @param v the view whose hierarchy will be traversed. // NOSONAR
     * @return the first MultiSheetView of the passed in view, or null if none can be found. // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    public static MultiSheetView getParentMultiSheetView(@Nullable View v) { //NOSONAR
        if (v == null) return null; //NOSONAR

        if (v instanceof MultiSheetView) { //NOSONAR
            return (MultiSheetView) v; //NOSONAR
        } // NOSONAR

        if (v.getParent() instanceof View) { //NOSONAR
            return getParentMultiSheetView((View) v.getParent()); //NOSONAR
        } // NOSONAR

        return null; //NOSONAR
    } // NOSONAR
} // NOSONAR
