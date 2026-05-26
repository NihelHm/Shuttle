package com.simplecity.amp_library.glide.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AlwaysCrossFade extends DrawableCrossFadeFactory<GlideDrawable> { //NOSONAR
    private final boolean transparentImagesPossible; //NOSONAR

    /**
     * @param transparentImagesPossible used to signal that are no transparent images possible with this load.
     * When cross-fading between opaque images a better-looking cross-fade is possible
     * via {@link TransitionDrawable#setCrossFadeEnabled(boolean)}.
     * @see <a href="https://github.com/bumptech/glide/issues/943">#943</a>
     */
    public AlwaysCrossFade(boolean transparentImagesPossible) { //NOSONAR
        super(600); //NOSONAR
        this.transparentImagesPossible = transparentImagesPossible; //NOSONAR
    }

    @Override //NOSONAR
    public GlideAnimation<GlideDrawable> build(boolean isFromMemoryCache, boolean isFirstResource) { //NOSONAR
        // passing isFirstResource instead of isFromMemoryCache achieves the result we want
        GlideAnimation<GlideDrawable> animation = super.build(isFirstResource, isFirstResource); //NOSONAR
        if (!transparentImagesPossible) { //NOSONAR
            animation = new RealCrossFadeAnimation(animation); //NOSONAR
        }
        return animation; //NOSONAR
    }

    private static class RealCrossFadeAnimation implements GlideAnimation<GlideDrawable> { //NOSONAR
        private final GlideAnimation<GlideDrawable> animation; //NOSONAR

        public RealCrossFadeAnimation(GlideAnimation<GlideDrawable> animation) { //NOSONAR
            this.animation = animation; //NOSONAR
        }

        @Override //NOSONAR
        public boolean animate(GlideDrawable current, final ViewAdapter adapter) { //NOSONAR
            return animation.animate(current, new CrossFadeDisablingViewAdapter(adapter)); //NOSONAR
        }
    }

    private static class CrossFadeDisablingViewAdapter extends WrappingViewAdapter { //NOSONAR
        public CrossFadeDisablingViewAdapter(ViewAdapter adapter) { //NOSONAR
            super(adapter); //NOSONAR
        }

        @Override //NOSONAR
        public void setDrawable(Drawable drawable) { //NOSONAR
            if (drawable instanceof TransitionDrawable) { //NOSONAR
                ((TransitionDrawable) drawable).setCrossFadeEnabled(false); //NOSONAR
            }
            super.setDrawable(drawable); //NOSONAR
        }
    }
}
