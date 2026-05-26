package com.simplecity.amp_library.utils; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.content.Context; // NOSONAR
import android.os.Handler; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.inputmethod.InputMethodManager; // NOSONAR
import android.widget.EditText; // NOSONAR
import com.afollestad.materialdialogs.MaterialDialog; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.rx.UnsafeAction; // NOSONAR
import io.reactivex.BackpressureStrategy; // NOSONAR
import io.reactivex.Flowable; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.subjects.BehaviorSubject; // NOSONAR
import java.util.concurrent.TimeUnit; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class SleepTimer { //NOSONAR

    private static final String TAG = "SleepTimer"; //NOSONAR

    private static SleepTimer instance; //NOSONAR

    private boolean isActive; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public boolean playToEnd = false; //NOSONAR

    private int timeRemaining = 0; //NOSONAR

    private Flowable<Long> currentTimeObservable; //NOSONAR

    private BehaviorSubject<Boolean> timerActiveObservable; //NOSONAR

    public static SleepTimer getInstance() { //NOSONAR
        if (instance == null) { //NOSONAR
            instance = new SleepTimer(); //NOSONAR
        } // NOSONAR
        return instance; //NOSONAR
    } // NOSONAR

    private SleepTimer() { //NOSONAR

        timerActiveObservable = BehaviorSubject.create(); //NOSONAR

        currentTimeObservable = timerActiveObservable //NOSONAR
                .doOnNext(isActive -> this.isActive = isActive) //NOSONAR
                .switchMap(ignored -> Observable //NOSONAR
                        .interval(1, TimeUnit.SECONDS) //NOSONAR
                        .filter(aLong -> isActive) //NOSONAR
                        .map(time -> timeRemaining - time) //NOSONAR
                        .distinctUntilChanged() //NOSONAR
                        .skip(1) //NOSONAR
                        .doOnNext(aLong -> { //NOSONAR
                            if (aLong == -1) { //NOSONAR
                                stop(); //NOSONAR
                            } // NOSONAR
                        })) // NOSONAR
                .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
                .share(); //NOSONAR
    } // NOSONAR

    public Flowable<Long> getCurrentTimeObservable() { //NOSONAR
        return currentTimeObservable; //NOSONAR
    } // NOSONAR

    public BehaviorSubject<Boolean> getTimerActiveSubject() { //NOSONAR
        return timerActiveObservable; //NOSONAR
    } // NOSONAR

    public void start(int seconds, boolean playToEnd) { //NOSONAR
        this.timeRemaining = seconds; //NOSONAR
        this.playToEnd = playToEnd; //NOSONAR
        timerActiveObservable.onNext(true); //NOSONAR
    } // NOSONAR

    public void stop() { //NOSONAR
        isActive = false; //NOSONAR
        timerActiveObservable.onNext(false); //NOSONAR
    } // NOSONAR

    public MaterialDialog getDialog(Context context, UnsafeAction showMinutesPicker, UnsafeAction timerStarted) { //NOSONAR

        if (isActive) { //NOSONAR
            return new MaterialDialog.Builder(context) //NOSONAR
                    .content(R.string.sleep_timer_stop_title) //NOSONAR
                    .positiveText(R.string.sleep_timer_stop_button) //NOSONAR
                    .negativeText(R.string.close) //NOSONAR
                    .onPositive((materialDialog, dialogAction) -> stop()) //NOSONAR
                    .build(); //NOSONAR
        } else { //NOSONAR
            return new MaterialDialog.Builder(context) //NOSONAR
                    .title(R.string.sleep_timer) //NOSONAR
                    .items(R.array.timerValues) //NOSONAR
                    .checkBoxPromptRes(R.string.sleep_timer_play_to_end, false, (compoundButton, b) -> playToEnd = b) //NOSONAR
                    .itemsCallback((materialDialog, view, i, charSequence) -> { //NOSONAR
                        switch (i) { //NOSONAR
                            case 0: //NOSONAR
                                // 5 mins // NOSONAR
                                start(5 * 60, playToEnd); //NOSONAR
                                timerStarted.run(); //NOSONAR
                                break; //NOSONAR
                            case 1: //NOSONAR
                                // 15 mins // NOSONAR
                                start(15 * 60, playToEnd); //NOSONAR
                                timerStarted.run(); //NOSONAR
                                break; //NOSONAR
                            case 2: //NOSONAR
                                // 30 mins // NOSONAR
                                start(30 * 60, playToEnd); //NOSONAR
                                timerStarted.run(); //NOSONAR
                                break; //NOSONAR
                            case 3: //NOSONAR
                                // 1 hour // NOSONAR
                                start(60 * 60, playToEnd); //NOSONAR
                                timerStarted.run(); //NOSONAR
                                break; //NOSONAR
                            case 4: //NOSONAR
                                // Set time manually // NOSONAR
                                showMinutesPicker.run(); //NOSONAR
                                break; //NOSONAR
                        } // NOSONAR
                    }).build(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void showMinutesDialog(Context context, UnsafeAction timerStarted) { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        View customView = LayoutInflater.from(context).inflate(R.layout.dialog_minutes_picker, null); //NOSONAR

        EditText editText = customView.findViewById(R.id.editText); //NOSONAR

        new MaterialDialog.Builder(context) //NOSONAR
                .title(R.string.sleep_timer_set_minutes) //NOSONAR
                .customView(customView, false) //NOSONAR
                .positiveText(R.string.button_ok) //NOSONAR
                .negativeText(R.string.cancel) //NOSONAR
                .autoDismiss(false) //NOSONAR
                .onPositive((materialDialog, dialogAction) -> { //NOSONAR
                    if (!TextUtils.isEmpty(editText.getText())) { //NOSONAR
                        start(Integer.parseInt(editText.getText().toString()) * 60, playToEnd); //NOSONAR
                        timerStarted.run(); //NOSONAR
                        materialDialog.dismiss(); //NOSONAR
                    } // NOSONAR
                }) // NOSONAR
                .onNegative((materialDialog, dialogAction) -> { //NOSONAR
                    materialDialog.dismiss(); //NOSONAR
                }) // NOSONAR
                .show(); //NOSONAR

        new Handler().post(() -> { //NOSONAR
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); //NOSONAR
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT); //NOSONAR
        }); // NOSONAR
    } // NOSONAR
} // NOSONAR
