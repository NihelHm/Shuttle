package com.simplecity.amp_library.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.view.View;
import com.simplecity.amp_library.rx.UnsafeAction;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class ViewUtils {

    public static void fadeOut(View view, @Nullable UnsafeAction action) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).setDuration(250);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                objectAnimator.removeAllListeners();

                if (action != null) {
                    action.run();
                }
            }
        });

        objectAnimator.start();
    }

    public static void fadeIn(View view, @Nullable UnsafeAction action) {

        view.setVisibility(View.VISIBLE);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).setDuration(250);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animation.removeAllListeners();
                if (action != null) {
                    action.run();
                }
            }
        });
        objectAnimator.start();
    }
}
