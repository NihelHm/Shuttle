package com.afollestad.aesthetic; // NOSONAR

import android.annotation.TargetApi; // NOSONAR
import android.app.Activity; // NOSONAR
import android.app.ActivityManager; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.pm.PackageManager; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.AdaptiveIconDrawable; // NOSONAR
import android.graphics.drawable.BitmapDrawable; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.graphics.drawable.LayerDrawable; // NOSONAR
import android.os.Build; // NOSONAR
import android.support.annotation.AttrRes; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.FloatRange; // NOSONAR
import android.support.annotation.IdRes; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.RequiresApi; // NOSONAR
import android.support.v4.view.LayoutInflaterCompat; // NOSONAR
import android.support.v7.app.AppCompatActivity; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import java.lang.reflect.Field; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings("WeakerAccess") //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class Util { //NOSONAR

  static void setInflaterFactory(@NonNull LayoutInflater li) { //NOSONAR
    LayoutInflaterCompat.setFactory(li, new InflationInterceptor()); //NOSONAR
  } // NOSONAR

  static Field findField(Class clazz, String... names) throws NoSuchFieldException{ //NOSONAR
    for (String name : names) { //NOSONAR
      try { //NOSONAR
        Field field = clazz.getDeclaredField(name); //NOSONAR
        field.setAccessible(true); //NOSONAR
        return field; //NOSONAR
      } catch (NoSuchFieldException ignored){ //NOSONAR
          // Intentionally left empty. // NOSONAR
      } // NOSONAR
    } // NOSONAR

    throw new NoSuchFieldException(); //NOSONAR
  } // NOSONAR

  /** Taken from CollapsingToolbarLayout's CollapsingTextHelper class. */ // NOSONAR
  @ColorInt //NOSONAR
  static int blendColors(int color1, int color2, float ratio) { //NOSONAR
    final float inverseRatio = 1f - ratio; //NOSONAR
    float a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio); //NOSONAR
    float r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio); //NOSONAR
    float g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio); //NOSONAR
    float b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio); //NOSONAR
    return Color.argb((int) a, (int) r, (int) g, (int) b); //NOSONAR
  } // NOSONAR

  @SuppressWarnings("deprecation") //NOSONAR
  static void setBackgroundCompat(@NonNull View view, @Nullable Drawable drawable) { //NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //NOSONAR
      view.setBackground(drawable); //NOSONAR
    } else { //NOSONAR
      view.setBackgroundDrawable(drawable); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setStatusBarColorCompat(@NonNull AppCompatActivity activity, @ColorInt int color) { //NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      activity.getWindow().setStatusBarColor(color); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setNavBarColorCompat(@NonNull AppCompatActivity activity, @ColorInt int color) { //NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      activity.getWindow().setNavigationBarColor(color); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  public static int stripAlpha(@ColorInt int color) { //NOSONAR
    return Color.rgb(Color.red(color), Color.green(color), Color.blue(color)); //NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  static int resolveColor(Context context, @AttrRes int attr) { //NOSONAR
    return resolveColor(context, attr, 0); //NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  static int resolveColor(Context context, @AttrRes int attr, int fallback) { //NOSONAR
    if (context!=null) { //NOSONAR
      TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr}); //NOSONAR
      try { //NOSONAR
        return a.getColor(0, fallback); //NOSONAR
      } catch (Throwable ignored) { //NOSONAR
        return fallback; //NOSONAR
      } finally { //NOSONAR
        a.recycle(); //NOSONAR
      } // NOSONAR
    } // NOSONAR
    return fallback; //NOSONAR
  } // NOSONAR

  @IdRes //NOSONAR
  public static int resolveResId(Context context, @AttrRes int attr, int fallback) { //NOSONAR
    TypedArray a = context.getTheme().obtainStyledAttributes(new int[] {attr}); //NOSONAR
    try { //NOSONAR
      return a.getResourceId(0, fallback); //NOSONAR
    } finally { //NOSONAR
      a.recycle(); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @IdRes //NOSONAR
  public static int resolveResId(Context context, AttributeSet attrs, @AttrRes int attrId) { //NOSONAR
    TypedArray ta = context.obtainStyledAttributes(attrs, new int[] {attrId}); //NOSONAR
    int result = ta.getResourceId(0, 0); //NOSONAR
    ta.recycle(); //NOSONAR
    return result; //NOSONAR
  } // NOSONAR

  //  static ColorStateList resolveActionTextColorStateList( // NOSONAR
  //      Context context, @AttrRes int colorAttr, ColorStateList fallback) { // NOSONAR
  //    TypedArray a = context.getTheme().obtainStyledAttributes(new int[] {colorAttr}); // NOSONAR
  //    try { // NOSONAR
  //      final TypedValue value = a.peekValue(0); // NOSONAR
  //      if (value == null) { // NOSONAR
  //        return fallback; // NOSONAR
  //      } // NOSONAR
  //      if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT // NOSONAR
  //          && value.type <= TypedValue.TYPE_LAST_COLOR_INT) { // NOSONAR
  //        return getActionTextStateList(context, value.data); // NOSONAR
  //      } else { // NOSONAR
  //        final ColorStateList stateList = a.getColorStateList(0); // NOSONAR
  //        if (stateList != null) { // NOSONAR
  //          return stateList; // NOSONAR
  //        } else { // NOSONAR
  //          return fallback; // NOSONAR
  //        } // NOSONAR
  //      } // NOSONAR
  //    } finally { // NOSONAR
  //      a.recycle(); // NOSONAR
  //    } // NOSONAR
  //  } // NOSONAR

  // Get the specified color resource, creating a ColorStateList if the resource // NOSONAR
  // points to a color value. // NOSONAR
  //  static ColorStateList getActionTextColorStateList(Context context, @ColorRes int colorId) { // NOSONAR
  //    final TypedValue value = new TypedValue(); // NOSONAR
  //    context.getResources().getValue(colorId, value, true); // NOSONAR
  //    if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT // NOSONAR
  //        && value.type <= TypedValue.TYPE_LAST_COLOR_INT) { // NOSONAR
  //      return getActionTextStateList(context, value.data); // NOSONAR
  //    } else { // NOSONAR
  // // NOSONAR
  //      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) { // NOSONAR
  //        //noinspection deprecation // NOSONAR
  //        return context.getResources().getColorStateList(colorId); // NOSONAR
  //      } else { // NOSONAR
  //        return context.getColorStateList(colorId); // NOSONAR
  //      } // NOSONAR
  //    } // NOSONAR
  //  } // NOSONAR

  //  static ColorStateList getActionTextStateList(Context context, int newPrimaryColor) { // NOSONAR
  //    final int fallBackButtonColor = resolveColor(context, android.R.attr.textColorPrimary); // NOSONAR
  //    if (newPrimaryColor == 0) { // NOSONAR
  //      newPrimaryColor = fallBackButtonColor; // NOSONAR
  //    } // NOSONAR
  //    int[][] states = // NOSONAR
  //        new int[][] { // NOSONAR
  //          new int[] {-android.R.attr.state_enabled}, // disabled // NOSONAR
  //          new int[] { // NOSONAR
      // Intentionally left empty. // NOSONAR
  } // enabled //NOSONAR
  //        }; // NOSONAR
  //    int[] colors = new int[] {adjustAlpha(newPrimaryColor, 0.4f), newPrimaryColor}; // NOSONAR
  //    return new ColorStateList(states, colors); // NOSONAR
  //  } // NOSONAR

  @ColorInt //NOSONAR
  public static int adjustAlpha( //NOSONAR
      @ColorInt int color, @SuppressWarnings("SameParameterValue") float factor) { //NOSONAR
    int alpha = Math.round(Color.alpha(color) * factor); //NOSONAR
    int red = Color.red(color); //NOSONAR
    int green = Color.green(color); //NOSONAR
    int blue = Color.blue(color); //NOSONAR
    return Color.argb(alpha, red, green, blue); //NOSONAR
  } // NOSONAR

  public static void setOverflowButtonColor(@NonNull final Toolbar toolbar, final @ColorInt int color) { //NOSONAR
    Drawable overflowDrawable = toolbar.getOverflowIcon(); //NOSONAR
    if (overflowDrawable != null) { //NOSONAR
      toolbar.setOverflowIcon(TintHelper.createTintedDrawable(overflowDrawable, color)); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  public static int shiftColor(@ColorInt int color, @FloatRange(from = 0.0f, to = 2.0f) float by) { //NOSONAR
    if (by == 1f) return color; //NOSONAR
    float[] hsv = new float[3]; //NOSONAR
    Color.colorToHSV(color, hsv); //NOSONAR
    hsv[2] *= by; // value component //NOSONAR
    return Color.HSVToColor(hsv); //NOSONAR
  } // NOSONAR

  @ColorInt //NOSONAR
  public static int darkenColor(@ColorInt int color) { //NOSONAR
    return shiftColor(color, 0.9f); //NOSONAR
  } // NOSONAR

  public static boolean isColorLight(@ColorInt int color) { //NOSONAR
    if (color == Color.BLACK) { //NOSONAR
      return false; //NOSONAR
    } else if (color == Color.WHITE || color == Color.TRANSPARENT) { //NOSONAR
      return true; //NOSONAR
    } // NOSONAR
    final double darkness = //NOSONAR
        1 //NOSONAR
            - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) //NOSONAR
                / 255; //NOSONAR
    return darkness < 0.4; //NOSONAR
  } // NOSONAR

  // optional convenience method, this can be called when we have information about the background color and want to consider it // NOSONAR
  static boolean isColorLight(@ColorInt int color, @ColorInt int bgColor) { //NOSONAR
    if (Color.alpha(color) //NOSONAR
        < 128) { // if the color is less than 50% visible rely on the background color //NOSONAR
      return isColorLight( //NOSONAR
          bgColor); // one could use some kind of color mixing here before passing the color //NOSONAR
    } // NOSONAR
    return isColorLight(color); //NOSONAR
  } // NOSONAR

  //  @ColorInt // NOSONAR
  //  static int invertColor(@ColorInt int color) { // NOSONAR
  //    final int r = 255 - Color.red(color); // NOSONAR
  //    final int g = 255 - Color.green(color); // NOSONAR
  //    final int b = 255 - Color.blue(color); // NOSONAR
  //    return Color.argb(Color.alpha(color), r, g, b); // NOSONAR
  //  } // NOSONAR

  static void setLightStatusBarCompat(@NonNull AppCompatActivity activity, boolean lightMode) { //NOSONAR
    final View view = activity.getWindow().getDecorView(); //NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //NOSONAR
      int flags = view.getSystemUiVisibility(); //NOSONAR
      if (lightMode) { //NOSONAR
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //NOSONAR
      } else { //NOSONAR
        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //NOSONAR
      } // NOSONAR
      view.setSystemUiVisibility(flags); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) //NOSONAR
  static void setTaskDescriptionColor(@NonNull Activity activity, @ColorInt int color) { //NOSONAR
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    // Task description requires fully opaque color // NOSONAR
    color = stripAlpha(color); //NOSONAR
    // Default is app's launcher icon // NOSONAR
    Bitmap icon; //NOSONAR
    if (Build.VERSION.SDK_INT >= 26) { //NOSONAR
      icon = getAppIcon(activity.getPackageManager(), activity.getPackageName()); //NOSONAR
    } else { //NOSONAR
      icon = //NOSONAR
          ((BitmapDrawable) activity.getApplicationInfo().loadIcon(activity.getPackageManager())) //NOSONAR
              .getBitmap(); //NOSONAR
    } // NOSONAR
    if (icon != null) { //NOSONAR
      // Sets color of entry in the system recents page // NOSONAR
      ActivityManager.TaskDescription td = //NOSONAR
          new ActivityManager.TaskDescription((String) activity.getTitle(), icon, color); //NOSONAR
      activity.setTaskDescription(td); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @RequiresApi(api = Build.VERSION_CODES.O) //NOSONAR
  private static Bitmap getAppIcon(PackageManager mPackageManager, String packageName) { //NOSONAR
    try { //NOSONAR
      Drawable drawable = mPackageManager.getApplicationIcon(packageName); //NOSONAR

      if (drawable instanceof BitmapDrawable) { //NOSONAR
        return ((BitmapDrawable) drawable).getBitmap(); //NOSONAR
      } else if (drawable instanceof AdaptiveIconDrawable) { //NOSONAR
        Drawable backgroundDr = ((AdaptiveIconDrawable) drawable).getBackground(); //NOSONAR
        Drawable foregroundDr = ((AdaptiveIconDrawable) drawable).getForeground(); //NOSONAR

        Drawable[] drr = new Drawable[2]; //NOSONAR
        drr[0] = backgroundDr; //NOSONAR
        drr[1] = foregroundDr; //NOSONAR

        LayerDrawable layerDrawable = new LayerDrawable(drr); //NOSONAR

        int width = layerDrawable.getIntrinsicWidth(); //NOSONAR
        int height = layerDrawable.getIntrinsicHeight(); //NOSONAR

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //NOSONAR

        Canvas canvas = new Canvas(bitmap); //NOSONAR

        layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight()); //NOSONAR
        layerDrawable.draw(canvas); //NOSONAR

        return bitmap; //NOSONAR
      } // NOSONAR
    } catch (PackageManager.NameNotFoundException e) { //NOSONAR
      e.printStackTrace(); //NOSONAR
    } // NOSONAR
    return null; //NOSONAR
  } // NOSONAR

  //  @Nullable // NOSONAR
  //  static Toolbar getSupportActionBarView(@Nullable ActionBar ab) { // NOSONAR
  //    if (ab == null) return null; // NOSONAR
  //    try { // NOSONAR
  //      Field field = ab.getClass().getDeclaredField("mDecorToolbar"); // NOSONAR
  //      field.setAccessible(true); // NOSONAR
  //      ToolbarWidgetWrapper wrapper = (ToolbarWidgetWrapper) field.get(ab); // NOSONAR
  //      field = ToolbarWidgetWrapper.class.getDeclaredField("mToolbar"); // NOSONAR
  //      field.setAccessible(true); // NOSONAR
  //      return (Toolbar) field.get(wrapper); // NOSONAR
  //    } catch (Throwable t) { // NOSONAR
  //      Log.d("Aesthetic", "Unable to get Toolbar from " + ab.getClass().getName()); // NOSONAR
  //      return null; // NOSONAR
  //    } // NOSONAR
  //  } // NOSONAR

  @NonNull //NOSONAR
  static ViewGroup getRootView(@NonNull Activity activity) { //NOSONAR
    return (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0); //NOSONAR
  } // NOSONAR
} // NOSONAR
