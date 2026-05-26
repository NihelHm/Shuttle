package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;
import static com.afollestad.aesthetic.Util.adjustAlpha;
import static com.afollestad.aesthetic.Util.resolveResId;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticTextInputLayout extends TextInputLayout { //NOSONAR

  private CompositeDisposable subs; //NOSONAR
  private int backgroundResId; //NOSONAR

  public AestheticTextInputLayout(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticTextInputLayout(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  public AestheticTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
    init(context, attrs); //NOSONAR
  }

  private void init(Context context, AttributeSet attrs) { //NOSONAR
    if (attrs != null) { //NOSONAR
      backgroundResId = resolveResId(context, attrs, android.R.attr.background); //NOSONAR
    }
  }

  private void invalidateColors(int color) { //NOSONAR
    TextInputLayoutUtil.setAccent(this, color); //NOSONAR
  }

  @SuppressWarnings("ConstantConditions") //NOSONAR
  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
    super.onAttachedToWindow(); //NOSONAR
    subs = new CompositeDisposable(); //NOSONAR
    subs.add( //NOSONAR
        Aesthetic.get(getContext()) //NOSONAR
            .textColorSecondary() //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull Integer color) { //NOSONAR
                    TextInputLayoutUtil.setHint( //NOSONAR
                        AestheticTextInputLayout.this, adjustAlpha(color, 0.7f)); //NOSONAR
                  }
                },
                onErrorLogAndRethrow())); //NOSONAR
    subs.add( //NOSONAR
        ViewUtil.getObservableForResId(getContext(), backgroundResId, Aesthetic.get(getContext()).colorAccent()) //NOSONAR
            .compose(Rx.<Integer>distinctToMainThread()) //NOSONAR
            .subscribe( //NOSONAR
                new Consumer<Integer>() { //NOSONAR
                  @Override //NOSONAR
                  public void accept(@NonNull Integer color) { //NOSONAR
                    invalidateColors(color); //NOSONAR
                  }
                },
                onErrorLogAndRethrow())); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subs.clear(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  }
}
