package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.resolveResId;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class AestheticTextInputEditText extends TextInputEditText {

  private CompositeDisposable subs;
  private int backgroundResId;
  private ColorIsDarkState lastState;

  public AestheticTextInputEditText(Context context) {
    super(context);
  }

  public AestheticTextInputEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AestheticTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      backgroundResId = resolveResId(context, attrs, android.R.attr.background);
    }
  }

  private void invalidateColors(ColorIsDarkState state) {
    this.lastState = state;
    TintHelper.setTintAuto(this, state.color(), true, state.isDark());
    TintHelper.setCursorTint(this, state.color());
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    subs = new CompositeDisposable();
    subs.add(
        Aesthetic.get(getContext())
            .textColorPrimary()
            .compose(Rx.<Integer>distinctToMainThread())
            .subscribe(ViewTextColorAction.create(this), onErrorLogAndRethrow()));
    subs.add(
        Aesthetic.get(getContext())
            .textColorSecondary()
            .compose(Rx.<Integer>distinctToMainThread())
            .subscribe(ViewHintTextColorAction.create(this), onErrorLogAndRethrow()));
    //noinspection ConstantConditions
    subs.add(
        Observable.combineLatest(
                ViewUtil.getObservableForResId(
                    getContext(), backgroundResId, Aesthetic.get(getContext()).colorAccent()),
                Aesthetic.get(getContext()).isDark(),
                ColorIsDarkState.creator())
            .compose(Rx.<ColorIsDarkState>distinctToMainThread())
            .subscribe(
                new Consumer<ColorIsDarkState>() {
                  @Override
                  public void accept(@NonNull ColorIsDarkState colorIsDarkState) {
                    invalidateColors(colorIsDarkState);
                  }
                },
                onErrorLogAndRethrow()));
  }

  @Override
  protected void onDetachedFromWindow() {
    subs.clear();
    super.onDetachedFromWindow();
  }

  @Override
  public void refreshDrawableState() {
    super.refreshDrawableState();
    if (lastState != null) {
      post(
          new Runnable() {
            @Override
            public void run() {
              invalidateColors(lastState);
            }
          });
    }
  }
}
