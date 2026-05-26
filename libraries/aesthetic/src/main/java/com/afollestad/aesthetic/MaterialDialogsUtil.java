package com.afollestad.aesthetic;

import android.content.res.ColorStateList;
import android.support.annotation.RestrictTo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Handles auto theming of dialogs from my Material Dialogs library, using the ThemeSingleton class.
 * Uses reflection so that Material Dialogs isn't a needed dependency if you depend on this library.
 *
 * @author Aidan Follestad (afollestad)
 */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class MaterialDialogsUtil { //NOSONAR

  static boolean shouldSupport() { //NOSONAR
    try { //NOSONAR
      Class.forName("com.afollestad.materialdialogs.internal.ThemeSingleton"); //NOSONAR
    } catch (ClassNotFoundException e) { //NOSONAR
      return false; //NOSONAR
    }
    return true; //NOSONAR
  }

  static class Params { //NOSONAR

    final int primaryTextColor; //NOSONAR
    final int secondaryTextColor; //NOSONAR
    final int accentColor; //NOSONAR
    final boolean darkTheme; //NOSONAR

    private Params( //NOSONAR
        int primaryTextColor, int secondaryTextColor, int accentColor, boolean darkTheme) { //NOSONAR
      this.primaryTextColor = primaryTextColor; //NOSONAR
      this.secondaryTextColor = secondaryTextColor; //NOSONAR
      this.accentColor = accentColor; //NOSONAR
      this.darkTheme = darkTheme; //NOSONAR
    }

    public static Params create( //NOSONAR
        int primaryTextColor, int secondaryTextColor, int accentColor, boolean darkTheme) { //NOSONAR
      return new Params(primaryTextColor, secondaryTextColor, accentColor, darkTheme); //NOSONAR
    }
  }

  @SuppressWarnings("TryWithIdenticalCatches") //NOSONAR
  static void theme(Params params) { //NOSONAR
    try { //NOSONAR
      Class<?> cls = Class.forName("com.afollestad.materialdialogs.internal.ThemeSingleton"); //NOSONAR
      Method getMethod = cls.getMethod("get"); //NOSONAR
      Object instance = getMethod.invoke(null); //NOSONAR

      Field fieldDarkTheme = cls.getField("darkTheme"); //NOSONAR
      fieldDarkTheme.set(instance, params.darkTheme); //NOSONAR

      Field fieldTitleColor = cls.getField("titleColor"); //NOSONAR
      fieldTitleColor.set(instance, params.primaryTextColor); //NOSONAR

      Field fieldContentColor = cls.getField("contentColor"); //NOSONAR
      fieldContentColor.set(instance, params.secondaryTextColor); //NOSONAR

      Field fieldItemColor = cls.getField("itemColor"); //NOSONAR
      fieldItemColor.set(instance, params.secondaryTextColor); //NOSONAR

      Field fieldPosColor = cls.getField("positiveColor"); //NOSONAR
      fieldPosColor.set(instance, ColorStateList.valueOf(params.accentColor)); //NOSONAR

      Field fieldNeuColor = cls.getField("neutralColor"); //NOSONAR
      fieldNeuColor.set(instance, ColorStateList.valueOf(params.accentColor)); //NOSONAR

      Field fieldNegColor = cls.getField("negativeColor"); //NOSONAR
      fieldNegColor.set(instance, ColorStateList.valueOf(params.accentColor)); //NOSONAR

      Field fieldWidgetColor = cls.getField("widgetColor"); //NOSONAR
      fieldWidgetColor.set(instance, ColorStateList.valueOf(params.accentColor)); //NOSONAR

      Field fieldLinkColor = cls.getField("linkColor"); //NOSONAR
      fieldLinkColor.set(instance, ColorStateList.valueOf(params.accentColor)); //NOSONAR

    } catch (Throwable t) { //NOSONAR
//      t.printStackTrace();
    }
  }

  static Disposable observe(Aesthetic instance) { //NOSONAR
    return Observable.combineLatest( //NOSONAR
            instance.textColorPrimary(), //NOSONAR
            instance.textColorSecondary(), //NOSONAR
            instance.colorAccent(), //NOSONAR
            instance.isDark(), //NOSONAR
            new Function4<Integer, Integer, Integer, Boolean, Params>() { //NOSONAR
              @Override //NOSONAR
              public MaterialDialogsUtil.Params apply( //NOSONAR
                  @io.reactivex.annotations.NonNull Integer primaryText, //NOSONAR
                  @io.reactivex.annotations.NonNull Integer secondaryText, //NOSONAR
                  @io.reactivex.annotations.NonNull Integer accent, //NOSONAR
                  @io.reactivex.annotations.NonNull Boolean isDark) //NOSONAR
                  throws Exception { //NOSONAR
                return MaterialDialogsUtil.Params.create( //NOSONAR
                    primaryText, secondaryText, accent, isDark); //NOSONAR
              }
            })
        .distinctUntilChanged() //NOSONAR
        .subscribe( //NOSONAR
            new Consumer<Params>() { //NOSONAR
              @Override //NOSONAR
              public void accept( //NOSONAR
                  @io.reactivex.annotations.NonNull MaterialDialogsUtil.Params params) //NOSONAR
                  throws Exception { //NOSONAR
                MaterialDialogsUtil.theme(params); //NOSONAR
              }
            });
  }
}
