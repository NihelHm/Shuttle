package com.simplecity.multisheetview.ui.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.simplecity.multisheetview.R;
import com.simplecity.multisheetview.ui.behavior.CustomBottomSheetBehavior;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MultiSheetView extends CoordinatorLayout { //NOSONAR

    private static final String TAG = "MultiSheetView"; //NOSONAR

    public interface SheetStateChangeListener { //NOSONAR
        void onSheetStateChanged(@Sheet int sheet, @BottomSheetBehavior.State int state); //NOSONAR

        void onSlide(@Sheet int sheet, float slideOffset); //NOSONAR
    }

    public @interface Sheet { //NOSONAR
        int NONE = 0; //NOSONAR
        int FIRST = 1; //NOSONAR
        int SECOND = 2; //NOSONAR
    }

    private CustomBottomSheetBehavior bottomSheetBehavior1; //NOSONAR
    private CustomBottomSheetBehavior bottomSheetBehavior2; //NOSONAR

    @Nullable //NOSONAR
    private SheetStateChangeListener sheetStateChangeListener; //NOSONAR

    public MultiSheetView(Context context) { //NOSONAR
        this(context, null); //NOSONAR
    }

    public MultiSheetView(@NonNull Context context, @Nullable AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    }

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
                }
            }

            @Override //NOSONAR
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { //NOSONAR
                fadeView(findViewById(getSheetPeekViewResId(Sheet.FIRST)), slideOffset); //NOSONAR

                if (sheetStateChangeListener != null) { //NOSONAR
                    sheetStateChangeListener.onSlide(Sheet.FIRST, slideOffset); //NOSONAR
                }
            }
        });

        View sheet2 = findViewById(R.id.sheet2); //NOSONAR
        bottomSheetBehavior2 = (CustomBottomSheetBehavior) BottomSheetBehavior.from(sheet2); //NOSONAR
        bottomSheetBehavior2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() { //NOSONAR
            @Override //NOSONAR
            public void onStateChanged(@NonNull View bottomSheet, int newState) { //NOSONAR
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_DRAGGING) { //NOSONAR
                    bottomSheetBehavior1.setAllowDragging(false); //NOSONAR
                } else { //NOSONAR
                    bottomSheetBehavior1.setAllowDragging(true); //NOSONAR
                }

                fadeView(Sheet.SECOND, newState); //NOSONAR

                if (sheetStateChangeListener != null) { //NOSONAR
                    sheetStateChangeListener.onSheetStateChanged(Sheet.SECOND, newState); //NOSONAR
                }
            }

            @Override //NOSONAR
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { //NOSONAR
                bottomSheetBehavior1.setAllowDragging(false); //NOSONAR
                fadeView(findViewById(getSheetPeekViewResId(Sheet.SECOND)), slideOffset); //NOSONAR

                if (sheetStateChangeListener != null) { //NOSONAR
                    sheetStateChangeListener.onSlide(Sheet.SECOND, slideOffset); //NOSONAR
                }
            }
        });

        //First sheet view click listener
        findViewById(getSheetPeekViewResId(Sheet.FIRST)).setOnClickListener(v -> //NOSONAR
                expandSheet(Sheet.FIRST)); //NOSONAR

        //Second sheet view click listener
        findViewById(getSheetPeekViewResId(Sheet.SECOND)).setOnClickListener(v -> //NOSONAR
                expandSheet(Sheet.SECOND)); //NOSONAR

        findViewById(getSheetPeekViewResId(Sheet.SECOND)).setOnTouchListener((v, event) -> { //NOSONAR
            bottomSheetBehavior1.setAllowDragging(false); //NOSONAR
            bottomSheetBehavior2.setAllowDragging(true); //NOSONAR
            return false; //NOSONAR
        });
    }

    public void setSheetStateChangeListener(@Nullable SheetStateChangeListener sheetStateChangeListener) { //NOSONAR
        this.sheetStateChangeListener = sheetStateChangeListener; //NOSONAR
    }

    public void expandSheet(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED); //NOSONAR
                break; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED); //NOSONAR
                break; //NOSONAR
        }
    }

    public void collapseSheet(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED); //NOSONAR
                break; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED); //NOSONAR
                break; //NOSONAR
        }
    }

    public boolean isHidden() { //NOSONAR
        int peekHeight = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_sheet_peek_1_height); //NOSONAR
        return bottomSheetBehavior1.getPeekHeight() < peekHeight; //NOSONAR
    }

    /**
     * Sets the peek height of sheet one to 0.
     *
     * @param collapse true if all expanded sheets should be collapsed.
     * @param animate  true if the change in peek height should be animated
     */
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
            }
            ((LayoutParams) findViewById(getMainContainerResId()).getLayoutParams()).bottomMargin = 0; //NOSONAR
            if (collapse) { //NOSONAR
                goToSheet(Sheet.NONE); //NOSONAR
            }
        }
    }

    /**
     * Restores the peek height to its default value.
     *
     * @param animate true if the change in peek height should be animated
     */
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
            }
            ((LayoutParams) findViewById(getMainContainerResId()).getLayoutParams()).bottomMargin = peekHeight; //NOSONAR
        }
    }

    /**
     * Expand the passed in sheet, collapsing/expanding the other sheet(s) as required.
     */
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
        }
    }

    /**
     * @return the currently expanded Sheet
     */
    @Sheet //NOSONAR
    public int getCurrentSheet() { //NOSONAR
        if (bottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
            return Sheet.SECOND; //NOSONAR
        } else if (bottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
            return Sheet.FIRST; //NOSONAR
        } else { //NOSONAR
            return Sheet.NONE; //NOSONAR
        }
    }

    public void restoreSheet(@Sheet int sheet) { //NOSONAR
        goToSheet(sheet); //NOSONAR
        fadeView(Sheet.FIRST, bottomSheetBehavior1.getState()); //NOSONAR
        fadeView(Sheet.SECOND, bottomSheetBehavior2.getState()); //NOSONAR
    }

    public boolean consumeBackPress() { //NOSONAR
        switch (getCurrentSheet()) { //NOSONAR
            case Sheet.SECOND: //NOSONAR
                collapseSheet(Sheet.SECOND); //NOSONAR
                return true; //NOSONAR
            case Sheet.FIRST: //NOSONAR
                collapseSheet(Sheet.FIRST); //NOSONAR
                return true; //NOSONAR
        }
        return false; //NOSONAR
    }

    @IdRes //NOSONAR
    public int getMainContainerResId() { //NOSONAR
        return R.id.mainContainer; //NOSONAR
    }

    @SuppressLint("DefaultLocale") //NOSONAR
    @IdRes //NOSONAR
    public int getSheetContainerViewResId(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                return R.id.sheet1Container; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                return R.id.sheet2Container; //NOSONAR
        }

        throw new IllegalStateException(String.format("No container view resId found for sheet: %d", sheet)); //NOSONAR
    }

    @SuppressLint("DefaultLocale") //NOSONAR
    @IdRes //NOSONAR
    public int getSheetPeekViewResId(@Sheet int sheet) { //NOSONAR
        switch (sheet) { //NOSONAR
            case Sheet.FIRST: //NOSONAR
                return R.id.sheet1PeekView; //NOSONAR
            case Sheet.SECOND: //NOSONAR
                return R.id.sheet2PeekView; //NOSONAR
        }

        throw new IllegalStateException(String.format("No peek view resId found for sheet: %d", sheet)); //NOSONAR
    }

    private void fadeView(@Sheet int sheet, @BottomSheetBehavior.State int state) { //NOSONAR
        if (state == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
            fadeView(findViewById(getSheetPeekViewResId(sheet)), 1f); //NOSONAR
        } else if (state == BottomSheetBehavior.STATE_COLLAPSED) { //NOSONAR
            fadeView(findViewById(getSheetPeekViewResId(sheet)), 0f); //NOSONAR
        }
    }

    private void fadeView(View v, float offset) { //NOSONAR
        float alpha = 1 - offset; //NOSONAR
        v.setAlpha(alpha); //NOSONAR
        v.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE); //NOSONAR
    }

    /**
     * A helper method to return the first MultiSheetView parent of the passed in View,
     * or null if none can be found.
     *
     * @param v the view whose hierarchy will be traversed.
     * @return the first MultiSheetView of the passed in view, or null if none can be found.
     */
    @Nullable //NOSONAR
    public static MultiSheetView getParentMultiSheetView(@Nullable View v) { //NOSONAR
        if (v == null) return null; //NOSONAR

        if (v instanceof MultiSheetView) { //NOSONAR
            return (MultiSheetView) v; //NOSONAR
        }

        if (v.getParent() instanceof View) { //NOSONAR
            return getParentMultiSheetView((View) v.getParent()); //NOSONAR
        }

        return null; //NOSONAR
    }
}
