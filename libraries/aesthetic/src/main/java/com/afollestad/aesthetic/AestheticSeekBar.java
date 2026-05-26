package com.afollestad.aesthetic; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.v7.widget.AppCompatSeekBar; // NOSONAR
import android.util.AttributeSet; // NOSONAR

import io.reactivex.Observable; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.annotations.Nullable; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR
import static com.afollestad.aesthetic.Util.resolveResId; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticSeekBar extends AppCompatSeekBar { //NOSONAR

  @Nullable //NOSONAR
  private Disposable subscription; //NOSONAR

  private int backgroundResId; //NOSONAR

  public AestheticSeekBar(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticSeekBar(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticSeekBar(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  } // NOSONAR

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      backgroundResId = resolveResId(context, attrs, android.R.attr.background); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  public void invalidateColors(ColorIsDarkState state) { //NOSONAR
    TintHelper.setTint(this, state.color(), state.isDark()); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR

    if(isInEditMode()) { //NOSONAR
      return; //NOSONAR
    } // NOSONAR

    //noinspection ConstantConditions // NOSONAR
    if(!":aesthetic_ignore".equals(getTag())){ //NOSONAR
      subscription = //NOSONAR
              Observable.combineLatest( //NOSONAR
                      ViewUtil.getObservableForResId(getContext(), backgroundResId, Aesthetic.get(getContext()).colorAccent()), //NOSONAR
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
                              onErrorLogAndRethrow()); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    if (subscription!=null) { //NOSONAR
        subscription.dispose(); //NOSONAR
    } // NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR
} // NOSONAR
