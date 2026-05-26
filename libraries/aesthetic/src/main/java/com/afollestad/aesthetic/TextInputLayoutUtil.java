package com.afollestad.aesthetic;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.TextInputLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** @author Aidan Follestad (afollestad) */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class TextInputLayoutUtil { //NOSONAR

  static void setHint(@NonNull TextInputLayout view, @ColorInt int hintColor) { //NOSONAR
    try { //NOSONAR
      final Field mDefaultTextColorField = //NOSONAR
         Util.findField(TextInputLayout.class, "defaultHintTextColor", "mDefaultTextColor"); //NOSONAR
      mDefaultTextColorField.setAccessible(true); //NOSONAR
      mDefaultTextColorField.set(view, ColorStateList.valueOf(hintColor)); //NOSONAR
      final Method updateLabelStateMethod = //NOSONAR
          TextInputLayout.class.getDeclaredMethod("updateLabelState", boolean.class, boolean.class); //NOSONAR
      updateLabelStateMethod.setAccessible(true); //NOSONAR
      updateLabelStateMethod.invoke(view, false, true); //NOSONAR
    } catch (Throwable t) { //NOSONAR
      throw new IllegalStateException( //NOSONAR
          "Failed to set TextInputLayout hint (collapsed) color: " + t.getLocalizedMessage(), t); //NOSONAR
    }
  }

  static void setAccent(@NonNull TextInputLayout view, @ColorInt int accentColor) { //NOSONAR
    try { //NOSONAR
      final Field mFocusedTextColorField = Util.findField(TextInputLayout.class, "focusedTextColor", "mFocusedTextColor"); //NOSONAR
      mFocusedTextColorField.setAccessible(true); //NOSONAR
      mFocusedTextColorField.set(view, ColorStateList.valueOf(accentColor)); //NOSONAR
      final Method updateLabelStateMethod = //NOSONAR
          TextInputLayout.class.getDeclaredMethod("updateLabelState", boolean.class, boolean.class); //NOSONAR
      updateLabelStateMethod.setAccessible(true); //NOSONAR
      updateLabelStateMethod.invoke(view, false, true); //NOSONAR
    } catch (Throwable t) { //NOSONAR
      throw new IllegalStateException( //NOSONAR
          "Failed to set TextInputLayout accent (expanded) color: " + t.getLocalizedMessage(), t); //NOSONAR
    }
  }
}
