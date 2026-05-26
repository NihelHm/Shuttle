package com.simplecity.amp_library.ui.common;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;

@TargetApi(Build.VERSION_CODES.KITKAT)
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
