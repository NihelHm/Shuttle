package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR
import static com.afollestad.aesthetic.Util.resolveResId; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v7.widget.CardView; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticCardView extends CardView { //NOSONAR

  private Disposable bgSubscription; //NOSONAR
  private int backgroundResId; //NOSONAR

  public AestheticCardView(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticCardView(Context context, @Nullable AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  } // NOSONAR

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      backgroundResId = resolveResId(context, attrs, R.attr.cardBackgroundColor); //NOSONAR
    } // NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    Observable<Integer> obs = //NOSONAR
        ViewUtil.getObservableForResId( //NOSONAR
            getContext(), backgroundResId, Aesthetic.get(getContext()).colorCardViewBackground()); //NOSONAR
    //noinspection ConstantConditions // NOSONAR
    bgSubscription = //NOSONAR
        obs.compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull Integer bgColor) throws Exception { //NOSONAR
                    setCardBackgroundColor(bgColor); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow()); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    if (bgSubscription != null) { //NOSONAR
      bgSubscription.dispose(); //NOSONAR
    } // NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR
} // NOSONAR
