package com.simplecity.amp_library.glide.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class AlwaysCrossFade extends DrawableCrossFadeFactory<GlideDrawable> {
    private final boolean transparentImagesPossible;

    /**
     * @param transparentImagesPossible used to signal that are no transparent images possible with this load.
     * When cross-fading between opaque images a better-looking cross-fade is possible
     * via {@link TransitionDrawable#setCrossFadeEnabled(boolean)}.
     * @see <a href="https://github.com/bumptech/glide/issues/943">#943</a>
     */
    public AlwaysCrossFade(boolean transparentImagesPossible) {
        super(600);
        this.transparentImagesPossible = transparentImagesPossible;
    }

    @Override
    public GlideAnimation<GlideDrawable> build(boolean isFromMemoryCache, boolean isFirstResource) {
        // passing isFirstResource instead of isFromMemoryCache achieves the result we want
        GlideAnimation<GlideDrawable> animation = super.build(isFirstResource, isFirstResource);
        if (!transparentImagesPossible) {
            animation = new RealCrossFadeAnimation(animation);
        }
        return animation;
    }

    private static class RealCrossFadeAnimation implements GlideAnimation<GlideDrawable> {
        private final GlideAnimation<GlideDrawable> animation;

        public RealCrossFadeAnimation(GlideAnimation<GlideDrawable> animation) {
            this.animation = animation;
        }

        @Override
        public boolean animate(GlideDrawable current, final ViewAdapter adapter) {
            return animation.animate(current, new CrossFadeDisablingViewAdapter(adapter));
        }
    }

    private static class CrossFadeDisablingViewAdapter extends WrappingViewAdapter {
        public CrossFadeDisablingViewAdapter(ViewAdapter adapter) {
            super(adapter);
        }

        @Override
        public void setDrawable(Drawable drawable) {
            if (drawable instanceof TransitionDrawable) {
                ((TransitionDrawable) drawable).setCrossFadeEnabled(false);
            }
            super.setDrawable(drawable);
        }
    }
}
