package com.simplecity.amp_library.ui.screens.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityExtraLarge;
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityLarge;
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityMedium;
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivitySmall;
import com.simplecity.amp_library.ui.common.BaseFragment;
import dagger.android.support.AndroidSupportInjection;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class WidgetFragment extends BaseFragment { //NOSONAR

    private static final String TAG = "WidgetFragment"; //NOSONAR

    private static final String ARG_WIDGET_LAYOUT_ID = "widget_layout_id"; //NOSONAR

    int mWidgetLayoutResId; //NOSONAR

    /**
     * Empty constructor as per the fragment docs
     */
    public WidgetFragment() { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onAttachFragment(Fragment childFragment) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onAttachFragment(childFragment); //NOSONAR
    }

    /**
     * Creates a new instance of the {@link WidgetFragment}
     *
     * @param widgetLayoutResId the id of the layout to use for the widget
     * @return a new instance of {@link WidgetFragment}
     */
    public static WidgetFragment newInstance(int widgetLayoutResId) { //NOSONAR
        WidgetFragment fragment = new WidgetFragment(); //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        args.putInt(ARG_WIDGET_LAYOUT_ID, widgetLayoutResId); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    }

    @Override //NOSONAR
    public void onAttach(Context context) { //NOSONAR
        super.onAttach(context); //NOSONAR

        mWidgetLayoutResId = getArguments().getInt(ARG_WIDGET_LAYOUT_ID); //NOSONAR
    }

    @Override //NOSONAR
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //NOSONAR
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_widget, container, false); //NOSONAR

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(mWidgetLayoutResId, null); //NOSONAR
        if (mWidgetLayoutResId == R.layout.widget_layout_extra_large) { //NOSONAR
            frameLayout.setScaleX(.75f); //NOSONAR
            frameLayout.setScaleY(.75f); //NOSONAR
        }

        float height; //NOSONAR
        float width; //NOSONAR
        FrameLayout.LayoutParams layoutParams = null; //NOSONAR
        if (mWidgetLayoutResId == R.layout.widget_layout_medium || mWidgetLayoutResId == R.layout.widget_layout_medium_alt) { //NOSONAR
            height = getActivity().getResources().getDimension(R.dimen.widget_medium_height); //NOSONAR
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) height); //NOSONAR
        }
        if (mWidgetLayoutResId == R.layout.widget_layout_large || mWidgetLayoutResId == R.layout.widget_layout_large_alt) { //NOSONAR
            height = getActivity().getResources().getDimension(R.dimen.widget_large_height); //NOSONAR
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) height); //NOSONAR
        }
        if (mWidgetLayoutResId == R.layout.widget_layout_small) { //NOSONAR
            height = getActivity().getResources().getDimension(R.dimen.widget_small_height); //NOSONAR
            width = getActivity().getResources().getDimension(R.dimen.widget_small_width); //NOSONAR
            layoutParams = new FrameLayout.LayoutParams((int) width, (int) height); //NOSONAR
        }

        FrameLayout frameLayout1 = rootView.findViewById(R.id.frame); //NOSONAR

        if (layoutParams != null) { //NOSONAR
            frameLayout1.addView(frameLayout, layoutParams); //NOSONAR
        } else { //NOSONAR
            frameLayout1.addView(frameLayout); //NOSONAR
        }

        return rootView; //NOSONAR
    }

    @Override //NOSONAR
    public void onActivityCreated(@Nullable Bundle savedInstanceState) { //NOSONAR
        super.onActivityCreated(savedInstanceState); //NOSONAR

        if (getActivity() instanceof WidgetConfigureActivitySmall) { //NOSONAR
            ((WidgetConfigureActivitySmall) getActivity()).updateWidgetUI(); //NOSONAR
        } else if (getActivity() instanceof WidgetConfigureActivityMedium) { //NOSONAR
            ((WidgetConfigureActivityMedium) getActivity()).updateWidgetUI(); //NOSONAR
        } else if (getActivity() instanceof WidgetConfigureActivityLarge) { //NOSONAR
            ((WidgetConfigureActivityLarge) getActivity()).updateWidgetUI(); //NOSONAR
        } else if (getActivity() instanceof WidgetConfigureActivityExtraLarge) { //NOSONAR
            ((WidgetConfigureActivityExtraLarge) getActivity()).updateWidgetUI(); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    }
}
