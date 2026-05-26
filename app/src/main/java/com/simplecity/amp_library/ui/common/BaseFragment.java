package com.simplecity.amp_library.ui.common; // NOSONAR

import android.content.res.Resources; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import android.view.View; // NOSONAR
import android.view.animation.AlphaAnimation; // NOSONAR
import android.view.animation.Animation; // NOSONAR
import android.view.animation.AnimationUtils; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay; // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetEventRelay; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.squareup.leakcanary.RefWatcher; // NOSONAR
import dagger.android.support.AndroidSupportInjection; // NOSONAR
import java.lang.reflect.Field; // NOSONAR
import javax.inject.Inject; // NOSONAR
import test.com.androidnavigation.fragment.BaseController; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseFragment extends BaseController { //NOSONAR

    private static final String TAG = "BaseFragment"; //NOSONAR

    // Arbitrary value; set it to some reasonable default // NOSONAR
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
    } // NOSONAR

    @Override //NOSONAR
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //NOSONAR
        super.onViewCreated(view, savedInstanceState); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        analyticsManager.logScreenName(getActivity(), screenName()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) { //NOSONAR

        /* // NOSONAR
         * When a fragment transaction is performed on a parent fragment containing nested children, // NOSONAR
         * the children disappear as soon as the transaction begins.. So if you're relying on some // NOSONAR
         * fancy animation for the parent, we need to ensure the child waits before performing its // NOSONAR
         * own animation. // NOSONAR
         * see https://code.google.com/p/android/issues/detail?id=55228 // NOSONAR
         */ // NOSONAR
        final Fragment parent = getParentFragment(); //NOSONAR

        // Apply the workaround only if this is a child fragment, and the parent // NOSONAR
        // is being removed. // NOSONAR
        if (!enter && parent != null && parent.isRemoving()) { //NOSONAR
            // This is a workaround for the bug where child fragments disappear when // NOSONAR
            // the parent is removed (as all children are first removed from the parent) // NOSONAR
            // See https://code.google.com/p/android/issues/detail?id=55228 // NOSONAR
            Animation doNothingAnim = new AlphaAnimation(1, 1); //NOSONAR
            doNothingAnim.setDuration(getNextAnimationDuration(parent, DEFAULT_CHILD_ANIMATION_DURATION)); //NOSONAR
            return doNothingAnim; //NOSONAR
        } else { //NOSONAR
            return super.onCreateAnimation(transit, enter, nextAnim); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Attempt to get the resource ID of the next animation that // NOSONAR
     * will be applied to the given fragment. // NOSONAR
     * // NOSONAR
     * @param fragment the parent fragment // NOSONAR
     * @param defValue default animation value // NOSONAR
     * @return the duration of the parent fragment's animation // NOSONAR
     */ // NOSONAR
    private static long getNextAnimationDuration(Fragment fragment, long defValue) { //NOSONAR
        try { //NOSONAR
            // Attempt to get the resource ID of the next animation that // NOSONAR
            // will be applied to the given fragment. // NOSONAR
            Field nextAnimField = Fragment.class.getDeclaredField("mNextAnim"); //NOSONAR
            nextAnimField.setAccessible(true); //NOSONAR
            int nextAnimResource = nextAnimField.getInt(fragment); //NOSONAR
            Animation nextAnim = AnimationUtils.loadAnimation(fragment.getActivity(), nextAnimResource); //NOSONAR

            // ...and if it can be loaded, return that animation's duration // NOSONAR
            return (nextAnim == null) ? defValue : nextAnim.getDuration(); //NOSONAR
        } catch (NoSuchFieldException | IllegalAccessException | Resources.NotFoundException ignored) { //NOSONAR
            return defValue; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR

        super.onDestroy(); //NOSONAR
        RefWatcher refWatcher = ((ShuttleApplication) getContext().getApplicationContext()).getRefWatcher(); //NOSONAR
        refWatcher.watch(this); //NOSONAR
    } // NOSONAR

    public MediaManager getMediaManager() { //NOSONAR
        return mediaManager; //NOSONAR
    } // NOSONAR

    public NavigationEventRelay getNavigationEventRelay() { //NOSONAR
        return navigationEventRelay; //NOSONAR
    } // NOSONAR

    protected abstract String screenName(); //NOSONAR
} // NOSONAR
