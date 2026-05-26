package com.afollestad.aesthetic;

import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticListView extends ListView { //NOSONAR

  private Disposable subscription; //NOSONAR

  public AestheticListView(Context context) { //NOSONAR
    super(context); //NOSONAR
  }

  public AestheticListView(Context context, AttributeSet attrs) { //NOSONAR
    super(context, attrs); //NOSONAR
  }

  public AestheticListView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
    super(context, attrs, defStyleAttr); //NOSONAR
  }

  private void invalidateColors(int color) { //NOSONAR
    EdgeGlowUtil.setEdgeGlowColor(this, color); //NOSONAR
  }

  @Override //NOSONAR
  protected void onAttachedToWindow() { //NOSONAR
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
                  }
                },
                onErrorLogAndRethrow()); //NOSONAR
  }

  @Override //NOSONAR
  protected void onDetachedFromWindow() { //NOSONAR
    subscription.dispose(); //NOSONAR
    super.onDetachedFromWindow(); //NOSONAR
  }
}
