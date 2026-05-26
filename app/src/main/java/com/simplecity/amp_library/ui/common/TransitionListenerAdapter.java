package com.simplecity.amp_library.ui.common;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;

@TargetApi(Build.VERSION_CODES.KITKAT) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class TransitionListenerAdapter implements Transition.TransitionListener { //NOSONAR

    @Override //NOSONAR
    public void onTransitionStart(Transition transition) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onTransitionEnd(Transition transition) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onTransitionCancel(Transition transition) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onTransitionPause(Transition transition) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onTransitionResume(Transition transition) { //NOSONAR
        // Intentionally left empty.
    }
}
