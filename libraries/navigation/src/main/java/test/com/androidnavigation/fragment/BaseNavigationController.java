package test.com.androidnavigation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import test.com.androidnavigation.R;
import test.com.androidnavigation.base.NavigationController;

/**
 * An abstract implementation of {@link NavigationController}. Subclasses need only provide a {@link FragmentInfo} object
 * which will be used to instantiate the root view controller.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseNavigationController extends BaseController //NOSONAR
        implements NavigationController<Fragment> { //NOSONAR

    private static final String TAG = "BaseNavigationControlle"; //NOSONAR

    public abstract FragmentInfo getRootViewControllerInfo(); //NOSONAR

    private List<BackPressListener> backPressListeners = new ArrayList<>(); //NOSONAR

    @Override //NOSONAR
    public void onCreate(@Nullable Bundle savedInstanceState) { //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //NOSONAR
        return inflater.inflate(R.layout.navigation_fragment, container, false); //NOSONAR
    }

    @Override //NOSONAR
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) { //NOSONAR
        super.onViewCreated(view, savedInstanceState); //NOSONAR

        if (savedInstanceState == null) { //NOSONAR
            addRootFragment(); //NOSONAR
        }
    }

    protected void addRootFragment() { //NOSONAR
        getChildFragmentManager() //NOSONAR
                .beginTransaction() //NOSONAR
                .add(R.id.mainContainer, getRootViewControllerInfo().instantiateFragment(getContext()), getRootViewControllerInfo().rootViewControllerTag) //NOSONAR
                .commit(); //NOSONAR
    }

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (getActivity() instanceof BackPressHandler) { //NOSONAR
            ((BackPressHandler) getActivity()).addBackPressListener(this); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        super.onPause(); //NOSONAR

        if (getActivity() instanceof BackPressHandler) { //NOSONAR
            ((BackPressHandler) getActivity()).removeBackPressListener(this); //NOSONAR
        }
    }

    @Override //NOSONAR
    public boolean consumeBackPress() { //NOSONAR

        for (int i = backPressListeners.size() - 1; i >= 0; i--) { //NOSONAR
            if (backPressListeners.get(i).consumeBackPress()) { //NOSONAR
                return true; //NOSONAR
            }
        }

        if (getChildFragmentManager().getBackStackEntryCount() > 0) { //NOSONAR
            popViewController(); //NOSONAR
            return true; //NOSONAR
        }

        return false; //NOSONAR
    }

    @Override //NOSONAR
    public void pushViewController(@NonNull Fragment fragment, @Nullable String tag, @Nullable List<Pair<View, String>> sharedElements) { //NOSONAR
        FragmentTransaction fragmentTransaction = getChildFragmentManager() //NOSONAR
                .beginTransaction(); //NOSONAR

        if (sharedElements != null) { //NOSONAR
            for (Pair<View, String> pair : sharedElements) { //NOSONAR
                fragmentTransaction.addSharedElement(pair.first, pair.second); //NOSONAR
            }
        }

        fragmentTransaction.addToBackStack(null) //NOSONAR
                .replace(R.id.mainContainer, fragment, tag) //NOSONAR
                .commit(); //NOSONAR
    }

    @Override //NOSONAR
    public void pushViewController(@NonNull Fragment controller, @Nullable String tag) { //NOSONAR
        pushViewController(controller, tag, null); //NOSONAR
    }

    @Override //NOSONAR
    public void popViewController() { //NOSONAR
        getChildFragmentManager().popBackStack(); //NOSONAR
    }

    @Override //NOSONAR
    public void popToRootViewController() { //NOSONAR
        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //NOSONAR
    }

    @Override //NOSONAR
    public void addBackPressListener(@NonNull BackPressListener listener) { //NOSONAR
        if (!backPressListeners.contains(listener)) { //NOSONAR
            backPressListeners.add(listener); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void removeBackPressListener(@NonNull BackPressListener listener) { //NOSONAR
        if (backPressListeners.contains(listener)) { //NOSONAR
            backPressListeners.remove(listener); //NOSONAR
        }
    }
}
