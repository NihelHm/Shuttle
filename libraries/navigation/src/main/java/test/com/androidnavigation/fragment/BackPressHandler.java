package test.com.androidnavigation.fragment; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR

import test.com.androidnavigation.base.NavigationController; // NOSONAR

/** // NOSONAR
 * Classes which are capable of handling the Android back-button press (such as {@link android.app.Activity}) // NOSONAR
 * should implement this method to allow back-presses to be propagated to the NavigationController, which then // NOSONAR
 * gets an opportunity to consume the back press event via {@link NavigationController#consumeBackPress()} // NOSONAR
 * // NOSONAR
 * @see {@link BaseNavigationController#onResume()} // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface BackPressHandler { //NOSONAR

    void addBackPressListener(@NonNull BackPressListener listener); //NOSONAR

    void removeBackPressListener(@NonNull BackPressListener listener); //NOSONAR
} // NOSONAR
