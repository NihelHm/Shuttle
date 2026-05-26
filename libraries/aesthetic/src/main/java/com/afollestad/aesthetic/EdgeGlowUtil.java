package com.afollestad.aesthetic; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR

import android.annotation.TargetApi; // NOSONAR
import android.graphics.PorterDuff; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.Build; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.RestrictTo; // NOSONAR
import android.support.v4.view.ViewPager; // NOSONAR
import android.support.v4.widget.EdgeEffectCompat; // NOSONAR
import android.support.v4.widget.NestedScrollView; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.widget.AbsListView; // NOSONAR
import android.widget.EdgeEffect; // NOSONAR
import android.widget.ScrollView; // NOSONAR
import java.lang.reflect.Field; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class EdgeGlowUtil { //NOSONAR

  private static Field EDGE_GLOW_FIELD_EDGE; //NOSONAR
  private static Field EDGE_GLOW_FIELD_GLOW; //NOSONAR
  private static Field EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT; //NOSONAR
  private static Field SCROLL_VIEW_FIELD_EDGE_GLOW_TOP; //NOSONAR
  private static Field SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM; //NOSONAR
  private static Field NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP; //NOSONAR
  private static Field NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM; //NOSONAR
  private static Field LIST_VIEW_FIELD_EDGE_GLOW_TOP; //NOSONAR
  private static Field LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM; //NOSONAR
  private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP; //NOSONAR
  private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT; //NOSONAR
  private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT; //NOSONAR
  private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM; //NOSONAR
  private static Field VIEW_PAGER_FIELD_EDGE_GLOW_LEFT; //NOSONAR
  private static Field VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT; //NOSONAR

  private static void invalidateEdgeEffectFields() { //NOSONAR
    if (EDGE_GLOW_FIELD_EDGE != null //NOSONAR
        && EDGE_GLOW_FIELD_GLOW != null //NOSONAR
        && EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT != null) { //NOSONAR
      EDGE_GLOW_FIELD_EDGE.setAccessible(true); //NOSONAR
      EDGE_GLOW_FIELD_GLOW.setAccessible(true); //NOSONAR
      EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true); //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      Field edge = null; //NOSONAR
      Field glow = null; //NOSONAR
      for (Field f : EdgeEffect.class.getDeclaredFields()) { //NOSONAR
        switch (f.getName()) { //NOSONAR
          case "mEdge": //NOSONAR
            f.setAccessible(true); //NOSONAR
            edge = f; //NOSONAR
            break; //NOSONAR
          case "mGlow": //NOSONAR
            f.setAccessible(true); //NOSONAR
            glow = f; //NOSONAR
            break; //NOSONAR
        } // NOSONAR
      } // NOSONAR
      EDGE_GLOW_FIELD_EDGE = edge; //NOSONAR
      EDGE_GLOW_FIELD_GLOW = glow; //NOSONAR
    } else { //NOSONAR
      EDGE_GLOW_FIELD_EDGE = null; //NOSONAR
      EDGE_GLOW_FIELD_GLOW = null; //NOSONAR
    } // NOSONAR

    Field efc = null; //NOSONAR
    try { //NOSONAR
      efc = EdgeEffectCompat.class.getDeclaredField("mEdgeEffect"); //NOSONAR
    } catch (NoSuchFieldException e) { //NOSONAR
      if (BuildConfig.DEBUG) e.printStackTrace(); //NOSONAR
    } // NOSONAR
    EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT = efc; //NOSONAR
  } // NOSONAR

  private static void invalidateScrollViewFields() { //NOSONAR
    if (SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null && SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) { //NOSONAR
      SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true); //NOSONAR
      SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true); //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    final Class<?> cls = ScrollView.class; //NOSONAR
    for (Field f : cls.getDeclaredFields()) { //NOSONAR
      switch (f.getName()) { //NOSONAR
        case "mEdgeGlowTop": //NOSONAR
          f.setAccessible(true); //NOSONAR
          SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = f; //NOSONAR
          break; //NOSONAR
        case "mEdgeGlowBottom": //NOSONAR
          f.setAccessible(true); //NOSONAR
          SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = f; //NOSONAR
          break; //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  private static void invalidateNestedScrollViewFields() { //NOSONAR
    if (NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null //NOSONAR
        && NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) { //NOSONAR
      NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true); //NOSONAR
      NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true); //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    Class cls = NestedScrollView.class; //NOSONAR
    for (Field f : cls.getDeclaredFields()) { //NOSONAR
      switch (f.getName()) { //NOSONAR
        case "mEdgeGlowTop": //NOSONAR
          f.setAccessible(true); //NOSONAR
          NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = f; //NOSONAR
          break; //NOSONAR
        case "mEdgeGlowBottom": //NOSONAR
          f.setAccessible(true); //NOSONAR
          NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = f; //NOSONAR
          break; //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  private static void invalidateListViewFields() { //NOSONAR
    if (LIST_VIEW_FIELD_EDGE_GLOW_TOP != null && LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) { //NOSONAR
      LIST_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true); //NOSONAR
      LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true); //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    final Class<?> cls = AbsListView.class; //NOSONAR
    for (Field f : cls.getDeclaredFields()) { //NOSONAR
      switch (f.getName()) { //NOSONAR
        case "mEdgeGlowTop": //NOSONAR
          f.setAccessible(true); //NOSONAR
          LIST_VIEW_FIELD_EDGE_GLOW_TOP = f; //NOSONAR
          break; //NOSONAR
        case "mEdgeGlowBottom": //NOSONAR
          f.setAccessible(true); //NOSONAR
          LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = f; //NOSONAR
          break; //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  private static void invalidateRecyclerViewFields() { //NOSONAR
    if (RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP != null //NOSONAR
        && RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT != null //NOSONAR
        && RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT != null //NOSONAR
        && RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) { //NOSONAR
      RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true); //NOSONAR
      RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.setAccessible(true); //NOSONAR
      RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.setAccessible(true); //NOSONAR
      RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true); //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    Class cls = RecyclerView.class; //NOSONAR
    for (Field f : cls.getDeclaredFields()) { //NOSONAR
      switch (f.getName()) { //NOSONAR
        case "mTopGlow": //NOSONAR
          f.setAccessible(true); //NOSONAR
          RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP = f; //NOSONAR
          break; //NOSONAR
        case "mBottomGlow": //NOSONAR
          f.setAccessible(true); //NOSONAR
          RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM = f; //NOSONAR
          break; //NOSONAR
        case "mLeftGlow": //NOSONAR
          f.setAccessible(true); //NOSONAR
          RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT = f; //NOSONAR
          break; //NOSONAR
        case "mRightGlow": //NOSONAR
          f.setAccessible(true); //NOSONAR
          RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT = f; //NOSONAR
          break; //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  private static void invalidateViewPagerFields() { //NOSONAR
    if (VIEW_PAGER_FIELD_EDGE_GLOW_LEFT != null && VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT != null) { //NOSONAR
      VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.setAccessible(true); //NOSONAR
      VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.setAccessible(true); //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    Class cls = ViewPager.class; //NOSONAR
    for (Field f : cls.getDeclaredFields()) { //NOSONAR
      switch (f.getName()) { //NOSONAR
        case "mLeftEdge": //NOSONAR
          f.setAccessible(true); //NOSONAR
          VIEW_PAGER_FIELD_EDGE_GLOW_LEFT = f; //NOSONAR
          break; //NOSONAR
        case "mRightEdge": //NOSONAR
          f.setAccessible(true); //NOSONAR
          VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT = f; //NOSONAR
          break; //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  // Setter methods // NOSONAR

  static void setEdgeGlowColor(@NonNull ScrollView scrollView, @ColorInt int color) { //NOSONAR
    invalidateScrollViewFields(); //NOSONAR
    try { //NOSONAR
      Object ee; //NOSONAR
      ee = SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
      ee = SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
    } catch (Exception ex) { //NOSONAR
      if (BuildConfig.DEBUG) ex.printStackTrace(); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setEdgeGlowColor(@NonNull NestedScrollView scrollView, @ColorInt int color) { //NOSONAR
    invalidateNestedScrollViewFields(); //NOSONAR
    try { //NOSONAR
      Object ee = NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
      ee = NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
    } catch (Exception ex) { //NOSONAR
      if (BuildConfig.DEBUG) ex.printStackTrace(); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setEdgeGlowColor(@NonNull AbsListView listView, @ColorInt int color) { //NOSONAR
    invalidateListViewFields(); //NOSONAR
    try { //NOSONAR
      Object ee = LIST_VIEW_FIELD_EDGE_GLOW_TOP.get(listView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
      ee = LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(listView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
    } catch (Exception ex) { //NOSONAR
      if (BuildConfig.DEBUG) ex.printStackTrace(); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setEdgeGlowColor( //NOSONAR
      @NonNull RecyclerView scrollView, //NOSONAR
      final @ColorInt int color, //NOSONAR
      @Nullable RecyclerView.OnScrollListener scrollListener) { //NOSONAR
    invalidateRecyclerViewFields(); //NOSONAR
    invalidateRecyclerViewFields(); //NOSONAR
    if (scrollListener == null) { //NOSONAR
      scrollListener = //NOSONAR
          new RecyclerView.OnScrollListener() { //NOSONAR
            @Override //NOSONAR
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) { //NOSONAR
              super.onScrollStateChanged(recyclerView, newState); //NOSONAR
              EdgeGlowUtil.setEdgeGlowColor(recyclerView, color, this); //NOSONAR
            } // NOSONAR
          }; // NOSONAR
      scrollView.addOnScrollListener(scrollListener); //NOSONAR
    } // NOSONAR
    try { //NOSONAR
      Object ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
      ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
      ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
      ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.get(scrollView); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
    } catch (Exception ex) { //NOSONAR
      if (BuildConfig.DEBUG) ex.printStackTrace(); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setEdgeGlowColor(@NonNull ViewPager pager, @ColorInt int color) { //NOSONAR
    invalidateViewPagerFields(); //NOSONAR
    try { //NOSONAR
      Object ee = VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.get(pager); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
      ee = VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.get(pager); //NOSONAR
      setEffectColor(ee, color); //NOSONAR
    } catch (Exception ex) { //NOSONAR
      if (BuildConfig.DEBUG) ex.printStackTrace(); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  // Utilities // NOSONAR

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) //NOSONAR
  private static void setEffectColor(Object edgeEffect, @ColorInt int color) { //NOSONAR
    invalidateEdgeEffectFields(); //NOSONAR
    if (edgeEffect instanceof EdgeEffectCompat) { //NOSONAR
      // EdgeEffectCompat // NOSONAR
      try { //NOSONAR
        EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true); //NOSONAR
        edgeEffect = EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.get(edgeEffect); //NOSONAR
      } catch (IllegalAccessException e) { //NOSONAR
        e.printStackTrace(); //NOSONAR
        return; //NOSONAR
      } // NOSONAR
    } // NOSONAR
    if (edgeEffect == null) { //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      // EdgeGlow // NOSONAR
      try { //NOSONAR
        EDGE_GLOW_FIELD_EDGE.setAccessible(true); //NOSONAR
        final Drawable mEdge = (Drawable) EDGE_GLOW_FIELD_EDGE.get(edgeEffect); //NOSONAR
        EDGE_GLOW_FIELD_GLOW.setAccessible(true); //NOSONAR
        final Drawable mGlow = (Drawable) EDGE_GLOW_FIELD_GLOW.get(edgeEffect); //NOSONAR
        mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN); //NOSONAR
        mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN); //NOSONAR
        mEdge.setCallback(null); // free up any references //NOSONAR
        mGlow.setCallback(null); // free up any references //NOSONAR
      } catch (Exception ex) { //NOSONAR
        ex.printStackTrace(); //NOSONAR
      } // NOSONAR
    } else { //NOSONAR
      // EdgeEffect // NOSONAR
      ((EdgeEffect) edgeEffect).setColor(color); //NOSONAR
    } // NOSONAR
  } // NOSONAR
} // NOSONAR
