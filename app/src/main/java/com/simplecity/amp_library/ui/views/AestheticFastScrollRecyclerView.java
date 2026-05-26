package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.Util;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticFastScrollRecyclerView extends FastScrollRecyclerView { //NOSONAR

    Disposable aestheticDisposable; //NOSONAR

    public AestheticFastScrollRecyclerView(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public AestheticFastScrollRecyclerView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    public AestheticFastScrollRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR
        aestheticDisposable = Aesthetic.get(getContext()).colorAccent().subscribe(color -> { //NOSONAR
            setThumbColor(color); //NOSONAR
            setThumbInactiveColor(color); //NOSONAR
            setPopupBgColor(color); //NOSONAR
            setPopupTextColor(Util.isColorLight(color) ? Color.BLACK : Color.WHITE); //NOSONAR
        });
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    }
}
