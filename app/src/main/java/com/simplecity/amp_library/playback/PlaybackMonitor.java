package com.simplecity.amp_library.playback; // NOSONAR

import com.simplecity.amp_library.utils.MusicServiceConnectionUtils; // NOSONAR
import io.reactivex.BackpressureStrategy; // NOSONAR
import io.reactivex.Flowable; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import java.util.concurrent.TimeUnit; // NOSONAR
import javax.inject.Inject; // NOSONAR
import javax.inject.Singleton; // NOSONAR

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PlaybackMonitor { //NOSONAR

    private static final String TAG = "PlaybackMonitor"; //NOSONAR

    private Flowable<Float> progressObservable; //NOSONAR
    private Flowable<Long> currentTimeObservable; //NOSONAR

    @Inject //NOSONAR
    PlaybackMonitor(MediaManager mediaManager) { //NOSONAR
        progressObservable = Flowable.defer(() -> Observable.interval(32, TimeUnit.MILLISECONDS) //NOSONAR
                .filter(aLong -> { //NOSONAR
                    if (MusicServiceConnectionUtils.serviceBinder != null //NOSONAR
                            && MusicServiceConnectionUtils.serviceBinder.getService() != null) { //NOSONAR
                        if (mediaManager.getDuration() > 0) { //NOSONAR
                            return true; //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                    return false; //NOSONAR
                }) // NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .map(aLong -> (float) mediaManager.getPosition() / (float) mediaManager.getDuration()) //NOSONAR
                .toFlowable(BackpressureStrategy.DROP)) //NOSONAR
                .share(); //NOSONAR

        currentTimeObservable = Flowable.defer(() -> Observable.interval(150, TimeUnit.MILLISECONDS) //NOSONAR
                .filter(aLong -> MusicServiceConnectionUtils.serviceBinder != null //NOSONAR
                        && MusicServiceConnectionUtils.serviceBinder.getService() != null) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .map(time -> mediaManager.getPosition()) //NOSONAR
                .toFlowable(BackpressureStrategy.DROP)) //NOSONAR
                .share(); //NOSONAR
    } // NOSONAR

    public Flowable<Float> getProgressObservable() { //NOSONAR
        return progressObservable; //NOSONAR
    } // NOSONAR

    public Flowable<Long> getCurrentTimeObservable() { //NOSONAR
        return currentTimeObservable; //NOSONAR
    } // NOSONAR
} // NOSONAR
