package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.PorterDuff; // NOSONAR
import android.graphics.PorterDuffColorFilter; // NOSONAR
import android.graphics.drawable.ColorDrawable; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.design.widget.AppBarLayout; // NOSONAR
import android.support.design.widget.CollapsingToolbarLayout; // NOSONAR
import android.support.design.widget.CoordinatorLayout; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.support.v7.view.menu.ActionMenuItemView; // NOSONAR
import android.support.v7.widget.ActionMenuView; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.Menu; // NOSONAR
import android.view.View; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.BiFunction; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR
import java.lang.reflect.Field; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticCoordinatorLayout extends CoordinatorLayout //NOSONAR
    implements AppBarLayout.OnOffsetChangedListener { //NOSONAR

  private Disposable toolbarColorSubscription; //NOSONAR
  private Disposable statusBarColorSubscription; //NOSONAR
  private AppBarLayout appBarLayout; //NOSONAR
  private View colorView; //NOSONAR
  private AestheticToolbar toolbar; //NOSONAR
  private CollapsingToolbarLayout collapsingToolbarLayout; //NOSONAR

  private int toolbarColor; //NOSONAR
  private ActiveInactiveColors iconTextColors; //NOSONAR
  private int lastOffset = -1; //NOSONAR

  public AestheticCoordinatorLayout(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticCoordinatorLayout(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  } // NOSONAR

  @SuppressWarnings("unchecked") //NOSONAR
  private static void tintMenu( //NOSONAR
      @NonNull AestheticToolbar toolbar, @Nullable Menu menu, final ActiveInactiveColors colors) { //NOSONAR
    if (toolbar.getNavigationIcon() != null) { //NOSONAR
      toolbar.setNavigationIcon(toolbar.getNavigationIcon(), colors.activeColor()); //NOSONAR
    } // NOSONAR
    Util.setOverflowButtonColor(toolbar, colors.activeColor()); //NOSONAR

    try { //NOSONAR
      final Field field = Toolbar.class.getDeclaredField("mCollapseIcon"); //NOSONAR
      field.setAccessible(true); //NOSONAR
      Drawable collapseIcon = (Drawable) field.get(toolbar); //NOSONAR
      if (collapseIcon != null) { //NOSONAR
        field.set(toolbar, TintHelper.createTintedDrawable(collapseIcon, colors.toEnabledSl())); //NOSONAR
      } // NOSONAR
    } catch (Exception e) { //NOSONAR
      e.printStackTrace(); //NOSONAR
    } // NOSONAR

    final PorterDuffColorFilter colorFilter = //NOSONAR
        new PorterDuffColorFilter(colors.activeColor(), PorterDuff.Mode.SRC_IN); //NOSONAR
    for (int i = 0; i < toolbar.getChildCount(); i++) { //NOSONAR
      final View v = toolbar.getChildAt(i); //NOSONAR
      // We can't iterate through the toolbar.getMenu() here, because we need the ActionMenuItemView. // NOSONAR
      if (v instanceof ActionMenuView) { //NOSONAR
        for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) { //NOSONAR
          final View innerView = ((ActionMenuView) v).getChildAt(j); //NOSONAR
          if (innerView instanceof ActionMenuItemView) { //NOSONAR
            int drawablesCount = ((ActionMenuItemView) innerView).getCompoundDrawables().length; //NOSONAR
            for (int k = 0; k < drawablesCount; k++) { //NOSONAR
              if (((ActionMenuItemView) innerView).getCompoundDrawables()[k] != null) { //NOSONAR
                ((ActionMenuItemView) innerView) //NOSONAR
                    .getCompoundDrawables()[k].setColorFilter(colorFilter); //NOSONAR
              } // NOSONAR
            } // NOSONAR
          } // NOSONAR
        } // NOSONAR
      } // NOSONAR
    } // NOSONAR

    if (menu == null) { //NOSONAR
      menu = toolbar.getMenu(); //NOSONAR
    } // NOSONAR
    ViewUtil.tintToolbarMenu(toolbar, menu, colors); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  public void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR

    // Find the toolbar and color view used to blend the scroll transition // NOSONAR
    if (getChildCount() > 0 && getChildAt(0) instanceof AppBarLayout) { //NOSONAR
      appBarLayout = (AppBarLayout) getChildAt(0); //NOSONAR
      if (appBarLayout.getChildCount() > 0 //NOSONAR
          && appBarLayout.getChildAt(0) instanceof CollapsingToolbarLayout) { //NOSONAR
        collapsingToolbarLayout = (CollapsingToolbarLayout) appBarLayout.getChildAt(0); //NOSONAR
        for (int i = 0; i < collapsingToolbarLayout.getChildCount(); i++) { //NOSONAR
          if (this.toolbar != null && this.colorView != null) { //NOSONAR
            break; //NOSONAR
          } // NOSONAR
          View child = collapsingToolbarLayout.getChildAt(i); //NOSONAR
          if (child instanceof AestheticToolbar) { //NOSONAR
            this.toolbar = (AestheticToolbar) child; //NOSONAR
          } else if (child.getBackground() != null //NOSONAR
              && child.getBackground() instanceof ColorDrawable) { //NOSONAR
            this.colorView = child; //NOSONAR
          } // NOSONAR
        } // NOSONAR
      } // NOSONAR
    } // NOSONAR

    if (toolbar != null && colorView != null) { //NOSONAR
      this.appBarLayout.addOnOffsetChangedListener(this); //NOSONAR
      toolbarColorSubscription = //NOSONAR
          Observable.combineLatest( //NOSONAR
                  toolbar.colorUpdated(), //NOSONAR
                  Aesthetic.get(getContext()).colorIconTitle(toolbar.colorUpdated()), //NOSONAR
                  new BiFunction< //NOSONAR
                      Integer, ActiveInactiveColors, Pair<Integer, ActiveInactiveColors>>() { //NOSONAR
                    @Override //NOSONAR
                    public Pair<Integer, ActiveInactiveColors> apply( //NOSONAR
                        Integer integer, ActiveInactiveColors activeInactiveColors) { //NOSONAR
                      return Pair.create(integer, activeInactiveColors); //NOSONAR
                    } // NOSONAR
                  }) // NOSONAR
              .compose(Rx.<Pair<Integer, ActiveInactiveColors>>distinctToMainThread()) //NOSONAR
              .subscribe( //NOSONAR
                  new Consumer<Pair<Integer, ActiveInactiveColors>>() { //NOSONAR
                    @Override //NOSONAR
                    public void accept(@NonNull Pair<Integer, ActiveInactiveColors> result) { //NOSONAR
                      toolbarColor = result.first; //NOSONAR
                      iconTextColors = result.second; //NOSONAR
                      invalidateColors(); //NOSONAR
                    } // NOSONAR
                  }, // NOSONAR
                  onErrorLogAndRethrow()); //NOSONAR
    } // NOSONAR

    if (collapsingToolbarLayout != null) { //NOSONAR
      statusBarColorSubscription = //NOSONAR
          Aesthetic.get(getContext()) //NOSONAR
              .colorStatusBar() //NOSONAR
              .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
              .subscribe( //NOSONAR
                  new Consumer<Integer>() { //NOSONAR
                    @Override //NOSONAR
                    public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
                      collapsingToolbarLayout.setContentScrimColor(color); //NOSONAR
                      collapsingToolbarLayout.setStatusBarScrimColor(color); //NOSONAR
                    } // NOSONAR
                  }, // NOSONAR
                  onErrorLogAndRethrow()); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  public void onDetachedFromWindow() { //NOSONAR
    if (toolbarColorSubscription != null) { //NOSONAR
      toolbarColorSubscription.dispose(); //NOSONAR
    } // NOSONAR
    if (statusBarColorSubscription != null) { //NOSONAR
      statusBarColorSubscription.dispose(); //NOSONAR
    } // NOSONAR
    if (this.appBarLayout != null) { //NOSONAR
      this.appBarLayout.removeOnOffsetChangedListener(this); //NOSONAR
      this.appBarLayout = null; //NOSONAR
    } // NOSONAR
    this.toolbar = null; //NOSONAR
    this.colorView = null; //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) { //NOSONAR
    if (lastOffset == Math.abs(verticalOffset)) { //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    lastOffset = Math.abs(verticalOffset); //NOSONAR
    invalidateColors(); //NOSONAR
  } // NOSONAR

  private void invalidateColors() { //NOSONAR
    if (iconTextColors == null) { //NOSONAR
      return; //NOSONAR
    } // NOSONAR

    final int maxOffset = appBarLayout.getMeasuredHeight() - toolbar.getMeasuredHeight(); //NOSONAR
    final float ratio = (float) lastOffset / (float) maxOffset; //NOSONAR

    final int colorViewColor = ((ColorDrawable) colorView.getBackground()).getColor(); //NOSONAR
    final int blendedColor = Util.blendColors(colorViewColor, toolbarColor, ratio); //NOSONAR
    final int collapsedTitleColor = iconTextColors.activeColor(); //NOSONAR
    final int expandedTitleColor = Util.isColorLight(colorViewColor) ? Color.BLACK : Color.WHITE; //NOSONAR
    final int blendedTitleColor = Util.blendColors(expandedTitleColor, collapsedTitleColor, ratio); //NOSONAR

    toolbar.setBackgroundColor(blendedColor); //NOSONAR

    collapsingToolbarLayout.setCollapsedTitleTextColor(collapsedTitleColor); //NOSONAR
    collapsingToolbarLayout.setExpandedTitleColor(expandedTitleColor); //NOSONAR

    tintMenu( //NOSONAR
        toolbar, //NOSONAR
        toolbar.getMenu(), //NOSONAR
        ActiveInactiveColors.create(blendedTitleColor, Util.adjustAlpha(blendedColor, 0.7f))); //NOSONAR
  } // NOSONAR
} // NOSONAR
