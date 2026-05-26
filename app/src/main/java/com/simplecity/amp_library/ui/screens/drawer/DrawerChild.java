package com.simplecity.amp_library.ui.screens.drawer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.Playlist;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DrawerChild { //NOSONAR

    interface ClickListener { //NOSONAR
        void onClick(Playlist playlist); //NOSONAR

        void onOverflowClick(View v, Playlist playlist); //NOSONAR
    }

    @NonNull //NOSONAR
    Playlist playlist; //NOSONAR

    @Nullable //NOSONAR
    ClickListener listener; //NOSONAR

    public DrawerChild(@NonNull Playlist playlist) { //NOSONAR
        this.playlist = playlist; //NOSONAR
    }

    public void setListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    public void bindView(ChildHolder holder) { //NOSONAR
        holder.bind(this); //NOSONAR

        holder.lineOne.setText(playlist.name); //NOSONAR
    }

    void onClick() { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onClick(playlist); //NOSONAR
        }
    }

    void onOverflowClick(View v) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onOverflowClick(v, playlist); //NOSONAR
        }
    }

    static class ChildHolder extends ChildViewHolder { //NOSONAR

        DrawerChild drawerChild; //NOSONAR

        void bind(DrawerChild drawerChild) { //NOSONAR
            this.drawerChild = drawerChild; //NOSONAR
        }

        @BindView(R.id.icon) //NOSONAR
        ImageView icon; //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        TextView lineOne; //NOSONAR

        @BindView(R.id.btn_overflow) //NOSONAR
        ImageButton overFlow; //NOSONAR

        ChildHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            lineOne.setAlpha(0.54f); //NOSONAR

            overFlow.setVisibility(View.VISIBLE); //NOSONAR

            itemView.setOnClickListener(v -> drawerChild.onClick()); //NOSONAR

            overFlow.setOnClickListener(v -> drawerChild.onOverflowClick(v)); //NOSONAR
        }
    }
}
