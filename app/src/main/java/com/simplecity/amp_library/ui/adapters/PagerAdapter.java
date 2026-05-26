package com.simplecity.amp_library.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.annimon.stream.IntStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PagerAdapter extends FragmentPagerAdapter { //NOSONAR

    private static final String ARG_PAGE_TITLE = "title"; //NOSONAR

    private FragmentManager fragmentManager; //NOSONAR

    private SparseArray<Fragment> fragmentMap = new SparseArray<>(); //NOSONAR

    public PagerAdapter(FragmentManager fragmentManager) { //NOSONAR
        super(fragmentManager); //NOSONAR
        this.fragmentManager = fragmentManager; //NOSONAR
    }

    @Override //NOSONAR
    public Fragment getItem(int position) { //NOSONAR
        return fragmentMap.get(position); //NOSONAR
    }

    @Override //NOSONAR
    public Object instantiateItem(@NonNull ViewGroup container, int position) { //NOSONAR
        Fragment fragment = (Fragment) super.instantiateItem(container, position); //NOSONAR
        fragmentMap.put(position, fragment); //NOSONAR
        return fragment; //NOSONAR
    }

    @Override //NOSONAR
    public int getCount() { //NOSONAR
        return fragmentMap.size(); //NOSONAR
    }

    @Override //NOSONAR
    public CharSequence getPageTitle(int position) { //NOSONAR
        return getItem(position).getArguments().getString(ARG_PAGE_TITLE); //NOSONAR
    }

    public void addFragment(Fragment fragment) { //NOSONAR
        fragmentMap.put(fragmentMap.size(), fragment); //NOSONAR
        notifyDataSetChanged(); //NOSONAR
    }

    public void clear() { //NOSONAR
        fragmentMap.clear(); //NOSONAR

        notifyDataSetChanged(); //NOSONAR
    }

    public void removeAllChildFragments() { //NOSONAR
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //NOSONAR
        IntStream.range(0, fragmentMap.size()).forEach(value -> fragmentTransaction.remove(fragmentMap.get(value))); //NOSONAR
        fragmentTransaction.commitAllowingStateLoss(); //NOSONAR

        clear(); //NOSONAR
    }
}
