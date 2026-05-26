package com.afollestad.aesthetic; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.res.ColorStateList; // NOSONAR
import android.graphics.PorterDuff; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.graphics.drawable.RippleDrawable; // NOSONAR
import android.os.Build; // NOSONAR
import android.support.annotation.CheckResult; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.RestrictTo; // NOSONAR
import android.support.design.widget.FloatingActionButton; // NOSONAR
import android.support.design.widget.TextInputEditText; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.support.v4.view.TintableBackgroundView; // NOSONAR
import android.support.v4.view.ViewCompat; // NOSONAR
import android.support.v7.widget.SwitchCompat; // NOSONAR
import android.view.View; // NOSONAR
import android.widget.Button; // NOSONAR
import android.widget.CheckBox; // NOSONAR
import android.widget.EditText; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.ProgressBar; // NOSONAR
import android.widget.RadioButton; // NOSONAR
import android.widget.SeekBar; // NOSONAR
import android.widget.Switch; // NOSONAR
import android.widget.TextView; // NOSONAR
import java.lang.reflect.Field; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class TintHelper { //NOSONAR

  @SuppressLint("PrivateResource") //NOSONAR
  @ColorInt //NOSONAR
  private static int getDefaultRippleColor(@NonNull Context context, boolean useDarkRipple) { //NOSONAR
    // Light ripple is actually translucent black, and vice versa // NOSONAR
    return ContextCompat.getColor( //NOSONAR
        context, useDarkRipple ? R.color.ripple_material_light : R.color.ripple_material_dark); //NOSONAR
  } // NOSONAR

  @NonNull //NOSONAR
  private static ColorStateList getDisabledColorStateList( //NOSONAR
      @ColorInt int normal, @ColorInt int disabled) { //NOSONAR
    return new ColorStateList( //NOSONAR
        new int[][] { //NOSONAR
          new int[] {-android.R.attr.state_enabled}, new int[] {android.R.attr.state_enabled} //NOSONAR
        }, // NOSONAR
        new int[] {disabled, normal}); //NOSONAR
  } // NOSONAR

  @SuppressWarnings("deprecation") //NOSONAR
  private static void setTintSelector( //NOSONAR
      @NonNull View view, //NOSONAR
      @ColorInt final int color, //NOSONAR
      final boolean darker, //NOSONAR
      final boolean useDarkTheme) { //NOSONAR
    final boolean isColorLight = Util.isColorLight(color); //NOSONAR
    final int disabled = //NOSONAR
        ContextCompat.getColor( //NOSONAR
            view.getContext(), //NOSONAR
            useDarkTheme ? R.color.ate_button_disabled_dark : R.color.ate_button_disabled_light); //NOSONAR
    final int pressed = Util.shiftColor(color, darker ? 0.9f : 1.1f); //NOSONAR
    final int activated = Util.shiftColor(color, darker ? 1.1f : 0.9f); //NOSONAR
    final int rippleColor = getDefaultRippleColor(view.getContext(), isColorLight); //NOSONAR
    final int textColor = //NOSONAR
        ContextCompat.getColor( //NOSONAR
            view.getContext(), //NOSONAR
            isColorLight ? R.color.ate_primary_text_light : R.color.ate_primary_text_dark); //NOSONAR

    final ColorStateList sl; //NOSONAR
    if (view instanceof Button) { //NOSONAR
      sl = getDisabledColorStateList(color, disabled); //NOSONAR
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP //NOSONAR
          && view.getBackground() instanceof RippleDrawable) { //NOSONAR
        RippleDrawable rd = (RippleDrawable) view.getBackground(); //NOSONAR
        rd.setColor(ColorStateList.valueOf(rippleColor)); //NOSONAR
      } // NOSONAR

      // Disabled text color state for buttons, may get overridden later by ATE tags // NOSONAR
      final Button button = (Button) view; //NOSONAR
      button.setTextColor( //NOSONAR
          getDisabledColorStateList( //NOSONAR
              textColor, //NOSONAR
              ContextCompat.getColor( //NOSONAR
                  view.getContext(), //NOSONAR
                  useDarkTheme //NOSONAR
                      ? R.color.ate_button_text_disabled_dark //NOSONAR
                      : R.color.ate_button_text_disabled_light))); //NOSONAR
    } else if (view instanceof FloatingActionButton) { //NOSONAR
      // FloatingActionButton doesn't support disabled state? // NOSONAR
      sl = //NOSONAR
          new ColorStateList( //NOSONAR
              new int[][] { //NOSONAR
                new int[] {-android.R.attr.state_pressed}, new int[] {android.R.attr.state_pressed} //NOSONAR
              }, // NOSONAR
              new int[] {color, pressed}); //NOSONAR

      final FloatingActionButton fab = (FloatingActionButton) view; //NOSONAR
      fab.setRippleColor(rippleColor); //NOSONAR
      fab.setBackgroundTintList(sl); //NOSONAR
      if (fab.getDrawable() != null) //NOSONAR
        fab.setImageDrawable(createTintedDrawable(fab.getDrawable(), textColor)); //NOSONAR
      return; //NOSONAR
    } else { //NOSONAR
      sl = //NOSONAR
          new ColorStateList( //NOSONAR
              new int[][] { //NOSONAR
                new int[] {-android.R.attr.state_enabled}, //NOSONAR
                new int[] {android.R.attr.state_enabled}, //NOSONAR
                new int[] {android.R.attr.state_enabled, android.R.attr.state_pressed}, //NOSONAR
                new int[] {android.R.attr.state_enabled, android.R.attr.state_activated}, //NOSONAR
                new int[] {android.R.attr.state_enabled, android.R.attr.state_checked} //NOSONAR
              }, // NOSONAR
              new int[] {disabled, color, pressed, activated, activated}); //NOSONAR
    } // NOSONAR

    Drawable drawable = view.getBackground(); //NOSONAR
    if (drawable != null) { //NOSONAR
      drawable = createTintedDrawable(drawable, sl); //NOSONAR
      Util.setBackgroundCompat(view, drawable); //NOSONAR
    } // NOSONAR

    if (view instanceof TextView && !(view instanceof Button)) { //NOSONAR
      final TextView tv = (TextView) view; //NOSONAR
      tv.setTextColor( //NOSONAR
          getDisabledColorStateList( //NOSONAR
              textColor, //NOSONAR
              ContextCompat.getColor( //NOSONAR
                  view.getContext(), //NOSONAR
                  isColorLight //NOSONAR
                      ? R.color.ate_text_disabled_light //NOSONAR
                      : R.color.ate_text_disabled_dark))); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @SuppressWarnings("deprecation") //NOSONAR
  @SuppressLint("PrivateResource") //NOSONAR
  static void setTintAuto( //NOSONAR
      final @NonNull View view, //NOSONAR
      final @ColorInt int color, //NOSONAR
      boolean background, //NOSONAR
      final boolean isDark) { //NOSONAR
    if (!background) { //NOSONAR
      if (view instanceof RadioButton) { //NOSONAR
        setTint((RadioButton) view, color, isDark); //NOSONAR
      } else if (view instanceof SeekBar) { //NOSONAR
        setTint((SeekBar) view, color, isDark); //NOSONAR
      } else if (view instanceof ProgressBar) { //NOSONAR
        setTint((ProgressBar) view, color); //NOSONAR
      } else if (view instanceof EditText) { //NOSONAR
        setTint((EditText) view, color, isDark); //NOSONAR
      } else if (view instanceof CheckBox) { //NOSONAR
        setTint((CheckBox) view, color, isDark); //NOSONAR
      } else if (view instanceof ImageView) { //NOSONAR
        setTint((ImageView) view, color); //NOSONAR
      } else if (view instanceof Switch) { //NOSONAR
        setTint((Switch) view, color, isDark); //NOSONAR
      } else if (view instanceof SwitchCompat) { //NOSONAR
        setTint((SwitchCompat) view, color, isDark); //NOSONAR
      } else { //NOSONAR
        background = true; //NOSONAR
      } // NOSONAR

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP //NOSONAR
          && !background //NOSONAR
          && view.getBackground() instanceof RippleDrawable) { //NOSONAR
        // Ripples for the above views (e.g. when you tap and hold a switch or checkbox) // NOSONAR
        RippleDrawable rd = (RippleDrawable) view.getBackground(); //NOSONAR
        final int unchecked = //NOSONAR
            ContextCompat.getColor( //NOSONAR
                view.getContext(), //NOSONAR
                isDark ? R.color.ripple_material_dark : R.color.ripple_material_light); //NOSONAR
        final int checked = Util.adjustAlpha(color, 0.4f); //NOSONAR
        final ColorStateList sl = //NOSONAR
            new ColorStateList( //NOSONAR
                new int[][] { //NOSONAR
                  new int[] {-android.R.attr.state_activated, -android.R.attr.state_checked}, //NOSONAR
                  new int[] {android.R.attr.state_activated}, //NOSONAR
                  new int[] {android.R.attr.state_checked} //NOSONAR
                }, // NOSONAR
                new int[] {unchecked, checked, checked}); //NOSONAR
        rd.setColor(sl); //NOSONAR
      } // NOSONAR
    } // NOSONAR
    if (background) { //NOSONAR
      // Need to tint the background of a view // NOSONAR
      if (view instanceof FloatingActionButton || view instanceof Button) { //NOSONAR
        setTintSelector(view, color, false, isDark); //NOSONAR
      } else if (view.getBackground() != null) { //NOSONAR
        Drawable drawable = view.getBackground(); //NOSONAR
        if (drawable != null) { //NOSONAR
          if (view instanceof TextInputEditText) { //NOSONAR
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN); //NOSONAR
          } else { //NOSONAR
            drawable = createTintedDrawable(drawable, color); //NOSONAR
            Util.setBackgroundCompat(view, drawable); //NOSONAR
          } // NOSONAR
        } // NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setTint(@NonNull RadioButton radioButton, @ColorInt int color, boolean useDarker) { //NOSONAR
    ColorStateList sl = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_enabled}, //NOSONAR
              new int[] {android.R.attr.state_enabled, -android.R.attr.state_checked}, //NOSONAR
              new int[] {android.R.attr.state_enabled, android.R.attr.state_checked} //NOSONAR
            }, // NOSONAR
            new int[] { //NOSONAR
              // Rdio button includes own alpha for disabled state // NOSONAR
              Util.stripAlpha( //NOSONAR
                  ContextCompat.getColor( //NOSONAR
                      radioButton.getContext(), //NOSONAR
                      useDarker //NOSONAR
                          ? R.color.ate_control_disabled_dark //NOSONAR
                          : R.color.ate_control_disabled_light)), //NOSONAR
              ContextCompat.getColor( //NOSONAR
                  radioButton.getContext(), //NOSONAR
                  useDarker ? R.color.ate_control_normal_dark : R.color.ate_control_normal_light), //NOSONAR
              color //NOSONAR
            }); // NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      radioButton.setButtonTintList(sl); //NOSONAR
    } else { //NOSONAR
      @SuppressLint("PrivateResource") //NOSONAR
      Drawable d = //NOSONAR
          createTintedDrawable( //NOSONAR
              ContextCompat.getDrawable( //NOSONAR
                  radioButton.getContext(), R.drawable.abc_btn_radio_material), //NOSONAR
              sl); //NOSONAR
      radioButton.setButtonDrawable(d); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setTint(@NonNull SeekBar seekBar, @ColorInt int color, boolean useDarker) { //NOSONAR
    final ColorStateList s1 = //NOSONAR
        getDisabledColorStateList( //NOSONAR
            color, //NOSONAR
            ContextCompat.getColor( //NOSONAR
                seekBar.getContext(), //NOSONAR
                useDarker //NOSONAR
                    ? R.color.ate_control_disabled_dark //NOSONAR
                    : R.color.ate_control_disabled_light)); //NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      seekBar.setThumbTintList(s1); //NOSONAR
      seekBar.setProgressTintList(s1); //NOSONAR
    } else { //NOSONAR
      Drawable progressDrawable = createTintedDrawable(seekBar.getProgressDrawable(), s1); //NOSONAR
      seekBar.setProgressDrawable(progressDrawable); //NOSONAR
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //NOSONAR
        Drawable thumbDrawable = createTintedDrawable(seekBar.getThumb(), s1); //NOSONAR
        seekBar.setThumb(thumbDrawable); //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setTint(@NonNull ProgressBar progressBar, @ColorInt int color) { //NOSONAR
    setTint(progressBar, color, false); //NOSONAR
  } // NOSONAR

  private static void setTint( //NOSONAR
      @NonNull ProgressBar progressBar, @ColorInt int color, boolean skipIndeterminate) { //NOSONAR
    ColorStateList sl = ColorStateList.valueOf(color); //NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      progressBar.setProgressTintList(sl); //NOSONAR
      progressBar.setSecondaryProgressTintList(sl); //NOSONAR
      if (!skipIndeterminate) { //NOSONAR
        progressBar.setIndeterminateTintList(sl); //NOSONAR
      } // NOSONAR
    } else { //NOSONAR
      PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN; //NOSONAR
      if (!skipIndeterminate && progressBar.getIndeterminateDrawable() != null) { //NOSONAR
        progressBar.getIndeterminateDrawable().setColorFilter(color, mode); //NOSONAR
      } // NOSONAR
      if (progressBar.getProgressDrawable() != null) { //NOSONAR
        progressBar.getProgressDrawable().setColorFilter(color, mode); //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  private static void setTint(@NonNull EditText editText, @ColorInt int color, boolean useDarker) { //NOSONAR
    final ColorStateList editTextColorStateList = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_enabled}, //NOSONAR
              new int[] { //NOSONAR
                android.R.attr.state_enabled, //NOSONAR
                -android.R.attr.state_pressed, //NOSONAR
                -android.R.attr.state_focused //NOSONAR
              }, // NOSONAR
              new int[] { //NOSONAR
                  // Intentionally left empty. // NOSONAR
              } // NOSONAR
            }, // NOSONAR
            new int[] { //NOSONAR
              ContextCompat.getColor( //NOSONAR
                  editText.getContext(), //NOSONAR
                  useDarker ? R.color.ate_text_disabled_dark : R.color.ate_text_disabled_light), //NOSONAR
              ContextCompat.getColor( //NOSONAR
                  editText.getContext(), //NOSONAR
                  useDarker ? R.color.ate_control_normal_dark : R.color.ate_control_normal_light), //NOSONAR
              color //NOSONAR
            }); // NOSONAR
    if (editText instanceof TintableBackgroundView) { //NOSONAR
      ViewCompat.setBackgroundTintList(editText, editTextColorStateList); //NOSONAR
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      editText.setBackgroundTintList(editTextColorStateList); //NOSONAR
    } // NOSONAR
    setCursorTint(editText, color); //NOSONAR
  } // NOSONAR

  static void setTint(@NonNull CheckBox box, @ColorInt int color, boolean useDarker) { //NOSONAR
    ColorStateList sl = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_enabled}, //NOSONAR
              new int[] {android.R.attr.state_enabled, -android.R.attr.state_checked}, //NOSONAR
              new int[] {android.R.attr.state_enabled, android.R.attr.state_checked} //NOSONAR
            }, // NOSONAR
            new int[] { //NOSONAR
              ContextCompat.getColor( //NOSONAR
                  box.getContext(), //NOSONAR
                  useDarker //NOSONAR
                      ? R.color.ate_control_disabled_dark //NOSONAR
                      : R.color.ate_control_disabled_light), //NOSONAR
              ContextCompat.getColor( //NOSONAR
                  box.getContext(), //NOSONAR
                  useDarker ? R.color.ate_control_normal_dark : R.color.ate_control_normal_light), //NOSONAR
              color //NOSONAR
            }); // NOSONAR
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
      box.setButtonTintList(sl); //NOSONAR
    } else { //NOSONAR
      @SuppressLint("PrivateResource") //NOSONAR
      Drawable drawable = //NOSONAR
          createTintedDrawable( //NOSONAR
              ContextCompat.getDrawable(box.getContext(), R.drawable.abc_btn_check_material), sl); //NOSONAR
      box.setButtonDrawable(drawable); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  private static void setTint(@NonNull ImageView image, @ColorInt int color) { //NOSONAR
    image.setColorFilter(color, PorterDuff.Mode.SRC_ATOP); //NOSONAR
  } // NOSONAR

  private static Drawable modifySwitchDrawable( //NOSONAR
      @NonNull Context context, //NOSONAR
      @NonNull Drawable from, //NOSONAR
      @ColorInt int tint, //NOSONAR
      boolean thumb, //NOSONAR
      boolean compatSwitch, //NOSONAR
      boolean useDarker) { //NOSONAR
    if (useDarker) { //NOSONAR
      tint = Util.shiftColor(tint, 1.1f); //NOSONAR
    } // NOSONAR
    tint = Util.adjustAlpha(tint, (compatSwitch && !thumb) ? 0.5f : 1.0f); //NOSONAR
    int disabled; //NOSONAR
    int normal; //NOSONAR
    if (thumb) { //NOSONAR
      disabled = //NOSONAR
          ContextCompat.getColor( //NOSONAR
              context, //NOSONAR
              useDarker //NOSONAR
                  ? R.color.ate_switch_thumb_disabled_dark //NOSONAR
                  : R.color.ate_switch_thumb_disabled_light); //NOSONAR
      normal = //NOSONAR
          ContextCompat.getColor( //NOSONAR
              context, //NOSONAR
              useDarker //NOSONAR
                  ? R.color.ate_switch_thumb_normal_dark //NOSONAR
                  : R.color.ate_switch_thumb_normal_light); //NOSONAR
    } else { //NOSONAR
      disabled = //NOSONAR
          ContextCompat.getColor( //NOSONAR
              context, //NOSONAR
              useDarker //NOSONAR
                  ? R.color.ate_switch_track_disabled_dark //NOSONAR
                  : R.color.ate_switch_track_disabled_light); //NOSONAR
      normal = //NOSONAR
          ContextCompat.getColor( //NOSONAR
              context, //NOSONAR
              useDarker //NOSONAR
                  ? R.color.ate_switch_track_normal_dark //NOSONAR
                  : R.color.ate_switch_track_normal_light); //NOSONAR
    } // NOSONAR

    // Stock switch includes its own alpha // NOSONAR
    if (!compatSwitch) { //NOSONAR
      normal = Util.stripAlpha(normal); //NOSONAR
    } // NOSONAR

    final ColorStateList sl = //NOSONAR
        new ColorStateList( //NOSONAR
            new int[][] { //NOSONAR
              new int[] {-android.R.attr.state_enabled}, //NOSONAR
              new int[] { //NOSONAR
                android.R.attr.state_enabled, //NOSONAR
                -android.R.attr.state_activated, //NOSONAR
                -android.R.attr.state_checked //NOSONAR
              }, // NOSONAR
              new int[] {android.R.attr.state_enabled, android.R.attr.state_activated}, //NOSONAR
              new int[] {android.R.attr.state_enabled, android.R.attr.state_checked} //NOSONAR
            }, // NOSONAR
            new int[] {disabled, normal, tint, tint}); //NOSONAR
    return createTintedDrawable(from, sl); //NOSONAR
  } // NOSONAR

  static void setTint(@NonNull Switch switchView, @ColorInt int color, boolean useDarker) { //NOSONAR
    if (switchView.getTrackDrawable() != null) { //NOSONAR
      switchView.setTrackDrawable( //NOSONAR
          modifySwitchDrawable( //NOSONAR
              switchView.getContext(), //NOSONAR
              switchView.getTrackDrawable(), //NOSONAR
              color, //NOSONAR
              false, //NOSONAR
              false, //NOSONAR
              useDarker)); //NOSONAR
    } // NOSONAR
    if (switchView.getThumbDrawable() != null) { //NOSONAR
      switchView.setThumbDrawable( //NOSONAR
          modifySwitchDrawable( //NOSONAR
              switchView.getContext(), //NOSONAR
              switchView.getThumbDrawable(), //NOSONAR
              color, //NOSONAR
              true, //NOSONAR
              false, //NOSONAR
              useDarker)); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  static void setTint(@NonNull SwitchCompat switchView, @ColorInt int color, boolean useDarker) { //NOSONAR
    if (switchView.getTrackDrawable() != null) { //NOSONAR
      switchView.setTrackDrawable( //NOSONAR
          modifySwitchDrawable( //NOSONAR
              switchView.getContext(), //NOSONAR
              switchView.getTrackDrawable(), //NOSONAR
              color, //NOSONAR
              false, //NOSONAR
              true, //NOSONAR
              useDarker)); //NOSONAR
    } // NOSONAR
    if (switchView.getThumbDrawable() != null) { //NOSONAR
      switchView.setThumbDrawable( //NOSONAR
          modifySwitchDrawable( //NOSONAR
              switchView.getContext(), //NOSONAR
              switchView.getThumbDrawable(), //NOSONAR
              color, //NOSONAR
              true, //NOSONAR
              true, //NOSONAR
              useDarker)); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  // This returns a NEW Drawable because of the mutate() call. The mutate() call is necessary because Drawables with the same resource have shared states otherwise. // NOSONAR
  @CheckResult //NOSONAR
  @Nullable //NOSONAR
  static Drawable createTintedDrawable(@Nullable Drawable drawable, @ColorInt int color) { //NOSONAR
    if (drawable == null) return null; //NOSONAR
    drawable = DrawableCompat.wrap(drawable.mutate()); //NOSONAR
    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN); //NOSONAR
    DrawableCompat.setTint(drawable, color); //NOSONAR
    return drawable; //NOSONAR
  } // NOSONAR

  // This returns a NEW Drawable because of the mutate() call. The mutate() call is necessary because Drawables with the same resource have shared states otherwise. // NOSONAR
  @CheckResult //NOSONAR
  @Nullable //NOSONAR
  static Drawable createTintedDrawable(@Nullable Drawable drawable, @NonNull ColorStateList sl) { //NOSONAR
    if (drawable == null) return null; //NOSONAR
    drawable = DrawableCompat.wrap(drawable.mutate()); //NOSONAR
    DrawableCompat.setTintList(drawable, sl); //NOSONAR
    return drawable; //NOSONAR
  } // NOSONAR

  static void setCursorTint(@NonNull EditText editText, @ColorInt int color) { //NOSONAR
    try { //NOSONAR
      Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes"); //NOSONAR
      fCursorDrawableRes.setAccessible(true); //NOSONAR
      int mCursorDrawableRes = fCursorDrawableRes.getInt(editText); //NOSONAR
      Field fEditor = TextView.class.getDeclaredField("mEditor"); //NOSONAR
      fEditor.setAccessible(true); //NOSONAR
      Object editor = fEditor.get(editText); //NOSONAR
      Class<?> clazz = editor.getClass(); //NOSONAR
      Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable"); //NOSONAR
      fCursorDrawable.setAccessible(true); //NOSONAR
      Drawable[] drawables = new Drawable[2]; //NOSONAR
      drawables[0] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes); //NOSONAR
      drawables[0] = createTintedDrawable(drawables[0], color); //NOSONAR
      drawables[1] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes); //NOSONAR
      drawables[1] = createTintedDrawable(drawables[1], color); //NOSONAR
      fCursorDrawable.set(editor, drawables); //NOSONAR
    } catch (Exception e) { //NOSONAR
      e.printStackTrace(); //NOSONAR
    } // NOSONAR
  } // NOSONAR
} // NOSONAR
