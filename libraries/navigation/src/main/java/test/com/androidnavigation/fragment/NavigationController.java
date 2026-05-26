package test.com.androidnavigation.fragment;

import android.os.Bundle;

/**
 * A concrete implementation of {@link BaseNavigationController} which creates the root view
 * controller based on the params provided to {@link #newInstance(FragmentInfo)}.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class NavigationController extends BaseNavigationController {

    private static final String ARG_FRAGMENT_INFO = "fragment_info";

    /**
     * @param fragmentInfo the {@link FragmentInfo} of the root view controller to be added to this {@link BaseNavigationController}
     */
    public static NavigationController newInstance(FragmentInfo fragmentInfo) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_FRAGMENT_INFO, fragmentInfo);
        NavigationController fragment = new NavigationController();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public FragmentInfo getRootViewControllerInfo() {
        return ((FragmentInfo) getArguments().getParcelable(ARG_FRAGMENT_INFO));
    }
}
