package com.simplecity.amp_library.ui.screens.drawer; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter; // NOSONAR
import com.bignerdranch.expandablerecyclerview.ParentViewHolder; // NOSONAR
import com.bignerdranch.expandablerecyclerview.model.Parent; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DrawerAdapter extends ExpandableRecyclerAdapter<Parent<DrawerChild>, DrawerChild, ParentViewHolder<Parent<DrawerChild>, DrawerChild>, DrawerChild.ChildHolder> { //NOSONAR

    public DrawerAdapter(@NonNull List<Parent<DrawerChild>> parentList) { //NOSONAR
        super(parentList); //NOSONAR
    } // NOSONAR

    static final int TYPE_DIVIDER = 3; //NOSONAR

    @Override //NOSONAR
    public int getParentViewType(int parentPosition) { //NOSONAR

        if (getParentList().get(parentPosition) instanceof DrawerDivider) { //NOSONAR
            return TYPE_DIVIDER; //NOSONAR
        } // NOSONAR

        return super.getParentViewType(parentPosition); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean isParentViewType(int viewType) { //NOSONAR
        return super.isParentViewType(viewType) || viewType == TYPE_DIVIDER; //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    @Override //NOSONAR
    public ParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) { //NOSONAR
        switch (viewType) { //NOSONAR
            case TYPE_DIVIDER: //NOSONAR
                return new DrawerDivider.DividerHolder(LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.list_item_drawer_divider, parentViewGroup, false)); //NOSONAR
            case TYPE_PARENT: //NOSONAR
                return new DrawerParent.ParentHolder(LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.list_item_drawer, parentViewGroup, false)); //NOSONAR
        } // NOSONAR
        throw new IllegalStateException("onCreateParentViewHolder failed to return holder for type: " + viewType); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    @Override //NOSONAR
    public DrawerChild.ChildHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) { //NOSONAR
        return new DrawerChild.ChildHolder(LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.list_item_drawer, childViewGroup, false)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onBindParentViewHolder(@NonNull ParentViewHolder<Parent<DrawerChild>, DrawerChild> parentViewHolder, int parentPosition, @NonNull Parent<DrawerChild> parent) { //NOSONAR
        switch (getParentViewType(parentPosition)) { //NOSONAR
            case TYPE_DIVIDER: //NOSONAR
                ((DrawerDivider) getParentList().get(parentPosition)).bindView(); //NOSONAR
                break; //NOSONAR
            case TYPE_PARENT: //NOSONAR
                ((DrawerParent) getParentList().get(parentPosition)).bindView((DrawerParent.ParentHolder) parentViewHolder); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onBindChildViewHolder(@NonNull DrawerChild.ChildHolder childViewHolder, int parentPosition, int childPosition, @NonNull DrawerChild drawerChild) { //NOSONAR
        List<Parent<DrawerChild>> parentList = getParentList(); //NOSONAR
        if (parentPosition >= 0 && !parentList.isEmpty() && parentPosition < parentList.size()) { //NOSONAR
            List<DrawerChild> childList = parentList.get(parentPosition).getChildList(); //NOSONAR
            if (childPosition >= 0 && !childList.isEmpty() && childPosition < childList.size()) { //NOSONAR
                childList.get(childPosition).bindView(childViewHolder); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
