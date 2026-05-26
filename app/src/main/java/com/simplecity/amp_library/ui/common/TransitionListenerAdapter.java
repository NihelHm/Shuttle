package com.simplecity.amp_library.ui.common;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;

@TargetApi(Build.VERSION_CODES.KITKAT)
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public abstract class TransitionListenerAdapter implements Transition.TransitionListener {

    @Override
    public void onTransitionStart(Transition transition) {
        // Intentionally left empty.
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        // Intentionally left empty.
    }

    @Override
    public void onTransitionCancel(Transition transition) {
        // Intentionally left empty.
    }

    @Override
    public void onTransitionPause(Transition transition) {
        // Intentionally left empty.
    }

    @Override
    public void onTransitionResume(Transition transition) {
        // Intentionally left empty.
    }
}
