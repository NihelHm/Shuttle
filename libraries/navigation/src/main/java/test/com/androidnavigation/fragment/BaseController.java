package test.com.androidnavigation.fragment; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR

import test.com.androidnavigation.base.Controller; // NOSONAR
import test.com.androidnavigation.base.NavigationController; // NOSONAR

/** // NOSONAR
 * An abstract implementation of {@link Controller}, which can be used as a base for {@link Fragment}s // NOSONAR
 * who wish to be aware of their parent {@link NavigationController} // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseController extends Fragment implements Controller<Fragment> { //NOSONAR

    @NonNull //NOSONAR
    @Override //NOSONAR
    public NavigationController<Fragment> getNavigationController() { //NOSONAR
        return findNavigationController(this); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Traverses the fragment hierarchy searching for the first available {@link NavigationController}. // NOSONAR
     * If none are found, then this method checks whether the parent {@link android.app.Activity} is // NOSONAR
     * a {@link NavigationController} and returns that instead. // NOSONAR
     * // NOSONAR
     * @param fragment the fragment whose hierarchy will be searched. // NOSONAR
     */ // NOSONAR
    @NonNull //NOSONAR
    public static NavigationController<Fragment> findNavigationController(@NonNull Fragment fragment) { //NOSONAR

        Fragment parent = fragment.getParentFragment(); //NOSONAR

        if (parent instanceof NavigationController) { //NOSONAR
            return (NavigationController) parent; //NOSONAR
        } // NOSONAR

        if (parent != null) { //NOSONAR
            return findNavigationController(parent); //NOSONAR
        } else { //NOSONAR
            if (fragment.getActivity() instanceof NavigationController) { //NOSONAR
                return (NavigationController) fragment.getActivity(); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        throw new IllegalStateException("Couldn't find parent navigation controller."); //NOSONAR
    } // NOSONAR
} // NOSONAR
