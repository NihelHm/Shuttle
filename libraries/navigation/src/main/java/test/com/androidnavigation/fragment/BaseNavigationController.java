package test.com.androidnavigation.fragment; // NOSONAR

import android.os.Bundle; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import android.support.v4.app.FragmentManager; // NOSONAR
import android.support.v4.app.FragmentTransaction; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR

import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

import test.com.androidnavigation.R; // NOSONAR
import test.com.androidnavigation.base.NavigationController; // NOSONAR

/** // NOSONAR
 * An abstract implementation of {@link NavigationController}. Subclasses need only provide a {@link FragmentInfo} object // NOSONAR
 * which will be used to instantiate the root view controller. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseNavigationController extends BaseController //NOSONAR
        implements NavigationController<Fragment> { //NOSONAR

    private static final String TAG = "BaseNavigationControlle"; //NOSONAR

    public abstract FragmentInfo getRootViewControllerInfo(); //NOSONAR

    private List<BackPressListener> backPressListeners = new ArrayList<>(); //NOSONAR

    @Override //NOSONAR
    public void onCreate(@Nullable Bundle savedInstanceState) { //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //NOSONAR
        return inflater.inflate(R.layout.navigation_fragment, container, false); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) { //NOSONAR
        super.onViewCreated(view, savedInstanceState); //NOSONAR

        if (savedInstanceState == null) { //NOSONAR
            addRootFragment(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    protected void addRootFragment() { //NOSONAR
        getChildFragmentManager() //NOSONAR
                .beginTransaction() //NOSONAR
                .add(R.id.mainContainer, getRootViewControllerInfo().instantiateFragment(getContext()), getRootViewControllerInfo().rootViewControllerTag) //NOSONAR
                .commit(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (getActivity() instanceof BackPressHandler) { //NOSONAR
            ((BackPressHandler) getActivity()).addBackPressListener(this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        super.onPause(); //NOSONAR

        if (getActivity() instanceof BackPressHandler) { //NOSONAR
            ((BackPressHandler) getActivity()).removeBackPressListener(this); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean consumeBackPress() { //NOSONAR

        for (int i = backPressListeners.size() - 1; i >= 0; i--) { //NOSONAR
            if (backPressListeners.get(i).consumeBackPress()) { //NOSONAR
                return true; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (getChildFragmentManager().getBackStackEntryCount() > 0) { //NOSONAR
            popViewController(); //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        return false; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void pushViewController(@NonNull Fragment fragment, @Nullable String tag, @Nullable List<Pair<View, String>> sharedElements) { //NOSONAR
        FragmentTransaction fragmentTransaction = getChildFragmentManager() //NOSONAR
                .beginTransaction(); //NOSONAR

        if (sharedElements != null) { //NOSONAR
            for (Pair<View, String> pair : sharedElements) { //NOSONAR
                fragmentTransaction.addSharedElement(pair.first, pair.second); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        fragmentTransaction.addToBackStack(null) //NOSONAR
                .replace(R.id.mainContainer, fragment, tag) //NOSONAR
                .commit(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void pushViewController(@NonNull Fragment controller, @Nullable String tag) { //NOSONAR
        pushViewController(controller, tag, null); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void popViewController() { //NOSONAR
        getChildFragmentManager().popBackStack(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void popToRootViewController() { //NOSONAR
        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void addBackPressListener(@NonNull BackPressListener listener) { //NOSONAR
        if (!backPressListeners.contains(listener)) { //NOSONAR
            backPressListeners.add(listener); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void removeBackPressListener(@NonNull BackPressListener listener) { //NOSONAR
        if (backPressListeners.contains(listener)) { //NOSONAR
            backPressListeners.remove(listener); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
