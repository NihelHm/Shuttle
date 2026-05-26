package android.support.design.widget; // NOSONAR

import android.animation.ValueAnimator; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.res.ColorStateList; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Rect; // NOSONAR
import android.graphics.Typeface; // NOSONAR
import android.graphics.drawable.ColorDrawable; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.DrawableRes; // NOSONAR
import android.support.annotation.IntDef; // NOSONAR
import android.support.annotation.IntRange; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.RestrictTo; // NOSONAR
import android.support.annotation.StyleRes; // NOSONAR
import android.support.design.animation.AnimationUtils; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.support.v4.math.MathUtils; // NOSONAR
import android.support.v4.view.GravityCompat; // NOSONAR
import android.support.v4.view.ViewCompat; // NOSONAR
import android.support.v4.view.WindowInsetsCompat; // NOSONAR
import android.support.v4.widget.ViewGroupUtils; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.Gravity; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.view.ViewParent; // NOSONAR
import android.view.animation.Interpolator; // NOSONAR
import android.widget.FrameLayout; // NOSONAR

import com.simplecity.amp_library.R; // NOSONAR

import java.lang.annotation.Retention; // NOSONAR
import java.lang.annotation.RetentionPolicy; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR

/** // NOSONAR
 * @author Hendra Anggrian (hendraanggrian@gmail.com) // NOSONAR
 */ // NOSONAR
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
    } // NOSONAR

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs) { //NOSONAR
        this(context, attrs, 0); //NOSONAR
    } // NOSONAR

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
        } // NOSONAR
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) { //NOSONAR
            mExpandedMarginEnd = a1.getDimensionPixelSize( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0); //NOSONAR
        } // NOSONAR
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) { //NOSONAR
            mExpandedMarginTop = a1.getDimensionPixelSize( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0); //NOSONAR
        } // NOSONAR
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) { //NOSONAR
            mExpandedMarginBottom = a1.getDimensionPixelSize( //NOSONAR
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0); //NOSONAR
        } // NOSONAR

        mCollapsingTitleEnabled = a1.getBoolean( //NOSONAR
                R.styleable.CollapsingToolbarLayout_titleEnabled, true); //NOSONAR
        setTitle(a1.getText(R.styleable.CollapsingToolbarLayout_title)); //NOSONAR

        // begin modification // NOSONAR
        TypedArray a2 = context.obtainStyledAttributes(attrs, //NOSONAR
                R.styleable.CustomCollapsingToolbarLayout, defStyleAttr, //NOSONAR
                R.style.CustomCollapsingToolbarLayout); //NOSONAR
        if (a2.hasValue(R.styleable.CustomCollapsingToolbarLayout_subtitle)) //NOSONAR
            setSubtitle(a2.getText(R.styleable.CustomCollapsingToolbarLayout_subtitle).toString()); //NOSONAR

        //load default appearances first // NOSONAR
        mCollapsingTextHelper.setCollapsedSubAppearance(R.style.CollapsedSubtitleAppearance); //NOSONAR
        mCollapsingTextHelper.setExpandedSubAppearance(R.style.ExpandedSubtitleAppearance); //NOSONAR

        // now apply custom sub appearance // NOSONAR
        if (a2.hasValue(R.styleable.CustomCollapsingToolbarLayout_collapsedSubtitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setCollapsedSubAppearance( //NOSONAR
                    a2.getResourceId(R.styleable.CustomCollapsingToolbarLayout_collapsedSubtitleTextAppearance, 0) //NOSONAR
            ); // NOSONAR
        } // NOSONAR

        if (a2.hasValue(R.styleable.CustomCollapsingToolbarLayout_expandedSubtitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setExpandedSubAppearance( //NOSONAR
                    a2.getResourceId(R.styleable.CustomCollapsingToolbarLayout_expandedSubtitleTextAppearance, 0) //NOSONAR
            ); // NOSONAR
        } // NOSONAR
        // end // NOSONAR

        // First load the default text appearances // NOSONAR
        mCollapsingTextHelper.setExpandedTextAppearance( //NOSONAR
                R.style.TextAppearance_Design_CollapsingToolbar_Expanded); //NOSONAR
        mCollapsingTextHelper.setCollapsedTextAppearance( //NOSONAR
                android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title); //NOSONAR

        // Now overlay any custom text appearances // NOSONAR
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setExpandedTextAppearance( //NOSONAR
                    a1.getResourceId( //NOSONAR
                            R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0)); //NOSONAR
        } // NOSONAR
        if (a1.hasValue(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance)) { //NOSONAR
            mCollapsingTextHelper.setCollapsedTextAppearance( //NOSONAR
                    a1.getResourceId( //NOSONAR
                            R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0)); //NOSONAR
        } // NOSONAR

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
    } // NOSONAR

    private static int getHeightWithMargins(@NonNull final View view) { //NOSONAR
        final ViewGroup.LayoutParams lp = view.getLayoutParams(); //NOSONAR
        if (lp instanceof MarginLayoutParams) { //NOSONAR
            final MarginLayoutParams mlp = (MarginLayoutParams) lp; //NOSONAR
            return view.getHeight() + mlp.topMargin + mlp.bottomMargin; //NOSONAR
        } // NOSONAR
        return view.getHeight(); //NOSONAR
    } // NOSONAR

    static ViewOffsetHelper getViewOffsetHelper(View view) { //NOSONAR
        ViewOffsetHelper offsetHelper = (ViewOffsetHelper) view.getTag(R.id.view_offset_helper); //NOSONAR
        if (offsetHelper == null) { //NOSONAR
            offsetHelper = new ViewOffsetHelper(view); //NOSONAR
            view.setTag(R.id.view_offset_helper, offsetHelper); //NOSONAR
        } // NOSONAR
        return offsetHelper; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        // Add an OnOffsetChangedListener if possible // NOSONAR
        final ViewParent parent = getParent(); //NOSONAR
        if (parent instanceof AppBarLayout) { //NOSONAR
            // Copy over from the ABL whether we should fit system windows // NOSONAR
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent)); //NOSONAR

            if (mOnOffsetChangedListener == null) { //NOSONAR
                mOnOffsetChangedListener = new OffsetUpdateListener(); //NOSONAR
            } // NOSONAR
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOnOffsetChangedListener); //NOSONAR

            // We're attached, so lets request an inset dispatch // NOSONAR
            ViewCompat.requestApplyInsets(this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        // Remove our OnOffsetChangedListener if possible and it exists // NOSONAR
        final ViewParent parent = getParent(); //NOSONAR
        if (mOnOffsetChangedListener != null && parent instanceof AppBarLayout) { //NOSONAR
            ((AppBarLayout) parent).removeOnOffsetChangedListener(mOnOffsetChangedListener); //NOSONAR
        } // NOSONAR

        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR

    WindowInsetsCompat onWindowInsetChanged(final WindowInsetsCompat insets) { //NOSONAR
        WindowInsetsCompat newInsets = null; //NOSONAR

        if (ViewCompat.getFitsSystemWindows(this)) { //NOSONAR
            // If we're set to fit system windows, keep the insets // NOSONAR
            newInsets = insets; //NOSONAR
        } // NOSONAR

        // If our insets have changed, keep them and invalidate the scroll ranges... // NOSONAR
        if (mLastInsets != newInsets) { //NOSONAR
            mLastInsets = newInsets; //NOSONAR
            requestLayout(); //NOSONAR
        } // NOSONAR

        // Consume the insets. This is done so that child views with fitSystemWindows=true do not // NOSONAR
        // get the default padding functionality from View // NOSONAR
        return insets.consumeSystemWindowInsets(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void draw(Canvas canvas) { //NOSONAR
        super.draw(canvas); //NOSONAR

        // If we don't have a toolbar, the scrim will be not be drawn in drawChild() below. // NOSONAR
        // Instead, we draw it here, before our collapsing text. // NOSONAR
        ensureToolbar(); //NOSONAR
        if (mToolbar == null && mContentScrim != null && mScrimAlpha > 0) { //NOSONAR
            mContentScrim.mutate().setAlpha(mScrimAlpha); //NOSONAR
            mContentScrim.draw(canvas); //NOSONAR
        } // NOSONAR

        // Let the collapsing text helper draw its text // NOSONAR
        if (mCollapsingTitleEnabled && mDrawCollapsingTitle) { //NOSONAR
            mCollapsingTextHelper.draw(canvas); //NOSONAR
        } // NOSONAR

        // Now draw the status bar scrim // NOSONAR
        if (mStatusBarScrim != null && mScrimAlpha > 0) { //NOSONAR
            final int topInset = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0; //NOSONAR
            if (topInset > 0) { //NOSONAR
                mStatusBarScrim.setBounds(0, -mCurrentOffset, getWidth(), //NOSONAR
                        topInset - mCurrentOffset); //NOSONAR
                mStatusBarScrim.mutate().setAlpha(mScrimAlpha); //NOSONAR
                mStatusBarScrim.draw(canvas); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) { //NOSONAR
        // This is a little weird. Our scrim needs to be behind the Toolbar (if it is present), // NOSONAR
        // but in front of any other children which are behind it. To do this we intercept the // NOSONAR
        // drawChild() call, and draw our scrim after the preceding view is drawn // NOSONAR
        boolean invalidate = super.drawChild(canvas, child, drawingTime); //NOSONAR

        if (mContentScrim != null && mScrimAlpha > 0 && isToolbarChildDrawnNext(child)) { //NOSONAR
            mContentScrim.mutate().setAlpha(mScrimAlpha); //NOSONAR
            mContentScrim.draw(canvas); //NOSONAR
            invalidate = true; //NOSONAR
        } // NOSONAR

        return invalidate; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { //NOSONAR
        super.onSizeChanged(w, h, oldw, oldh); //NOSONAR
        if (mContentScrim != null) { //NOSONAR
            mContentScrim.setBounds(0, 0, w, h); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void ensureToolbar() { //NOSONAR
        if (!mRefreshToolbar) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        // First clear out the current Toolbar // NOSONAR
        mToolbar = null; //NOSONAR
        mToolbarDirectChild = null; //NOSONAR

        if (mToolbarId != -1) { //NOSONAR
            // If we have an ID set, try and find it and it's direct parent to us // NOSONAR
            mToolbar = findViewById(mToolbarId); //NOSONAR
            if (mToolbar != null) { //NOSONAR
                mToolbarDirectChild = findDirectChild(mToolbar); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (mToolbar == null) { //NOSONAR
            // If we don't have an ID, or couldn't find a Toolbar with the correct ID, try and find // NOSONAR
            // one from our direct children // NOSONAR
            Toolbar toolbar = null; //NOSONAR
            for (int i = 0, count = getChildCount(); i < count; i++) { //NOSONAR
                final View child = getChildAt(i); //NOSONAR
                if (child instanceof Toolbar) { //NOSONAR
                    toolbar = (Toolbar) child; //NOSONAR
                    break; //NOSONAR
                } // NOSONAR
            } // NOSONAR
            mToolbar = toolbar; //NOSONAR
        } // NOSONAR

        updateDummyView(); //NOSONAR
        mRefreshToolbar = false; //NOSONAR
    } // NOSONAR

    private boolean isToolbarChildDrawnNext(View child) { //NOSONAR
        return mToolbarDrawIndex >= 0 && mToolbarDrawIndex == indexOfChild(child) + 1; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the direct child of this layout, which itself is the ancestor of the // NOSONAR
     * given view. // NOSONAR
     */ // NOSONAR
    private View findDirectChild(final View descendant) { //NOSONAR
        View directChild = descendant; //NOSONAR
        for (ViewParent p = descendant.getParent(); p != this && p != null; p = p.getParent()) { //NOSONAR
            if (p instanceof View) { //NOSONAR
                directChild = (View) p; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return directChild; //NOSONAR
    } // NOSONAR

    private void updateDummyView() { //NOSONAR
        if (!mCollapsingTitleEnabled && mDummyView != null) { //NOSONAR
            // If we have a dummy view and we have our title disabled, remove it from its parent // NOSONAR
            final ViewParent parent = mDummyView.getParent(); //NOSONAR
            if (parent instanceof ViewGroup) { //NOSONAR
                ((ViewGroup) parent).removeView(mDummyView); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (mCollapsingTitleEnabled && mToolbar != null) { //NOSONAR
            if (mDummyView == null) { //NOSONAR
                mDummyView = new View(getContext()); //NOSONAR
            } // NOSONAR
            if (mDummyView.getParent() == null) { //NOSONAR
                mToolbar.addView(mDummyView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //NOSONAR
        ensureToolbar(); //NOSONAR
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) { //NOSONAR
        super.onLayout(changed, left, top, right, bottom); //NOSONAR

        if (mLastInsets != null) { //NOSONAR
            // Shift down any views which are not set to fit system windows // NOSONAR
            final int insetTop = mLastInsets.getSystemWindowInsetTop(); //NOSONAR
            for (int i = 0, z = getChildCount(); i < z; i++) { //NOSONAR
                final View child = getChildAt(i); //NOSONAR
                if (!ViewCompat.getFitsSystemWindows(child)) { //NOSONAR
                    if (child.getTop() < insetTop) { //NOSONAR
                        // If the child isn't set to fit system windows but is drawing within // NOSONAR
                        // the inset offset it down // NOSONAR
                        ViewCompat.offsetTopAndBottom(child, insetTop); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        // Update the collapsed bounds by getting it's transformed bounds // NOSONAR
        if (mCollapsingTitleEnabled && mDummyView != null) { //NOSONAR
            // We only draw the title if the dummy view is being displayed (Toolbar removes // NOSONAR
            // views if there is no space) // NOSONAR
            mDrawCollapsingTitle = ViewCompat.isAttachedToWindow(mDummyView) //NOSONAR
                    && mDummyView.getVisibility() == VISIBLE; //NOSONAR

            if (mDrawCollapsingTitle) { //NOSONAR
                final boolean isRtl = ViewCompat.getLayoutDirection(this) //NOSONAR
                        == ViewCompat.LAYOUT_DIRECTION_RTL; //NOSONAR

                // Update the collapsed bounds // NOSONAR
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

                // Update the expanded bounds // NOSONAR
                mCollapsingTextHelper.setExpandedBounds( //NOSONAR
                        isRtl ? mExpandedMarginEnd : mExpandedMarginStart, //NOSONAR
                        mTmpRect.top + mExpandedMarginTop, //NOSONAR
                        right - left - (isRtl ? mExpandedMarginStart : mExpandedMarginEnd), //NOSONAR
                        bottom - top - mExpandedMarginBottom); //NOSONAR
                // Now recalculate using the new bounds // NOSONAR
                mCollapsingTextHelper.recalculate(); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        // Update our child view offset helpers. This needs to be done after the title has been // NOSONAR
        // setup, so that any Toolbars are in their original position // NOSONAR
        for (int i = 0, z = getChildCount(); i < z; i++) { //NOSONAR
            getViewOffsetHelper(getChildAt(i)).onViewLayout(); //NOSONAR
        } // NOSONAR

        // Finally, set our minimum height to enable proper AppBarLayout collapsing // NOSONAR
        if (mToolbar != null) { //NOSONAR
            if (mCollapsingTitleEnabled && TextUtils.isEmpty(mCollapsingTextHelper.getText())) { //NOSONAR
                // If we do not currently have a title, try and grab it from the Toolbar // NOSONAR
                mCollapsingTextHelper.setText(mToolbar.getTitle()); //NOSONAR
            } // NOSONAR
            if (mToolbarDirectChild == null || mToolbarDirectChild == this) { //NOSONAR
                setMinimumHeight(getHeightWithMargins(mToolbar)); //NOSONAR
                mToolbarDrawIndex = indexOfChild(mToolbar); //NOSONAR
            } else { //NOSONAR
                setMinimumHeight(getHeightWithMargins(mToolbarDirectChild)); //NOSONAR
                mToolbarDrawIndex = indexOfChild(mToolbarDirectChild); //NOSONAR
            } // NOSONAR
        } else { //NOSONAR
            mToolbarDrawIndex = -1; //NOSONAR
        } // NOSONAR

        updateScrimVisibility(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the title currently being displayed by this view. If the title is not enabled, then // NOSONAR
     * this will return {@code null}. // NOSONAR
     * // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_title // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    public CharSequence getTitle() { //NOSONAR
        return mCollapsingTitleEnabled ? mCollapsingTextHelper.getText() : null; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the title to be displayed by this view, if enabled. // NOSONAR
     * // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_title // NOSONAR
     * @see #setTitleEnabled(boolean) // NOSONAR
     * @see #getTitle() // NOSONAR
     */ // NOSONAR
    public void setTitle(@Nullable CharSequence title) { //NOSONAR
        mCollapsingTextHelper.setText(title); //NOSONAR
    } // NOSONAR

    // begin modification // NOSONAR
    public void setSubtitle(@Nullable CharSequence subtitle) { //NOSONAR
        mCollapsingTextHelper.setSubtitle(subtitle); //NOSONAR
    } // NOSONAR
    // end modif // NOSONAR

    /** // NOSONAR
     * Returns whether this view is currently displaying its own title. // NOSONAR
     * // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_titleEnabled // NOSONAR
     * @see #setTitleEnabled(boolean) // NOSONAR
     */ // NOSONAR
    public boolean isTitleEnabled() { //NOSONAR
        return mCollapsingTitleEnabled; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets whether this view should display its own title. // NOSONAR
     * <p> // NOSONAR
     * <p>The title displayed by this view will shrink and grow based on the scroll offset.</p> // NOSONAR
     * // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_titleEnabled // NOSONAR
     * @see #setTitle(CharSequence) // NOSONAR
     * @see #isTitleEnabled() // NOSONAR
     */ // NOSONAR
    public void setTitleEnabled(boolean enabled) { //NOSONAR
        if (enabled != mCollapsingTitleEnabled) { //NOSONAR
            mCollapsingTitleEnabled = enabled; //NOSONAR
            updateDummyView(); //NOSONAR
            requestLayout(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change // NOSONAR
     * in the vertical scroll may overwrite this value. Any visibility change will be animated if // NOSONAR
     * this view has already been laid out. // NOSONAR
     * // NOSONAR
     * @param shown whether the scrims should be shown // NOSONAR
     * @see #getStatusBarScrim() // NOSONAR
     * @see #getContentScrim() // NOSONAR
     */ // NOSONAR
    public void setScrimsShown(boolean shown) { //NOSONAR
        setScrimsShown(shown, ViewCompat.isLaidOut(this) && !isInEditMode()); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change // NOSONAR
     * in the vertical scroll may overwrite this value. // NOSONAR
     * // NOSONAR
     * @param shown   whether the scrims should be shown // NOSONAR
     * @param animate whether to animate the visibility change // NOSONAR
     * @see #getStatusBarScrim() // NOSONAR
     * @see #getContentScrim() // NOSONAR
     */ // NOSONAR
    public void setScrimsShown(boolean shown, boolean animate) { //NOSONAR
        if (mScrimsAreShown != shown) { //NOSONAR
            if (animate) { //NOSONAR
                animateScrim(shown ? 0xFF : 0x0); //NOSONAR
            } else { //NOSONAR
                setScrimAlpha(shown ? 0xFF : 0x0); //NOSONAR
            } // NOSONAR
            mScrimsAreShown = shown; //NOSONAR
        } // NOSONAR
    } // NOSONAR

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
        } // NOSONAR

        mScrimAnimator.setIntValues(mScrimAlpha, targetAlpha); //NOSONAR
        mScrimAnimator.start(); //NOSONAR
    } // NOSONAR

    void setScrimAlpha(int alpha) { //NOSONAR
        if (alpha != mScrimAlpha) { //NOSONAR
            final Drawable contentScrim = mContentScrim; //NOSONAR
            if (contentScrim != null && mToolbar != null) { //NOSONAR
                ViewCompat.postInvalidateOnAnimation(mToolbar); //NOSONAR
            } // NOSONAR
            mScrimAlpha = alpha; //NOSONAR
            ViewCompat.postInvalidateOnAnimation(CustomCollapsingToolbarLayout.this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the color to use for the content scrim. // NOSONAR
     * // NOSONAR
     * @param color the color to display // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim // NOSONAR
     * @see #getContentScrim() // NOSONAR
     */ // NOSONAR
    public void setContentScrimColor(@ColorInt int color) { //NOSONAR
        setContentScrim(new ColorDrawable(color)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the drawable to use for the content scrim from resources. // NOSONAR
     * // NOSONAR
     * @param resId drawable resource id // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim // NOSONAR
     * @see #getContentScrim() // NOSONAR
     */ // NOSONAR
    public void setContentScrimResource(@DrawableRes int resId) { //NOSONAR
        setContentScrim(ContextCompat.getDrawable(getContext(), resId)); //NOSONAR

    } // NOSONAR

    /** // NOSONAR
     * Returns the drawable which is used for the foreground scrim. // NOSONAR
     * // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim // NOSONAR
     * @see #setContentScrim(Drawable) // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    public Drawable getContentScrim() { //NOSONAR
        return mContentScrim; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the drawable to use for the content scrim from resources. Providing null will disable // NOSONAR
     * the scrim functionality. // NOSONAR
     * // NOSONAR
     * @param drawable the drawable to display // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim // NOSONAR
     * @see #getContentScrim() // NOSONAR
     */ // NOSONAR
    public void setContentScrim(@Nullable Drawable drawable) { //NOSONAR
        if (mContentScrim != drawable) { //NOSONAR
            if (mContentScrim != null) { //NOSONAR
                mContentScrim.setCallback(null); //NOSONAR
            } // NOSONAR
            mContentScrim = drawable != null ? drawable.mutate() : null; //NOSONAR
            if (mContentScrim != null) { //NOSONAR
                mContentScrim.setBounds(0, 0, getWidth(), getHeight()); //NOSONAR
                mContentScrim.setCallback(this); //NOSONAR
                mContentScrim.setAlpha(mScrimAlpha); //NOSONAR
            } // NOSONAR
            ViewCompat.postInvalidateOnAnimation(this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void drawableStateChanged() { //NOSONAR
        super.drawableStateChanged(); //NOSONAR

        final int[] state = getDrawableState(); //NOSONAR
        boolean changed = false; //NOSONAR

        Drawable d = mStatusBarScrim; //NOSONAR
        if (d != null && d.isStateful()) { //NOSONAR
            changed |= d.setState(state); //NOSONAR
        } // NOSONAR
        d = mContentScrim; //NOSONAR
        if (d != null && d.isStateful()) { //NOSONAR
            changed |= d.setState(state); //NOSONAR
        } // NOSONAR
        if (mCollapsingTextHelper != null) { //NOSONAR
            changed |= mCollapsingTextHelper.setState(state); //NOSONAR
        } // NOSONAR

        if (changed) { //NOSONAR
            invalidate(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected boolean verifyDrawable(Drawable who) { //NOSONAR
        return super.verifyDrawable(who) || who == mContentScrim || who == mStatusBarScrim; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setVisibility(int visibility) { //NOSONAR
        super.setVisibility(visibility); //NOSONAR

        final boolean visible = visibility == VISIBLE; //NOSONAR
        if (mStatusBarScrim != null && mStatusBarScrim.isVisible() != visible) { //NOSONAR
            mStatusBarScrim.setVisible(visible, false); //NOSONAR
        } // NOSONAR
        if (mContentScrim != null && mContentScrim.isVisible() != visible) { //NOSONAR
            mContentScrim.setVisible(visible, false); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the color to use for the status bar scrim. // NOSONAR
     * <p> // NOSONAR
     * <p>This scrim is only shown when we have been given a top system inset.</p> // NOSONAR
     * // NOSONAR
     * @param color the color to display // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim // NOSONAR
     * @see #getStatusBarScrim() // NOSONAR
     */ // NOSONAR
    public void setStatusBarScrimColor(@ColorInt int color) { //NOSONAR
        setStatusBarScrim(new ColorDrawable(color)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the drawable to use for the content scrim from resources. // NOSONAR
     * // NOSONAR
     * @param resId drawable resource id // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim // NOSONAR
     * @see #getStatusBarScrim() // NOSONAR
     */ // NOSONAR
    public void setStatusBarScrimResource(@DrawableRes int resId) { //NOSONAR
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), resId)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the drawable which is used for the status bar scrim. // NOSONAR
     * // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim // NOSONAR
     * @see #setStatusBarScrim(Drawable) // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    public Drawable getStatusBarScrim() { //NOSONAR
        return mStatusBarScrim; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the drawable to use for the status bar scrim from resources. // NOSONAR
     * Providing null will disable the scrim functionality. // NOSONAR
     * <p> // NOSONAR
     * <p>This scrim is only shown when we have been given a top system inset.</p> // NOSONAR
     * // NOSONAR
     * @param drawable the drawable to display // NOSONAR
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim // NOSONAR
     * @see #getStatusBarScrim() // NOSONAR
     */ // NOSONAR
    public void setStatusBarScrim(@Nullable Drawable drawable) { //NOSONAR
        if (mStatusBarScrim != drawable) { //NOSONAR
            if (mStatusBarScrim != null) { //NOSONAR
                mStatusBarScrim.setCallback(null); //NOSONAR
            } // NOSONAR
            mStatusBarScrim = drawable != null ? drawable.mutate() : null; //NOSONAR
            if (mStatusBarScrim != null) { //NOSONAR
                if (mStatusBarScrim.isStateful()) { //NOSONAR
                    mStatusBarScrim.setState(getDrawableState()); //NOSONAR
                } // NOSONAR
                DrawableCompat.setLayoutDirection(mStatusBarScrim, //NOSONAR
                        ViewCompat.getLayoutDirection(this)); //NOSONAR
                mStatusBarScrim.setVisible(getVisibility() == VISIBLE, false); //NOSONAR
                mStatusBarScrim.setCallback(this); //NOSONAR
                mStatusBarScrim.setAlpha(mScrimAlpha); //NOSONAR
            } // NOSONAR
            ViewCompat.postInvalidateOnAnimation(this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text color and size for the collapsed title from the specified // NOSONAR
     * TextAppearance resource. // NOSONAR
     * // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleTextAppearance // NOSONAR
     */ // NOSONAR
    public void setCollapsedTitleTextAppearance(@StyleRes int resId) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTextAppearance(resId); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text color of the collapsed title. // NOSONAR
     * // NOSONAR
     * @param color The new text color in ARGB format // NOSONAR
     */ // NOSONAR
    public void setCollapsedTitleTextColor(@ColorInt int color) { //NOSONAR
        setCollapsedTitleTextColor(ColorStateList.valueOf(color)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text colors of the collapsed title. // NOSONAR
     * // NOSONAR
     * @param colors ColorStateList containing the new text colors // NOSONAR
     */ // NOSONAR
    public void setCollapsedTitleTextColor(@NonNull ColorStateList colors) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTextColor(colors); //NOSONAR
    } // NOSONAR

    public ColorStateList getCollapsedTitleTextColor() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedTextColor(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text color of the collapsed title. // NOSONAR
     * // NOSONAR
     * @param color The new text color in ARGB format // NOSONAR
     */ // NOSONAR
    public void setCollapsedSubTextColor(@ColorInt int color) { //NOSONAR
        setCollapsedSubTextColor(ColorStateList.valueOf(color)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text colors of the collapsed title. // NOSONAR
     * // NOSONAR
     * @param colors ColorStateList containing the new text colors // NOSONAR
     */ // NOSONAR
    public void setCollapsedSubTextColor(@NonNull ColorStateList colors) { //NOSONAR
        mCollapsingTextHelper.setCollapsedSubColor(colors); //NOSONAR
    } // NOSONAR

    public ColorStateList getCollapsedSubTextColor() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedSubColor(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the horizontal and vertical alignment for title when collapsed. // NOSONAR
     * // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleGravity // NOSONAR
     */ // NOSONAR
    public int getCollapsedTitleGravity() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedTextGravity(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the horizontal alignment of the collapsed title and the vertical gravity that will // NOSONAR
     * be used when there is extra space in the collapsed bounds beyond what is required for // NOSONAR
     * the title itself. // NOSONAR
     * // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleGravity // NOSONAR
     */ // NOSONAR
    public void setCollapsedTitleGravity(int gravity) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTextGravity(gravity); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text color and size for the expanded title from the specified // NOSONAR
     * TextAppearance resource. // NOSONAR
     * // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleTextAppearance // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleTextAppearance(@StyleRes int resId) { //NOSONAR
        mCollapsingTextHelper.setExpandedTextAppearance(resId); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text color of the expanded title. // NOSONAR
     * // NOSONAR
     * @param color The new text color in ARGB format // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleColor(@ColorInt int color) { //NOSONAR
        setExpandedTitleTextColor(ColorStateList.valueOf(color)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text colors of the expanded title. // NOSONAR
     * // NOSONAR
     * @param colors ColorStateList containing the new text colors // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleTextColor(@NonNull ColorStateList colors) { //NOSONAR
        mCollapsingTextHelper.setExpandedTextColor(colors); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the horizontal and vertical alignment for title when expanded. // NOSONAR
     * // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleGravity // NOSONAR
     */ // NOSONAR
    public int getExpandedTitleGravity() { //NOSONAR
        return mCollapsingTextHelper.getExpandedTextGravity(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the horizontal alignment of the expanded title and the vertical gravity that will // NOSONAR
     * be used when there is extra space in the expanded bounds beyond what is required for // NOSONAR
     * the title itself. // NOSONAR
     * // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleGravity // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleGravity(int gravity) { //NOSONAR
        mCollapsingTextHelper.setExpandedTextGravity(gravity); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the typeface used for the collapsed title. // NOSONAR
     */ // NOSONAR
    @NonNull //NOSONAR
    public Typeface getCollapsedTitleTypeface() { //NOSONAR
        return mCollapsingTextHelper.getCollapsedTypeface(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the typeface to use for the collapsed title. // NOSONAR
     * // NOSONAR
     * @param typeface typeface to use, or {@code null} to use the default. // NOSONAR
     */ // NOSONAR
    public void setCollapsedTitleTypeface(@Nullable Typeface typeface) { //NOSONAR
        mCollapsingTextHelper.setCollapsedTypeface(typeface); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the typeface used for the expanded title. // NOSONAR
     */ // NOSONAR
    @NonNull //NOSONAR
    public Typeface getExpandedTitleTypeface() { //NOSONAR
        return mCollapsingTextHelper.getExpandedTypeface(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the typeface to use for the expanded title. // NOSONAR
     * // NOSONAR
     * @param typeface typeface to use, or {@code null} to use the default. // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleTypeface(@Nullable Typeface typeface) { //NOSONAR
        mCollapsingTextHelper.setExpandedTypeface(typeface); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the expanded title margins. // NOSONAR
     * // NOSONAR
     * @param start  the starting title margin in pixels // NOSONAR
     * @param top    the top title margin in pixels // NOSONAR
     * @param end    the ending title margin in pixels // NOSONAR
     * @param bottom the bottom title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMargin // NOSONAR
     * @see #getExpandedTitleMarginStart() // NOSONAR
     * @see #getExpandedTitleMarginTop() // NOSONAR
     * @see #getExpandedTitleMarginEnd() // NOSONAR
     * @see #getExpandedTitleMarginBottom() // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleMargin(int start, int top, int end, int bottom) { //NOSONAR
        mExpandedMarginStart = start; //NOSONAR
        mExpandedMarginTop = top; //NOSONAR
        mExpandedMarginEnd = end; //NOSONAR
        mExpandedMarginBottom = bottom; //NOSONAR
        requestLayout(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return the starting expanded title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginStart // NOSONAR
     * @see #setExpandedTitleMarginStart(int) // NOSONAR
     */ // NOSONAR
    public int getExpandedTitleMarginStart() { //NOSONAR
        return mExpandedMarginStart; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the starting expanded title margin in pixels. // NOSONAR
     * // NOSONAR
     * @param margin the starting title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginStart // NOSONAR
     * @see #getExpandedTitleMarginStart() // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleMarginStart(int margin) { //NOSONAR
        mExpandedMarginStart = margin; //NOSONAR
        requestLayout(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return the top expanded title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginTop // NOSONAR
     * @see #setExpandedTitleMarginTop(int) // NOSONAR
     */ // NOSONAR
    public int getExpandedTitleMarginTop() { //NOSONAR
        return mExpandedMarginTop; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the top expanded title margin in pixels. // NOSONAR
     * // NOSONAR
     * @param margin the top title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginTop // NOSONAR
     * @see #getExpandedTitleMarginTop() // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleMarginTop(int margin) { //NOSONAR
        mExpandedMarginTop = margin; //NOSONAR
        requestLayout(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return the ending expanded title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginEnd // NOSONAR
     * @see #setExpandedTitleMarginEnd(int) // NOSONAR
     */ // NOSONAR
    public int getExpandedTitleMarginEnd() { //NOSONAR
        return mExpandedMarginEnd; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the ending expanded title margin in pixels. // NOSONAR
     * // NOSONAR
     * @param margin the ending title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginEnd // NOSONAR
     * @see #getExpandedTitleMarginEnd() // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleMarginEnd(int margin) { //NOSONAR
        mExpandedMarginEnd = margin; //NOSONAR
        requestLayout(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return the bottom expanded title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginBottom // NOSONAR
     * @see #setExpandedTitleMarginBottom(int) // NOSONAR
     */ // NOSONAR
    public int getExpandedTitleMarginBottom() { //NOSONAR
        return mExpandedMarginBottom; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the bottom expanded title margin in pixels. // NOSONAR
     * // NOSONAR
     * @param margin the bottom title margin in pixels // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginBottom // NOSONAR
     * @see #getExpandedTitleMarginBottom() // NOSONAR
     */ // NOSONAR
    public void setExpandedTitleMarginBottom(int margin) { //NOSONAR
        mExpandedMarginBottom = margin; //NOSONAR
        requestLayout(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the amount of visible height in pixels used to define when to trigger a scrim // NOSONAR
     * visibility change. // NOSONAR
     * // NOSONAR
     * @see #setScrimVisibleHeightTrigger(int) // NOSONAR
     */ // NOSONAR
    public int getScrimVisibleHeightTrigger() { //NOSONAR
        if (mScrimVisibleHeightTrigger >= 0) { //NOSONAR
            // If we have one explicitly set, return it // NOSONAR
            return mScrimVisibleHeightTrigger; //NOSONAR
        } // NOSONAR

        // Otherwise we'll use the default computed value // NOSONAR
        final int insetTop = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0; //NOSONAR

        final int minHeight = ViewCompat.getMinimumHeight(this); //NOSONAR
        if (minHeight > 0) { //NOSONAR
            // If we have a minHeight set, lets use 2 * minHeight (capped at our height) // NOSONAR
            return Math.min((minHeight * 2) + insetTop, getHeight()); //NOSONAR
        } // NOSONAR

        // If we reach here then we don't have a min height set. Instead we'll take a // NOSONAR
        // guess at 1/3 of our height being visible // NOSONAR
        return getHeight() / 3; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the amount of visible height in pixels used to define when to trigger a scrim // NOSONAR
     * visibility change. // NOSONAR
     * <p> // NOSONAR
     * <p>If the visible height of this view is less than the given value, the scrims will be // NOSONAR
     * made visible, otherwise they are hidden.</p> // NOSONAR
     * // NOSONAR
     * @param height value in pixels used to define when to trigger a scrim visibility change // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_scrimVisibleHeightTrigger // NOSONAR
     */ // NOSONAR
    public void setScrimVisibleHeightTrigger(@IntRange(from = 0) final int height) { //NOSONAR
        if (mScrimVisibleHeightTrigger != height) { //NOSONAR
            mScrimVisibleHeightTrigger = height; //NOSONAR
            // Update the scrim visibility // NOSONAR
            updateScrimVisibility(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the duration in milliseconds used for scrim visibility animations. // NOSONAR
     */ // NOSONAR
    public long getScrimAnimationDuration() { //NOSONAR
        return mScrimAnimationDuration; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Set the duration used for scrim visibility animations. // NOSONAR
     * // NOSONAR
     * @param duration the duration to use in milliseconds // NOSONAR
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_scrimAnimationDuration // NOSONAR
     */ // NOSONAR
    public void setScrimAnimationDuration(@IntRange(from = 0) final long duration) { //NOSONAR
        mScrimAnimationDuration = duration; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) { //NOSONAR
        return p instanceof LayoutParams; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected LayoutParams generateDefaultLayoutParams() { //NOSONAR
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) { //NOSONAR
        return new LayoutParams(getContext(), attrs); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) { //NOSONAR
        return new LayoutParams(p); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Show or hide the scrims if needed // NOSONAR
     */ // NOSONAR
    final void updateScrimVisibility() { //NOSONAR
        if (mContentScrim != null || mStatusBarScrim != null) { //NOSONAR
            setScrimsShown(getHeight() + mCurrentOffset < getScrimVisibleHeightTrigger()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    final int getMaxOffsetForPinChild(View child) { //NOSONAR
        final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child); //NOSONAR
        final LayoutParams lp = (LayoutParams) child.getLayoutParams(); //NOSONAR
        return getHeight() //NOSONAR
                - offsetHelper.getLayoutTop() //NOSONAR
                - child.getHeight() //NOSONAR
                - lp.bottomMargin; //NOSONAR
    } // NOSONAR

    public static class LayoutParams extends FrameLayout.LayoutParams { //NOSONAR

        /** // NOSONAR
         * The view will act as normal with no collapsing behavior. // NOSONAR
         */ // NOSONAR
        public static final int COLLAPSE_MODE_OFF = 0; //NOSONAR
        /** // NOSONAR
         * The view will pin in place until it reaches the bottom of the // NOSONAR
         * {@link CollapsingToolbarLayout}. // NOSONAR
         */ // NOSONAR
        public static final int COLLAPSE_MODE_PIN = 1; //NOSONAR
        /** // NOSONAR
         * The view will scroll in a parallax fashion. See {@link #setParallaxMultiplier(float)} // NOSONAR
         * to change the multiplier used. // NOSONAR
         */ // NOSONAR
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
        } // NOSONAR

        public LayoutParams(int width, int height) { //NOSONAR
            super(width, height); //NOSONAR
        } // NOSONAR

        public LayoutParams(int width, int height, int gravity) { //NOSONAR
            super(width, height, gravity); //NOSONAR
        } // NOSONAR

        public LayoutParams(ViewGroup.LayoutParams p) { //NOSONAR
            super(p); //NOSONAR
        } // NOSONAR

        public LayoutParams(MarginLayoutParams source) { //NOSONAR
            super(source); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Returns the requested collapse mode. // NOSONAR
         * // NOSONAR
         * @return the current mode. One of {@link #COLLAPSE_MODE_OFF}, {@link #COLLAPSE_MODE_PIN} // NOSONAR
         * or {@link #COLLAPSE_MODE_PARALLAX}. // NOSONAR
         */ // NOSONAR
        @CollapseMode //NOSONAR
        public int getCollapseMode() { //NOSONAR
            return mCollapseMode; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Set the collapse mode. // NOSONAR
         * // NOSONAR
         * @param collapseMode one of {@link #COLLAPSE_MODE_OFF}, {@link #COLLAPSE_MODE_PIN} // NOSONAR
         *                     or {@link #COLLAPSE_MODE_PARALLAX}. // NOSONAR
         */ // NOSONAR
        public void setCollapseMode(@CollapseMode int collapseMode) { //NOSONAR
            mCollapseMode = collapseMode; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Returns the parallax scroll multiplier used in conjunction with // NOSONAR
         * {@link #COLLAPSE_MODE_PARALLAX}. // NOSONAR
         * // NOSONAR
         * @see #setParallaxMultiplier(float) // NOSONAR
         */ // NOSONAR
        public float getParallaxMultiplier() { //NOSONAR
            return mParallaxMult; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Set the parallax scroll multiplier used in conjunction with // NOSONAR
         * {@link #COLLAPSE_MODE_PARALLAX}. A value of {@code 0.0} indicates no movement at all, // NOSONAR
         * {@code 1.0f} indicates normal scroll movement. // NOSONAR
         * // NOSONAR
         * @param multiplier the multiplier. // NOSONAR
         * @see #getParallaxMultiplier() // NOSONAR
         */ // NOSONAR
        public void setParallaxMultiplier(float multiplier) { //NOSONAR
            mParallaxMult = multiplier; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * @hide // NOSONAR
         */ // NOSONAR
        @RestrictTo(LIBRARY_GROUP) //NOSONAR
        @IntDef({ //NOSONAR
                COLLAPSE_MODE_OFF, //NOSONAR
                COLLAPSE_MODE_PIN, //NOSONAR
                COLLAPSE_MODE_PARALLAX //NOSONAR
        }) // NOSONAR
        @Retention(RetentionPolicy.SOURCE) //NOSONAR
        @interface CollapseMode { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener { //NOSONAR
        OffsetUpdateListener() { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

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
                } // NOSONAR
            } // NOSONAR

            // Show or hide the scrims if needed // NOSONAR
            updateScrimVisibility(); //NOSONAR

            if (mStatusBarScrim != null && insetTop > 0) { //NOSONAR
                ViewCompat.postInvalidateOnAnimation(CustomCollapsingToolbarLayout.this); //NOSONAR
            } // NOSONAR

            // Update the collapsing text's fraction // NOSONAR
            final int expandRange = getHeight() - ViewCompat.getMinimumHeight( //NOSONAR
                    CustomCollapsingToolbarLayout.this) - insetTop; //NOSONAR
            mCollapsingTextHelper.setExpansionFraction( //NOSONAR
                    Math.abs(verticalOffset) / (float) expandRange); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
