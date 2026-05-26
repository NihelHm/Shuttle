package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.InclExclItem;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.ui.views.OverflowButton;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class InclExclView extends BaseViewModel<InclExclView.ViewHolder> { //NOSONAR

    public interface ClickListener { //NOSONAR
        void onRemove(InclExclView InclExclView); //NOSONAR
    }

    @SuppressWarnings("java:S1104") //NOSONAR

    public InclExclItem inclExclItem; //NOSONAR

    public InclExclView(InclExclItem inclExclItem) { //NOSONAR
        this.inclExclItem = inclExclItem; //NOSONAR
    }

    @Nullable //NOSONAR
    ClickListener listener; //NOSONAR

    public void setClickListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    void onRemove() { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onRemove(this); //NOSONAR
        }
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.INCL_EXCL; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_one_line; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        holder.lineOne.setText(inclExclItem.path); //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder<InclExclView> { //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineOne; //NOSONAR

        @BindView(R.id.btn_overflow) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public OverflowButton overflow; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            lineOne.setSingleLine(false); //NOSONAR

            overflow.drawable = DrawableCompat.wrap(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_close_24dp)).mutate(); //NOSONAR

            overflow.setOnClickListener(v -> viewModel.onRemove()); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "InclExclView.ViewHolder"; //NOSONAR
        }
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        InclExclView that = (InclExclView) o; //NOSONAR

        return inclExclItem != null ? inclExclItem.equals(that.inclExclItem) : that.inclExclItem == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        return inclExclItem != null ? inclExclItem.hashCode() : 0; //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        return false; //NOSONAR
    }
}
