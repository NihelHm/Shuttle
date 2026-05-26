package com.simplecity.amp_library.ui.screens.drawer; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.view.View; // NOSONAR
import com.bignerdranch.expandablerecyclerview.ParentViewHolder; // NOSONAR
import com.bignerdranch.expandablerecyclerview.model.Parent; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DrawerDivider implements Parent<DrawerChild> { //NOSONAR

    @Override //NOSONAR
    public List<DrawerChild> getChildList() { //NOSONAR
        return Collections.emptyList(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean isInitiallyExpanded() { //NOSONAR
        return false; //NOSONAR
    } // NOSONAR

    public void bindView() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    static class DividerHolder extends ParentViewHolder { //NOSONAR

        DividerHolder(@NonNull View itemView) { //NOSONAR
            super(itemView); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
