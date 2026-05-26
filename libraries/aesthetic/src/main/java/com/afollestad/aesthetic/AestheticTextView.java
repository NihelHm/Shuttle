package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.resolveResId;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticTextView extends AppCompatTextView { //NOSONAR

  private Disposable subscription; //NOSONAR
  private int textColorResId; //NOSONAR

  public AestheticTextView(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticTextView(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  public AestheticTextView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      textColorResId = resolveResId(context, attrs, android.R.attr.textColor); //NOSONAR
    }
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    Observable<Integer> obs = //NOSONAR
        ViewUtil.getObservableForResId( //NOSONAR
            getContext(), textColorResId, Aesthetic.get(getContext()).textColorSecondary()); //NOSONAR
    //noinspection ConstantConditions
    subscription = //NOSONAR
        obs.compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe(ViewTextColorAction.create(this), onErrorLogAndRethrow()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  }
}
