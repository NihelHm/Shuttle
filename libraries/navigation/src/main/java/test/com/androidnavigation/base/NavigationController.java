package test.com.androidnavigation.base; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.view.View; // NOSONAR

import java.util.List; // NOSONAR

import test.com.androidnavigation.fragment.BackPressHandler; // NOSONAR
import test.com.androidnavigation.fragment.BackPressListener; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface NavigationController<T> extends Controller<T>, BackPressHandler, BackPressListener { //NOSONAR

    /** // NOSONAR
     * Handle a back press click event from the {@link BackPressHandler}. // NOSONAR
     * // NOSONAR
     * @return true if the back press was consumed, else false. // NOSONAR
     */ // NOSONAR
    @Override //NOSONAR
    boolean consumeBackPress(); //NOSONAR

    /** // NOSONAR
     * Push a {@link Controller} into this {@link NavigationController} // NOSONAR
     * // NOSONAR
     * @param controller the {@link Controller} to push // NOSONAR
     * @param tag        a {@link String} used to identify this controller // NOSONAR
     */ // NOSONAR
    void pushViewController(@NonNull T controller, @Nullable String tag); //NOSONAR

    /** // NOSONAR
     * Push a {@link Controller} into this {@link NavigationController}, with a shared element transition. // NOSONAR
     * // NOSONAR
     * @param controller  the {@link Controller} to push // NOSONAR
     * @param tag         a {@link String} used to identify this controller // NOSONAR
     * @param transitions a List of View/String pairs used for shared element transitions // NOSONAR
     */ // NOSONAR
    void pushViewController(@NonNull T controller, @Nullable String tag, @NonNull List<Pair<View, String>> transitions); //NOSONAR

    /** // NOSONAR
     * Pop a single child controller off the stack. // NOSONAR
     */ // NOSONAR
    void popViewController(); //NOSONAR

    /** // NOSONAR
     * Pop all children off the stack, revealing the root view controller. // NOSONAR
     */ // NOSONAR
    void popToRootViewController(); //NOSONAR
} // NOSONAR
