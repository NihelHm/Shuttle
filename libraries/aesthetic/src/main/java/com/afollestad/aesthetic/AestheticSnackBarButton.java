package com.afollestad.aesthetic; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.v7.widget.AppCompatButton; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class AestheticSnackBarButton extends AppCompatButton { //NOSONAR

  private Disposable subscription; //NOSONAR

  public AestheticSnackBarButton(Context context) { //NOSONAR
    super(context); //NOSONAR
  } // NOSONAR

  public AestheticSnackBarButton(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  } // NOSONAR

  public AestheticSnackBarButton(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    subscription = //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .snackbarActionTextColor() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe(ViewTextColorAction.create(this)); //NOSONAR
  } // NOSONAR

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  } // NOSONAR
} // NOSONAR
