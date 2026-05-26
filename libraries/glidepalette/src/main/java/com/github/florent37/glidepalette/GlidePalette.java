package com.github.florent37.glidepalette;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class GlidePalette<ModelType, TranscodeType> extends BitmapPalette implements RequestListener<ModelType, TranscodeType> { //NOSONAR

    protected RequestListener<ModelType, TranscodeType> callback; //NOSONAR

    protected GlidePalette() { //NOSONAR
        // Intentionally left empty.
    }

    public static GlidePalette with(String url) { //NOSONAR
        GlidePalette glidePalette = new GlidePalette(); //NOSONAR
        glidePalette.url = url; //NOSONAR
        return glidePalette; //NOSONAR
    }

    public GlidePalette use(@Profile int paletteProfile) { //NOSONAR
        super.use(paletteProfile); //NOSONAR
        return this; //NOSONAR
    }

    public GlidePalette<ModelType, TranscodeType> intoBackground(View view) { //NOSONAR
        return this.intoBackground(view, Swatch.RGB); //NOSONAR
    }

    @Override //NOSONAR
    public GlidePalette<ModelType, TranscodeType> intoBackground(View view, @Swatch int paletteSwatch) { //NOSONAR
        super.intoBackground(view, paletteSwatch); //NOSONAR
        return this; //NOSONAR
    }

    public GlidePalette<ModelType, TranscodeType> intoTextColor(TextView textView) { //NOSONAR
        return this.intoTextColor(textView, Swatch.TITLE_TEXT_COLOR); //NOSONAR
    }

    @Override //NOSONAR
    public GlidePalette<ModelType, TranscodeType> intoTextColor(TextView textView, @Swatch int paletteSwatch) { //NOSONAR
        super.intoTextColor(textView, paletteSwatch); //NOSONAR
        return this; //NOSONAR
    }

    @Override //NOSONAR
    public GlidePalette<ModelType, TranscodeType> crossfade(boolean crossfade) { //NOSONAR
        super.crossfade(crossfade); //NOSONAR
        return this; //NOSONAR
    }

    @Override //NOSONAR
    public GlidePalette<ModelType, TranscodeType> crossfade(boolean crossfade, int crossfadeSpeed) { //NOSONAR
        super.crossfade(crossfade, crossfadeSpeed); //NOSONAR
        return this; //NOSONAR
    }

    public GlidePalette<ModelType, TranscodeType> setGlideListener(RequestListener<ModelType, TranscodeType> listener) { //NOSONAR
        this.callback = listener; //NOSONAR
        return this; //NOSONAR
    }

    @Override //NOSONAR
    public GlidePalette<ModelType, TranscodeType> intoCallBack(GlidePalette.CallBack callBack) { //NOSONAR
        super.intoCallBack(callBack); //NOSONAR
        return this; //NOSONAR
    }

    @Override //NOSONAR
    public GlidePalette<ModelType, TranscodeType> setPaletteBuilderInterceptor(PaletteBuilderInterceptor interceptor) { //NOSONAR
        super.setPaletteBuilderInterceptor(interceptor); //NOSONAR
        return this; //NOSONAR
    }

    @Override //NOSONAR
    public GlidePalette<ModelType, TranscodeType> skipPaletteCache(boolean skipCache) { //NOSONAR
        super.skipPaletteCache(skipCache); //NOSONAR
        return this; //NOSONAR
    }

    @Override //NOSONAR
    public boolean onException(Exception e, ModelType model, Target<TranscodeType> target, boolean isFirstResource) { //NOSONAR
        return this.callback != null && this.callback.onException(e, model, target, isFirstResource); //NOSONAR
    }

    @Override //NOSONAR
    public boolean onResourceReady(TranscodeType resource, ModelType model, Target<TranscodeType> target, boolean isFromMemoryCache, boolean isFirstResource) { //NOSONAR
        boolean callbackResult = this.callback != null && this.callback.onResourceReady(resource, model, target, isFromMemoryCache, isFirstResource); //NOSONAR

        Bitmap b = null; //NOSONAR
        if (resource instanceof Bitmap) { //NOSONAR
            b = (Bitmap) resource; //NOSONAR
        } else if (resource instanceof GlideBitmapDrawable) { //NOSONAR
            b = ((GlideBitmapDrawable) resource).getBitmap(); //NOSONAR
        } else if (target instanceof BitmapHolder) { //NOSONAR
            b = ((BitmapHolder) target).getBitmap(); //NOSONAR
        }

        if (b != null) { //NOSONAR
            start(b); //NOSONAR
        }

        return callbackResult; //NOSONAR
    }

    public interface BitmapHolder { //NOSONAR
        @Nullable //NOSONAR
        Bitmap getBitmap(); //NOSONAR
    }

}
