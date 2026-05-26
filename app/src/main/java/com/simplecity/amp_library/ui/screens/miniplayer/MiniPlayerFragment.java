package com.simplecity.amp_library.ui.screens.miniplayer; // NOSONAR

import android.content.Context; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.view.GestureDetector; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.MotionEvent; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.ProgressBar; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import butterknife.Unbinder; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.Priority; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment; // NOSONAR
import com.simplecity.amp_library.ui.dialog.UpgradeDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerPresenter; // NOSONAR
import com.simplecity.amp_library.ui.views.PlayPauseView; // NOSONAR
import com.simplecity.amp_library.ui.views.PlayerViewAdapter; // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.multisheetview.ui.view.MultiSheetView; // NOSONAR
import dagger.android.support.AndroidSupportInjection; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MiniPlayerFragment extends BaseFragment { //NOSONAR

    private static final String TAG = "MiniPlayerFragment"; //NOSONAR

    View rootView; //NOSONAR

    @BindView(R.id.mini_play) //NOSONAR
    PlayPauseView playPauseView; //NOSONAR

    @BindView(R.id.progressbar) //NOSONAR
    ProgressBar progressBar; //NOSONAR

    @BindView(R.id.titleTextView) //NOSONAR
    TextView titleTextView; //NOSONAR

    @BindView(R.id.artworkImageView) //NOSONAR
    ImageView miniArtwork; //NOSONAR

    @Inject //NOSONAR
    PlayerPresenter presenter; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    private CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    private Unbinder unbinder; //NOSONAR

    public MiniPlayerFragment() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public static MiniPlayerFragment newInstance() { //NOSONAR
        MiniPlayerFragment fragment = new MiniPlayerFragment(); //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //NOSONAR
        rootView = inflater.inflate(R.layout.fragment_mini_player, container, false); //NOSONAR

        unbinder = ButterKnife.bind(this, rootView); //NOSONAR

        rootView.setOnClickListener(v -> { //NOSONAR
            MultiSheetView multiSheetView = MultiSheetView.getParentMultiSheetView(rootView); //NOSONAR
            if (multiSheetView != null) { //NOSONAR
                multiSheetView.expandSheet(MultiSheetView.Sheet.FIRST); //NOSONAR
            } // NOSONAR
        }); // NOSONAR
        rootView.setOnTouchListener(new OnSwipeTouchListener(getActivity())); //NOSONAR

        playPauseView.setOnClickListener(v -> playPauseView.toggle(() -> { //NOSONAR
            presenter.togglePlayback(); //NOSONAR
            return Unit.INSTANCE; //NOSONAR
        })); // NOSONAR

        progressBar.setMax(1000); //NOSONAR

        disposables.add(Aesthetic.get(getContext()).isDark() //NOSONAR
                .subscribe(isDark -> { //NOSONAR
                    int color = isDark ? getContext().getResources().getColor(android.R.color.primary_text_dark) : getContext().getResources().getColor(android.R.color.primary_text_light); //NOSONAR
                    titleTextView.setTextColor(color); //NOSONAR
                    playPauseView.setDrawableColor(color); //NOSONAR
                })); // NOSONAR

        return rootView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //NOSONAR
        super.onViewCreated(view, savedInstanceState); //NOSONAR

        presenter.bindView(playerViewAdapter); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (presenter != null) { //NOSONAR
            presenter.updateTrackInfo(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        presenter.unbindView(playerViewAdapter); //NOSONAR
        disposables.clear(); //NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroy() { //NOSONAR
        rootView.setOnTouchListener(null); //NOSONAR

        super.onDestroy(); //NOSONAR
    } // NOSONAR

    private class OnSwipeTouchListener implements View.OnTouchListener { //NOSONAR

        private final GestureDetector gestureDetector; //NOSONAR

        OnSwipeTouchListener(Context context) { //NOSONAR
            gestureDetector = new GestureDetector(context, new GestureListener()); //NOSONAR
        } // NOSONAR

        void onSwipeLeft() { //NOSONAR
            presenter.skip(); //NOSONAR
        } // NOSONAR

        void onSwipeRight() { //NOSONAR
            presenter.prev(false); //NOSONAR
        } // NOSONAR

        public boolean onTouch(View v, MotionEvent event) { //NOSONAR

            boolean consumed = gestureDetector.onTouchEvent(event); //NOSONAR

            if (!consumed) { //NOSONAR
                if (event.getAction() == MotionEvent.ACTION_UP) { //NOSONAR
                    v.performClick(); //NOSONAR
                } // NOSONAR
            } // NOSONAR

            return consumed; //NOSONAR
        } // NOSONAR

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener { //NOSONAR

            private static final int SWIPE_DISTANCE_THRESHOLD = 100; //NOSONAR
            private static final int SWIPE_VELOCITY_THRESHOLD = 100; //NOSONAR

            GestureListener() { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public boolean onDown(MotionEvent e) { //NOSONAR
                return true; //NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { //NOSONAR
                float distanceX = e2.getX() - e1.getX(); //NOSONAR
                float distanceY = e2.getY() - e1.getY(); //NOSONAR
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) { //NOSONAR
                    if (distanceX > 0) { //NOSONAR
                        onSwipeRight(); //NOSONAR
                    } else { //NOSONAR
                        onSwipeLeft(); //NOSONAR
                    } // NOSONAR
                    return true; //NOSONAR
                } // NOSONAR
                return false; //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR

    PlayerViewAdapter playerViewAdapter = new PlayerViewAdapter() { //NOSONAR

        @Override //NOSONAR
        public void setSeekProgress(int progress) { //NOSONAR
            progressBar.setProgress(progress); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void playbackChanged(boolean isPlaying) { //NOSONAR
            if (isPlaying) { //NOSONAR
                if (playPauseView.isPlay()) { //NOSONAR
                    playPauseView.toggle(null); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                if (!playPauseView.isPlay()) { //NOSONAR
                    playPauseView.toggle(null); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void trackInfoChanged(@Nullable Song song) { //NOSONAR

            if (song == null) return; //NOSONAR

            titleTextView.setText(String.format("%s • %s", song.name, song.artistName)); //NOSONAR

            Glide.with(getContext()) //NOSONAR
                    .load(song) //NOSONAR
                    .priority(Priority.HIGH) //NOSONAR
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                    .placeholder(PlaceholderProvider.getInstance(getContext()).getPlaceHolderDrawable(song.name, false, settingsManager)) //NOSONAR
                    .into(miniArtwork); //NOSONAR

            rootView.setContentDescription(getString(R.string.btn_now_playing, song.name, song.artistName)); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void showUpgradeDialog() { //NOSONAR
            UpgradeDialog.Companion.newInstance().show(getChildFragmentManager()); //NOSONAR
        } // NOSONAR
    }; // NOSONAR
} // NOSONAR
