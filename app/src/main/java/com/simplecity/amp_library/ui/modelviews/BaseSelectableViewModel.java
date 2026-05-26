package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.CallSuper;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseSelectableViewModel<VH extends BaseViewHolder> extends BaseViewModel<VH> implements SelectableViewModel { //NOSONAR

    private boolean isSelected = false; //NOSONAR

    @Override //NOSONAR
    public void setSelected(boolean selected) { //NOSONAR
        isSelected = selected; //NOSONAR
    }

    @Override //NOSONAR
    public boolean isSelected() { //NOSONAR
        return isSelected; //NOSONAR
    }

    @Override //NOSONAR
    @CallSuper //NOSONAR
    public void bindView(VH holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.itemView.setActivated(isSelected); //NOSONAR
    }

    @Override //NOSONAR
    @CallSuper //NOSONAR
    public void bindView(VH holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR

        holder.itemView.setActivated(isSelected); //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        if (this == other) return true; //NOSONAR
        if (other == null || getClass() != other.getClass()) return false; //NOSONAR

        BaseSelectableViewModel that = (BaseSelectableViewModel) other; //NOSONAR

        return isSelected == that.isSelected; //NOSONAR
    }
}
