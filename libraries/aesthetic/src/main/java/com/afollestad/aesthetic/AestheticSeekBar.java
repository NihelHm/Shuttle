package com.afollestad.aesthetic;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.resolveResId;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticSeekBar extends AppCompatSeekBar { //NOSONAR

  @Nullable //NOSONAR
  private Disposable subscription; //NOSONAR

  private int backgroundResId; //NOSONAR

  public AestheticSeekBar(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticSeekBar(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  public AestheticSeekBar(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      backgroundResId = resolveResId(context, attrs, android.R.attr.background); //NOSONAR
    }
  }

  public void invalidateColors(ColorIsDarkState state) { //NOSONAR
    TintHelper.setTint(this, state.color(), state.isDark()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR

    if(isInEditMode()) { //NOSONAR
      return; //NOSONAR
    }

    //noinspection ConstantConditions
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
                                }
                              },
                              onErrorLogAndRethrow()); //NOSONAR
    }
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    if (subscription!=null) { //NOSONAR
        subscription.dispose(); //NOSONAR
    }
    super.onDetachedFromWindow(); //NOSONAR
  }
}
