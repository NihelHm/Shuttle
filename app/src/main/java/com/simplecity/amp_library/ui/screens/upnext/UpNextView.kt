@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.upnext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.util.Pair
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import com.afollestad.aesthetic.Aesthetic
import com.afollestad.aesthetic.ColorIsDarkState
import com.afollestad.aesthetic.LightDarkColorState
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.SeekBarChangeEvent
import com.jakewharton.rxbinding2.widget.SeekBarProgressChangeEvent
import com.jakewharton.rxbinding2.widget.SeekBarStartChangeEvent
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent
import com.simplecity.amp_library.R
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.glide.palette.ColorSet
import com.simplecity.amp_library.glide.palette.ColorSetTranscoder
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.rx.UnsafeAction
import com.simplecity.amp_library.rx.UnsafeConsumer
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerPresenter
import com.simplecity.amp_library.ui.views.PlayerViewAdapter
import com.simplecity.amp_library.ui.views.RepeatButton
import com.simplecity.amp_library.ui.views.ShuffleButton
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.ShuttleUtils
import com.simplecity.amp_library.utils.color.ArgbEvaluator
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.up_next_view.view.arrowImageView
import kotlinx.android.synthetic.main.up_next_view.view.buttonContainer
import kotlinx.android.synthetic.main.up_next_view.view.nextButton
import kotlinx.android.synthetic.main.up_next_view.view.playPauseView
import kotlinx.android.synthetic.main.up_next_view.view.prevButton
import kotlinx.android.synthetic.main.up_next_view.view.queuePositionTextView
import kotlinx.android.synthetic.main.up_next_view.view.queueTextView
import kotlinx.android.synthetic.main.up_next_view.view.repeatButton
import kotlinx.android.synthetic.main.up_next_view.view.seekBar
import kotlinx.android.synthetic.main.up_next_view.view.shuffleButton
import kotlinx.android.synthetic.main.up_next_view.view.textContainer
import java.util.concurrent.TimeUnit

class UpNextView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) { //NOSONAR

    private lateinit var playerPresenter: PlayerPresenter //NOSONAR

    private lateinit var settingsManager: SettingsManager //NOSONAR

    private var isSeeking: Boolean = false //NOSONAR

    private val arrowDrawable: Drawable //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    private val playerViewAdapter: PlayerViewAdapter //NOSONAR

    private var isLandscape: Boolean = false //NOSONAR

    private var colorAnimator: ValueAnimator? = null //NOSONAR

    private var colorSet = ColorSet.empty() //NOSONAR

    init { //NOSONAR
        orientation = LinearLayout.HORIZONTAL //NOSONAR

        View.inflate(context, R.layout.up_next_view, this) //NOSONAR

        arrowDrawable = DrawableCompat.wrap(arrowImageView.drawable) //NOSONAR
        arrowImageView.setImageDrawable(arrowDrawable) //NOSONAR

        playPauseView?.setOnClickListener { v -> //NOSONAR
            playPauseView?.toggle { //NOSONAR
                playerPresenter.togglePlayback() //NOSONAR
            }
        }

        repeatButton?.setOnClickListener { playerPresenter.toggleRepeat() } //NOSONAR
        repeatButton?.tag = ":aesthetic_ignore" //NOSONAR

        shuffleButton?.setOnClickListener { playerPresenter.toggleShuffle() } //NOSONAR
        shuffleButton?.tag = ":aesthetic_ignore" //NOSONAR

        nextButton?.setOnClickListener { playerPresenter.skip() } //NOSONAR
        nextButton?.setRepeatListener { _, duration, repeatCount -> playerPresenter.scanForward(repeatCount, duration) } //NOSONAR

        prevButton?.setOnClickListener { playerPresenter.prev(false) } //NOSONAR
        prevButton?.setRepeatListener { _, duration, repeatCount -> playerPresenter.scanBackward(repeatCount, duration) } //NOSONAR

        seekBar?.max = 1000 //NOSONAR

        playerViewAdapter = object : PlayerViewAdapter() { //NOSONAR

            override fun queueChanged(queuePosition: Int, queueLength: Int) { //NOSONAR
                super.queueChanged(queuePosition, queueLength) //NOSONAR

                queuePositionTextView.text = String.format("%d / %d", queuePosition, queueLength) //NOSONAR
            }

            override fun playbackChanged(isPlaying: Boolean) { //NOSONAR
                playPauseView?.let { playPauseView -> //NOSONAR
                    if (isPlaying) { //NOSONAR
                        if (playPauseView.isPlay) { //NOSONAR
                            playPauseView.toggle(null) //NOSONAR
                            playPauseView.contentDescription = getContext().getString(R.string.btn_pause) //NOSONAR
                        }
                    } else { //NOSONAR
                        if (!playPauseView.isPlay) { //NOSONAR
                            playPauseView.toggle(null) //NOSONAR
                            playPauseView.contentDescription = getContext().getString(R.string.btn_play) //NOSONAR
                        }
                    }
                }
            }

            override fun shuffleChanged(shuffleMode: Int) { //NOSONAR
                (shuffleButton as? ShuffleButton)?.setShuffleMode(shuffleMode) //NOSONAR
            }

            override fun repeatChanged(repeatMode: Int) { //NOSONAR
                (repeatButton as? RepeatButton)?.setRepeatMode(repeatMode) //NOSONAR
            }

            override fun trackInfoChanged(song: Song?) { //NOSONAR
                super.trackInfoChanged(song) //NOSONAR

                if (isLandscape && settingsManager.usePalette) { //NOSONAR

                    Glide.clear(paletteTarget) //NOSONAR

                    Glide.with(getContext()) //NOSONAR
                        .load(song) //NOSONAR
                        .asBitmap() //NOSONAR
                        .transcode(ColorSetTranscoder(getContext()), ColorSet::class.java) //NOSONAR
                        .override(250, 250) //NOSONAR
                        .priority(Priority.HIGH) //NOSONAR
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                        .into(paletteTarget) //NOSONAR
                }
            }

            override fun setSeekProgress(progress: Int) { //NOSONAR
                if (!isSeeking) { //NOSONAR
                    seekBar?.progress = progress //NOSONAR
                }
            }
        }
    }

    override fun onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow() //NOSONAR

        isLandscape = ShuttleUtils.isLandscape(context.applicationContext as ShuttleApplication) //NOSONAR

        playerPresenter.bindView(playerViewAdapter) //NOSONAR

        if (!isLandscape) { //NOSONAR

            disposables.add( //NOSONAR
                Observable.combineLatest( //NOSONAR
                    Aesthetic.get(context).textColorPrimary(), //NOSONAR
                    Aesthetic.get(context).textColorPrimaryInverse(), //NOSONAR
                    Observable.just(false), //NOSONAR
                    LightDarkColorState.creator() //NOSONAR
                )
                    .subscribe { colorState -> //NOSONAR
                        DrawableCompat.setTint(arrowDrawable, colorState.color()) //NOSONAR
                    })

        }

        disposables.add(Aesthetic.get(context).isDark //NOSONAR
            .map { isDark -> isLandscape && !isDark } //NOSONAR
            .flatMap { isDark -> if (isDark) Aesthetic.get(context).textColorPrimaryInverse() else Aesthetic.get(context).textColorPrimary() } //NOSONAR
            .subscribe { color -> queueTextView.setTextColor(color) }) //NOSONAR

        disposables.add(Aesthetic.get(context).isDark //NOSONAR
            .map { isDark -> isLandscape && !isDark } //NOSONAR
            .flatMap { isDark -> if (isDark) Aesthetic.get(context).textColorSecondaryInverse() else Aesthetic.get(context).textColorSecondary() } //NOSONAR
            .subscribe { color -> queuePositionTextView.setTextColor(color) }) //NOSONAR

        if (isLandscape) { //NOSONAR

            var observable = getAestheticColorSetDisposable() //NOSONAR
            // If we're managing the color scheme ourselves based on artwork changes, we only need the first ColorSet
            // emission, as no artwork has been loaded yet.
            if (settingsManager.usePalette || settingsManager.usePaletteNowPlayingOnly) { //NOSONAR
                observable = observable.take(1) //NOSONAR
            }

            disposables.add( //NOSONAR
                observable.subscribe( //NOSONAR
                    { colorSet -> //NOSONAR
                        if (this@UpNextView.colorSet == colorSet) { //NOSONAR
                            invalidateColors(colorSet) //NOSONAR
                        } else { //NOSONAR
                            animateColors(this@UpNextView.colorSet, colorSet, 800, UnsafeConsumer { this.invalidateColors(it) }, null) //NOSONAR
                        }
                    },
                    { _ ->
                        // Nothing to do
                    })
            )
        }

        seekBar?.let { seekBar -> //NOSONAR
            val sharedSeekBarEvents = RxSeekBar.changeEvents(seekBar) //NOSONAR
                .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
                .ofType(SeekBarChangeEvent::class.java) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .share() //NOSONAR

            disposables.add(sharedSeekBarEvents.subscribe({ seekBarChangeEvent -> //NOSONAR
                if (seekBarChangeEvent is SeekBarStartChangeEvent) { //NOSONAR
                    isSeeking = true //NOSONAR
                } else if (seekBarChangeEvent is SeekBarStopChangeEvent) { //NOSONAR
                    isSeeking = false //NOSONAR
                }
            }, { error -> LogUtils.logException(TAG, "Error in seek change event", error) })) //NOSONAR

            disposables.add( //NOSONAR
                sharedSeekBarEvents //NOSONAR
                    .ofType(SeekBarProgressChangeEvent::class.java) //NOSONAR
                    .filter { it.fromUser() } //NOSONAR
                    .debounce(15, TimeUnit.MILLISECONDS) //NOSONAR
                    .subscribe({ seekBarChangeEvent -> playerPresenter.seekTo(seekBarChangeEvent.progress()) }, //NOSONAR
                        { error -> LogUtils.logException(TAG, "Error receiving seekbar progress", error) }) //NOSONAR
            )
        }
    }

    override fun onDetachedFromWindow() { //NOSONAR
        super.onDetachedFromWindow() //NOSONAR

        playerPresenter.unbindView(playerViewAdapter) //NOSONAR

        disposables.dispose() //NOSONAR
    }

    fun invalidateColors(colorSet: ColorSet) { //NOSONAR

        if (isLandscape) { //NOSONAR

            var ignorePalette = false //NOSONAR
            if (!settingsManager.usePalette && !settingsManager.usePaletteNowPlayingOnly) { //NOSONAR
                // If we're not using Palette at all, use non-tinted colors for text.
                colorSet.primaryTextColorTinted = colorSet.primaryTextColor //NOSONAR
                colorSet.secondaryTextColorTinted = colorSet.secondaryTextColor //NOSONAR
                ignorePalette = true //NOSONAR
            }

            buttonContainer?.setBackgroundColor(colorSet.primaryColor) //NOSONAR
            textContainer?.setBackgroundColor(colorSet.primaryColor) //NOSONAR

            (shuffleButton as? ShuffleButton)?.invalidateColors(colorSet.primaryTextColor, colorSet.primaryTextColorTinted) //NOSONAR

            (repeatButton as? RepeatButton)?.invalidateColors(colorSet.primaryTextColor, colorSet.primaryTextColorTinted) //NOSONAR

            prevButton?.invalidateColors(colorSet.primaryTextColor) //NOSONAR

            nextButton?.invalidateColors(colorSet.primaryTextColor) //NOSONAR

            playPauseView?.setDrawableColor(colorSet.primaryTextColor) //NOSONAR

            seekBar?.invalidateColors(ColorIsDarkState(if (ignorePalette) colorSet.accentColor else colorSet.primaryTextColorTinted, false)) //NOSONAR

            queueTextView?.setTextColor(colorSet.primaryTextColor) //NOSONAR

            queuePositionTextView?.setTextColor(colorSet.secondaryTextColor) //NOSONAR

            DrawableCompat.setTint(arrowDrawable, colorSet.primaryTextColor) //NOSONAR

            arrowDrawable //NOSONAR
        }

        this.colorSet = colorSet //NOSONAR
    }

    private fun animateColors(from: ColorSet, to: ColorSet, duration: Int, consumer: UnsafeConsumer<ColorSet>, onComplete: UnsafeAction?) { //NOSONAR
        colorAnimator = ValueAnimator.ofFloat(1f, 0f) //NOSONAR
        colorAnimator!!.duration = duration.toLong() //NOSONAR
        colorAnimator!!.interpolator = AccelerateDecelerateInterpolator() //NOSONAR
        val argbEvaluator = ArgbEvaluator.getInstance() //NOSONAR
        colorAnimator!!.addUpdateListener { animator -> //NOSONAR
            val colorSet = ColorSet( //NOSONAR
                argbEvaluator.evaluate(animator.animatedFraction, from.primaryColor, to.primaryColor) as Int, //NOSONAR
                argbEvaluator.evaluate(animator.animatedFraction, from.accentColor, to.accentColor) as Int, //NOSONAR
                argbEvaluator.evaluate(animator.animatedFraction, from.primaryTextColorTinted, to.primaryTextColorTinted) as Int, //NOSONAR
                argbEvaluator.evaluate(animator.animatedFraction, from.secondaryTextColorTinted, to.secondaryTextColorTinted) as Int, //NOSONAR
                argbEvaluator.evaluate(animator.animatedFraction, from.primaryTextColor, to.primaryTextColor) as Int, //NOSONAR
                argbEvaluator.evaluate(animator.animatedFraction, from.secondaryTextColor, to.secondaryTextColor) as Int //NOSONAR
            )
            consumer.accept(colorSet) //NOSONAR
        }
        colorAnimator!!.addListener(object : AnimatorListenerAdapter() { //NOSONAR
            override fun onAnimationEnd(animation: Animator) { //NOSONAR
                animation.removeAllListeners() //NOSONAR
                onComplete?.run() //NOSONAR
            }
        })
        colorAnimator!!.start() //NOSONAR
    }

    private fun getAestheticColorSetDisposable(): Observable<ColorSet> { //NOSONAR
        return Observable.combineLatest( //NOSONAR
            Aesthetic.get(context).colorPrimary(), //NOSONAR
            Aesthetic.get(context).colorAccent(), //NOSONAR
            BiFunction { first: Int, second: Int -> Pair(first, second) } //NOSONAR
        ).map { pair -> ColorSet.fromPrimaryAccentColors(context!!, pair.first!!, pair.second!!) } //NOSONAR
    }

    private val paletteTarget = object : SimpleTarget<ColorSet>() { //NOSONAR
        override fun onResourceReady(newColorSet: ColorSet, glideAnimation: GlideAnimation<in ColorSet>) { //NOSONAR

            if (colorSet === newColorSet) { //NOSONAR
                return //NOSONAR
            }

            val oldColorSet = colorSet //NOSONAR

            animateColors( //NOSONAR
                oldColorSet, //NOSONAR
                newColorSet, //NOSONAR
                800, //NOSONAR
                UnsafeConsumer { intermediateColorSet -> //NOSONAR
                    // Update all the colours related to the now playing screen first
                    invalidateColors(intermediateColorSet) //NOSONAR

                    // We need to update the nav bar colour at the same time, since it's visible as well.
                    if (settingsManager.tintNavBar) { //NOSONAR
                        Aesthetic.get(getContext()).colorNavigationBar(intermediateColorSet.primaryColor).apply() //NOSONAR
                    }
                },
                UnsafeAction { //NOSONAR
                    // Wait until the first set of color change animations is complete, before updating Aesthetic.
                    // This allows our invalidateColors() animation to run smoothly, as the Aesthetic color change
                    // introduces some jank.
                    if (!settingsManager.usePaletteNowPlayingOnly) { //NOSONAR

                        animateColors(oldColorSet, newColorSet, 450, UnsafeConsumer { intermediateColorSet -> //NOSONAR
                            val aesthetic = Aesthetic.get(getContext()) //NOSONAR
                                .colorPrimary(intermediateColorSet.primaryColor) //NOSONAR
                                .colorAccent(intermediateColorSet.accentColor) //NOSONAR
                                .colorStatusBarAuto() //NOSONAR

                            aesthetic.apply() //NOSONAR
                        }, null) //NOSONAR
                    }
                }
            )
        }

        @SuppressLint("CheckResult") //NOSONAR
        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) { //NOSONAR
            super.onLoadFailed(e, errorDrawable) //NOSONAR

            getAestheticColorSetDisposable() //NOSONAR
                .take(1) //NOSONAR
                .subscribe( //NOSONAR
                    { colorSet -> animateColors(this@UpNextView.colorSet, colorSet, 800, UnsafeConsumer { intermediateColorSet -> invalidateColors(intermediateColorSet) }, null) }, //NOSONAR
                    { _ ->
                        // Nothing ot do
                    }
                )
        }
    }

    companion object { //NOSONAR

        private val TAG = "UpNextView" //NOSONAR

        fun newInstance(context: Context, playerPresenter: PlayerPresenter, settingsManager: SettingsManager): UpNextView { //NOSONAR
            val upNextView = UpNextView(context) //NOSONAR
            upNextView.playerPresenter = playerPresenter //NOSONAR
            upNextView.settingsManager = settingsManager //NOSONAR
            return upNextView //NOSONAR
        }
    }
}
