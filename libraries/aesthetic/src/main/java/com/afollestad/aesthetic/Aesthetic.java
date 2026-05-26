package com.afollestad.aesthetic; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.ColorDrawable; // NOSONAR
import android.support.annotation.CheckResult; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.ColorRes; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.StyleRes; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.util.ArrayMap; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.support.v4.widget.DrawerLayout; // NOSONAR
import android.support.v7.app.AppCompatActivity; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.ViewGroup; // NOSONAR

import com.f2prateek.rx.preferences2.RxSharedPreferences; // NOSONAR

import io.reactivex.Observable; // NOSONAR
import io.reactivex.ObservableSource; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.functions.BiFunction; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR
import io.reactivex.functions.Function; // NOSONAR
import io.reactivex.functions.Predicate; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR
import static com.afollestad.aesthetic.Util.isColorLight; // NOSONAR
import static com.afollestad.aesthetic.Util.resolveColor; // NOSONAR
import static com.afollestad.aesthetic.Util.setLightStatusBarCompat; // NOSONAR
import static com.afollestad.aesthetic.Util.setNavBarColorCompat; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"WeakerAccess", "unused"}) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Aesthetic { //NOSONAR

  private static final String PREFS_NAME = "[aesthetic-prefs]"; //NOSONAR
  private static final String KEY_FIRST_TIME = "first_time_%s"; //NOSONAR
  private static final String KEY_ACTIVITY_THEME = "activity_theme_%s"; //NOSONAR
  private static final String KEY_IS_DARK = "is_dark_%s"; //NOSONAR
  private static final String KEY_PRIMARY_COLOR = "primary_color_%s"; //NOSONAR
  private static final String KEY_PRIMARY_DARK_COLOR = "primary_dark_color"; //NOSONAR
  private static final String KEY_ACCENT_COLOR = "accent_color_%s"; //NOSONAR
  private static final String KEY_PRIMARY_TEXT_COLOR = "primary_text"; //NOSONAR
  private static final String KEY_SECONDARY_TEXT_COLOR = "secondary_text"; //NOSONAR
  private static final String KEY_PRIMARY_TEXT_INVERSE_COLOR = "primary_text_inverse"; //NOSONAR
  private static final String KEY_SECONDARY_TEXT_INVERSE_COLOR = "secondary_text_inverse"; //NOSONAR
  private static final String KEY_WINDOW_BG_COLOR = "window_bg_color_%s"; //NOSONAR
  private static final String KEY_STATUS_BAR_COLOR = "status_bar_color_%s"; //NOSONAR
  private static final String KEY_NAV_BAR_COLOR = "nav_bar_color_%s"; //NOSONAR
  private static final String KEY_LIGHT_STATUS_MODE = "light_status_mode"; //NOSONAR
  private static final String KEY_TAB_LAYOUT_BG_MODE = "tab_layout_bg_mode"; //NOSONAR
  private static final String KEY_TAB_LAYOUT_INDICATOR_MODE = "tab_layout_indicator_mode"; //NOSONAR
  private static final String KEY_NAV_VIEW_MODE = "nav_view_mode"; //NOSONAR
  private static final String KEY_BOTTOM_NAV_BG_MODE = "bottom_nav_bg_mode"; //NOSONAR
  private static final String KEY_BOTTOM_NAV_ICONTEXT_MODE = "bottom_nav_icontext_mode"; //NOSONAR
  private static final String KEY_CARD_VIEW_BG_COLOR = "card_view_bg_color"; //NOSONAR
  private static final String KEY_ICON_TITLE_ACTIVE_COLOR = "icon_title_active_color"; //NOSONAR
  private static final String KEY_ICON_TITLE_INACTIVE_COLOR = "icon_title_inactive_color"; //NOSONAR
  private static final String KEY_SNACKBAR_TEXT = "snackbar_text_color"; //NOSONAR
  private static final String KEY_SNACKBAR_ACTION_TEXT = "snackbar_action_text_color"; //NOSONAR

  @SuppressLint("StaticFieldLeak") //NOSONAR
  private static Aesthetic instance; //NOSONAR

  private final ArrayMap<String, Integer> lastActivityThemes; //NOSONAR

  private CompositeDisposable subs; //NOSONAR
  private Context context; //NOSONAR
  private SharedPreferences prefs; //NOSONAR
  private SharedPreferences.Editor editor; //NOSONAR
  private RxSharedPreferences rxPrefs; //NOSONAR

  @SuppressLint("CommitPrefEdits") //NOSONAR
  private Aesthetic(Context context) { //NOSONAR
    this.context = context; //NOSONAR
    prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //NOSONAR
    editor = prefs.edit(); //NOSONAR
    rxPrefs = RxSharedPreferences.create(prefs); //NOSONAR
    lastActivityThemes = new ArrayMap<>(2); //NOSONAR
  } // NOSONAR

  private static String key(@Nullable Context context) { //NOSONAR
    String key; //NOSONAR
    if (context instanceof AestheticKeyProvider) { //NOSONAR
      key = ((AestheticKeyProvider) context).key(); //NOSONAR
    } else { //NOSONAR
      key = "default"; //NOSONAR
    } // NOSONAR
    if (key == null) { //NOSONAR
      key = "default"; //NOSONAR
    } // NOSONAR
    return key; //NOSONAR
  } // NOSONAR

  /** Should be called before super.onCreate() in each Activity. */ // NOSONAR
  @NonNull //NOSONAR
  public void attach(@NonNull AppCompatActivity activity) { //NOSONAR

    LayoutInflater li = activity.getLayoutInflater(); //NOSONAR
    Util.setInflaterFactory(li); //NOSONAR

    String activityThemeKey = String.format(KEY_ACTIVITY_THEME, key(activity)); //NOSONAR
    int latestActivityTheme = instance.prefs.getInt(activityThemeKey, 0); //NOSONAR
    instance.lastActivityThemes.put(activity.getClass().getName(), latestActivityTheme); //NOSONAR
    if (latestActivityTheme != 0) { //NOSONAR
      activity.setTheme(latestActivityTheme); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  private static int getLastActivityTheme(@Nullable Context forContext) { //NOSONAR
    if (forContext == null || instance == null) { //NOSONAR
      return 0; //NOSONAR
    } // NOSONAR
    Integer lastActivityTheme = instance.lastActivityThemes.get(forContext.getClass().getName()); //NOSONAR
    if (lastActivityTheme == null) { //NOSONAR
      return 0; //NOSONAR
    } // NOSONAR
    return lastActivityTheme; //NOSONAR
  } // NOSONAR

  @NonNull //NOSONAR
  @CheckResult //NOSONAR
  public static Aesthetic get(Context context) { //NOSONAR
    if (instance == null) { //NOSONAR
      instance = new Aesthetic(context); //NOSONAR
    } // NOSONAR
    instance.context = context; //NOSONAR

    return instance; //NOSONAR
  } // NOSONAR

  /** Should be called in onPause() of each Activity. */ // NOSONAR
  public void pause(@NonNull AppCompatActivity activity) { //NOSONAR
    if (instance == null) { //NOSONAR
        return; //NOSONAR
    } // NOSONAR
    if (instance.subs != null) { //NOSONAR
        instance.subs.clear(); //NOSONAR
    } // NOSONAR

    if (activity.isFinishing()) { //NOSONAR
      if (instance.context != null //NOSONAR
            && instance.context.getClass().getName().equals(activity.getClass().getName())) { //NOSONAR
        instance.context = null; //NOSONAR
      } // NOSONAR
    } // NOSONAR
  } // NOSONAR

  /** Should be called in onResume() of each Activity. */ // NOSONAR
  public void resume(@NonNull final AppCompatActivity activity) { //NOSONAR
    if (instance == null) { //NOSONAR
      return; //NOSONAR
    } // NOSONAR
    instance.context = activity; //NOSONAR

    if (instance.subs != null) { //NOSONAR
      instance.subs.clear(); //NOSONAR
    } // NOSONAR
    instance.subs = new CompositeDisposable(); //NOSONAR
    instance.subs.add( //NOSONAR
        instance //NOSONAR
            .colorPrimary() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
                    Util.setTaskDescriptionColor(activity, color); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow())); //NOSONAR
    instance.subs.add( //NOSONAR
        instance //NOSONAR
            .activityTheme() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@io.reactivex.annotations.NonNull Integer themeId) { //NOSONAR
                    if (getLastActivityTheme(activity) == themeId) { //NOSONAR
                      return; //NOSONAR
                    } // NOSONAR
                    instance.lastActivityThemes.put(activity.getClass().getName(), themeId); //NOSONAR
                    activity.recreate(); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow())); //NOSONAR
    instance.subs.add( //NOSONAR
        Observable.combineLatest( //NOSONAR
                instance.colorStatusBar(), //NOSONAR
                instance.lightStatusBarMode(), //NOSONAR
                new BiFunction<Integer, Integer, Pair<Integer, Integer>>() { //NOSONAR
                  @Override //NOSONAR
                  public Pair<Integer, Integer> apply(Integer integer, Integer integer2) { //NOSONAR
                    return Pair.create(integer, integer2); //NOSONAR
                  } // NOSONAR
                }) // NOSONAR
            .compose(Rx.<Pair<Integer, Integer>>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Pair<Integer, Integer>>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept( //NOSONAR
                      @io.reactivex.annotations.NonNull Pair<Integer, Integer> result) { //NOSONAR
                    instance.invalidateStatusBar(activity); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow())); //NOSONAR
    instance.subs.add( //NOSONAR
        instance //NOSONAR
            .colorNavigationBar() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
                    setNavBarColorCompat(activity, color); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow())); //NOSONAR
    instance.subs.add( //NOSONAR
        instance //NOSONAR
            .colorWindowBackground() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@io.reactivex.annotations.NonNull Integer color) { //NOSONAR
                    activity.getWindow().setBackgroundDrawable(new ColorDrawable(color)); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow())); //NOSONAR

    if (MaterialDialogsUtil.shouldSupport()) { //NOSONAR
      instance.subs.add(MaterialDialogsUtil.observe(instance)); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  /** Returns true if this method has never been called before. */ // NOSONAR
  public static boolean isFirstTime(AppCompatActivity appCompatActivity) { //NOSONAR
    String key = String.format(KEY_FIRST_TIME, key(appCompatActivity)); //NOSONAR
    boolean firstTime = instance.prefs.getBoolean(key, true); //NOSONAR
    instance.editor.putBoolean(key, false).commit(); //NOSONAR
    return firstTime; //NOSONAR
  } // NOSONAR

  private void invalidateStatusBar(AppCompatActivity activity) { //NOSONAR
    String key = String.format(KEY_STATUS_BAR_COLOR, key(activity)); //NOSONAR
    final int color = prefs.getInt(key, resolveColor(activity, R.attr.colorPrimaryDark)); //NOSONAR

    ViewGroup rootView = Util.getRootView(activity); //NOSONAR
    if (rootView instanceof DrawerLayout) { //NOSONAR
      // Color is set to DrawerLayout, Activity gets transparent status bar // NOSONAR
      setLightStatusBarCompat(activity, false); //NOSONAR
      Util.setStatusBarColorCompat( //NOSONAR
          activity, ContextCompat.getColor(activity, android.R.color.transparent)); //NOSONAR
      ((DrawerLayout) rootView).setStatusBarBackgroundColor(color); //NOSONAR
    } else { //NOSONAR
      Util.setStatusBarColorCompat(activity, color); //NOSONAR
    } // NOSONAR

    final int mode = prefs.getInt(KEY_LIGHT_STATUS_MODE, AutoSwitchMode.AUTO); //NOSONAR
    switch (mode) { //NOSONAR
      case AutoSwitchMode.OFF: //NOSONAR
        setLightStatusBarCompat(activity, false); //NOSONAR
        break; //NOSONAR
      case AutoSwitchMode.ON: //NOSONAR
        setLightStatusBarCompat(activity, true); //NOSONAR
        break; //NOSONAR
      default: //NOSONAR
        setLightStatusBarCompat(activity, isColorLight(color)); //NOSONAR
        break; //NOSONAR
    } // NOSONAR
  } // NOSONAR

  // // NOSONAR
  /////// GETTERS AND SETTERS OF THEME PROPERTIES // NOSONAR
  // // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic activityTheme(@StyleRes int theme) { //NOSONAR
    String key = String.format(KEY_ACTIVITY_THEME, key(context)); //NOSONAR
    editor.putInt(key, theme); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> activityTheme() { //NOSONAR
    String key = String.format(KEY_ACTIVITY_THEME, key(context)); //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(key, 0) //NOSONAR
        .asObservable() //NOSONAR
        .filter( //NOSONAR
            new Predicate<Integer>() { //NOSONAR
              @Override //NOSONAR
              public boolean test(@io.reactivex.annotations.NonNull Integer next) throws Exception { //NOSONAR
                return next != 0 && next != getLastActivityTheme(instance.context); //NOSONAR
              } // NOSONAR
            }); // NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic isDark(boolean isDark) { //NOSONAR
    String key = String.format(KEY_IS_DARK, key(context)); //NOSONAR
    editor.putBoolean(key, isDark).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Boolean> isDark() { //NOSONAR
    String key = String.format(KEY_IS_DARK, key(context)); //NOSONAR
    return rxPrefs.getBoolean(key, false).asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorPrimary(@ColorInt int color) { //NOSONAR
    // needs to be committed immediately so that for statusBarColorAuto() and other auto methods // NOSONAR
    String key = String.format(KEY_PRIMARY_COLOR, key(context)); //NOSONAR
    editor.putInt(key, color).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorPrimaryRes(@ColorRes int color) { //NOSONAR
    return colorPrimary(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> colorPrimary() { //NOSONAR
    String key = String.format(KEY_PRIMARY_COLOR, key(context)); //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(key, resolveColor(context, R.attr.colorPrimary)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorPrimaryDark(@ColorInt int color) { //NOSONAR
    // needs to be committed immediately so that for statusBarColorAuto() and other auto methods // NOSONAR
    editor.putInt(KEY_PRIMARY_DARK_COLOR, color).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorPrimaryDarkRes(@ColorRes int color) { //NOSONAR
    return colorPrimaryDark(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> colorPrimaryDark() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(KEY_PRIMARY_DARK_COLOR, resolveColor(context, R.attr.colorPrimaryDark)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorAccent(@ColorInt int color) { //NOSONAR
    String key = String.format(KEY_ACCENT_COLOR, key(context)); //NOSONAR
    editor.putInt(key, color).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorAccentRes(@ColorRes int color) { //NOSONAR
    return colorAccent(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> colorAccent() { //NOSONAR
    String key = String.format(KEY_ACCENT_COLOR, key(context)); //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(key, resolveColor(context, R.attr.colorAccent)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorPrimary(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_PRIMARY_TEXT_COLOR, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorPrimaryRes(@ColorRes int color) { //NOSONAR
    return textColorPrimary(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> textColorPrimary() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(KEY_PRIMARY_TEXT_COLOR, resolveColor(context, android.R.attr.textColorPrimary)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorSecondary(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_SECONDARY_TEXT_COLOR, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorSecondaryRes(@ColorRes int color) { //NOSONAR
    return textColorSecondary(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> textColorSecondary() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger( //NOSONAR
            KEY_SECONDARY_TEXT_COLOR, resolveColor(context, android.R.attr.textColorSecondary)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorPrimaryInverse(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_PRIMARY_TEXT_INVERSE_COLOR, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorPrimaryInverseRes(@ColorRes int color) { //NOSONAR
    return textColorPrimaryInverse(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> textColorPrimaryInverse() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger( //NOSONAR
            KEY_PRIMARY_TEXT_INVERSE_COLOR, //NOSONAR
            resolveColor(context, android.R.attr.textColorPrimaryInverse)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorSecondaryInverse(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_SECONDARY_TEXT_INVERSE_COLOR, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic textColorSecondaryInverseRes(@ColorRes int color) { //NOSONAR
    return textColorSecondaryInverse(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> textColorSecondaryInverse() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger( //NOSONAR
            KEY_SECONDARY_TEXT_INVERSE_COLOR, //NOSONAR
            resolveColor(context, android.R.attr.textColorSecondaryInverse)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorWindowBackground(@ColorInt int color) { //NOSONAR
    String key = String.format(KEY_WINDOW_BG_COLOR, key(context)); //NOSONAR
    editor.putInt(key, color).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorWindowBackgroundRes(@ColorRes int color) { //NOSONAR
    return colorWindowBackground(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> colorWindowBackground() { //NOSONAR
    String key = String.format(KEY_WINDOW_BG_COLOR, key(context)); //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(key, resolveColor(context, android.R.attr.windowBackground)) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorStatusBar(@ColorInt int color) { //NOSONAR
    String key = String.format(KEY_STATUS_BAR_COLOR, key(context)); //NOSONAR
    editor.putInt(key, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorStatusBarRes(@ColorRes int color) { //NOSONAR
    return colorStatusBar(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorStatusBarAuto() { //NOSONAR
    String statusBarKey = String.format(KEY_STATUS_BAR_COLOR, key(context)); //NOSONAR
    String primaryColorKey = String.format(KEY_PRIMARY_COLOR, key(context)); //NOSONAR
    editor.putInt( //NOSONAR
        statusBarKey, //NOSONAR
        Util.darkenColor( //NOSONAR
            prefs.getInt(primaryColorKey, resolveColor(context, R.attr.colorPrimary)))); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> colorStatusBar() { //NOSONAR
    return colorPrimaryDark() //NOSONAR
        .flatMap( //NOSONAR
            new Function<Integer, ObservableSource<Integer>>() { //NOSONAR
              @Override //NOSONAR
              public ObservableSource<Integer> apply( //NOSONAR
                  @io.reactivex.annotations.NonNull Integer primaryDarkColor) throws Exception { //NOSONAR
                String key = String.format(KEY_STATUS_BAR_COLOR, key(context)); //NOSONAR
                return rxPrefs.getInteger(key, primaryDarkColor).asObservable(); //NOSONAR
              } // NOSONAR
            }); // NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorNavigationBar(@ColorInt int color) { //NOSONAR
    String key = String.format(KEY_NAV_BAR_COLOR, key(context)); //NOSONAR
    editor.putInt(key, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorNavigationBarRes(@ColorRes int color) { //NOSONAR
    return colorNavigationBar(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorNavigationBarAuto(boolean auto) { //NOSONAR
    String navBarKey = String.format(KEY_NAV_BAR_COLOR, key(context)); //NOSONAR
    String primaryColorKey = String.format(KEY_PRIMARY_COLOR, key(context)); //NOSONAR
      if (auto) { //NOSONAR
      int color = prefs.getInt(primaryColorKey, resolveColor(context, R.attr.colorPrimary)); //NOSONAR
      editor.putInt(navBarKey, isColorLight(color) ? Color.BLACK : color); //NOSONAR
    } else { //NOSONAR
      editor.remove(navBarKey); //NOSONAR
    } // NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> colorNavigationBar() { //NOSONAR
    String key = String.format(KEY_NAV_BAR_COLOR, key(context)); //NOSONAR
    return rxPrefs.getInteger(key, Color.BLACK).asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic lightStatusBarMode(@AutoSwitchMode int mode) { //NOSONAR
    editor.putInt(KEY_LIGHT_STATUS_MODE, mode); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> lightStatusBarMode() { //NOSONAR
    return rxPrefs.getInteger(KEY_LIGHT_STATUS_MODE, AutoSwitchMode.AUTO).asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic tabLayoutIndicatorMode(@TabLayoutIndicatorMode int mode) { //NOSONAR
    editor.putInt(KEY_TAB_LAYOUT_INDICATOR_MODE, mode).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> tabLayoutIndicatorMode() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(KEY_TAB_LAYOUT_INDICATOR_MODE, TabLayoutIndicatorMode.ACCENT) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic tabLayoutBackgroundMode(@TabLayoutBgMode int mode) { //NOSONAR
    editor.putInt(KEY_TAB_LAYOUT_BG_MODE, mode).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> tabLayoutBackgroundMode() { //NOSONAR
    return rxPrefs.getInteger(KEY_TAB_LAYOUT_BG_MODE, TabLayoutBgMode.PRIMARY).asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic navigationViewMode(@NavigationViewMode int mode) { //NOSONAR
    editor.putInt(KEY_NAV_VIEW_MODE, mode).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> navigationViewMode() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(KEY_NAV_VIEW_MODE, NavigationViewMode.SELECTED_PRIMARY) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic bottomNavigationBackgroundMode(@BottomNavBgMode int mode) { //NOSONAR
    editor.putInt(KEY_BOTTOM_NAV_BG_MODE, mode).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> bottomNavigationBackgroundMode() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(KEY_BOTTOM_NAV_BG_MODE, BottomNavBgMode.BLACK_WHITE_AUTO) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic bottomNavigationIconTextMode(@BottomNavIconTextMode int mode) { //NOSONAR
    editor.putInt(KEY_BOTTOM_NAV_ICONTEXT_MODE, mode).commit(); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> bottomNavigationIconTextMode() { //NOSONAR
    return rxPrefs //NOSONAR
        .getInteger(KEY_BOTTOM_NAV_ICONTEXT_MODE, BottomNavIconTextMode.SELECTED_ACCENT) //NOSONAR
        .asObservable(); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> colorCardViewBackground() { //NOSONAR
    return isDark() //NOSONAR
        .flatMap( //NOSONAR
            new Function<Boolean, ObservableSource<Integer>>() { //NOSONAR
              @Override //NOSONAR
              public ObservableSource<Integer> apply( //NOSONAR
                  @io.reactivex.annotations.NonNull Boolean isDark) throws Exception { //NOSONAR
                return rxPrefs //NOSONAR
                    .getInteger( //NOSONAR
                        KEY_CARD_VIEW_BG_COLOR, //NOSONAR
                        ContextCompat.getColor( //NOSONAR
                            context, //NOSONAR
                            isDark ? R.color.ate_cardview_bg_dark : R.color.ate_cardview_bg_light)) //NOSONAR
                    .asObservable(); //NOSONAR
              } // NOSONAR
            }); // NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorCardViewBackground(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_CARD_VIEW_BG_COLOR, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorCardViewBackgroundRes(@ColorRes int color) { //NOSONAR
    return colorCardViewBackground(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<ActiveInactiveColors> colorIconTitle( //NOSONAR
      @Nullable Observable<Integer> backgroundObservable) { //NOSONAR
    if (backgroundObservable == null) { //NOSONAR
      backgroundObservable = Aesthetic.get(context).colorPrimary(); //NOSONAR
    } // NOSONAR
    return backgroundObservable.flatMap( //NOSONAR
        new Function<Integer, ObservableSource<ActiveInactiveColors>>() { //NOSONAR
          @Override //NOSONAR
          public ObservableSource<ActiveInactiveColors> apply( //NOSONAR
              @io.reactivex.annotations.NonNull Integer primaryColor) throws Exception { //NOSONAR
            final boolean isDark = !isColorLight(primaryColor); //NOSONAR
            return Observable.zip( //NOSONAR
                rxPrefs //NOSONAR
                    .getInteger( //NOSONAR
                        KEY_ICON_TITLE_ACTIVE_COLOR, //NOSONAR
                        ContextCompat.getColor( //NOSONAR
                            context, isDark ? R.color.ate_icon_dark : R.color.ate_icon_light)) //NOSONAR
                    .asObservable(), //NOSONAR
                rxPrefs //NOSONAR
                    .getInteger( //NOSONAR
                        KEY_ICON_TITLE_INACTIVE_COLOR, //NOSONAR
                        ContextCompat.getColor( //NOSONAR
                            context, //NOSONAR
                            isDark //NOSONAR
                                ? R.color.ate_icon_dark_inactive //NOSONAR
                                : R.color.ate_icon_light_inactive)) //NOSONAR
                    .asObservable(), //NOSONAR
                new BiFunction<Integer, Integer, ActiveInactiveColors>() { //NOSONAR
                  @Override //NOSONAR
                  public ActiveInactiveColors apply( //NOSONAR
                      @io.reactivex.annotations.NonNull Integer integer, //NOSONAR
                      @io.reactivex.annotations.NonNull Integer integer2) //NOSONAR
                      throws Exception { //NOSONAR
                    return ActiveInactiveColors.create(integer, integer2); //NOSONAR
                  } // NOSONAR
                }); // NOSONAR
          } // NOSONAR
        }); // NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorIconTitleActive(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_ICON_TITLE_ACTIVE_COLOR, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorIconTitleActiveRes(@ColorRes int color) { //NOSONAR
    return colorIconTitleActive(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorIconTitleInactive(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_ICON_TITLE_INACTIVE_COLOR, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic colorIconTitleInactiveRes(@ColorRes int color) { //NOSONAR
    return colorIconTitleActive(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> snackbarTextColor() { //NOSONAR
    return isDark() //NOSONAR
        .flatMap( //NOSONAR
            new Function<Boolean, ObservableSource<Integer>>() { //NOSONAR
              @Override //NOSONAR
              public ObservableSource<Integer> apply( //NOSONAR
                  @io.reactivex.annotations.NonNull Boolean isDark) throws Exception { //NOSONAR
                return (isDark ? textColorPrimary() : textColorPrimaryInverse()) //NOSONAR
                    .flatMap( //NOSONAR
                        new Function<Integer, ObservableSource<Integer>>() { //NOSONAR
                          @Override //NOSONAR
                          public ObservableSource<Integer> apply( //NOSONAR
                              @io.reactivex.annotations.NonNull Integer defaultTextColor) //NOSONAR
                              throws Exception { //NOSONAR
                            return rxPrefs //NOSONAR
                                .getInteger(KEY_SNACKBAR_TEXT, defaultTextColor) //NOSONAR
                                .asObservable(); //NOSONAR
                          } // NOSONAR
                        }); // NOSONAR
              } // NOSONAR
            }); // NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic snackbarTextColor(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_SNACKBAR_TEXT, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic snackbarTextColorRes(@ColorRes int color) { //NOSONAR
    return colorCardViewBackground(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Observable<Integer> snackbarActionTextColor() { //NOSONAR
    return colorAccent() //NOSONAR
        .flatMap( //NOSONAR
            new Function<Integer, ObservableSource<Integer>>() { //NOSONAR
              @Override //NOSONAR
              public ObservableSource<Integer> apply( //NOSONAR
                  @io.reactivex.annotations.NonNull Integer accentColor) throws Exception { //NOSONAR
                return rxPrefs.getInteger(KEY_SNACKBAR_ACTION_TEXT, accentColor).asObservable(); //NOSONAR
              } // NOSONAR
            }); // NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic snackbarActionTextColor(@ColorInt int color) { //NOSONAR
    editor.putInt(KEY_SNACKBAR_ACTION_TEXT, color); //NOSONAR
    return this; //NOSONAR
  } // NOSONAR

  @CheckResult //NOSONAR
  public Aesthetic snackbarActionTextColorRes(@ColorRes int color) { //NOSONAR
    return colorCardViewBackground(ContextCompat.getColor(context, color)); //NOSONAR
  } // NOSONAR

  /** Notifies all listening views that theme properties have been updated. */ // NOSONAR
  public void apply() { //NOSONAR
    editor.commit(); //NOSONAR
  } // NOSONAR
} // NOSONAR
