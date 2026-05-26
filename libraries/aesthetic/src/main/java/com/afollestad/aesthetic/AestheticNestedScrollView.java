package com.afollestad.aesthetic; // NOSONAR

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.v4.widget.NestedScrollView; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import io.reactivex.annotations.NonNull; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import io.reactivex.functions.Consumer; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticNestedScrollView extends NestedScrollView { //NOSONAR

  private Disposable subscription; //NOSONAR

  public AestheticNestedScrollView(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticNestedScrollView(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  } // NOSONAR

  private void invalidateColors(int color) { //NOSONAR
    EdgeGlowUtil.setEdgeGlowColor(this, color); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  public void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    subscription = //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .colorAccent() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull Integer color) { //NOSONAR
                    invalidateColors(color); //NOSONAR
                  } // NOSONAR
                }, // NOSONAR
                onErrorLogAndRethrow()); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR
} // NOSONAR
