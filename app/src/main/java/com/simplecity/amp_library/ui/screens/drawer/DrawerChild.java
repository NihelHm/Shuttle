package com.simplecity.amp_library.ui.screens.drawer; // NOSONAR

import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.view.View; // NOSONAR
import android.widget.ImageButton; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import com.bignerdranch.expandablerecyclerview.ChildViewHolder; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Playlist; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DrawerChild { //NOSONAR

    interface ClickListener { //NOSONAR
        void onClick(Playlist playlist); //NOSONAR

        void onOverflowClick(View v, Playlist playlist); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    Playlist playlist; //NOSONAR

    @Nullable //NOSONAR
    ClickListener listener; //NOSONAR

    public DrawerChild(@NonNull Playlist playlist) { //NOSONAR
        this.playlist = playlist; //NOSONAR
    } // NOSONAR

    public void setListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    } // NOSONAR

    public void bindView(ChildHolder holder) { //NOSONAR
        holder.bind(this); //NOSONAR

        holder.lineOne.setText(playlist.name); //NOSONAR
    } // NOSONAR

    void onClick() { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onClick(playlist); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void onOverflowClick(View v) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onOverflowClick(v, playlist); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    static class ChildHolder extends ChildViewHolder { //NOSONAR

        DrawerChild drawerChild; //NOSONAR

        void bind(DrawerChild drawerChild) { //NOSONAR
            this.drawerChild = drawerChild; //NOSONAR
        } // NOSONAR

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
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
