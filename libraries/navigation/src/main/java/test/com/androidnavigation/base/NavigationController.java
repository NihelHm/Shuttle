package test.com.androidnavigation.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.List;

import test.com.androidnavigation.fragment.BackPressHandler;
import test.com.androidnavigation.fragment.BackPressListener;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface NavigationController<T> extends Controller<T>, BackPressHandler, BackPressListener { //NOSONAR

    /**
     * Handle a back press click event from the {@link BackPressHandler}.
     *
     * @return true if the back press was consumed, else false.
     */
    @Override //NOSONAR
    boolean consumeBackPress(); //NOSONAR

    /**
     * Push a {@link Controller} into this {@link NavigationController}
     *
     * @param controller the {@link Controller} to push
     * @param tag        a {@link String} used to identify this controller
     */
    void pushViewController(@NonNull T controller, @Nullable String tag); //NOSONAR

    /**
     * Push a {@link Controller} into this {@link NavigationController}, with a shared element transition.
     *
     * @param controller  the {@link Controller} to push
     * @param tag         a {@link String} used to identify this controller
     * @param transitions a List of View/String pairs used for shared element transitions
     */
    void pushViewController(@NonNull T controller, @Nullable String tag, @NonNull List<Pair<View, String>> transitions); //NOSONAR

    /**
     * Pop a single child controller off the stack.
     */
    void popViewController(); //NOSONAR

    /**
     * Pop all children off the stack, revealing the root view controller.
     */
    void popToRootViewController(); //NOSONAR
}
