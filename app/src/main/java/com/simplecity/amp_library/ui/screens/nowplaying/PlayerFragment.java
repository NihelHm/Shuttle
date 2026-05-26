package com.simplecity.amp_library.ui.screens.nowplaying;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.ColorIsDarkState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.jakewharton.rxbinding2.widget.RxSeekBar;
import com.jakewharton.rxbinding2.widget.SeekBarChangeEvent;
import com.jakewharton.rxbinding2.widget.SeekBarProgressChangeEvent;
import com.jakewharton.rxbinding2.widget.SeekBarStartChangeEvent;
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent;
import com.jp.wasabeef.glide.transformations.BlurTransformation;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.cast.CastManager;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.glide.palette.ColorSet;
import com.simplecity.amp_library.glide.palette.ColorSetTranscoder;
import com.simplecity.amp_library.model.Playlist;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.playback.QueueManager;
import com.simplecity.amp_library.rx.UnsafeAction;
import com.simplecity.amp_library.rx.UnsafeConsumer;
import com.simplecity.amp_library.ui.common.BaseFragment;
import com.simplecity.amp_library.ui.dialog.ShareDialog;
import com.simplecity.amp_library.ui.dialog.SongInfoDialog;
import com.simplecity.amp_library.ui.dialog.UpgradeDialog;
import com.simplecity.amp_library.ui.screens.drawer.NavigationEventRelay;
import com.simplecity.amp_library.ui.screens.lyrics.LyricsDialog;
import com.simplecity.amp_library.ui.screens.queue.pager.QueuePagerFragment;
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog;
import com.simplecity.amp_library.ui.views.FavoriteActionBarView;
import com.simplecity.amp_library.ui.views.PlayPauseView;
import com.simplecity.amp_library.ui.views.RepeatButton;
import com.simplecity.amp_library.ui.views.RepeatingImageButton;
import com.simplecity.amp_library.ui.views.ShuffleButton;
import com.simplecity.amp_library.ui.views.SizableSeekBar;
import com.simplecity.amp_library.ui.views.SnowfallView;
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetSlideEventRelay;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.PlaceholderProvider;
import com.simplecity.amp_library.utils.RingtoneManager;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;
import com.simplecity.amp_library.utils.StringUtils;
import com.simplecity.amp_library.utils.color.ArgbEvaluator;
import com.simplecity.amp_library.utils.menu.song.SongMenuUtils;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

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
        // Intentionally left empty.
    }

    public static PlayerFragment newInstance() { //NOSONAR
        PlayerFragment playerFragment = new PlayerFragment(); //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        playerFragment.setArguments(args); //NOSONAR
        return playerFragment; //NOSONAR
    }

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR
    }

    @Override //NOSONAR
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //NOSONAR
        return inflater.inflate(R.layout.fragment_player, container, false); //NOSONAR
    }

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
        }

        MenuItem favoriteMenuItem = toolbar.getMenu().findItem(R.id.favorite); //NOSONAR
        FavoriteActionBarView menuActionView = (FavoriteActionBarView) favoriteMenuItem.getActionView(); //NOSONAR
        menuActionView.setOnClickListener(v -> onMenuItemClick(favoriteMenuItem)); //NOSONAR
        toolbar.setOnMenuItemClickListener(this); //NOSONAR

        if (playPauseView != null) { //NOSONAR
            playPauseView.setOnClickListener(v -> playPauseView.toggle(() -> { //NOSONAR
                presenter.togglePlayback(); //NOSONAR
                return Unit.INSTANCE; //NOSONAR
            }));
        }

        if (repeatButton != null) { //NOSONAR
            repeatButton.setOnClickListener(v -> presenter.toggleRepeat()); //NOSONAR
            repeatButton.setTag(":aesthetic_ignore"); //NOSONAR
        }

        if (shuffleButton != null) { //NOSONAR
            shuffleButton.setOnClickListener(v -> presenter.toggleShuffle()); //NOSONAR
            shuffleButton.setTag(":aesthetic_ignore"); //NOSONAR
        }

        if (nextButton != null) { //NOSONAR
            nextButton.setOnClickListener(v -> presenter.skip()); //NOSONAR
            nextButton.setRepeatListener((v, duration, repeatCount) -> presenter.scanForward(repeatCount, duration)); //NOSONAR
        }

        if (prevButton != null) { //NOSONAR
            prevButton.setOnClickListener(v -> presenter.prev(false)); //NOSONAR
            prevButton.setRepeatListener((v, duration, repeatCount) -> presenter.scanBackward(repeatCount, duration)); //NOSONAR
        }

        if (seekBar != null) { //NOSONAR
            seekBar.setMax(1000); //NOSONAR
        }

        if (savedInstanceState == null) { //NOSONAR
            getChildFragmentManager().beginTransaction() //NOSONAR
                    .add(R.id.main_container, QueuePagerFragment.newInstance(), "QueuePagerFragment") //NOSONAR
                    .commit(); //NOSONAR
        }

        getAestheticColorSetDisposable() //NOSONAR
                .take(1) //NOSONAR
                .subscribe( //NOSONAR
                        this::invalidateColors, //NOSONAR
                        error -> { //NOSONAR
                            // Nothing to do
                        }
                );

        presenter.bindView(this); //NOSONAR
    }

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        if (target != null) { //NOSONAR
            Glide.clear(target); //NOSONAR
        }
        snowfallView.clear(); //NOSONAR

        if (colorAnimator != null) { //NOSONAR
            colorAnimator.cancel(); //NOSONAR
        }

        presenter.unbindView(this); //NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    }

    public void update() { //NOSONAR
        if (presenter != null) { //NOSONAR
            presenter.updateTrackInfo(); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        if (!settingsManager.getUsePalette() && !settingsManager.getUsePaletteNowPlayingOnly()) { //NOSONAR
            disposables.add(getAestheticColorSetDisposable().subscribe( //NOSONAR
                    colorSet -> animateColors(PlayerFragment.this.colorSet, colorSet, 800, this::invalidateColors, null), //NOSONAR
                    error -> { //NOSONAR
                        // Nothing to do
                    })
            );
        }

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
                        }
                    },
                    error -> LogUtils.logException(TAG, "Error in seek change event", error)) //NOSONAR
            );

            disposables.add(sharedSeekBarEvents //NOSONAR
                    .ofType(SeekBarProgressChangeEvent.class) //NOSONAR
                    .filter(SeekBarProgressChangeEvent::fromUser) //NOSONAR
                    .debounce(15, TimeUnit.MILLISECONDS) //NOSONAR
                    .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                    .subscribe( //NOSONAR
                            seekBarChangeEvent -> presenter.seekTo(seekBarChangeEvent.progress()), //NOSONAR
                            error -> LogUtils.logException(TAG, "Error receiving seekbar progress", error)) //NOSONAR
            );
        }

        disposables.add(RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(getContext())) //NOSONAR
                .getBoolean(SettingsManager.KEY_DISPLAY_REMAINING_TIME) //NOSONAR
                .asObservable() //NOSONAR
                .subscribe( //NOSONAR
                        aBoolean -> presenter.updateRemainingTime(), //NOSONAR
                        error -> LogUtils.logException(TAG, "Remaining time changed", error) //NOSONAR
                )
        );

        disposables.add(sheetEventRelay.getEvents() //NOSONAR
                .subscribe( //NOSONAR
                        event -> { //NOSONAR
                            if (event.nowPlayingExpanded()) { //NOSONAR
                                isExpanded = true; //NOSONAR
                                snowfallView.letItSnow(analyticsManager); //NOSONAR
                            } else if (event.nowPlayingCollapsed()) { //NOSONAR
                                isExpanded = false; //NOSONAR
                                snowfallView.clear(); //NOSONAR
                            }
                        },
                        throwable -> Log.e(TAG, "error listening for sheet slide events", throwable)) //NOSONAR
        );

        update(); //NOSONAR
    }

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        disposables.clear(); //NOSONAR
        super.onPause(); //NOSONAR
    }

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    }

    // View implementation

    @Override //NOSONAR
    public void setSeekProgress(int progress) { //NOSONAR
        if (!isSeeking && seekBar != null) { //NOSONAR
            seekBar.setProgress(progress); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void currentTimeVisibilityChanged(boolean visible) { //NOSONAR
        if (currentTime != null) { //NOSONAR
            currentTime.setVisibility(visible ? View.VISIBLE : View.INVISIBLE); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void currentTimeChanged(long seconds) { //NOSONAR
        if (currentTime != null) { //NOSONAR
            currentTime.setText(StringUtils.makeTimeString(getContext(), seconds)); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void totalTimeChanged(long seconds) { //NOSONAR
        if (totalTime != null) { //NOSONAR
            totalTime.setText(StringUtils.makeTimeString(getContext(), seconds)); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void queueChanged(int queuePosition, int queueLength) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void playbackChanged(boolean isPlaying) { //NOSONAR
        if (playPauseView != null) { //NOSONAR
            if (isPlaying) { //NOSONAR
                if (playPauseView.isPlay()) { //NOSONAR
                    playPauseView.toggle(null); //NOSONAR
                    playPauseView.setContentDescription(getString(R.string.btn_pause)); //NOSONAR
                }
            } else { //NOSONAR
                if (!playPauseView.isPlay()) { //NOSONAR
                    playPauseView.toggle(null); //NOSONAR
                    playPauseView.setContentDescription(getString(R.string.btn_play)); //NOSONAR
                }
            }
        }

        if (!isPlaying) { //NOSONAR
            snowfallView.removeSnow(); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void shuffleChanged(@QueueManager.ShuffleMode int shuffleMode) { //NOSONAR
        if (shuffleButton != null) { //NOSONAR
            shuffleButton.setShuffleMode(shuffleMode); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void repeatChanged(@QueueManager.RepeatMode int repeatMode) { //NOSONAR
        if (repeatButton != null) { //NOSONAR
            repeatButton.setRepeatMode(repeatMode); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void favoriteChanged(boolean isFavorite) { //NOSONAR
        FavoriteActionBarView favoriteActionBarView = (FavoriteActionBarView) toolbar.getMenu().findItem(R.id.favorite).getActionView(); //NOSONAR
        favoriteActionBarView.setIsFavorite(isFavorite); //NOSONAR
    }

    Song song = null; //NOSONAR

    @Override //NOSONAR
    public void trackInfoChanged(@Nullable Song song) { //NOSONAR

        if (song == null) return; //NOSONAR

        if (isExpanded && !snowfallView.isSnowing()) { //NOSONAR
            snowfallView.letItSnow(analyticsManager); //NOSONAR
        } else { //NOSONAR
            snowfallView.removeSnow(); //NOSONAR
        }

        String totalTimeString = StringUtils.makeTimeString(getContext(), song.duration / 1000); //NOSONAR
        if (!TextUtils.isEmpty(totalTimeString)) { //NOSONAR
            if (totalTime != null) { //NOSONAR
                totalTime.setText(totalTimeString); //NOSONAR
            }
        }

        if (track != null) { //NOSONAR
            track.setText(song.name); //NOSONAR
            track.setSelected(true); //NOSONAR
        }
        if (album != null) { //NOSONAR
            album.setText(String.format("%s • %s", song.artistName, song.albumName)); //NOSONAR
        }

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
        }

        if (settingsManager.getUsePalette()) { //NOSONAR

            if (paletteTarget != null) { //NOSONAR
                Glide.clear(paletteTarget); //NOSONAR
            }

            Glide.with(this) //NOSONAR
                    .load(song) //NOSONAR
                    .asBitmap() //NOSONAR
                    .transcode(new ColorSetTranscoder(getContext()), ColorSet.class) //NOSONAR
                    .override(250, 250) //NOSONAR
                    .priority(Priority.HIGH) //NOSONAR
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                    .into(paletteTarget); //NOSONAR
        }
    }

    void invalidateColors(ColorSet colorSet) { //NOSONAR

        boolean ignorePalette = false; //NOSONAR
        if (!settingsManager.getUsePalette() && !settingsManager.getUsePaletteNowPlayingOnly()) { //NOSONAR
            // If we're not using Palette at all, use non-tinted colors for text.
            colorSet.setPrimaryTextColorTinted(colorSet.getPrimaryTextColor()); //NOSONAR
            colorSet.setSecondaryTextColorTinted(colorSet.getSecondaryTextColor()); //NOSONAR
            ignorePalette = true; //NOSONAR
        }

        if (!isLandscape && backgroundView != null) { //NOSONAR
            backgroundView.setBackgroundColor(colorSet.getPrimaryColor()); //NOSONAR
        }

        if (!isLandscape && currentTime != null) { //NOSONAR
            currentTime.setTextColor(colorSet.getPrimaryTextColor()); //NOSONAR
        }

        if (!isLandscape && totalTime != null) { //NOSONAR
            totalTime.setTextColor(colorSet.getPrimaryTextColor()); //NOSONAR
        }

        if (track != null) { //NOSONAR
            track.setTextColor(colorSet.getPrimaryTextColorTinted()); //NOSONAR
        }

        if (album != null) { //NOSONAR
            album.setTextColor(colorSet.getSecondaryTextColorTinted()); //NOSONAR
        }

        if (artist != null) { //NOSONAR
            artist.setTextColor(colorSet.getSecondaryTextColorTinted()); //NOSONAR
        }

        if (seekBar != null) { //NOSONAR
            seekBar.invalidateColors(new ColorIsDarkState(ignorePalette ? colorSet.getAccentColor() : colorSet.getPrimaryTextColorTinted(), false)); //NOSONAR
        }

        if (shuffleButton != null) { //NOSONAR
            shuffleButton.invalidateColors(colorSet.getPrimaryTextColor(), colorSet.getPrimaryTextColorTinted()); //NOSONAR
        }

        if (repeatButton != null) { //NOSONAR
            repeatButton.invalidateColors(colorSet.getPrimaryTextColor(), colorSet.getPrimaryTextColorTinted()); //NOSONAR
        }

        if (prevButton != null) { //NOSONAR
            prevButton.invalidateColors(colorSet.getPrimaryTextColor()); //NOSONAR
        }

        if (nextButton != null) { //NOSONAR
            nextButton.invalidateColors(colorSet.getPrimaryTextColor()); //NOSONAR
        }

        if (playPauseView != null) { //NOSONAR
            playPauseView.setDrawableColor(colorSet.getPrimaryTextColor()); //NOSONAR
        }

        this.colorSet = colorSet; //NOSONAR
    }

    @Override //NOSONAR
    public void showLyricsDialog() { //NOSONAR
        LyricsDialog.Companion.newInstance().show(getChildFragmentManager()); //NOSONAR
    }

    @Override //NOSONAR
    public void showUpgradeDialog() { //NOSONAR
        UpgradeDialog.Companion.newInstance().show(getChildFragmentManager()); //NOSONAR
    }

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
            }
        }

        return true; //NOSONAR
    }

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
            );
            consumer.accept(colorSet); //NOSONAR
        });
        colorAnimator.addListener(new AnimatorListenerAdapter() { //NOSONAR
            @Override //NOSONAR
            public void onAnimationEnd(Animator animation) { //NOSONAR
                animation.removeAllListeners(); //NOSONAR
                if (onComplete != null) { //NOSONAR
                    onComplete.run(); //NOSONAR
                }
            }
        });
        colorAnimator.start(); //NOSONAR
    }

    private SimpleTarget<ColorSet> paletteTarget = new SimpleTarget<ColorSet>() { //NOSONAR
        @Override //NOSONAR
        public void onResourceReady(ColorSet newColorSet, GlideAnimation<? super ColorSet> glideAnimation) { //NOSONAR

            if (!isAdded() || getContext() == null) { //NOSONAR
                return; //NOSONAR
            }

            if (colorSet == newColorSet) { //NOSONAR
                return; //NOSONAR
            }

            ColorSet oldColorSet = colorSet; //NOSONAR

            animateColors( //NOSONAR
                    oldColorSet, //NOSONAR
                    newColorSet, //NOSONAR
                    800, //NOSONAR
                    intermediateColorSet -> { //NOSONAR

                        if (!isAdded() || getContext() == null) return; //NOSONAR

                        // Update all the colours related to the now playing screen first
                        invalidateColors(intermediateColorSet); //NOSONAR

                        // We need to update the nav bar colour at the same time, since it's visible as well.
                        if (settingsManager.getTintNavBar()) { //NOSONAR
                            Aesthetic.get(getContext()).colorNavigationBar(intermediateColorSet.getPrimaryColor()).apply(); //NOSONAR
                        }
                    },
                    () -> {
                        if (!isAdded() || getContext() == null) return; //NOSONAR

                        // Wait until the first set of color change animations is complete, before updating Aesthetic.
                        // This allows our invalidateColors() animation to run smoothly, as the Aesthetic color change
                        // introduces some jank.
                        if (!settingsManager.getUsePaletteNowPlayingOnly()) { //NOSONAR

                            animateColors(oldColorSet, newColorSet, 450, intermediateColorSet -> { //NOSONAR

                                if (!isAdded() || getContext() == null) return; //NOSONAR

                                Aesthetic.get(getContext()) //NOSONAR
                                        .colorPrimary(intermediateColorSet.getPrimaryColor()) //NOSONAR
                                        .colorAccent(intermediateColorSet.getAccentColor()) //NOSONAR
                                        .colorStatusBarAuto().apply(); //NOSONAR
                            }, null); //NOSONAR
                        }
                    }
            );
        }

        @SuppressLint("CheckResult") //NOSONAR
        @Override //NOSONAR
        public void onLoadFailed(Exception e, Drawable errorDrawable) { //NOSONAR
            super.onLoadFailed(e, errorDrawable); //NOSONAR

            getAestheticColorSetDisposable() //NOSONAR
                    .take(1) //NOSONAR
                    .subscribe( //NOSONAR
                            colorSet -> animateColors(PlayerFragment.this.colorSet, colorSet, 800, intermediateColorSet -> invalidateColors(intermediateColorSet), null), //NOSONAR
                            error -> { //NOSONAR
                                // Nothing ot do
                            }
                    );
        }
    };

    private Observable<ColorSet> getAestheticColorSetDisposable() { //NOSONAR
        return Observable.combineLatest( //NOSONAR
                Aesthetic.get(getContext()).colorPrimary(), //NOSONAR
                Aesthetic.get(getContext()).colorAccent(), //NOSONAR
                Pair::new //NOSONAR
        ).map(pair -> ColorSet.Companion.fromPrimaryAccentColors(getContext(), pair.first, pair.second)); //NOSONAR
    }

    // SongMenuContract.View implementation

    @Override //NOSONAR
    public void presentCreatePlaylistDialog(@NotNull List<? extends Song> songs) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void presentSongInfoDialog(@NotNull Song song) { //NOSONAR
        SongInfoDialog.Companion.newInstance(song).show(getChildFragmentManager()); //NOSONAR
    }

    @Override //NOSONAR
    public void onSongsAddedToPlaylist(@NotNull Playlist playlist, int numSongs) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onSongsAddedToQueue(int numSongs) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void presentTagEditorDialog(@NotNull Song song) { //NOSONAR
        TaggerDialog.newInstance(song).show(getChildFragmentManager()); //NOSONAR
    }

    @Override //NOSONAR
    public void presentDeleteDialog(@NotNull List<? extends Song> songs) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void shareSong(@NotNull Song song) { //NOSONAR
        ShareDialog.Companion.newInstance(song).show(getChildFragmentManager()); //NOSONAR
    }

    @Override //NOSONAR
    public void presentRingtonePermissionDialog() { //NOSONAR
        RingtoneManager.Companion.getDialog(getContext()).show(); //NOSONAR
    }

    @Override //NOSONAR
    public void showRingtoneSetMessage() { //NOSONAR
        Toast.makeText(getContext(), R.string.ringtone_set_new, Toast.LENGTH_SHORT).show(); //NOSONAR
    }
}
