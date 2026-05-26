package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import com.afollestad.aesthetic.AestheticToolbar;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ContextualToolbar extends AestheticToolbar { //NOSONAR

    public ContextualToolbar(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    public ContextualToolbar(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    public ContextualToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { //NOSONAR
        super(context, attrs, defStyleAttr); //NOSONAR
    }

    public void show() { //NOSONAR
        setVisibility(View.VISIBLE); //NOSONAR
    }

    public void hide() { //NOSONAR
        setVisibility(View.GONE); //NOSONAR
    }

    /**
     * Traverses the fragment hierarchy, searching for an instance of {@link ContextualToolbarHost}
     *
     * @return {@link ContextualToolbar} or null if none can be found in the fragment hierarchy.
     */
    @Nullable //NOSONAR
    public static ContextualToolbar findContextualToolbar(Fragment fragment) { //NOSONAR
        if (fragment instanceof ContextualToolbarHost) { //NOSONAR
            return ((ContextualToolbarHost) fragment).getContextualToolbar(); //NOSONAR
        } else { //NOSONAR
            Fragment parentFragment = fragment.getParentFragment(); //NOSONAR
            if (parentFragment != null) { //NOSONAR
                return findContextualToolbar(parentFragment); //NOSONAR
            }
        }
        return null; //NOSONAR
    }
}
