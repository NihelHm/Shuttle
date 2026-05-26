package test.com.androidnavigation.fragment;

import android.os.Bundle;

/**
 * A concrete implementation of {@link BaseNavigationController} which creates the root view
 * controller based on the params provided to {@link #newInstance(FragmentInfo)}.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class NavigationController extends BaseNavigationController { //NOSONAR

    private static final String ARG_FRAGMENT_INFO = "fragment_info"; //NOSONAR

    /**
     * @param fragmentInfo the {@link FragmentInfo} of the root view controller to be added to this {@link BaseNavigationController}
     */
    public static NavigationController newInstance(FragmentInfo fragmentInfo) { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        args.putParcelable(ARG_FRAGMENT_INFO, fragmentInfo); //NOSONAR
        NavigationController fragment = new NavigationController(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    }

    @Override //NOSONAR
    public FragmentInfo getRootViewControllerInfo() { //NOSONAR
        return ((FragmentInfo) getArguments().getParcelable(ARG_FRAGMENT_INFO)); //NOSONAR
    }
}
