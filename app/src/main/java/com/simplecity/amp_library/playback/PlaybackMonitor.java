package com.simplecity.amp_library.playback;

import com.simplecity.amp_library.utils.MusicServiceConnectionUtils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class PlaybackMonitor {

    private static final String TAG = "PlaybackMonitor";

    private Flowable<Float> progressObservable;
    private Flowable<Long> currentTimeObservable;

    @Inject
    PlaybackMonitor(MediaManager mediaManager) {
        progressObservable = Flowable.defer(() -> Observable.interval(32, TimeUnit.MILLISECONDS)
                .filter(aLong -> {
                    if (MusicServiceConnectionUtils.serviceBinder != null
                            && MusicServiceConnectionUtils.serviceBinder.getService() != null) {
                        if (mediaManager.getDuration() > 0) {
                            return true;
                        }
                    }
                    return false;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> (float) mediaManager.getPosition() / (float) mediaManager.getDuration())
                .toFlowable(BackpressureStrategy.DROP))
                .share();

        currentTimeObservable = Flowable.defer(() -> Observable.interval(150, TimeUnit.MILLISECONDS)
                .filter(aLong -> MusicServiceConnectionUtils.serviceBinder != null
                        && MusicServiceConnectionUtils.serviceBinder.getService() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .map(time -> mediaManager.getPosition())
                .toFlowable(BackpressureStrategy.DROP))
                .share();
    }

    public Flowable<Float> getProgressObservable() {
        return progressObservable;
    }

    public Flowable<Long> getCurrentTimeObservable() {
        return currentTimeObservable;
    }
}
