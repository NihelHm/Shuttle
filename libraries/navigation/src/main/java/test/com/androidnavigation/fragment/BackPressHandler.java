package test.com.androidnavigation.fragment;

import android.support.annotation.NonNull;

import test.com.androidnavigation.base.NavigationController;

/**
 * Classes which are capable of handling the Android back-button press (such as {@link android.app.Activity})
 * should implement this method to allow back-presses to be propagated to the NavigationController, which then
 * gets an opportunity to consume the back press event via {@link NavigationController#consumeBackPress()}
 *
 * @see {@link BaseNavigationController#onResume()}
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface BackPressHandler {

    void addBackPressListener(@NonNull BackPressListener listener);

    void removeBackPressListener(@NonNull BackPressListener listener);
}
