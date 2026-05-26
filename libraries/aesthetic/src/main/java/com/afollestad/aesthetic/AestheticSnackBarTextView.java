package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import io.reactivex.disposables.Disposable;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class AestheticSnackBarTextView extends AppCompatTextView { //NOSONAR

  private Disposable subscription; //NOSONAR

  public AestheticSnackBarTextView(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticSnackBarTextView(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  }

  public AestheticSnackBarTextView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    subscription = //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .snackbarTextColor() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe(ViewTextColorAction.create(this), onErrorLogAndRethrow()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  }
}
