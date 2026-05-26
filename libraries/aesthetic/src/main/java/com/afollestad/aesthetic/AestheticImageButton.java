package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.resolveResId;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticImageButton extends AppCompatImageButton { //NOSONAR

  private Disposable bgSubscription; //NOSONAR
  private int backgroundResId; //NOSONAR

  public AestheticImageButton(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticImageButton(Context context, @Nullable AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  public AestheticImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      backgroundResId = resolveResId(context, attrs, android.R.attr.background); //NOSONAR
    }
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    Observable<Integer> obs = ViewUtil.getObservableForResId(getContext(), backgroundResId, null); //NOSONAR
    if (obs != null) { //NOSONAR
      bgSubscription = //NOSONAR
          obs.compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
              .subscribe(ViewBackgroundAction.create(this), onErrorLogAndRethrow()); //NOSONAR
    }
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    if (bgSubscription != null) { //NOSONAR
      bgSubscription.dispose(); //NOSONAR
    }
    super.onDetachedFromWindow(); //NOSONAR
  }
}
