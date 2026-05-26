package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.adjustAlpha;
import static com.afollestad.aesthetic.Util.resolveResId;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class AestheticTextInputLayout extends TextInputLayout {

  private CompositeDisposable subs;
  private int backgroundResId;

  public AestheticTextInputLayout(Context context) {
    super(context);
  }

  public AestheticTextInputLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AestheticTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      backgroundResId = resolveResId(context, attrs, android.R.attr.background);
    }
  }

  private void invalidateColors(int color) {
    TextInputLayoutUtil.setAccent(this, color);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    subs = new CompositeDisposable();
    subs.add(
        Aesthetic.get(getContext())
            .textColorSecondary()
            .compose(Rx.<Integer>distinctToMainThread())
            .subscribe(
                new Consumer<Integer>() {
                  @Override
                  public void accept(@NonNull Integer color) {
                    TextInputLayoutUtil.setHint(
                        AestheticTextInputLayout.this, adjustAlpha(color, 0.7f));
                  }
                },
                onErrorLogAndRethrow()));
    subs.add(
        ViewUtil.getObservableForResId(getContext(), backgroundResId, Aesthetic.get(getContext()).colorAccent())
            .compose(Rx.<Integer>distinctToMainThread())
            .subscribe(
                new Consumer<Integer>() {
                  @Override
                  public void accept(@NonNull Integer color) {
                    invalidateColors(color);
                  }
                },
                onErrorLogAndRethrow()));
  }

  @Override
  protected void onDetachedFromWindow() {
    subs.clear();
    super.onDetachedFromWindow();
  }
}
