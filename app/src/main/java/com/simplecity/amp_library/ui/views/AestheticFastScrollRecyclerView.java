package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.Util;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import io.reactivex.disposables.Disposable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class AestheticFastScrollRecyclerView extends FastScrollRecyclerView {

    Disposable aestheticDisposable;

    public AestheticFastScrollRecyclerView(Context context) {
        super(context);
    }

    public AestheticFastScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AestheticFastScrollRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        aestheticDisposable = Aesthetic.get(getContext()).colorAccent().subscribe(color -> {
            setThumbColor(color);
            setThumbInactiveColor(color);
            setPopupBgColor(color);
            setPopupTextColor(Util.isColorLight(color) ? Color.BLACK : Color.WHITE);
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        aestheticDisposable.dispose();
        super.onDetachedFromWindow();
    }
}
