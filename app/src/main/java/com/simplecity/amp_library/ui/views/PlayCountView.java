package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.support.v7.widget.AppCompatTextView; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.Util; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlayCountView extends AppCompatTextView { //NOSONAR

    private Disposable disposable; //NOSONAR

    private Drawable backgroundDrawable; //NOSONAR

    public PlayCountView(Context context) { //NOSONAR
        super(context); //NOSONAR

        init(); //NOSONAR
    } // NOSONAR

    public PlayCountView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR

        init(); //NOSONAR
    } // NOSONAR

    private void init() { //NOSONAR
        backgroundDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.bg_rounded)); //NOSONAR
    } // NOSONAR

    public void setCount(int count) { //NOSONAR
        setText(String.valueOf(count)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        disposable = Aesthetic.get(getContext()) //NOSONAR
                .colorPrimary() //NOSONAR
                .subscribe(colorPrimary -> { //NOSONAR
                    DrawableCompat.setTint(backgroundDrawable, colorPrimary); //NOSONAR
                    setBackground(backgroundDrawable); //NOSONAR
                    if (Util.isColorLight(colorPrimary)) { //NOSONAR
                        setTextColor(Color.BLACK); //NOSONAR
                    } else { //NOSONAR
                        setTextColor(Color.WHITE); //NOSONAR
                    } // NOSONAR
                }); // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR

        disposable.dispose(); //NOSONAR

        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR
} // NOSONAR
