package android.support.design.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.animation.AnimationUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.math.MathUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.ViewGroupUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.simplecity.amp_library.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomCollapsingToolbarLayout extends FrameLayout { //NOSONAR

    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600; //NOSONAR

    private boolean mRefreshToolbar = true; //NOSONAR
    private int mToolbarId; //NOSONAR
    private Toolbar mToolbar; //NOSONAR
    private View mToolbarDirectChild; //NOSONAR
    private View mDummyView; //NOSONAR

    private int mExpandedMarginStart; //NOSONAR
    private int mExpandedMarginTop; //NOSONAR
    private int mExpandedMarginEnd; //NOSONAR
    private int mExpandedMarginBottom; //NOSONAR

    private final Rect mTmpRect = new Rect(); //NOSONAR
    final CustomCollapsingTextHelper mCollapsingTextHelper; //NOSONAR
    private boolean mCollapsingTitleEnabled; //NOSONAR
    private boolean mDrawCollapsingTitle; //NOSONAR

    Drawable mStatusBarScrim; //NOSONAR
    int mCurrentOffset; //NOSONAR
    private int mScrimAlpha; //NOSONAR
    private boolean mScrimsAreShown; //NOSONAR
    private ValueAnimator mScrimAnimator; //NOSONAR
    private long mScrimAnimationDuration; //NOSONAR
    private int mScrimVisibleHeightTrigger = -1; //NOSONAR

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener; //NOSONAR

    WindowInsetsCompat mLastInsets; //NOSONAR

    private int mToolbarDrawIndex; //NOSONAR
    private Drawable mContentScrim; //NOSONAR

    public CustomCollapsingToolbarLayout(Context context) { //NOSONAR
        this(context, null); //NOSONAR
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR

        mCollapsingTextHelper = new CustomCollapsingTextHelper(this); //NOSONAR
        mCollapsingTextHelper.setTextSizeInterpolator((Interpolator) AnimationUtils.DECELERATE_INTERPOLATOR); //NOSONAR

        TypedArray a1 = context.obtainStyledAttributes(attrs, //NOSONAR
                R.styleable.CollapsingToolbarLayout, defStyleAttr, //NOSONAR
                R.style.Widget_Design_CollapsingToolbar); //NOSONAR

        mCollapsingTextHelper.setExpandedTextGravity( //NOSONAR
                a1.getInt(R.styleable.CollapsingToolbarLayout_expandedTitleGravity, //NOSONAR
                        GravityCompat.START | Gravity.BOTTOM)); //NOSONAR
        mCollapsingTextHelper.setCollapsedTextGravity( //NOSONAR
                a1.getInt(R.styleable.CollapsingToolbarLayout_collapsedTitleGravity, //NOSONAR
                        GravityCompat.START | Gravity.CENTER_VERTICAL)); //NOSONAR

        mExpandedMarginStart = mExpandedMarginTop = mExpandedMarginEnd = mExpandedMarginBottom = //NOSONAR
                a1.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0); //NOSONAR

        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) { //NOSONAR
            mExpandedMarginStart = a1.getDimensionPixelSize( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0); //NOSONAR
        }
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) { //NOSONAR
            mExpandedMarginEnd = a1.getDimensionPixelSize( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0); //NOSONAR
        }
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) { //NOSONAR
            mExpandedMarginTop = a1.getDimensionPixelSize( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0); //NOSONAR
        }
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) { //NOSONAR
            mExpandedMarginBottom = a1.getDimensionPixelSize( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0); //NOSONAR
        }

        mCollapsingTitleEnabled = a1.getBoolean( //NOSONAR
                R.styleable.CollapsingToolbarLayout_titleEnabled, true); //NOSONAR
        setTitle(a1.getText(R.styleable.CollapsingToolbarLayout_title)); //NOSONAR

        // begin modification
        TypedArray a2 = context.obtainStyledAttributes(attrs, //NOSONAR
                R.styleable.CustomCollapsingToolbarLayout, defStyleAttr, //NOSONAR
                R.style.CustomCollapsingToolbarLayout); //NOSONAR
        if (a2.hasValue(R.styleable.CustomCollapsingToolbarLayout_subtitle)) //NOSONAR
            setSubtitle(a2.getText(R.styleable.CustomCollapsingToolbarLayout_subtitle).toString()); //NOSONAR

        //load default appearances first
        mCollapsingTextHelper.setCollapsedSubAppearance(R.style.CollapsedSubtitleAppearance); //NOSONAR
        mCollapsingTextHelper.setExpandedSubAppearance(R.style.ExpandedSubtitleAppearance); //NOSONAR

        // now apply custom sub appearance
        if (a2.hasValue(R.styleable.CustomCollapsingToolbarLayout_collapsedSubtitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setCollapsedSubAppearance( //NOSONAR
                    a2.getResourceId(R.styleable.CustomCollapsingToolbarLayout_collapsedSubtitleTextAppearance, 0) //NOSONAR
            );
        }

        if (a2.hasValue(R.styleable.CustomCollapsingToolbarLayout_expandedSubtitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setExpandedSubAppearance( //NOSONAR
                    a2.getResourceId(R.styleable.CustomCollapsingToolbarLayout_expandedSubtitleTextAppearance, 0) //NOSONAR
            );
        }
        // end

        // First load the default text appearances
        mCollapsingTextHelper.setExpandedTextAppearance( //NOSONAR
                R.style.TextAppearance_Design_CollapsingToolbar_Expanded); //NOSONAR
        mCollapsingTextHelper.setCollapsedTextAppearance( //NOSONAR
                android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title); //NOSONAR

        // Now overlay any custom text appearances
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setExpandedTextAppearance( //NOSONAR
                    a1.getResourceId( //NOSONAR
                            R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0)); //NOSONAR
        }
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setCollapsedTextAppearance( //NOSONAR
                    a1.getResourceId( //NOSONAR
                            R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0)); //NOSONAR
        }

        mScrimVisibleHeightTrigger = a1.getDimensionPixelSize( //NOSONAR
                R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1); //NOSONAR

        mScrimAnimationDuration = a1.getInt( //NOSONAR
                R.styleable.CollapsingToolbarLayout_scrimAnimationDuration, //NOSONAR
                DEFAULT_SCRIM_ANIMATION_DURATION); //NOSONAR

        setContentScrim(a1.getDrawable(R.styleable.CollapsingToolbarLayout_contentScrim)); //NOSONAR
        setStatusBarScrim(a1.getDrawable(R.styleable.CollapsingToolbarLayout_statusBarScrim)); //NOSONAR

        mToolbarId = a1.getResourceId(R.styleable.CollapsingToolbarLayout_toolbarId, -1); //NOSONAR

        a1.recycle(); //NOSONAR
        a2.recycle(); //NOSONAR

        setWillNotDraw(false); //NOSONAR

        ViewCompat.setOnApplyWindowInsetsListener(this, //NOSONAR
                (v, insets) -> onWindowInsetChanged(insets)); //NOSONAR
    }

    private static int getHeightWithMargins(@NonNull final View view) { //NOSONAR
        final ViewGroup.LayoutParams lp = view.getLayoutParams(); //NOSONAR
        if (lp instanceof MarginLayoutParams) { //NOSONAR
            final MarginLayoutParams mlp = (MarginLayoutParams) lp; //NOSONAR
            return view.getHeight() + mlp.topMargin + mlp.bottomMargin; //NOSONAR
        }
        return view.getHeight(); //NOSONAR
    }

    static ViewOffsetHelper getViewOffsetHelper(View view) { //NOSONAR
        ViewOffsetHelper offsetHelper = (ViewOffsetHelper) view.getTag(R.id.view_offset_helper); //NOSONAR
        if (offsetHelper == null) { //NOSONAR
            offsetHelper = new ViewOffsetHelper(view); //NOSONAR
            view.setTag(R.id.view_offset_helper, offsetHelper); //NOSONAR
        }
        return offsetHelper; //NOSONAR
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        // Add an OnOffsetChangedListener if possible
        final ViewParent parent = getParent(); //NOSONAR
        if (parent instanceof AppBarLayout) { //NOSONAR
            // Copy over from the ABL whether we should fit system windows
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent)); //NOSONAR

            if (mOnOffsetChangedListener == null) { //NOSONAR
                mOnOffsetChangedListener = new OffsetUpdateListener(); //NOSONAR
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOnOffsetChangedListener); //NOSONAR

            // We're attached, so lets request an inset dispatch
            ViewCompat.requestApplyInsets(this); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        // Remove our OnOffsetChangedListener if possible and it exists
        final ViewParent parent = getParent(); //NOSONAR
        if (mOnOffsetChangedListener != null && parent instanceof AppBarLayout) { //NOSONAR
            ((AppBarLayout) parent).removeOnOffsetChangedListener(mOnOffsetChangedListener); //NOSONAR
        }

        super.onDetachedFromWindow(); //NOSONAR
    }

    WindowInsetsCompat onWindowInsetChanged(final WindowInsetsCompat insets) { //NOSONAR
        WindowInsetsCompat newInsets = null; //NOSONAR

        if (ViewCompat.getFitsSystemWindows(this)) { //NOSONAR
            // If we're set to fit system windows, keep the insets
            newInsets = insets; //NOSONAR
        }

        // If our insets have changed, keep them and invalidate the scroll ranges...
        if (mLastInsets != newInsets) { //NOSONAR
            mLastInsets = newInsets; //NOSONAR
            requestLayout(); //NOSONAR
        }

        // Consume the insets. This is done so that child views with fitSystemWindows=true do not
        // get the default padding functionality from View
        return insets.consumeSystemWindowInsets(); //NOSONAR
    }

    @Override //NOSONAR
    public void draw(Canvas canvas) { //NOSONAR
        super.draw(canvas); //NOSONAR

        // If we don't have a toolbar, the scrim will be not be drawn in drawChild() below.
        // Instead, we draw it here, before our collapsing text.
        ensureToolbar(); //NOSONAR
        if (mToolbar == null && mContentScrim != null && mScrimAlpha > 0) { //NOSONAR
            mContentScrim.mutate().setAlpha(mScrimAlpha); //NOSONAR
            mContentScrim.draw(canvas); //NOSONAR
        }

        // Let the collapsing text helper draw its text
        if (mCollapsingTitleEnabled && mDrawCollapsingTitle) { //NOSONAR
            mCollapsingTextHelper.draw(canvas); //NOSONAR
        }

        // Now draw the status bar scrim
        if (mStatusBarScrim != null && mScrimAlpha > 0) { //NOSONAR
            final int topInset = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0; //NOSONAR
            if (topInset > 0) { //NOSONAR
                mStatusBarScrim.setBounds(0, -mCurrentOffset, getWidth(), //NOSONAR
                        topInset - mCurrentOffset); //NOSONAR
                mStatusBarScrim.mutate().setAlpha(mScrimAlpha); //NOSONAR
                mStatusBarScrim.draw(canvas); //NOSONAR
            }
        }
    }

    @Override //NOSONAR
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) { //NOSONAR
        // This is a little weird. Our scrim needs to be behind the Toolbar (if it is present),
        // but in front of any other children which are behind it. To do this we intercept the
        // drawChild() call, and draw our scrim after the preceding view is drawn
        boolean invalidate = super.drawChild(canvas, child, drawingTime); //NOSONAR

        if (mContentScrim != null && mScrimAlpha > 0 && isToolbarChildDrawnNext(child)) { //NOSONAR
            mContentScrim.mutate().setAlpha(mScrimAlpha); //NOSONAR
            mContentScrim.draw(canvas); //NOSONAR
            invalidate = true; //NOSONAR
        }

        return invalidate; //NOSONAR
    }

    @Override //NOSONAR
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { //NOSONAR
        super.onSizeChanged(w, h, oldw, oldh); //NOSONAR
        if (mContentScrim != null) { //NOSONAR
            mContentScrim.setBounds(0, 0, w, h); //NOSONAR
        }
    }

    private void ensureToolbar() { //NOSONAR
        if (!mRefreshToolbar) { //NOSONAR
            return; //NOSONAR
        }

        // First clear out the current Toolbar
        mToolbar = null; //NOSONAR
        mToolbarDirectChild = null; //NOSONAR

        if (mToolbarId != -1) { //NOSONAR
            // If we have an ID set, try and find it and it's direct parent to us
            mToolbar = findViewById(mToolbarId); //NOSONAR
            if (mToolbar != null) { //NOSONAR
                mToolbarDirectChild = findDirectChild(mToolbar); //NOSONAR
            }
        }

        if (mToolbar == null) { //NOSONAR
            // If we don't have an ID, or couldn't find a Toolbar with the correct ID, try and find
            // one from our direct children
            Toolbar toolbar = null; //NOSONAR
            for (int i = 0, count = getChildCount(); i < count; i++) { //NOSONAR
                final View child = getChildAt(i); //NOSONAR
                if (child instanceof Toolbar) { //NOSONAR
                    toolbar = (Toolbar) child; //NOSONAR
                    break; //NOSONAR
                }
            }
            mToolbar = toolbar; //NOSONAR
        }

        updateDummyView(); //NOSONAR
        mRefreshToolbar = false; //NOSONAR
    }

    private boolean isToolbarChildDrawnNext(View child) { //NOSONAR
        return mToolbarDrawIndex >= 0 && mToolbarDrawIndex == indexOfChild(child) + 1; //NOSONAR
    }

    /**
     * Returns the direct child of this layout, which itself is the ancestor of the
     * given view.
     */
    private View findDirectChild(final View descendant) { //NOSONAR
        View directChild = descendant; //NOSONAR
        for (ViewParent p = descendant.getParent(); p != this && p != null; p = p.getParent()) { //NOSONAR
            if (p instanceof View) { //NOSONAR
                directChild = (View) p; //NOSONAR
            }
        }
        return directChild; //NOSONAR
    }

    private void updateDummyView() { //NOSONAR
        if (!mCollapsingTitleEnabled && mDummyView != null) { //NOSONAR
            // If we have a dummy view and we have our title disabled, remove it from its parent
            final ViewParent parent = mDummyView.getParent(); //NOSONAR
            if (parent instanceof ViewGroup) { //NOSONAR
                ((ViewGroup) parent).removeView(mDummyView); //NOSONAR
            }
        }
        if (mCollapsingTitleEnabled && mToolbar != null) { //NOSONAR
            if (mDummyView == null) { //NOSONAR
                mDummyView = new View(getContext()); //NOSONAR
            }
            if (mDummyView.getParent() == null) { //NOSONAR
                mToolbar.addView(mDummyView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); //NOSONAR
            }
        }
    }

    @Override //NOSONAR
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        ensureToolbar(); //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR
    }

    @Override //NOSONAR
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) { //NOSONAR
        super.onLayout(changed, left, top, right, bottom); //NOSONAR

        if (mLastInsets != null) { //NOSONAR
            // Shift down any views which are not set to fit system windows
            final int insetTop = mLastInsets.getSystemWindowInsetTop(); //NOSONAR
            for (int i = 0, z = getChildCount(); i < z; i++) { //NOSONAR
                final View child = getChildAt(i); //NOSONAR
                if (!ViewCompat.getFitsSystemWindows(child)) { //NOSONAR
                    if (child.getTop() < insetTop) { //NOSONAR
                        // If the child isn't set to fit system windows but is drawing within
                        // the inset offset it down
                        ViewCompat.offsetTopAndBottom(child, insetTop); //NOSONAR
                    }
                }
            }
        }

        // Update the collapsed bounds by getting it's transformed bounds
        if (mCollapsingTitleEnabled && mDummyView != null) { //NOSONAR
            // We only draw the title if the dummy view is being displayed (Toolbar removes
            // views if there is no space)
            mDrawCollapsingTitle = ViewCompat.isAttachedToWindow(mDummyView) //NOSONAR
                    && mDummyView.getVisibility() == VISIBLE; //NOSONAR

            if (mDrawCollapsingTitle) { //NOSONAR
                final boolean isRtl = ViewCompat.getLayoutDirection(this) //NOSONAR
                        == ViewCompat.LAYOUT_DIRECTION_RTL; //NOSONAR

                // Update the collapsed bounds
                final int maxOffset = getMaxOffsetForPinChild( //NOSONAR
                        mToolbarDirectChild != null ? mToolbarDirectChild : mToolbar); //NOSONAR
                ViewGroupUtils.getDescendantRect(this, mDummyView, mTmpRect); //NOSONAR
                mCollapsingTextHelper.setCollapsedBounds( //NOSONAR
                        mTmpRect.left + (isRtl //NOSONAR
                                ? mToolbar.getTitleMarginEnd() //NOSONAR
                                : mToolbar.getTitleMarginStart()), //NOSONAR
                        mTmpRect.top + maxOffset + mToolbar.getTitleMarginTop(), //NOSONAR
                        mTmpRect.right + (isRtl //NOSONAR
                                ? mToolbar.getTitleMarginStart() //NOSONAR
                                : mToolbar.getTitleMarginEnd()), //NOSONAR
                        mTmpRect.bottom + maxOffset - mToolbar.getTitleMarginBottom()); //NOSONAR

                // Update the expanded bounds
                mCollapsingTextHelper.setExpandedBounds( //NOSONAR
                        isRtl ? mExpandedMarginEnd : mExpandedMarginStart, //NOSONAR
                        mTmpRect.top + mExpandedMarginTop, //NOSONAR
                        right - left - (isRtl ? mExpandedMarginStart : mExpandedMarginEnd), //NOSONAR
                        bottom - top - mExpandedMarginBottom); //NOSONAR
                // Now recalculate using the new bounds
                mCollapsingTextHelper.recalculate(); //NOSONAR
            }
        }

        // Update our child view offset helpers. This needs to be done after the title has been
        // setup, so that any Toolbars are in their original position
        for (int i = 0, z = getChildCount(); i < z; i++) { //NOSONAR
            getViewOffsetHelper(getChildAt(i)).onViewLayout(); //NOSONAR
        }

        // Finally, set our minimum height to enable proper AppBarLayout collapsing
        if (mToolbar != null) { //NOSONAR
            if (mCollapsingTitleEnabled && TextUtils.isEmpty(mCollapsingTextHelper.getText())) { //NOSONAR
                // If we do not currently have a title, try and grab it from the Toolbar
                mCollapsingTextHelper.setText(mToolbar.getTitle()); //NOSONAR
            }
            if (mToolbarDirectChild == null || mToolbarDirectChild == this) { //NOSONAR
                setMinimumHeight(getHeightWithMargins(mToolbar)); //NOSONAR
                mToolbarDrawIndex = indexOfChild(mToolbar); //NOSONAR
            } else { //NOSONAR
                setMinimumHeight(getHeightWithMargins(mToolbarDirectChild)); //NOSONAR
                mToolbarDrawIndex = indexOfChild(mToolbarDirectChild); //NOSONAR
            }
        } else { //NOSONAR
            mToolbarDrawIndex = -1; //NOSONAR
        }

        updateScrimVisibility(); //NOSONAR
    }

    /**
     * Returns the title currently being displayed by this view. If the title is not enabled, then
     * this will return {@code null}.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_title
     */
    @Nullable //NOSONAR
    public CharSequence getTitle() { //NOSONAR
        return mCollapsingTitleEnabled ? mCollapsingTextHelper.getText() : null; //NOSONAR
    }

    /**
     * Sets the title to be displayed by this view, if enabled.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_title
     * @see #setTitleEnabled(boolean)
     * @see #getTitle()
     */
    public void setTitle(@Nullable CharSequence title) { //NOSONAR
        mCollapsingTextHelper.setText(title); //NOSONAR
    }

    // begin modification
    public void setSubtitle(@Nullable CharSequence subtitle) { //NOSONAR
        mCollapsingTextHelper.setSubtitle(subtitle); //NOSONAR
    }
    // end modif

    /**
     * Returns whether this view is currently displaying its own title.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_titleEnabled
     * @see #setTitleEnabled(boolean)
     */
    public boolean isTitleEnabled() { //NOSONAR
        return mCollapsingTitleEnabled; //NOSONAR
    }

    /**
     * Sets whether this view should display its own title.
     * <p>
     * <p>The title displayed by this view will shrink and grow based on the scroll offset.</p>
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_titleEnabled
     * @see #setTitle(CharSequence)
     * @see #isTitleEnabled()
     */
    public void setTitleEnabled(boolean enabled) { //NOSONAR
        if (enabled != mCollapsingTitleEnabled) { //NOSONAR
            mCollapsingTitleEnabled = enabled; //NOSONAR
            updateDummyView(); //NOSONAR
            requestLayout(); //NOSONAR
        }
    }

    /**
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change
     * in the vertical scroll may overwrite this value. Any visibility change will be animated if
     * this view has already been laid out.
     *
     * @param shown whether the scrims should be shown
     * @see #getStatusBarScrim()
     * @see #getContentScrim()
     */
    public void setScrimsShown(boolean shown) { //NOSONAR
        setScrimsShown(shown, ViewCompat.isLaidOut(this) && !isInEditMode()); //NOSONAR
    }

    /**
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change
     * in the vertical scroll may overwrite this value.
     *
     * @param shown   whether the scrims should be shown
     * @param animate whether to animate the visibility change
     * @see #getStatusBarScrim()
     * @see #getContentScrim()
     */
    public void setScrimsShown(boolean shown, boolean animate) { //NOSONAR
        if (mScrimsAreShown != shown) { //NOSONAR
            if (animate) { //NOSONAR
                animateScrim(shown ? 0xFF : 0x0); //NOSONAR
            } else { //NOSONAR
                setScrimAlpha(shown ? 0xFF : 0x0); //NOSONAR
            }
            mScrimsAreShown = shown; //NOSONAR
        }
    }

    private void animateScrim(int targetAlpha) { //NOSONAR
        ensureToolbar(); //NOSONAR
        if (mScrimAnimator == null) { //NOSONAR
            mScrimAnimator = new ValueAnimator(); //NOSONAR
            mScrimAnimator.setDuration(mScrimAnimationDuration); //NOSONAR
            mScrimAnimator.setInterpolator( //NOSONAR
                    targetAlpha > mScrimAlpha //NOSONAR
                            ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR //NOSONAR
                            : AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR); //NOSONAR
            mScrimAnimator.addUpdateListener(animator -> setScrimAlpha((Integer) animator.getAnimatedValue())); //NOSONAR
        } else if (mScrimAnimator.isRunning()) { //NOSONAR
            mScrimAnimator.cancel(); //NOSONAR
        }

        mScrimAnimator.setIntValues(mScrimAlpha, targetAlpha); //NOSONAR
        mScrimAnimator.start(); //NOSONAR
    }

    void setScrimAlpha(int alpha) { //NOSONAR
        if (alpha != mScrimAlpha) { //NOSONAR
            final Drawable contentScrim = mContentScrim; //NOSONAR
            if (contentScrim != null && mToolbar != null) { //NOSONAR
                ViewCompat.postInvalidateOnAnimation(mToolbar); //NOSONAR
            }
            mScrimAlpha = alpha; //NOSONAR
            ViewCompat.postInvalidateOnAnimation(CustomCollapsingToolbarLayout.this); //NOSONAR
        }
    }

    /**
     * Set the color to use for the content scrim.
     *
     * @param color the color to display
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #getContentScrim()
     */
    public void setContentScrimColor(@ColorInt int color) { //NOSONAR
        setContentScrim(new ColorDrawable(color)); //NOSONAR
    }

    /**
     * Set the drawable to use for the content scrim from resources.
     *
     * @param resId drawable resource id
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #getContentScrim()
     */
    public void setContentScrimResource(@DrawableRes int resId) { //NOSONAR
        setContentScrim(ContextCompat.getDrawable(getContext(), resId)); //NOSONAR

    }

    /**
     * Returns the drawable which is used for the foreground scrim.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #setContentScrim(Drawable)
     */
    @Nullable //NOSONAR
    public Drawable getContentScrim() { //NOSONAR
        return mContentScrim; //NOSONAR
    }

    /**
     * Set the drawable to use for the content scrim from resources. Providing null will disable
     * the scrim functionality.
     *
     * @param drawable the drawable to display
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #getContentScrim()
     */
    public void setContentScrim(@Nullable Drawable drawable) { //NOSONAR
        if (mContentScrim != drawable) { //NOSONAR
            if (mContentScrim != null) { //NOSONAR
                mContentScrim.setCallback(null); //NOSONAR
            }
            mContentScrim = drawable != null ? drawable.mutate() : null; //NOSONAR
            if (mContentScrim != null) { //NOSONAR
                mContentScrim.setBounds(0, 0, getWidth(), getHeight()); //NOSONAR
                mContentScrim.setCallback(this); //NOSONAR
                mContentScrim.setAlpha(mScrimAlpha); //NOSONAR
            }
            ViewCompat.postInvalidateOnAnimation(this); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void drawableStateChanged() { //NOSONAR
        super.drawableStateChanged(); //NOSONAR

        final int[] state = getDrawableState(); //NOSONAR
        boolean changed = false; //NOSONAR

        Drawable d = mStatusBarScrim; //NOSONAR
        if (d != null && d.isStateful()) { //NOSONAR
            changed |= d.setState(state); //NOSONAR
        }
        d = mContentScrim; //NOSONAR
        if (d != null && d.isStateful()) { //NOSONAR
            changed |= d.setState(state); //NOSONAR
        }
        if (mCollapsingTextHelper != null) { //NOSONAR
            changed |= mCollapsingTextHelper.setState(state); //NOSONAR
        }

        if (changed) { //NOSONAR
            invalidate(); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected boolean verifyDrawable(Drawable who) { //NOSONAR
        return super.verifyDrawable(who) || who == mContentScrim || who == mStatusBarScrim; //NOSONAR
    }

    @Override //NOSONAR
    public void setVisibility(int visibility) { //NOSONAR
        super.setVisibility(visibility); //NOSONAR

        final boolean visible = visibility == VISIBLE; //NOSONAR
        if (mStatusBarScrim != null && mStatusBarScrim.isVisible() != visible) { //NOSONAR
            mStatusBarScrim.setVisible(visible, false); //NOSONAR
        }
        if (mContentScrim != null && mContentScrim.isVisible() != visible) { //NOSONAR
            mContentScrim.setVisible(visible, false); //NOSONAR
        }
    }

    /**
     * Set the color to use for the status bar scrim.
     * <p>
     * <p>This scrim is only shown when we have been given a top system inset.</p>
     *
     * @param color the color to display
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #getStatusBarScrim()
     */
    public void setStatusBarScrimColor(@ColorInt int color) { //NOSONAR
        setStatusBarScrim(new ColorDrawable(color)); //NOSONAR
    }

    /**
     * Set the drawable to use for the content scrim from resources.
     *
     * @param resId drawable resource id
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #getStatusBarScrim()
     */
    public void setStatusBarScrimResource(@DrawableRes int resId) { //NOSONAR
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), resId)); //NOSONAR
    }

    /**
     * Returns the drawable which is used for the status bar scrim.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #setStatusBarScrim(Drawable)
     */
    @Nullable //NOSONAR
    public Drawable getStatusBarScrim() { //NOSONAR
        return mStatusBarScrim; //NOSONAR
    }

    /**
     * Set the drawable to use for the status bar scrim from resources.
     * Providing null will disable the scrim functionality.
     * <p>
     * <p>This scrim is only shown when we have been given a top system inset.</p>
     *
     * @param drawable the drawable to display
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #getStatusBarScrim()
     */
    public void setStatusBarScrim(@Nullable Drawable drawable) { //NOSONAR
        if (mStatusBarScrim != drawable) { //NOSONAR
            if (mStatusBarScrim != null) { //NOSONAR
                mStatusBarScrim.setCallback(null); //NOSONAR
            }
            mStatusBarScrim = drawable != null ? drawable.mutate() : null; //NOSONAR
            if (mStatusBarScrim != null) { //NOSONAR
                if (mStatusBarScrim.isStateful()) { //NOSONAR
                    mStatusBarScrim.setState(getDrawableState()); //NOSONAR
                }
                DrawableCompat.setLayoutDirection(mStatusBarScrim, //NOSONAR
                        ViewCompat.getLayoutDirection(this)); //NOSONAR
                mStatusBarScrim.setVisible(getVisibility() == VISIBLE, false); //NOSONAR
                mStatusBarScrim.setCallback(this); //NOSONAR
                mStatusBarScrim.setAlpha(mScrimAlpha); //NOSONAR
            }
            ViewCompat.postInvalidateOnAnimation(this); //NOSONAR
        }
    }

    /**
     * Sets the text color and size for the collapsed title from the specified
     * TextAppearance resource.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleTextAppearance
     */
    public void setCollapsedTitleTextAppearance(@StyleRes int resId) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTextAppearance(resId); //NOSONAR
    }

    /**
     * Sets the text color of the collapsed title.
     *
     * @param color The new text color in ARGB format
     */
    public void setCollapsedTitleTextColor(@ColorInt int color) { //NOSONAR
        setCollapsedTitleTextColor(ColorStateList.valueOf(color)); //NOSONAR
    }

    /**
     * Sets the text colors of the collapsed title.
     *
     * @param colors ColorStateList containing the new text colors
     */
    public void setCollapsedTitleTextColor(@NonNull ColorStateList colors) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTextColor(colors); //NOSONAR
    }

    public ColorStateList getCollapsedTitleTextColor() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedTextColor(); //NOSONAR
    }

    /**
     * Sets the text color of the collapsed title.
     *
     * @param color The new text color in ARGB format
     */
    public void setCollapsedSubTextColor(@ColorInt int color) { //NOSONAR
        setCollapsedSubTextColor(ColorStateList.valueOf(color)); //NOSONAR
    }

    /**
     * Sets the text colors of the collapsed title.
     *
     * @param colors ColorStateList containing the new text colors
     */
    public void setCollapsedSubTextColor(@NonNull ColorStateList colors) { //NOSONAR
        mCollapsingTextHelper.setCollapsedSubColor(colors); //NOSONAR
    }

    public ColorStateList getCollapsedSubTextColor() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedSubColor(); //NOSONAR
    }

    /**
     * Returns the horizontal and vertical alignment for title when collapsed.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleGravity
     */
    public int getCollapsedTitleGravity() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedTextGravity(); //NOSONAR
    }

    /**
     * Sets the horizontal alignment of the collapsed title and the vertical gravity that will
     * be used when there is extra space in the collapsed bounds beyond what is required for
     * the title itself.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleGravity
     */
    public void setCollapsedTitleGravity(int gravity) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTextGravity(gravity); //NOSONAR
    }

    /**
     * Sets the text color and size for the expanded title from the specified
     * TextAppearance resource.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleTextAppearance
     */
    public void setExpandedTitleTextAppearance(@StyleRes int resId) { //NOSONAR
        mCollapsingTextHelper.setExpandedTextAppearance(resId); //NOSONAR
    }

    /**
     * Sets the text color of the expanded title.
     *
     * @param color The new text color in ARGB format
     */
    public void setExpandedTitleColor(@ColorInt int color) { //NOSONAR
        setExpandedTitleTextColor(ColorStateList.valueOf(color)); //NOSONAR
    }

    /**
     * Sets the text colors of the expanded title.
     *
     * @param colors ColorStateList containing the new text colors
     */
    public void setExpandedTitleTextColor(@NonNull ColorStateList colors) { //NOSONAR
        mCollapsingTextHelper.setExpandedTextColor(colors); //NOSONAR
    }

    /**
     * Returns the horizontal and vertical alignment for title when expanded.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleGravity
     */
    public int getExpandedTitleGravity() { //NOSONAR
        return mCollapsingTextHelper.getExpandedTextGravity(); //NOSONAR
    }

    /**
     * Sets the horizontal alignment of the expanded title and the vertical gravity that will
     * be used when there is extra space in the expanded bounds beyond what is required for
     * the title itself.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleGravity
     */
    public void setExpandedTitleGravity(int gravity) { //NOSONAR
        mCollapsingTextHelper.setExpandedTextGravity(gravity); //NOSONAR
    }

    /**
     * Returns the typeface used for the collapsed title.
     */
    @NonNull //NOSONAR
    public Typeface getCollapsedTitleTypeface() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedTypeface(); //NOSONAR
    }

    /**
     * Set the typeface to use for the collapsed title.
     *
     * @param typeface typeface to use, or {@code null} to use the default.
     */
    public void setCollapsedTitleTypeface(@Nullable Typeface typeface) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTypeface(typeface); //NOSONAR
    }

    /**
     * Returns the typeface used for the expanded title.
     */
    @NonNull //NOSONAR
    public Typeface getExpandedTitleTypeface() { //NOSONAR
        return mCollapsingTextHelper.getExpandedTypeface(); //NOSONAR
    }

    /**
     * Set the typeface to use for the expanded title.
     *
     * @param typeface typeface to use, or {@code null} to use the default.
     */
    public void setExpandedTitleTypeface(@Nullable Typeface typeface) { //NOSONAR
        mCollapsingTextHelper.setExpandedTypeface(typeface); //NOSONAR
    }

    /**
     * Sets the expanded title margins.
     *
     * @param start  the starting title margin in pixels
     * @param top    the top title margin in pixels
     * @param end    the ending title margin in pixels
     * @param bottom the bottom title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMargin
     * @see #getExpandedTitleMarginStart()
     * @see #getExpandedTitleMarginTop()
     * @see #getExpandedTitleMarginEnd()
     * @see #getExpandedTitleMarginBottom()
     */
    public void setExpandedTitleMargin(int start, int top, int end, int bottom) { //NOSONAR
        mExpandedMarginStart = start; //NOSONAR
        mExpandedMarginTop = top; //NOSONAR
        mExpandedMarginEnd = end; //NOSONAR
        mExpandedMarginBottom = bottom; //NOSONAR
        requestLayout(); //NOSONAR
    }

    /**
     * @return the starting expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginStart
     * @see #setExpandedTitleMarginStart(int)
     */
    public int getExpandedTitleMarginStart() { //NOSONAR
        return mExpandedMarginStart; //NOSONAR
    }

    /**
     * Sets the starting expanded title margin in pixels.
     *
     * @param margin the starting title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginStart
     * @see #getExpandedTitleMarginStart()
     */
    public void setExpandedTitleMarginStart(int margin) { //NOSONAR
        mExpandedMarginStart = margin; //NOSONAR
        requestLayout(); //NOSONAR
    }

    /**
     * @return the top expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginTop
     * @see #setExpandedTitleMarginTop(int)
     */
    public int getExpandedTitleMarginTop() { //NOSONAR
        return mExpandedMarginTop; //NOSONAR
    }

    /**
     * Sets the top expanded title margin in pixels.
     *
     * @param margin the top title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginTop
     * @see #getExpandedTitleMarginTop()
     */
    public void setExpandedTitleMarginTop(int margin) { //NOSONAR
        mExpandedMarginTop = margin; //NOSONAR
        requestLayout(); //NOSONAR
    }

    /**
     * @return the ending expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginEnd
     * @see #setExpandedTitleMarginEnd(int)
     */
    public int getExpandedTitleMarginEnd() { //NOSONAR
        return mExpandedMarginEnd; //NOSONAR
    }

    /**
     * Sets the ending expanded title margin in pixels.
     *
     * @param margin the ending title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginEnd
     * @see #getExpandedTitleMarginEnd()
     */
    public void setExpandedTitleMarginEnd(int margin) { //NOSONAR
        mExpandedMarginEnd = margin; //NOSONAR
        requestLayout(); //NOSONAR
    }

    /**
     * @return the bottom expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginBottom
     * @see #setExpandedTitleMarginBottom(int)
     */
    public int getExpandedTitleMarginBottom() { //NOSONAR
        return mExpandedMarginBottom; //NOSONAR
    }

    /**
     * Sets the bottom expanded title margin in pixels.
     *
     * @param margin the bottom title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginBottom
     * @see #getExpandedTitleMarginBottom()
     */
    public void setExpandedTitleMarginBottom(int margin) { //NOSONAR
        mExpandedMarginBottom = margin; //NOSONAR
        requestLayout(); //NOSONAR
    }

    /**
     * Returns the amount of visible height in pixels used to define when to trigger a scrim
     * visibility change.
     *
     * @see #setScrimVisibleHeightTrigger(int)
     */
    public int getScrimVisibleHeightTrigger() { //NOSONAR
        if (mScrimVisibleHeightTrigger >= 0) { //NOSONAR
            // If we have one explicitly set, return it
            return mScrimVisibleHeightTrigger; //NOSONAR
        }

        // Otherwise we'll use the default computed value
        final int insetTop = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0; //NOSONAR

        final int minHeight = ViewCompat.getMinimumHeight(this); //NOSONAR
        if (minHeight > 0) { //NOSONAR
            // If we have a minHeight set, lets use 2 * minHeight (capped at our height)
            return Math.min((minHeight * 2) + insetTop, getHeight()); //NOSONAR
        }

        // If we reach here then we don't have a min height set. Instead we'll take a
        // guess at 1/3 of our height being visible
        return getHeight() / 3; //NOSONAR
    }

    /**
     * Set the amount of visible height in pixels used to define when to trigger a scrim
     * visibility change.
     * <p>
     * <p>If the visible height of this view is less than the given value, the scrims will be
     * made visible, otherwise they are hidden.</p>
     *
     * @param height value in pixels used to define when to trigger a scrim visibility change
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_scrimVisibleHeightTrigger
     */
    public void setScrimVisibleHeightTrigger(@IntRange(from = 0) final int height) { //NOSONAR
        if (mScrimVisibleHeightTrigger != height) { //NOSONAR
            mScrimVisibleHeightTrigger = height; //NOSONAR
            // Update the scrim visibility
            updateScrimVisibility(); //NOSONAR
        }
    }

    /**
     * Returns the duration in milliseconds used for scrim visibility animations.
     */
    public long getScrimAnimationDuration() { //NOSONAR
        return mScrimAnimationDuration; //NOSONAR
    }

    /**
     * Set the duration used for scrim visibility animations.
     *
     * @param duration the duration to use in milliseconds
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_scrimAnimationDuration
     */
    public void setScrimAnimationDuration(@IntRange(from = 0) final long duration) { //NOSONAR
        mScrimAnimationDuration = duration; //NOSONAR
    }

    @Override //NOSONAR
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) { //NOSONAR
        return p instanceof LayoutParams; //NOSONAR
    }

    @Override //NOSONAR
    protected LayoutParams generateDefaultLayoutParams() { //NOSONAR
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); //NOSONAR
    }

    @Override //NOSONAR
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) { //NOSONAR
        return new LayoutParams(getContext(), attrs); //NOSONAR
    }

    @Override //NOSONAR
    protected FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) { //NOSONAR
        return new LayoutParams(p); //NOSONAR
    }

    /**
     * Show or hide the scrims if needed
     */
    final void updateScrimVisibility() { //NOSONAR
        if (mContentScrim != null || mStatusBarScrim != null) { //NOSONAR
            setScrimsShown(getHeight() + mCurrentOffset < getScrimVisibleHeightTrigger()); //NOSONAR
        }
    }

    final int getMaxOffsetForPinChild(View child) { //NOSONAR
        final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child); //NOSONAR
        final LayoutParams lp = (LayoutParams) child.getLayoutParams(); //NOSONAR
        return getHeight() //NOSONAR
                - offsetHelper.getLayoutTop() //NOSONAR
                - child.getHeight() //NOSONAR
                - lp.bottomMargin; //NOSONAR
    }

    public static class LayoutParams extends FrameLayout.LayoutParams { //NOSONAR

        /**
         * The view will act as normal with no collapsing behavior.
         */
        public static final int COLLAPSE_MODE_OFF = 0; //NOSONAR
        /**
         * The view will pin in place until it reaches the bottom of the
         * {@link CollapsingToolbarLayout}.
         */
        public static final int COLLAPSE_MODE_PIN = 1; //NOSONAR
        /**
         * The view will scroll in a parallax fashion. See {@link #setParallaxMultiplier(float)}
         * to change the multiplier used.
         */
        public static final int COLLAPSE_MODE_PARALLAX = 2; //NOSONAR
        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f; //NOSONAR
        int mCollapseMode = COLLAPSE_MODE_OFF; //NOSONAR
        float mParallaxMult = DEFAULT_PARALLAX_MULTIPLIER; //NOSONAR

        public LayoutParams(Context c, AttributeSet attrs) { //NOSONAR
            super(c, attrs); //NOSONAR

            TypedArray a = c.obtainStyledAttributes(attrs, //NOSONAR
                    R.styleable.CollapsingToolbarLayout_Layout); //NOSONAR
            mCollapseMode = a.getInt( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, //NOSONAR
                    COLLAPSE_MODE_OFF); //NOSONAR
            setParallaxMultiplier(a.getFloat( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, //NOSONAR
                    DEFAULT_PARALLAX_MULTIPLIER)); //NOSONAR
            a.recycle(); //NOSONAR
        }

        public LayoutParams(int width, int height) { //NOSONAR
            super(width, height); //NOSONAR
        }

        public LayoutParams(int width, int height, int gravity) { //NOSONAR
            super(width, height, gravity); //NOSONAR
        }

        public LayoutParams(ViewGroup.LayoutParams p) { //NOSONAR
            super(p); //NOSONAR
        }

        public LayoutParams(MarginLayoutParams source) { //NOSONAR
            super(source); //NOSONAR
        }

        /**
         * Returns the requested collapse mode.
         *
         * @return the current mode. One of {@link #COLLAPSE_MODE_OFF}, {@link #COLLAPSE_MODE_PIN}
         * or {@link #COLLAPSE_MODE_PARALLAX}.
         */
        @CollapseMode //NOSONAR
        public int getCollapseMode() { //NOSONAR
            return mCollapseMode; //NOSONAR
        }

        /**
         * Set the collapse mode.
         *
         * @param collapseMode one of {@link #COLLAPSE_MODE_OFF}, {@link #COLLAPSE_MODE_PIN}
         *                     or {@link #COLLAPSE_MODE_PARALLAX}.
         */
        public void setCollapseMode(@CollapseMode int collapseMode) { //NOSONAR
            mCollapseMode = collapseMode; //NOSONAR
        }

        /**
         * Returns the parallax scroll multiplier used in conjunction with
         * {@link #COLLAPSE_MODE_PARALLAX}.
         *
         * @see #setParallaxMultiplier(float)
         */
        public float getParallaxMultiplier() { //NOSONAR
            return mParallaxMult; //NOSONAR
        }

        /**
         * Set the parallax scroll multiplier used in conjunction with
         * {@link #COLLAPSE_MODE_PARALLAX}. A value of {@code 0.0} indicates no movement at all,
         * {@code 1.0f} indicates normal scroll movement.
         *
         * @param multiplier the multiplier.
         * @see #getParallaxMultiplier()
         */
        public void setParallaxMultiplier(float multiplier) { //NOSONAR
            mParallaxMult = multiplier; //NOSONAR
        }

        /**
         * @hide
         */
        @RestrictTo(LIBRARY_GROUP) //NOSONAR
        @IntDef({ //NOSONAR
                COLLAPSE_MODE_OFF, //NOSONAR
                COLLAPSE_MODE_PIN, //NOSONAR
                COLLAPSE_MODE_PARALLAX //NOSONAR
        })
        @Retention(RetentionPolicy.SOURCE) //NOSONAR
        @interface CollapseMode { //NOSONAR
            // Intentionally left empty.
        }
    }

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener { //NOSONAR
        OffsetUpdateListener() { //NOSONAR
            // Intentionally left empty.
        }

        @Override //NOSONAR
        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) { //NOSONAR
            mCurrentOffset = verticalOffset; //NOSONAR

            final int insetTop = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0; //NOSONAR

            for (int i = 0, z = getChildCount(); i < z; i++) { //NOSONAR
                final View child = getChildAt(i); //NOSONAR
                final LayoutParams lp = (LayoutParams) child.getLayoutParams(); //NOSONAR
                final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child); //NOSONAR

                switch (lp.mCollapseMode) { //NOSONAR
                    case LayoutParams.COLLAPSE_MODE_PIN: //NOSONAR
                        offsetHelper.setTopAndBottomOffset( //NOSONAR
                                MathUtils.clamp(-verticalOffset, 0, getMaxOffsetForPinChild(child))); //NOSONAR
                        break; //NOSONAR
                    case LayoutParams.COLLAPSE_MODE_PARALLAX: //NOSONAR
                        offsetHelper.setTopAndBottomOffset( //NOSONAR
                                Math.round(-verticalOffset * lp.mParallaxMult)); //NOSONAR
                        break; //NOSONAR
                }
            }

            // Show or hide the scrims if needed
            updateScrimVisibility(); //NOSONAR

            if (mStatusBarScrim != null && insetTop > 0) { //NOSONAR
                ViewCompat.postInvalidateOnAnimation(CustomCollapsingToolbarLayout.this); //NOSONAR
            }

            // Update the collapsing text's fraction
            final int expandRange = getHeight() - ViewCompat.getMinimumHeight( //NOSONAR
                    CustomCollapsingToolbarLayout.this) - insetTop; //NOSONAR
            mCollapsingTextHelper.setExpansionFraction( //NOSONAR
                    Math.abs(verticalOffset) / (float) expandRange); //NOSONAR
        }
    }
}
