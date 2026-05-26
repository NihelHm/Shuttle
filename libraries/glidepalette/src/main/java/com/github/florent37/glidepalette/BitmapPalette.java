package com.github.florent37.glidepalette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BitmapPalette { //NOSONAR

    private static final String TAG = "BitmapPalette"; //NOSONAR

    public interface CallBack { //NOSONAR
        void onPaletteLoaded(@Nullable Palette palette); //NOSONAR
    }

    public interface PaletteBuilderInterceptor { //NOSONAR
        @NonNull //NOSONAR
        Palette.Builder intercept(Palette.Builder builder); //NOSONAR
    }

    @IntDef({Profile.VIBRANT, Profile.VIBRANT_DARK, Profile.VIBRANT_LIGHT, //NOSONAR
            Profile.MUTED, Profile.MUTED_DARK, Profile.MUTED_LIGHT}) //NOSONAR
    @Retention(RetentionPolicy.SOURCE) //NOSONAR
    public @interface Profile { //NOSONAR
        int VIBRANT = 0; //NOSONAR
        int VIBRANT_DARK = 1; //NOSONAR
        int VIBRANT_LIGHT = 2; //NOSONAR
        int MUTED = 3; //NOSONAR
        int MUTED_DARK = 4; //NOSONAR
        int MUTED_LIGHT = 5; //NOSONAR
    }

    @IntDef({Swatch.RGB, Swatch.TITLE_TEXT_COLOR, Swatch.BODY_TEXT_COLOR}) //NOSONAR
    @Retention(RetentionPolicy.SOURCE) //NOSONAR
    public @interface Swatch { //NOSONAR
        int RGB = 0; //NOSONAR
        int TITLE_TEXT_COLOR = 1; //NOSONAR
        int BODY_TEXT_COLOR = 2; //NOSONAR
    }

    static final LruCache<String, Palette> CACHE = new LruCache<>(40); //NOSONAR

    protected String url; //NOSONAR

    protected LinkedList<PaletteTarget> targets = new LinkedList<>(); //NOSONAR
    protected ArrayList<BitmapPalette.CallBack> callbacks = new ArrayList<>(); //NOSONAR
    private PaletteBuilderInterceptor interceptor; //NOSONAR
    private boolean skipCache; //NOSONAR

    public BitmapPalette use(@Profile int paletteProfile) { //NOSONAR
        this.targets.add(new PaletteTarget(paletteProfile)); //NOSONAR
        return this; //NOSONAR
    }

    protected BitmapPalette intoBackground(View view, @Swatch int paletteSwatch) { //NOSONAR
        assertTargetsIsNotEmpty(); //NOSONAR

        this.targets.getLast().targetsBackground.add(new Pair<>(view, paletteSwatch)); //NOSONAR
        return this; //NOSONAR
    }

    protected BitmapPalette intoTextColor(TextView textView, @Swatch int paletteSwatch) { //NOSONAR
        assertTargetsIsNotEmpty(); //NOSONAR

        this.targets.getLast().targetsText.add(new Pair<>(textView, paletteSwatch)); //NOSONAR
        return this; //NOSONAR
    }

    protected BitmapPalette crossfade(boolean crossfade) { //NOSONAR
        assertTargetsIsNotEmpty(); //NOSONAR

        this.targets.getLast().targetCrossfade = crossfade; //NOSONAR
        return this; //NOSONAR
    }

    protected BitmapPalette crossfade(boolean crossfade, int crossfadeSpeed) { //NOSONAR
        assertTargetsIsNotEmpty(); //NOSONAR

        this.targets.getLast().targetCrossfadeSpeed = crossfadeSpeed; //NOSONAR
        return this.crossfade(crossfade); //NOSONAR
    }

    private void assertTargetsIsNotEmpty() { //NOSONAR
        if (this.targets.isEmpty()) { //NOSONAR
            throw new UnsupportedOperationException("You must specify a palette with use(Profile.Profile)"); //NOSONAR
        }
    }

    protected BitmapPalette intoCallBack(BitmapPalette.CallBack callBack) { //NOSONAR
        if (callBack != null) //NOSONAR
            callbacks.add(callBack); //NOSONAR
        return this; //NOSONAR
    }

    protected BitmapPalette skipPaletteCache(boolean skipCache) { //NOSONAR
        this.skipCache = skipCache; //NOSONAR
        return this; //NOSONAR
    }

    protected BitmapPalette setPaletteBuilderInterceptor(PaletteBuilderInterceptor interceptor) { //NOSONAR
        this.interceptor = interceptor; //NOSONAR
        return this; //NOSONAR
    }

    /*
     * Apply the Palette Profile & Swatch to our current targets
     *
     * palette  the palette to apply
     * cacheHit true if the palette was retrieved from the cache, else false
     */
    protected void apply(Palette palette, boolean cacheHit) { //NOSONAR

        for (CallBack c : callbacks) { //NOSONAR
            c.onPaletteLoaded(palette); //NOSONAR
        }

        if (palette == null) return; //NOSONAR

        for (PaletteTarget target : targets) { //NOSONAR
            Palette.Swatch swatch = null; //NOSONAR
            switch (target.paletteProfile) { //NOSONAR
                case Profile.VIBRANT: //NOSONAR
                    swatch = palette.getVibrantSwatch(); //NOSONAR
                    break; //NOSONAR
                case Profile.VIBRANT_DARK: //NOSONAR
                    swatch = palette.getDarkVibrantSwatch(); //NOSONAR
                    break; //NOSONAR
                case Profile.VIBRANT_LIGHT: //NOSONAR
                    swatch = palette.getLightVibrantSwatch(); //NOSONAR
                    break; //NOSONAR
                case Profile.MUTED: //NOSONAR
                    swatch = palette.getMutedSwatch(); //NOSONAR
                    break; //NOSONAR
                case Profile.MUTED_DARK: //NOSONAR
                    swatch = palette.getDarkMutedSwatch(); //NOSONAR
                    break; //NOSONAR
                case Profile.MUTED_LIGHT: //NOSONAR
                    swatch = palette.getLightMutedSwatch(); //NOSONAR
                    break; //NOSONAR
            }

            if (swatch == null) { //NOSONAR
                swatch = new Palette.Swatch(Color.BLACK, 1); //NOSONAR
            }

            for (Pair<View, Integer> t : target.targetsBackground) { //NOSONAR
                int color = getColor(swatch, t.second); //NOSONAR
                //Only crossfade if we're not coming from a cache hit.
                if (!cacheHit && target.targetCrossfade) { //NOSONAR
                    crossfadeTargetBackground(target, t, color); //NOSONAR
                } else { //NOSONAR
                    t.first.setBackgroundColor(color); //NOSONAR
                }
            }

            for (Pair<TextView, Integer> t : target.targetsText) { //NOSONAR
                int color = getColor(swatch, t.second); //NOSONAR
                t.first.setTextColor(color); //NOSONAR
            }

            target.clear(); //NOSONAR
            this.callbacks = null; //NOSONAR
        }
    }

    private void crossfadeTargetBackground(PaletteTarget target, Pair<View, Integer> t, int newColor) { //NOSONAR

        final Drawable oldColor = t.first.getBackground(); //NOSONAR
        final Drawable[] drawables = new Drawable[2]; //NOSONAR

        drawables[0] = oldColor != null ? oldColor : new ColorDrawable(t.first.getSolidColor()); //NOSONAR
        drawables[1] = new ColorDrawable(newColor); //NOSONAR
        TransitionDrawable transitionDrawable = new TransitionDrawable(drawables); //NOSONAR

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //NOSONAR
            t.first.setBackground(transitionDrawable); //NOSONAR
        } else { //NOSONAR
            //noinspection deprecation
            t.first.setBackgroundDrawable(transitionDrawable); //NOSONAR
        }
        transitionDrawable.startTransition(target.targetCrossfadeSpeed); //NOSONAR
    }

    protected static int getColor(Palette.Swatch swatch, @Swatch int paletteSwatch) { //NOSONAR
        if (swatch != null) { //NOSONAR
            switch (paletteSwatch) { //NOSONAR
                case Swatch.RGB: //NOSONAR
                    return swatch.getRgb(); //NOSONAR
                case Swatch.TITLE_TEXT_COLOR: //NOSONAR
                    return swatch.getTitleTextColor(); //NOSONAR
                case Swatch.BODY_TEXT_COLOR: //NOSONAR
                    return swatch.getBodyTextColor(); //NOSONAR
            }
        } else { //NOSONAR
            Log.e(TAG, "error while generating Palette, null palette returned"); //NOSONAR
        }
        return 0; //NOSONAR
    }

    protected void start(@NonNull final Bitmap bitmap) { //NOSONAR
        final boolean skipCache = this.skipCache; //NOSONAR
        if (!skipCache) { //NOSONAR
            Palette palette = CACHE.get(url); //NOSONAR
            if (palette != null) { //NOSONAR
                apply(palette, true); //NOSONAR
                return; //NOSONAR
            }
        }
        Palette.Builder builder = new Palette.Builder(bitmap); //NOSONAR
        if (interceptor != null) { //NOSONAR
            builder = interceptor.intercept(builder); //NOSONAR
        }
        builder.generate(new Palette.PaletteAsyncListener() { //NOSONAR
            @Override //NOSONAR
            public void onGenerated(Palette palette) { //NOSONAR
                if (!skipCache) { //NOSONAR
                    CACHE.put(url, palette); //NOSONAR
                }
                apply(palette, false); //NOSONAR
            }
        });
    }
}
