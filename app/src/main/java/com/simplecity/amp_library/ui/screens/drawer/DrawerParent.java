package com.simplecity.amp_library.ui.screens.drawer;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.aesthetic.Aesthetic;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;
import com.simplecity.amp_library.utils.StringUtils;
import com.simplecity.amp_library.utils.TypefaceManager;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DrawerParent implements Parent<DrawerChild> { //NOSONAR

    private static final String TAG = "DrawerParent"; //NOSONAR

    static DrawerParent getLibraryParent(SettingsManager settingsManager) { //NOSONAR
        return new DrawerParent( //NOSONAR
                Type.LIBRARY, //NOSONAR
                R.string.library_title, //NOSONAR
                R.drawable.ic_library_music_24dp, //NOSONAR
                NavigationEventRelay.librarySelectedEvent, //NOSONAR
                true, //NOSONAR
                settingsManager); //NOSONAR
    }

    static DrawerParent getPlaylistsParent(SettingsManager settingsManager) { //NOSONAR
        return new DrawerParent( //NOSONAR
                Type.PLAYLISTS, //NOSONAR
                R.string.playlists_title, //NOSONAR
                R.drawable.ic_queue_music_24dp, //NOSONAR
                null, //NOSONAR
                true, //NOSONAR
                settingsManager); //NOSONAR
    }

    static DrawerParent getSleepTimerParent(SettingsManager settingsManager) { //NOSONAR
        return new DrawerParent( //NOSONAR
                Type.SLEEP_TIMER, //NOSONAR
                R.string.sleep_timer, //NOSONAR
                R.drawable.ic_sleep_24dp, //NOSONAR
                NavigationEventRelay.sleepTimerSelectedEvent, //NOSONAR
                false, //NOSONAR
                settingsManager); //NOSONAR
    }

    static DrawerParent getEqualizerParent(SettingsManager settingsManager) { //NOSONAR
        return new DrawerParent( //NOSONAR
                Type.EQUALIZER, //NOSONAR
                R.string.equalizer, //NOSONAR
                R.drawable.ic_equalizer_24dp, //NOSONAR
                NavigationEventRelay.equalizerSelectedEvent, //NOSONAR
                false, //NOSONAR
                settingsManager); //NOSONAR
    }

    static DrawerParent getSettingsParent(SettingsManager settingsManager) { //NOSONAR
        return new DrawerParent( //NOSONAR
                Type.SETTINGS, //NOSONAR
                R.string.settings, //NOSONAR
                R.drawable.ic_settings_24dp, //NOSONAR
                NavigationEventRelay.settingsSelectedEvent, //NOSONAR
                false, //NOSONAR
                settingsManager); //NOSONAR
    }

    static DrawerParent getSupportParent(SettingsManager settingsManager) { //NOSONAR
        return new DrawerParent( //NOSONAR
                Type.SUPPORT, //NOSONAR
                R.string.pref_title_support, //NOSONAR
                R.drawable.ic_help_24dp, //NOSONAR
                NavigationEventRelay.supportSelectedEvent, //NOSONAR
                false, //NOSONAR
                settingsManager); //NOSONAR
    }

    static DrawerParent getFolderParent(Context context, SettingsManager settingsManager) { //NOSONAR
        return new DrawerParent( //NOSONAR
                DrawerParent.Type.FOLDERS, //NOSONAR
                R.string.folders_title, //NOSONAR
                R.drawable.ic_folder_multiple_24dp, //NOSONAR
                NavigationEventRelay.getFoldersSelectedEvent((ShuttleApplication) context.getApplicationContext(), settingsManager), //NOSONAR
                true, //NOSONAR
                settingsManager //NOSONAR
        ) {
            @Override //NOSONAR
            public boolean isSelectable() { //NOSONAR
                return ShuttleUtils.isUpgraded((ShuttleApplication) context.getApplicationContext(), settingsManager); //NOSONAR
            }
        };
    }

    public @interface Type { //NOSONAR
        int LIBRARY = 0; //NOSONAR
        int FOLDERS = 1; //NOSONAR
        int PLAYLISTS = 2; //NOSONAR
        int SLEEP_TIMER = 3; //NOSONAR
        int EQUALIZER = 4; //NOSONAR
        int SETTINGS = 5; //NOSONAR
        int SUPPORT = 6; //NOSONAR
    }

    private boolean selectable = true; //NOSONAR

    public interface ClickListener { //NOSONAR
        void onClick(DrawerParent drawerParent); //NOSONAR
    }

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    public void setListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    @DrawerParent.Type //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int type; //NOSONAR

    @Nullable //NOSONAR
    NavigationEventRelay.NavigationEvent navigationEvent; //NOSONAR

    @StringRes //NOSONAR
    private int titleResId; //NOSONAR

    @DrawableRes //NOSONAR
    private int iconResId; //NOSONAR

    List<DrawerChild> children = new ArrayList<>(); //NOSONAR

    private boolean isSelected; //NOSONAR

    private boolean timerActive = false; //NOSONAR
    private long timeRemaining = 0L; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    DrawerParent( //NOSONAR
            @DrawerParent.Type int type, //NOSONAR
            int titleResId, //NOSONAR
            int iconResId, @ //NOSONAR
            Nullable NavigationEventRelay.NavigationEvent navigationEvent, //NOSONAR
            boolean selectable, //NOSONAR
            SettingsManager settingsManager //NOSONAR
    ) {
        this.type = type; //NOSONAR
        this.titleResId = titleResId; //NOSONAR
        this.iconResId = iconResId; //NOSONAR
        this.navigationEvent = navigationEvent; //NOSONAR
        this.selectable = selectable; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    }

    @Override //NOSONAR
    public List<DrawerChild> getChildList() { //NOSONAR
        return children; //NOSONAR
    }

    @Override //NOSONAR
    public boolean isInitiallyExpanded() { //NOSONAR
        return false; //NOSONAR
    }

    public boolean isSelected() { //NOSONAR
        return isSelected; //NOSONAR
    }

    public void setSelected(boolean selected) { //NOSONAR
        isSelected = selected; //NOSONAR
    }

    public void setTimerActive(boolean timerActive) { //NOSONAR
        this.timerActive = timerActive; //NOSONAR
    }

    public void setTimeRemaining(long timeRemaining) { //NOSONAR
        this.timeRemaining = timeRemaining; //NOSONAR
    }

    public boolean isSelectable() { //NOSONAR
        return selectable; //NOSONAR
    }

    void onClick() { //NOSONAR
        if (listener != null && type != Type.PLAYLISTS) { //NOSONAR
            listener.onClick(this); //NOSONAR
        }
    }

    public void bindView(ParentHolder holder) { //NOSONAR

        holder.bind(this); //NOSONAR

        Drawable arrowDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(holder.itemView.getContext(), holder.isExpanded() ? R.drawable.ic_arrow_up_24dp : R.drawable.ic_arrow_down_24dp)); //NOSONAR
        DrawableCompat.setTint(arrowDrawable, Aesthetic.get(holder.itemView.getContext()).textColorSecondary().blockingFirst()); //NOSONAR
        holder.expandableIcon.setImageDrawable(arrowDrawable); //NOSONAR

        holder.expandableIcon.setVisibility(getChildList().isEmpty() ? View.GONE : View.VISIBLE); //NOSONAR

        holder.icon.setImageResource(iconResId); //NOSONAR
        if (iconResId != -1) { //NOSONAR
            holder.icon.setVisibility(View.VISIBLE); //NOSONAR
        } else { //NOSONAR
            holder.icon.setVisibility(View.GONE); //NOSONAR
        }

        if (titleResId != -1) { //NOSONAR
            holder.lineOne.setText(holder.itemView.getResources().getString(titleResId)); //NOSONAR
            holder.lineOne.setTypeface(TypefaceManager.getInstance().getTypeface(holder.itemView.getContext(), TypefaceManager.SANS_SERIF_MEDIUM)); //NOSONAR
        }

        if (isSelected) { //NOSONAR
            holder.itemView.setActivated(true); //NOSONAR
        } else { //NOSONAR
            holder.itemView.setActivated(false); //NOSONAR
            holder.icon.setAlpha(0.6f); //NOSONAR
        }

        if (type == DrawerParent.Type.FOLDERS && !ShuttleUtils.isUpgraded((ShuttleApplication) holder.itemView.getContext().getApplicationContext(), settingsManager)) { //NOSONAR
            holder.itemView.setAlpha(0.4f); //NOSONAR
        } else { //NOSONAR
            holder.itemView.setAlpha(1.0f); //NOSONAR
        }

        if (type == DrawerParent.Type.PLAYLISTS) { //NOSONAR
            holder.itemView.setAlpha(getChildList().isEmpty() ? 0.4f : 1.0f); //NOSONAR
            holder.itemView.setEnabled(!getChildList().isEmpty()); //NOSONAR
        }

        if (type == Type.SLEEP_TIMER) { //NOSONAR
            holder.timeRemaining.setVisibility(timerActive ? View.VISIBLE : View.GONE); //NOSONAR
            holder.timeRemaining.setText(StringUtils.makeTimeString(holder.itemView.getContext(), timeRemaining)); //NOSONAR
        } else { //NOSONAR
            holder.timeRemaining.setVisibility(View.GONE); //NOSONAR
        }
    }

    static class ParentHolder extends ParentViewHolder { //NOSONAR

        private DrawerParent drawerParent; //NOSONAR

        @BindView(R.id.icon) //NOSONAR
        ImageView icon; //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        TextView lineOne; //NOSONAR

        @BindView(R.id.expandable_icon) //NOSONAR
        ImageView expandableIcon; //NOSONAR

        @BindView(R.id.timeRemaining) //NOSONAR
        TextView timeRemaining; //NOSONAR

        private ObjectAnimator objectAnimator; //NOSONAR

        ParentHolder(@NonNull View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR
        }

        void bind(DrawerParent drawerParent) { //NOSONAR
            this.drawerParent = drawerParent; //NOSONAR
        }

        @Override //NOSONAR
        public void onExpansionToggled(boolean expanded) { //NOSONAR
            super.onExpansionToggled(expanded); //NOSONAR

            if (objectAnimator != null) { //NOSONAR
                objectAnimator.cancel(); //NOSONAR
            }

            objectAnimator = ObjectAnimator.ofFloat(expandableIcon, View.ROTATION, //NOSONAR
                    expanded ? expandableIcon.getRotation() : expandableIcon.getRotation(), //NOSONAR
                    expanded ? 0f : -180f); //NOSONAR
            objectAnimator.setDuration(250); //NOSONAR
            objectAnimator.setStartDelay(expanded ? 100 : 0); //NOSONAR
            objectAnimator.setInterpolator(new DecelerateInterpolator(1.2f)); //NOSONAR
            objectAnimator.start(); //NOSONAR
        }

        @Override //NOSONAR
        public void onClick(View v) { //NOSONAR
            super.onClick(v); //NOSONAR

            drawerParent.onClick(); //NOSONAR
        }
    }
}
