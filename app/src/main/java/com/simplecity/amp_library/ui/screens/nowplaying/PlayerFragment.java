package com.simplecity.amp_library.ui.screens.nowplaying; // NOSONAR

import android.animation.Animator; // NOSONAR
import android.animation.AnimatorListenerAdapter; // NOSONAR
import android.animation.ValueAnimator; // NOSONAR
import android.annotation.SuppressLint; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.util.Log; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.MenuItem; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.view.animation.AccelerateDecelerateInterpolator; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.TextView; // NOSONAR
import android.widget.Toast; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import butterknife.Unbinder; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.ColorIsDarkState; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.Priority; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.bumptech.glide.load.resource.drawable.GlideDrawable; // NOSONAR
import com.bumptech.glide.request.animation.GlideAnimation; // NOSONAR
import com.bumptech.glide.request.target.SimpleTarget; // NOSONAR
import com.bumptech.glide.request.target.Target; // NOSONAR
import com.f2prateek.rx.preferences2.RxSharedPreferences; // NOSONAR
import com.google.android.gms.cast.framework.CastButtonFactory; // NOSONAR
import com.jakewharton.rxbinding2.widget.RxSeekBar; // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarChangeEvent; // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarProgressChangeEvent; // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarStartChangeEvent; // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent; // NOSONAR
import com.jp.wasabeef.glide.transformations.BlurTransformation; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.cast.CastManager; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.glide.palette.ColorSet; // NOSONAR
import com.simplecity.amp_library.glide.palette.ColorSetTranscoder; // NOSONAR
import com.simplecity.amp_library.model.Playlist; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.QueueManager; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeAction; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeConsumer; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment; // NOSONAR
import com.simplecity.amp_library.ui.dialog.ShareDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.SongInfoDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.UpgradeDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay; // NOSONAR
import com.simplecity.amp_library.ui.screens.lyrics.LyricsDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.pager.QueuePagerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog; // NOSONAR
import com.simplecity.amp_library.ui.views.FavoriteActionBarView; // NOSONAR
import com.simplecity.amp_library.ui.views.PlayPauseView; // NOSONAR
import com.simplecity.amp_library.ui.views.RepeatButton; // NOSONAR
import com.simplecity.amp_library.ui.views.RepeatingImageButton; // NOSONAR
import com.simplecity.amp_library.ui.views.ShuffleButton; // NOSONAR
import com.simplecity.amp_library.ui.views.SizableSeekBar; // NOSONAR
import com.simplecity.amp_library.ui.views.SnowfallView; // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetSlideEventRelay; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider; // NOSONAR
import com.simplecity.amp_library.utils.RingtoneManager; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR
import com.simplecity.amp_library.utils.color.ArgbEvaluator; // NOSONAR
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils; // NOSONAR
import dagger.android.support.AndroidSupportInjection; // NOSONAR
import io.reactivex.BackpressureStrategy; // NOSONAR
import io.reactivex.Flowable; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR
import java.util.List; // NOSONAR
import java.util.concurrent.TimeUnit; // NOSONAR
import javax.inject.Inject; // NOSONAR
import kotlin.Unit; // NOSONAR
import org.jetbrains.annotations.NotNull; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlayerFragment extends BaseFragment implements //NOSONAR
        PlayerView, //NOSONAR
        Toolbar.OnMenuItemClickListener { //NOSONAR

    private static final String TAG = "PlayerFragment"; //NOSONAR

    private boolean isSeeking; //NOSONAR

    @BindView(R.id.toolbar) //NOSONAR
    Toolbar toolbar; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.play) //NOSONAR
    PlayPauseView playPauseView; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.shuffle) //NOSONAR
    ShuffleButton shuffleButton; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.repeat) //NOSONAR
    RepeatButton repeatButton; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.next) //NOSONAR
    RepeatingImageButton nextButton; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.prev) //NOSONAR
    RepeatingImageButton prevButton; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.current_time) //NOSONAR
    TextView currentTime; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.total_time) //NOSONAR
    TextView totalTime; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.text1) //NOSONAR
    TextView track; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.text2) //NOSONAR
    TextView album; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.text3) //NOSONAR
    TextView artist; //NOSONAR

    @BindView(R.id.backgroundView) //NOSONAR
    ImageView backgroundView; //NOSONAR

    @Nullable //NOSONAR
    @BindView(R.id.seekbar) //NOSONAR
    SizableSeekBar seekBar; //NOSONAR

    @BindView(R.id.snowfallView) //NOSONAR
    SnowfallView snowfallView; //NOSONAR

    CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    @Inject //NOSONAR
    PlayerPresenter presenter; //NOSONAR

    @Inject //NOSONAR
    NavigationEventRelay navigationEventRelay; //NOSONAR

    @Inject //NOSONAR
    MultiSheetSlideEventRelay sheetEventRelay; //NOSONAR

    @Inject //NOSONAR
    Repository.AlbumArtistsRepository albumArtistsRepository; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    private Unbinder unbinder; //NOSONAR

    ColorSet colorSet = ColorSet.Companion.empty(); //NOSONAR

    @Nullable //NOSONAR
    private Target<GlideDrawable> target; //NOSONAR

    private boolean isLandscape; //NOSONAR

    private boolean isExpanded; //NOSONAR

    @Nullable //NOSONAR
    private ValueAnimator colorAnimator; //NOSONAR

    public PlayerFragment() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public static PlayerFragment newInstance() { //NOSONAR
        PlayerFragment playerFragment = new PlayerFragment(); //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        playerFragment.setArguments(args); //NOSONAR
        return playerFragment; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //NOSONAR
        return inflater.inflate(R.layout.fragment_player, container, false); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //NOSONAR
        super.onViewCreated(view, savedInstanceState); //NOSONAR

        isLandscape = ShuttleUtils.isLandscape(getContext()); //NOSONAR

        unbinder = ButterKnife.bind(this, view); //NOSONAR

        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed()); //NOSONAR
        toolbar.inflateMenu(R.menu.menu_now_playing); //NOSONAR

        if (CastManager.isCastAvailable(getContext(), settingsManager)) { //NOSONAR
            MenuItem menuItem = CastButtonFactory.setUpMediaRouteButton(getContext(), toolbar.getMenu(), R.id.media_route_menu_item); //NOSONAR
            menuItem.setVisible(true); //NOSONAR
        } // NOSONAR

        MenuItem favoriteMenuItem = toolbar.getMenu().findItem(R.id.favorite); //NOSONAR
        FavoriteActionBarView menuActionView = (FavoriteActionBarView) favoriteMenuItem.getActionView(); //NOSONAR
        menuActionView.setOnClickListener(v -> onMenuItemClick(favoriteMenuItem)); //NOSONAR
        toolbar.setOnMenuItemClickListener(this); //NOSONAR

        if (playPauseView != null) { //NOSONAR
            playPauseView.setOnClickListener(v -> playPauseView.toggle(() -> { //NOSONAR
                presenter.togglePlayback(); //NOSONAR
                return Unit.INSTANCE; //NOSONAR
            })); // NOSONAR
        } // NOSONAR

        if (repeatButton != null) { //NOSONAR
            repeatButton.setOnClickListener(v -> presenter.toggleRepeat()); //NOSONAR
            repeatButton.setTag(":aesthetic_ignore"); //NOSONAR
        } // NOSONAR

        if (shuffleButton != null) { //NOSONAR
            shuffleButton.setOnClickListener(v -> presenter.toggleShuffle()); //NOSONAR
            shuffleButton.setTag(":aesthetic_ignore"); //NOSONAR
        } // NOSONAR

        if (nextButton != null) { //NOSONAR
            nextButton.setOnClickListener(v -> presenter.skip()); //NOSONAR
            nextButton.setRepeatListener((v, duration, repeatCount) -> presenter.scanForward(repeatCount, duration)); //NOSONAR
        } // NOSONAR

        if (prevButton != null) { //NOSONAR
            prevButton.setOnClickListener(v -> presenter.prev(false)); //NOSONAR
            prevButton.setRepeatListener((v, duration, repeatCount) -> presenter.scanBackward(repeatCount, duration)); //NOSONAR
        } // NOSONAR

        if (seekBar != null) { //NOSONAR
            seekBar.setMax(1000); //NOSONAR
        } // NOSONAR

        if (savedInstanceState == null) { //NOSONAR
            getChildFragmentManager().beginTransaction() //NOSONAR
                    .add(R.id.main_container, QueuePagerFragment.newInstance(), "QueuePagerFragment") //NOSONAR
                    .commit(); //NOSONAR
        } // NOSONAR

        getAestheticColorSetDisposable() //NOSONAR
                .take(1) //NOSONAR
                .subscribe( //NOSONAR
                        this::invalidateColors, //NOSONAR
                        error -> { //NOSONAR
                            // Nothing to do // NOSONAR
                        } // NOSONAR
                ); // NOSONAR

        presenter.bindView(this); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        if (target != null) { //NOSONAR
            Glide.clear(target); //NOSONAR
        } // NOSONAR
        snowfallView.clear(); //NOSONAR

        if (colorAnimator != null) { //NOSONAR
            colorAnimator.cancel(); //NOSONAR
        } // NOSONAR

        presenter.unbindView(this); //NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    } // NOSONAR

    public void update() { //NOSONAR
        if (presenter != null) { //NOSONAR
            presenter.updateTrackInfo(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (!settingsManager.getUsePalette() && !settingsManager.getUsePaletteNowPlayingOnly()) { //NOSONAR
            disposables.add(getAestheticColorSetDisposable().subscribe( //NOSONAR
                    colorSet -> animateColors(PlayerFragment.this.colorSet, colorSet, 800, this::invalidateColors, null), //NOSONAR
                    error -> { //NOSONAR
                        // Nothing to do // NOSONAR
                    }) // NOSONAR
            ); // NOSONAR
        } // NOSONAR

        if (seekBar != null) { //NOSONAR
            Flowable<SeekBarChangeEvent> sharedSeekBarEvents = RxSeekBar.changeEvents(seekBar) //NOSONAR
                    .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
                    .ofType(SeekBarChangeEvent.class) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .share(); //NOSONAR

            disposables.add(sharedSeekBarEvents.subscribe( //NOSONAR
                    seekBarChangeEvent -> { //NOSONAR
                        if (seekBarChangeEvent instanceof SeekBarStartChangeEvent) { //NOSONAR
                            isSeeking = true; //NOSONAR
                        } else if (seekBarChangeEvent instanceof SeekBarStopChangeEvent) { //NOSONAR
                            isSeeking = false; //NOSONAR
                        } // NOSONAR
                    }, // NOSONAR
                    error -> LogUtils.logException(TAG, "Error in seek change event", error)) //NOSONAR
            ); // NOSONAR

            disposables.add(sharedSeekBarEvents //NOSONAR
                    .ofType(SeekBarProgressChangeEvent.class) //NOSONAR
                    .filter(SeekBarProgressChangeEvent::fromUser) //NOSONAR
                    .debounce(15, TimeUnit.MILLISECONDS) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                            seekBarChangeEvent -> presenter.seekTo(seekBarChangeEvent.progress()), //NOSONAR
                            error -> LogUtils.logException(TAG, "Error receiving seekbar progress", error)) //NOSONAR
            ); // NOSONAR
        } // NOSONAR

        disposables.add(RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(getContext())) //NOSONAR
                .getBoolean(SettingsManager.KEY_DISPLAY_REMAINING_TIME) //NOSONAR
                .asObservable() //NOSONAR
                .subscribe( //NOSONAR
                        aBoolean -> presenter.updateRemainingTime(), //NOSONAR
                        error -> LogUtils.logException(TAG, "Remaining time changed", error) //NOSONAR
                ) // NOSONAR
        ); // NOSONAR

        disposables.add(sheetEventRelay.getEvents() //NOSONAR
                .subscribe( //NOSONAR
                        event -> { //NOSONAR
                            if (event.nowPlayingExpanded()) { //NOSONAR
                                isExpanded = true; //NOSONAR
                                snowfallView.letItSnow(analyticsManager); //NOSONAR
                            } else if (event.nowPlayingCollapsed()) { //NOSONAR
                                isExpanded = false; //NOSONAR
                                snowfallView.clear(); //NOSONAR
                            } // NOSONAR
                        }, // NOSONAR
                        throwable -> Log.e(TAG, "error listening for sheet slide events", throwable)) //NOSONAR
        ); // NOSONAR

        update(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        disposables.clear(); //NOSONAR
        super.onPause(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR

    // View implementation // NOSONAR

    @Override //NOSONAR
    public void setSeekProgress(int progress) { //NOSONAR
        if (!isSeeking && seekBar != null) { //NOSONAR
            seekBar.setProgress(progress); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void currentTimeVisibilityChanged(boolean visible) { //NOSONAR
        if (currentTime != null) { //NOSONAR
            currentTime.setVisibility(visible ? View.VISIBLE : View.INVISIBLE); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void currentTimeChanged(long seconds) { //NOSONAR
        if (currentTime != null) { //NOSONAR
            currentTime.setText(StringUtils.makeTimeString(getContext(), seconds)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void totalTimeChanged(long seconds) { //NOSONAR
        if (totalTime != null) { //NOSONAR
            totalTime.setText(StringUtils.makeTimeString(getContext(), seconds)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void queueChanged(int queuePosition, int queueLength) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void playbackChanged(boolean isPlaying) { //NOSONAR
        if (playPauseView != null) { //NOSONAR
            if (isPlaying) { //NOSONAR
                if (playPauseView.isPlay()) { //NOSONAR
                    playPauseView.toggle(null); //NOSONAR
                    playPauseView.setContentDescription(getString(R.string.btn_pause)); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                if (!playPauseView.isPlay()) { //NOSONAR
                    playPauseView.toggle(null); //NOSONAR
                    playPauseView.setContentDescription(getString(R.string.btn_play)); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (!isPlaying) { //NOSONAR
            snowfallView.removeSnow(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void shuffleChanged(@QueueManager.ShuffleMode int shuffleMode) { //NOSONAR
        if (shuffleButton != null) { //NOSONAR
            shuffleButton.setShuffleMode(shuffleMode); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void repeatChanged(@QueueManager.RepeatMode int repeatMode) { //NOSONAR
        if (repeatButton != null) { //NOSONAR
            repeatButton.setRepeatMode(repeatMode); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void favoriteChanged(boolean isFavorite) { //NOSONAR
        FavoriteActionBarView favoriteActionBarView = (FavoriteActionBarView) toolbar.getMenu().findItem(R.id.favorite).getActionView(); //NOSONAR
        favoriteActionBarView.setIsFavorite(isFavorite); //NOSONAR
    } // NOSONAR

    Song song = null; //NOSONAR

    @Override //NOSONAR
    public void trackInfoChanged(@Nullable Song song) { //NOSONAR

        if (song == null) return; //NOSONAR

        if (isExpanded && !snowfallView.isSnowing()) { //NOSONAR
            snowfallView.letItSnow(analyticsManager); //NOSONAR
        } else { //NOSONAR
            snowfallView.removeSnow(); //NOSONAR
        } // NOSONAR

        String totalTimeString = StringUtils.makeTimeString(getContext(), song.duration / 1000); //NOSONAR
        if (!TextUtils.isEmpty(totalTimeString)) { //NOSONAR
            if (totalTime != null) { //NOSONAR
                totalTime.setText(totalTimeString); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (track != null) { //NOSONAR
            track.setText(song.name); //NOSONAR
            track.setSelected(true); //NOSONAR
        } // NOSONAR
        if (album != null) { //NOSONAR
            album.setText(String.format("%s • %s", song.artistName, song.albumName)); //NOSONAR
        } // NOSONAR

        if (isLandscape) { //NOSONAR
            toolbar.setTitle(song.name); //NOSONAR
            toolbar.setSubtitle(String.format("%s • %s", song.artistName, song.albumName)); //NOSONAR

            target = Glide.with(this) //NOSONAR
                    .load(song) //NOSONAR
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
                    .bitmapTransform(new BlurTransformation(getContext(), 15, 4)) //NOSONAR
                    .error(PlaceholderProvider.getInstance(getContext()).getPlaceHolderDrawable(song.name, true, settingsManager)) //NOSONAR
                    .thumbnail(Glide //NOSONAR
                            .with(this) //NOSONAR
                            .load(this.song) //NOSONAR
                            .bitmapTransform(new BlurTransformation(getContext(), 15, 4))) //NOSONAR
                    .crossFade(600) //NOSONAR
                    .into(backgroundView); //NOSONAR

            this.song = song; //NOSONAR
        } else { //NOSONAR
            backgroundView.setImageDrawable(null); //NOSONAR
            toolbar.setTitle(null); //NOSONAR
            toolbar.setSubtitle(null); //NOSONAR
        } // NOSONAR

        if (settingsManager.getUsePalette()) { //NOSONAR

            if (paletteTarget != null) { //NOSONAR
                Glide.clear(paletteTarget); //NOSONAR
            } // NOSONAR

            Glide.with(this) //NOSONAR
                    .load(song) //NOSONAR
                    .asBitmap() //NOSONAR
                    .transcode(new ColorSetTranscoder(getContext()), ColorSet.class) //NOSONAR
                    .override(250, 250) //NOSONAR
                    .priority(Priority.HIGH) //NOSONAR
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                    .into(paletteTarget); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void invalidateColors(ColorSet colorSet) { //NOSONAR

        boolean ignorePalette = false; //NOSONAR
        if (!settingsManager.getUsePalette() && !settingsManager.getUsePaletteNowPlayingOnly()) { //NOSONAR
            // If we're not using Palette at all, use non-tinted colors for text. // NOSONAR
            colorSet.setPrimaryTextColorTinted(colorSet.getPrimaryTextColor()); //NOSONAR
            colorSet.setSecondaryTextColorTinted(colorSet.getSecondaryTextColor()); //NOSONAR
            ignorePalette = true; //NOSONAR
        } // NOSONAR

        if (!isLandscape && backgroundView != null) { //NOSONAR
            backgroundView.setBackgroundColor(colorSet.getPrimaryColor()); //NOSONAR
        } // NOSONAR

        if (!isLandscape && currentTime != null) { //NOSONAR
            currentTime.setTextColor(colorSet.getPrimaryTextColor()); //NOSONAR
        } // NOSONAR

        if (!isLandscape && totalTime != null) { //NOSONAR
            totalTime.setTextColor(colorSet.getPrimaryTextColor()); //NOSONAR
        } // NOSONAR

        if (track != null) { //NOSONAR
            track.setTextColor(colorSet.getPrimaryTextColorTinted()); //NOSONAR
        } // NOSONAR

        if (album != null) { //NOSONAR
            album.setTextColor(colorSet.getSecondaryTextColorTinted()); //NOSONAR
        } // NOSONAR

        if (artist != null) { //NOSONAR
            artist.setTextColor(colorSet.getSecondaryTextColorTinted()); //NOSONAR
        } // NOSONAR

        if (seekBar != null) { //NOSONAR
            seekBar.invalidateColors(new ColorIsDarkState(ignorePalette ? colorSet.getAccentColor() : colorSet.getPrimaryTextColorTinted(), false)); //NOSONAR
        } // NOSONAR

        if (shuffleButton != null) { //NOSONAR
            shuffleButton.invalidateColors(colorSet.getPrimaryTextColor(), colorSet.getPrimaryTextColorTinted()); //NOSONAR
        } // NOSONAR

        if (repeatButton != null) { //NOSONAR
            repeatButton.invalidateColors(colorSet.getPrimaryTextColor(), colorSet.getPrimaryTextColorTinted()); //NOSONAR
        } // NOSONAR

        if (prevButton != null) { //NOSONAR
            prevButton.invalidateColors(colorSet.getPrimaryTextColor()); //NOSONAR
        } // NOSONAR

        if (nextButton != null) { //NOSONAR
            nextButton.invalidateColors(colorSet.getPrimaryTextColor()); //NOSONAR
        } // NOSONAR

        if (playPauseView != null) { //NOSONAR
            playPauseView.setDrawableColor(colorSet.getPrimaryTextColor()); //NOSONAR
        } // NOSONAR

        this.colorSet = colorSet; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void showLyricsDialog() { //NOSONAR
        LyricsDialog.Companion.newInstance().show(getChildFragmentManager()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void showUpgradeDialog() { //NOSONAR
        UpgradeDialog.Companion.newInstance().show(getChildFragmentManager()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean onMenuItemClick(MenuItem item) { //NOSONAR
        if (!SongMenuUtils.INSTANCE.getSongMenuClickListener(mediaManager.getSong(), presenter).onMenuItemClick(item)) { //NOSONAR
            switch (item.getItemId()) { //NOSONAR
                case R.id.favorite: //NOSONAR
                    ((FavoriteActionBarView) item.getActionView()).toggle(); //NOSONAR
                    presenter.toggleFavorite(); //NOSONAR
                    return true; //NOSONAR
                case R.id.lyrics: //NOSONAR
                    presenter.showLyrics(); //NOSONAR
                    return true; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        return true; //NOSONAR
    } // NOSONAR

    void animateColors(@NonNull ColorSet from, @NonNull ColorSet to, int duration, @NonNull UnsafeConsumer<ColorSet> consumer, @Nullable UnsafeAction onComplete) { //NOSONAR
        colorAnimator = ValueAnimator.ofFloat(1, 0); //NOSONAR
        colorAnimator.setDuration(duration); //NOSONAR
        colorAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); //NOSONAR
        ArgbEvaluator argbEvaluator = ArgbEvaluator.getInstance(); //NOSONAR
        colorAnimator.addUpdateListener(animator -> { //NOSONAR
            ColorSet colorSet = new ColorSet( //NOSONAR
                    (int) argbEvaluator.evaluate(animator.getAnimatedFraction(), from.getPrimaryColor(), to.getPrimaryColor()), //NOSONAR
                    (int) argbEvaluator.evaluate(animator.getAnimatedFraction(), from.getAccentColor(), to.getAccentColor()), //NOSONAR
                    (int) argbEvaluator.evaluate(animator.getAnimatedFraction(), from.getPrimaryTextColorTinted(), to.getPrimaryTextColorTinted()), //NOSONAR
                    (int) argbEvaluator.evaluate(animator.getAnimatedFraction(), from.getSecondaryTextColorTinted(), to.getSecondaryTextColorTinted()), //NOSONAR
                    (int) argbEvaluator.evaluate(animator.getAnimatedFraction(), from.getPrimaryTextColor(), to.getPrimaryTextColor()), //NOSONAR
                    (int) argbEvaluator.evaluate(animator.getAnimatedFraction(), from.getSecondaryTextColor(), to.getSecondaryTextColor()) //NOSONAR
            ); // NOSONAR
            consumer.accept(colorSet); //NOSONAR
        }); // NOSONAR
        colorAnimator.addListener(new AnimatorListenerAdapter() { //NOSONAR
            @Override //NOSONAR
            public void onAnimationEnd(Animator animation) { //NOSONAR
                animation.removeAllListeners(); //NOSONAR
                if (onComplete != null) { //NOSONAR
                    onComplete.run(); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        }); // NOSONAR
        colorAnimator.start(); //NOSONAR
    } // NOSONAR

    private SimpleTarget<ColorSet> paletteTarget = new SimpleTarget<ColorSet>() { //NOSONAR
        @Override //NOSONAR
        public void onResourceReady(ColorSet newColorSet, GlideAnimation<? super ColorSet> glideAnimation) { //NOSONAR

            if (!isAdded() || getContext() == null) { //NOSONAR
                return; //NOSONAR
            } // NOSONAR

            if (colorSet == newColorSet) { //NOSONAR
                return; //NOSONAR
            } // NOSONAR

            ColorSet oldColorSet = colorSet; //NOSONAR

            animateColors( //NOSONAR
                    oldColorSet, //NOSONAR
                    newColorSet, //NOSONAR
                    800, //NOSONAR
                    intermediateColorSet -> { //NOSONAR

                        if (!isAdded() || getContext() == null) return; //NOSONAR

                        // Update all the colours related to the now playing screen first // NOSONAR
                        invalidateColors(intermediateColorSet); //NOSONAR

                        // We need to update the nav bar colour at the same time, since it's visible as well. // NOSONAR
                        if (settingsManager.getTintNavBar()) { //NOSONAR
                            Aesthetic.get(getContext()).colorNavigationBar(intermediateColorSet.getPrimaryColor()).apply(); //NOSONAR
                        } // NOSONAR
                    }, // NOSONAR
                    () -> { // NOSONAR
                        if (!isAdded() || getContext() == null) return; //NOSONAR

                        // Wait until the first set of color change animations is complete, before updating Aesthetic. // NOSONAR
                        // This allows our invalidateColors() animation to run smoothly, as the Aesthetic color change // NOSONAR
                        // introduces some jank. // NOSONAR
                        if (!settingsManager.getUsePaletteNowPlayingOnly()) { //NOSONAR

                            animateColors(oldColorSet, newColorSet, 450, intermediateColorSet -> { //NOSONAR

                                if (!isAdded() || getContext() == null) return; //NOSONAR

                                Aesthetic.get(getContext()) //NOSONAR
                                        .colorPrimary(intermediateColorSet.getPrimaryColor()) //NOSONAR
                                        .colorAccent(intermediateColorSet.getAccentColor()) //NOSONAR
                                        .colorStatusBarAuto().apply(); //NOSONAR
                            }, null); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
            ); // NOSONAR
        } // NOSONAR

        @SuppressLint("CheckResult") //NOSONAR
        @Override //NOSONAR
        public void onLoadFailed(Exception e, Drawable errorDrawable) { //NOSONAR
            super.onLoadFailed(e, errorDrawable); //NOSONAR

            getAestheticColorSetDisposable() //NOSONAR
                    .take(1) //NOSONAR
                    .subscribe( //NOSONAR
                            colorSet -> animateColors(PlayerFragment.this.colorSet, colorSet, 800, intermediateColorSet -> invalidateColors(intermediateColorSet), null), //NOSONAR
                            error -> { //NOSONAR
                                // Nothing ot do // NOSONAR
                            } // NOSONAR
                    ); // NOSONAR
        } // NOSONAR
    }; // NOSONAR

    private Observable<ColorSet> getAestheticColorSetDisposable() { //NOSONAR
        return Observable.combineLatest( //NOSONAR
                Aesthetic.get(getContext()).colorPrimary(), //NOSONAR
                Aesthetic.get(getContext()).colorAccent(), //NOSONAR
                Pair::new //NOSONAR
        ).map(pair -> ColorSet.Companion.fromPrimaryAccentColors(getContext(), pair.first, pair.second)); //NOSONAR
    } // NOSONAR

    // SongMenuContract.View implementation // NOSONAR

    @Override //NOSONAR
    public void presentCreatePlaylistDialog(@NotNull List<? extends Song> songs) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void presentSongInfoDialog(@NotNull Song song) { //NOSONAR
        SongInfoDialog.Companion.newInstance(song).show(getChildFragmentManager()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onSongsAddedToPlaylist(@NotNull Playlist playlist, int numSongs) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onSongsAddedToQueue(int numSongs) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void presentTagEditorDialog(@NotNull Song song) { //NOSONAR
        TaggerDialog.newInstance(song).show(getChildFragmentManager()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void presentDeleteDialog(@NotNull List<? extends Song> songs) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void shareSong(@NotNull Song song) { //NOSONAR
        ShareDialog.Companion.newInstance(song).show(getChildFragmentManager()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void presentRingtonePermissionDialog() { //NOSONAR
        RingtoneManager.Companion.getDialog(getContext()).show(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void showRingtoneSetMessage() { //NOSONAR
        Toast.makeText(getContext(), R.string.ringtone_set_new, Toast.LENGTH_SHORT).show(); //NOSONAR
    } // NOSONAR
} // NOSONAR
