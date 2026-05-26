package com.simplecity.amp_library.ui.common;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.playback.MediaManager;
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay;
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetEventRelay;
import com.simplecity.amp_library.utils.AnalyticsManager;
import com.squareup.leakcanary.RefWatcher;
import dagger.android.support.AndroidSupportInjection;
import java.lang.reflect.Field;
import javax.inject.Inject;
import test.com.androidnavigation.fragment.BaseController;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseFragment extends BaseController { //NOSONAR

    private static final String TAG = "BaseFragment"; //NOSONAR

    // Arbitrary value; set it to some reasonable default
    private static final int DEFAULT_CHILD_ANIMATION_DURATION = 250; //NOSONAR

    @Inject //NOSONAR
    MultiSheetEventRelay multiSheetEventRelay; //NOSONAR

    @Inject //NOSONAR
    protected MediaManager mediaManager; //NOSONAR

    @Inject //NOSONAR
    protected NavigationEventRelay navigationEventRelay; //NOSONAR

    @Inject //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public AnalyticsManager analyticsManager; //NOSONAR

    @Override //NOSONAR
    public void onCreate(@Nullable Bundle savedInstanceState) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR
    }

    @Override //NOSONAR
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //NOSONAR
        super.onViewCreated(view, savedInstanceState); //NOSONAR
    }

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        analyticsManager.logScreenName(getActivity(), screenName()); //NOSONAR
    }

    @Override //NOSONAR
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) { //NOSONAR

        /*
         * When a fragment transaction is performed on a parent fragment containing nested children,
         * the children disappear as soon as the transaction begins.. So if you're relying on some
         * fancy animation for the parent, we need to ensure the child waits before performing its
         * own animation.
         * see https://code.google.com/p/android/issues/detail?id=55228
         */
        final Fragment parent = getParentFragment(); //NOSONAR

        // Apply the workaround only if this is a child fragment, and the parent
        // is being removed.
        if (!enter && parent != null && parent.isRemoving()) { //NOSONAR
            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            // See https://code.google.com/p/android/issues/detail?id=55228
            Animation doNothingAnim = new AlphaAnimation(1, 1); //NOSONAR
            doNothingAnim.setDuration(getNextAnimationDuration(parent, DEFAULT_CHILD_ANIMATION_DURATION)); //NOSONAR
            return doNothingAnim; //NOSONAR
        } else { //NOSONAR
            return super.onCreateAnimation(transit, enter, nextAnim); //NOSONAR
        }
    }

    /**
     * Attempt to get the resource ID of the next animation that
     * will be applied to the given fragment.
     *
     * @param fragment the parent fragment
     * @param defValue default animation value
     * @return the duration of the parent fragment's animation
     */
    private static long getNextAnimationDuration(Fragment fragment, long defValue) { //NOSONAR
        try { //NOSONAR
            // Attempt to get the resource ID of the next animation that
            // will be applied to the given fragment.
            Field nextAnimField = Fragment.class.getDeclaredField("mNextAnim"); //NOSONAR
            nextAnimField.setAccessible(true); //NOSONAR
            int nextAnimResource = nextAnimField.getInt(fragment); //NOSONAR
            Animation nextAnim = AnimationUtils.loadAnimation(fragment.getActivity(), nextAnimResource); //NOSONAR

            // ...and if it can be loaded, return that animation's duration
            return (nextAnim == null) ? defValue : nextAnim.getDuration(); //NOSONAR
        } catch (NoSuchFieldException | IllegalAccessException | Resources.NotFoundException ignored) { //NOSONAR
            return defValue; //NOSONAR
        }
    }

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR

        super.onDestroy(); //NOSONAR
        RefWatcher refWatcher = ((ShuttleApplication) getContext().getApplicationContext()).getRefWatcher(); //NOSONAR
        refWatcher.watch(this); //NOSONAR
    }

    public MediaManager getMediaManager() { //NOSONAR
        return mediaManager; //NOSONAR
    }

    public NavigationEventRelay getNavigationEventRelay() { //NOSONAR
        return navigationEventRelay; //NOSONAR
    }

    protected abstract String screenName(); //NOSONAR
}
