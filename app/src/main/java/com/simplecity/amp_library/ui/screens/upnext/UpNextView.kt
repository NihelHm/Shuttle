@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.upnext // NOSONAR

import android.animation.Animator // NOSONAR
import android.animation.AnimatorListenerAdapter // NOSONAR
import android.animation.ValueAnimator // NOSONAR
import android.annotation.SuppressLint // NOSONAR
import android.content.Context // NOSONAR
import android.graphics.drawable.Drawable // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat // NOSONAR
import android.support.v4.util.Pair // NOSONAR
import android.util.AttributeSet // NOSONAR
import android.view.View // NOSONAR
import android.view.animation.AccelerateDecelerateInterpolator // NOSONAR
import android.widget.LinearLayout // NOSONAR
import com.afollestad.aesthetic.Aesthetic // NOSONAR
import com.afollestad.aesthetic.ColorIsDarkState // NOSONAR
import com.afollestad.aesthetic.LightDarkColorState // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog // NOSONAR
import com.bumptech.glide.Glide // NOSONAR
import com.bumptech.glide.Priority // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy // NOSONAR
import com.bumptech.glide.request.animation.GlideAnimation // NOSONAR
import com.bumptech.glide.request.target.SimpleTarget // NOSONAR
import com.jakewharton.rxbinding2.widget.RxSeekBar // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarChangeEvent // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarProgressChangeEvent // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarStartChangeEvent // NOSONAR
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent // NOSONAR
import com.simplecity.amp_library.R // NOSONAR
import com.simplecity.amp_library.ShuttleApplication // NOSONAR
import com.simplecity.amp_library.glide.palette.ColorSet // NOSONAR
import com.simplecity.amp_library.glide.palette.ColorSetTranscoder // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.rx.UnsafeAction // NOSONAR
import com.simplecity.amp_library.rx.UnsafeConsumer // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerPresenter // NOSONAR
import com.simplecity.amp_library.ui.views.PlayerViewAdapter // NOSONAR
import com.simplecity.amp_library.ui.views.RepeatButton // NOSONAR
import com.simplecity.amp_library.ui.views.ShuffleButton // NOSONAR
import com.simplecity.amp_library.utils.LogUtils // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils // NOSONAR
import com.simplecity.amp_library.utils.color.ArgbEvaluator // NOSONAR
import io.reactivex.BackpressureStrategy // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers // NOSONAR
import io.reactivex.disposables.CompositeDisposable // NOSONAR
import io.reactivex.functions.BiFunction // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.arrowImageView // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.buttonContainer // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.nextButton // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.playPauseView // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.prevButton // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.queuePositionTextView // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.queueTextView // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.repeatButton // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.seekBar // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.shuffleButton // NOSONAR
import kotlinx.android.synthetic.main.up_next_view.view.textContainer // NOSONAR
import java.util.concurrent.TimeUnit // NOSONAR

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
            } // NOSONAR
        } // NOSONAR

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
            } // NOSONAR

            override fun playbackChanged(isPlaying: Boolean) { //NOSONAR
                playPauseView?.let { playPauseView -> //NOSONAR
                    if (isPlaying) { //NOSONAR
                        if (playPauseView.isPlay) { //NOSONAR
                            playPauseView.toggle(null) //NOSONAR
                            playPauseView.contentDescription = getContext().getString(R.string.btn_pause) //NOSONAR
                        } // NOSONAR
                    } else { //NOSONAR
                        if (!playPauseView.isPlay) { //NOSONAR
                            playPauseView.toggle(null) //NOSONAR
                            playPauseView.contentDescription = getContext().getString(R.string.btn_play) //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR

            override fun shuffleChanged(shuffleMode: Int) { //NOSONAR
                (shuffleButton as? ShuffleButton)?.setShuffleMode(shuffleMode) //NOSONAR
            } // NOSONAR

            override fun repeatChanged(repeatMode: Int) { //NOSONAR
                (repeatButton as? RepeatButton)?.setRepeatMode(repeatMode) //NOSONAR
            } // NOSONAR

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
                } // NOSONAR
            } // NOSONAR

            override fun setSeekProgress(progress: Int) { //NOSONAR
                if (!isSeeking) { //NOSONAR
                    seekBar?.progress = progress //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

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
                ) // NOSONAR
                    .subscribe { colorState -> //NOSONAR
                        DrawableCompat.setTint(arrowDrawable, colorState.color()) //NOSONAR
                    }) // NOSONAR

        } // NOSONAR

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
            // If we're managing the color scheme ourselves based on artwork changes, we only need the first ColorSet // NOSONAR
            // emission, as no artwork has been loaded yet. // NOSONAR
            if (settingsManager.usePalette || settingsManager.usePaletteNowPlayingOnly) { //NOSONAR
                observable = observable.take(1) //NOSONAR
            } // NOSONAR

            disposables.add( //NOSONAR
                observable.subscribe( //NOSONAR
                    { colorSet -> //NOSONAR
                        if (this@UpNextView.colorSet == colorSet) { //NOSONAR
                            invalidateColors(colorSet) //NOSONAR
                        } else { //NOSONAR
                            animateColors(this@UpNextView.colorSet, colorSet, 800, UnsafeConsumer { this.invalidateColors(it) }, null) //NOSONAR
                        } // NOSONAR
                    }, // NOSONAR
                    { _ -> // NOSONAR
                        // Nothing to do // NOSONAR
                    }) // NOSONAR
            ) // NOSONAR
        } // NOSONAR

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
                } // NOSONAR
            }, { error -> LogUtils.logException(TAG, "Error in seek change event", error) })) //NOSONAR

            disposables.add( //NOSONAR
                sharedSeekBarEvents //NOSONAR
                    .ofType(SeekBarProgressChangeEvent::class.java) //NOSONAR
                    .filter { it.fromUser() } //NOSONAR
                    .debounce(15, TimeUnit.MILLISECONDS) //NOSONAR
                    .subscribe({ seekBarChangeEvent -> playerPresenter.seekTo(seekBarChangeEvent.progress()) }, //NOSONAR
                        { error -> LogUtils.logException(TAG, "Error receiving seekbar progress", error) }) //NOSONAR
            ) // NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun onDetachedFromWindow() { //NOSONAR
        super.onDetachedFromWindow() //NOSONAR

        playerPresenter.unbindView(playerViewAdapter) //NOSONAR

        disposables.dispose() //NOSONAR
    } // NOSONAR

    fun invalidateColors(colorSet: ColorSet) { //NOSONAR

        if (isLandscape) { //NOSONAR

            var ignorePalette = false //NOSONAR
            if (!settingsManager.usePalette && !settingsManager.usePaletteNowPlayingOnly) { //NOSONAR
                // If we're not using Palette at all, use non-tinted colors for text. // NOSONAR
                colorSet.primaryTextColorTinted = colorSet.primaryTextColor //NOSONAR
                colorSet.secondaryTextColorTinted = colorSet.secondaryTextColor //NOSONAR
                ignorePalette = true //NOSONAR
            } // NOSONAR

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
        } // NOSONAR

        this.colorSet = colorSet //NOSONAR
    } // NOSONAR

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
            ) // NOSONAR
            consumer.accept(colorSet) //NOSONAR
        } // NOSONAR
        colorAnimator!!.addListener(object : AnimatorListenerAdapter() { //NOSONAR
            override fun onAnimationEnd(animation: Animator) { //NOSONAR
                animation.removeAllListeners() //NOSONAR
                onComplete?.run() //NOSONAR
            } // NOSONAR
        }) // NOSONAR
        colorAnimator!!.start() //NOSONAR
    } // NOSONAR

    private fun getAestheticColorSetDisposable(): Observable<ColorSet> { //NOSONAR
        return Observable.combineLatest( //NOSONAR
            Aesthetic.get(context).colorPrimary(), //NOSONAR
            Aesthetic.get(context).colorAccent(), //NOSONAR
            BiFunction { first: Int, second: Int -> Pair(first, second) } //NOSONAR
        ).map { pair -> ColorSet.fromPrimaryAccentColors(context!!, pair.first!!, pair.second!!) } //NOSONAR
    } // NOSONAR

    private val paletteTarget = object : SimpleTarget<ColorSet>() { //NOSONAR
        override fun onResourceReady(newColorSet: ColorSet, glideAnimation: GlideAnimation<in ColorSet>) { //NOSONAR

            if (colorSet === newColorSet) { //NOSONAR
                return //NOSONAR
            } // NOSONAR

            val oldColorSet = colorSet //NOSONAR

            animateColors( //NOSONAR
                oldColorSet, //NOSONAR
                newColorSet, //NOSONAR
                800, //NOSONAR
                UnsafeConsumer { intermediateColorSet -> //NOSONAR
                    // Update all the colours related to the now playing screen first // NOSONAR
                    invalidateColors(intermediateColorSet) //NOSONAR

                    // We need to update the nav bar colour at the same time, since it's visible as well. // NOSONAR
                    if (settingsManager.tintNavBar) { //NOSONAR
                        Aesthetic.get(getContext()).colorNavigationBar(intermediateColorSet.primaryColor).apply() //NOSONAR
                    } // NOSONAR
                }, // NOSONAR
                UnsafeAction { //NOSONAR
                    // Wait until the first set of color change animations is complete, before updating Aesthetic. // NOSONAR
                    // This allows our invalidateColors() animation to run smoothly, as the Aesthetic color change // NOSONAR
                    // introduces some jank. // NOSONAR
                    if (!settingsManager.usePaletteNowPlayingOnly) { //NOSONAR

                        animateColors(oldColorSet, newColorSet, 450, UnsafeConsumer { intermediateColorSet -> //NOSONAR
                            val aesthetic = Aesthetic.get(getContext()) //NOSONAR
                                .colorPrimary(intermediateColorSet.primaryColor) //NOSONAR
                                .colorAccent(intermediateColorSet.accentColor) //NOSONAR
                                .colorStatusBarAuto() //NOSONAR

                            aesthetic.apply() //NOSONAR
                        }, null) //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            ) // NOSONAR
        } // NOSONAR

        @SuppressLint("CheckResult") //NOSONAR
        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) { //NOSONAR
            super.onLoadFailed(e, errorDrawable) //NOSONAR

            getAestheticColorSetDisposable() //NOSONAR
                .take(1) //NOSONAR
                .subscribe( //NOSONAR
                    { colorSet -> animateColors(this@UpNextView.colorSet, colorSet, 800, UnsafeConsumer { intermediateColorSet -> invalidateColors(intermediateColorSet) }, null) }, //NOSONAR
                    { _ -> // NOSONAR
                        // Nothing ot do // NOSONAR
                    } // NOSONAR
                ) // NOSONAR
        } // NOSONAR
    } // NOSONAR

    companion object { //NOSONAR

        private val TAG = "UpNextView" //NOSONAR

        fun newInstance(context: Context, playerPresenter: PlayerPresenter, settingsManager: SettingsManager): UpNextView { //NOSONAR
            val upNextView = UpNextView(context) //NOSONAR
            upNextView.playerPresenter = playerPresenter //NOSONAR
            upNextView.settingsManager = settingsManager //NOSONAR
            return upNextView //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
