package com.simplecity.amp_library.utils; // NOSONAR

import android.animation.Animator; // NOSONAR
import android.animation.AnimatorListenerAdapter; // NOSONAR
import android.animation.ObjectAnimator; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.view.View; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeAction; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ViewUtils { //NOSONAR

    public static void fadeOut(View view, @Nullable UnsafeAction action) { //NOSONAR

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).setDuration(250); //NOSONAR
        objectAnimator.addListener(new AnimatorListenerAdapter() { //NOSONAR
            @Override //NOSONAR
            public void onAnimationEnd(Animator animation) { //NOSONAR
                view.setVisibility(View.GONE); //NOSONAR
                objectAnimator.removeAllListeners(); //NOSONAR

                if (action != null) { //NOSONAR
                    action.run(); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR

        objectAnimator.start(); //NOSONAR
    } // NOSONAR

    public static void fadeIn(View view, @Nullable UnsafeAction action) { //NOSONAR

        view.setVisibility(View.VISIBLE); //NOSONAR

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).setDuration(250); //NOSONAR
        objectAnimator.addListener(new AnimatorListenerAdapter() { //NOSONAR
            @Override //NOSONAR
            public void onAnimationEnd(Animator animation) { //NOSONAR
                animation.removeAllListeners(); //NOSONAR
                if (action != null) { //NOSONAR
                    action.run(); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR
        objectAnimator.start(); //NOSONAR
    } // NOSONAR
} // NOSONAR
