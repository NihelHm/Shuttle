package com.afollestad.aesthetic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import io.reactivex.Observable;
import java.lang.reflect.Field;

import static com.afollestad.aesthetic.TintHelper.createTintedDrawable;
import static com.afollestad.aesthetic.Util.isColorLight;
import static com.afollestad.aesthetic.Util.resolveResId;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class ViewUtil { //NOSONAR

  @Nullable //NOSONAR
  public static Observable<Integer> getObservableForResId( //NOSONAR
      @NonNull Context context, @IdRes int resId, @Nullable Observable<Integer> fallback) { //NOSONAR
    if (resId == 0) { //NOSONAR
      return fallback; //NOSONAR
    } else if (resId == resolveResId(context, R.attr.colorPrimary, 0)) { //NOSONAR
      return Aesthetic.get(context).colorPrimary(); //NOSONAR
    } else if (resId == resolveResId(context, R.attr.colorPrimaryDark, 0)) { //NOSONAR
      return Aesthetic.get(context).colorPrimaryDark(); //NOSONAR
    } else if (resId == resolveResId(context, android.R.attr.statusBarColor, 0)) { //NOSONAR
      return Aesthetic.get(context).colorStatusBar(); //NOSONAR
    } else if (resId == resolveResId(context, R.attr.colorAccent, 0)) { //NOSONAR
      return Aesthetic.get(context).colorAccent(); //NOSONAR
    } else if (resId == resolveResId(context, android.R.attr.windowBackground, 0)) { //NOSONAR
      return Aesthetic.get(context).colorWindowBackground(); //NOSONAR
    } else if (resId == resolveResId(context, android.R.attr.textColorPrimary, 0)) { //NOSONAR
      return Aesthetic.get(context).textColorPrimary(); //NOSONAR
    } else if (resId == resolveResId(context, android.R.attr.textColorPrimaryInverse, 0)) { //NOSONAR
      return Aesthetic.get(context).textColorPrimaryInverse(); //NOSONAR
    } else if (resId == resolveResId(context, android.R.attr.textColorSecondary, 0)) { //NOSONAR
      return Aesthetic.get(context).textColorSecondary(); //NOSONAR
    } else if (resId == resolveResId(context, android.R.attr.textColorSecondaryInverse, 0)) { //NOSONAR
      return Aesthetic.get(context).textColorSecondaryInverse(); //NOSONAR
    }
    return fallback; //NOSONAR
  }

  static void tintToolbarMenu( //NOSONAR
      @NonNull Toolbar toolbar, @NonNull Menu menu, ActiveInactiveColors titleIconColors) { //NOSONAR
    // The collapse icon displays when action views are expanded (e.g. SearchView)
    try { //NOSONAR
      final Field field = Toolbar.class.getDeclaredField("mCollapseIcon"); //NOSONAR
      field.setAccessible(true); //NOSONAR
      Drawable collapseIcon = (Drawable) field.get(toolbar); //NOSONAR
      if (collapseIcon != null) //NOSONAR
        field.set(toolbar, createTintedDrawable(collapseIcon, titleIconColors.toEnabledSl())); //NOSONAR
    } catch (Exception e) { //NOSONAR
      e.printStackTrace(); //NOSONAR
    }

    // Theme menu action views
    for (int i = 0; i < menu.size(); i++) { //NOSONAR
      MenuItem item = menu.getItem(i); //NOSONAR
      if (item.getActionView() instanceof SearchView) { //NOSONAR
        themeSearchView(titleIconColors, (SearchView) item.getActionView()); //NOSONAR
      }
    }
  }

  private static void themeSearchView(ActiveInactiveColors tintColors, SearchView view) { //NOSONAR
    final Class<?> cls = view.getClass(); //NOSONAR
    try { //NOSONAR
      final Field mSearchSrcTextViewField = cls.getDeclaredField("mSearchSrcTextView"); //NOSONAR
      mSearchSrcTextViewField.setAccessible(true); //NOSONAR
      final EditText mSearchSrcTextView = (EditText) mSearchSrcTextViewField.get(view); //NOSONAR
      mSearchSrcTextView.setTextColor(tintColors.activeColor()); //NOSONAR
      mSearchSrcTextView.setHintTextColor(tintColors.inactiveColor()); //NOSONAR
      TintHelper.setCursorTint(mSearchSrcTextView, tintColors.activeColor()); //NOSONAR

      Field field = cls.getDeclaredField("mSearchButton"); //NOSONAR
      tintImageView(view, field, tintColors); //NOSONAR
      field = cls.getDeclaredField("mGoButton"); //NOSONAR
      tintImageView(view, field, tintColors); //NOSONAR
      field = cls.getDeclaredField("mCloseButton"); //NOSONAR
      tintImageView(view, field, tintColors); //NOSONAR
      field = cls.getDeclaredField("mVoiceButton"); //NOSONAR
      tintImageView(view, field, tintColors); //NOSONAR

      field = cls.getDeclaredField("mSearchPlate"); //NOSONAR
      field.setAccessible(true); //NOSONAR
      TintHelper.setTintAuto( //NOSONAR
          (View) field.get(view), //NOSONAR
          tintColors.activeColor(), //NOSONAR
          true, //NOSONAR
          !isColorLight(tintColors.activeColor())); //NOSONAR

      field = cls.getDeclaredField("mSearchHintIcon"); //NOSONAR
      field.setAccessible(true); //NOSONAR
      field.set(view, createTintedDrawable((Drawable) field.get(view), tintColors.toEnabledSl())); //NOSONAR
    } catch (Exception e) { //NOSONAR
      e.printStackTrace(); //NOSONAR
    }
  }

  private static void tintImageView(Object target, Field field, ActiveInactiveColors tintColors) //NOSONAR
      throws Exception { //NOSONAR
    field.setAccessible(true); //NOSONAR
    final ImageView imageView = (ImageView) field.get(target); //NOSONAR
    if (imageView.getDrawable() != null) { //NOSONAR
      imageView.setImageDrawable( //NOSONAR
          createTintedDrawable(imageView.getDrawable(), tintColors.toEnabledSl())); //NOSONAR
    }
  }
}
