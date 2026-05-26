package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.TypedArray; // NOSONAR
import android.support.v7.widget.AppCompatEditText; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticEditText extends AppCompatEditText { //NOSONAR

  private CompositeDisposable subscriptions; //NOSONAR
  private int backgroundResId; //NOSONAR
  private int textColorResId; //NOSONAR
  private int textColorHintResId; //NOSONAR

  public AestheticEditText(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticEditText(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticEditText(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  } // NOSONAR

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      int[] attrsArray = //NOSONAR
          new int[] { //NOSONAR
            android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint //NOSONAR
          }; // NOSONAR
      TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray); //NOSONAR
      backgroundResId = ta.getResourceId(0, 0); //NOSONAR
      textColorResId = ta.getResourceId(1, 0); //NOSONAR
      textColorHintResId = ta.getResourceId(2, 0); //NOSONAR
      ta.recycle(); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  private void invalidateColors(ColorIsDarkState state) { //NOSONAR
    TintHelper.setTintAuto(this, state.color(), true, state.isDark()); //NOSONAR
    TintHelper.setCursorTint(this, state.color()); //NOSONAR
  } // NOSONAR

  @SuppressWarnings("ConstantConditions") //NOSONAR
  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    subscriptions = new CompositeDisposable(); //NOSONAR
    subscriptions.add( //NOSONAR
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
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow())); //NOSONAR
    subscriptions.add( //NOSONAR
        ViewUtil.getObservableForResId( //NOSONAR
                getContext(), textColorResId, Aesthetic.get(getContext()).textColorPrimary()) //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe(ViewTextColorAction.create(this), onErrorLogAndRethrow())); //NOSONAR
    subscriptions.add( //NOSONAR
        ViewUtil.getObservableForResId( //NOSONAR
                getContext(), textColorHintResId, Aesthetic.get(getContext()).textColorSecondary()) //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe(ViewHintTextColorAction.create(this), onErrorLogAndRethrow())); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscriptions.clear(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR
} // NOSONAR
