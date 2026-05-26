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
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticTextInputEditText extends TextInputEditText { //NOSONAR

  private CompositeDisposable subs; //NOSONAR
  private int backgroundResId; //NOSONAR
  private ColorIsDarkState lastState; //NOSONAR

  public AestheticTextInputEditText(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticTextInputEditText(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  public AestheticTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      backgroundResId = resolveResId(context, attrs, android.R.attr.background); //NOSONAR
    }
  }

  private void invalidateColors(ColorIsDarkState state) { //NOSONAR
    this.lastState = state; //NOSONAR
    TintHelper.setTintAuto(this, state.color(), true, state.isDark()); //NOSONAR
    TintHelper.setCursorTint(this, state.color()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    subs = new CompositeDisposable(); //NOSONAR
    subs.add( //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .textColorPrimary() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe(ViewTextColorAction.create(this), onErrorLogAndRethrow())); //NOSONAR
    subs.add( //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .textColorSecondary() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe(ViewHintTextColorAction.create(this), onErrorLogAndRethrow())); //NOSONAR
    //noinspection ConstantConditions
    subs.add( //NOSONAR
        Observable.combineLatest( //NOSONAR
                ViewUtil.getObservableForResId( //NOSONAR
                    getContext(), backgroundResId, Aesthetic.get(getContext()).colorAccent()), //NOSONAR
                Aesthetic.get(getContext()).isDark(), //NOSONAR
                ColorIsDarkState.creator()) //NOSONAR
            .compose(Rx.<ColorIsDarkState>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<ColorIsDarkState>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull ColorIsDarkState colorIsDarkState) { //NOSONAR
                    invalidateColors(colorIsDarkState); //NOSONAR
                  }
                },
                onErrorLogAndRethrow())); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subs.clear(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  }

  @Override //NOSONAR
  public void refreshDrawableState() { //NOSONAR
    super.refreshDrawableState(); //NOSONAR
    if (lastState != null) { //NOSONAR
      post( //NOSONAR
          new Runnable() { //NOSONAR
            @Override //NOSONAR
            public void run() { //NOSONAR
              invalidateColors(lastState); //NOSONAR
            }
          });
    }
  }
}
