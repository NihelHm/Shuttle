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

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class DrawerChild {

    interface ClickListener {
        void onClick(Playlist playlist);

        void onOverflowClick(View v, Playlist playlist);
    }

    @NonNull
    Playlist playlist;

    @Nullable
    ClickListener listener;

    public DrawerChild(@NonNull Playlist playlist) {
        this.playlist = playlist;
    }

    public void setListener(@Nullable ClickListener listener) {
        this.listener = listener;
    }

    public void bindView(ChildHolder holder) {
        holder.bind(this);

        holder.lineOne.setText(playlist.name);
    }

    void onClick() {
        if (listener != null) {
            listener.onClick(playlist);
        }
    }

    void onOverflowClick(View v) {
        if (listener != null) {
            listener.onOverflowClick(v, playlist);
        }
    }

    static class ChildHolder extends ChildViewHolder {

        DrawerChild drawerChild;

        void bind(DrawerChild drawerChild) {
            this.drawerChild = drawerChild;
        }

        @BindView(R.id.icon)
        ImageView icon;

        @BindView(R.id.line_one)
        TextView lineOne;

        @BindView(R.id.btn_overflow)
        ImageButton overFlow;

        ChildHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            lineOne.setAlpha(0.54f);

            overFlow.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(v -> drawerChild.onClick());

            overFlow.setOnClickListener(v -> drawerChild.onOverflowClick(v));
        }
    }
}
