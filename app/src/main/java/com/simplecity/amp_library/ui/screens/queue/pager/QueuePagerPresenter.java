package com.simplecity.amp_library.ui.screens.queue.pager; // NOSONAR

import android.content.Intent; // NOSONAR
import android.content.IntentFilter; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import com.annimon.stream.Collectors; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.bumptech.glide.RequestManager; // NOSONAR
import com.cantrowitz.rxbroadcast.RxBroadcast; // NOSONAR
import com.simplecity.amp_library.ShuttleApplication; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.ui.common.Presenter; // NOSONAR
import com.simplecity.amp_library.ui.modelviews.QueuePagerItemView; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import io.reactivex.BackpressureStrategy; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import java.util.List; // NOSONAR
import javax.inject.Inject; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class QueuePagerPresenter extends Presenter<QueuePagerView> { //NOSONAR

    private ShuttleApplication application; //NOSONAR

    private RequestManager requestManager; //NOSONAR

    private MediaManager mediaManager; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    @Inject //NOSONAR
    public QueuePagerPresenter( //NOSONAR
            ShuttleApplication application, //NOSONAR
            RequestManager requestManager, //NOSONAR
            MediaManager mediaManager, //NOSONAR
            SettingsManager settingsManager) { //NOSONAR
        this.application = application; //NOSONAR
        this.requestManager = requestManager; //NOSONAR
        this.mediaManager = mediaManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void bindView(@NonNull QueuePagerView view) { //NOSONAR
        super.bindView(view); //NOSONAR

        IntentFilter filter = new IntentFilter(); //NOSONAR
        filter.addAction(InternalIntents.META_CHANGED); //NOSONAR
        filter.addAction(InternalIntents.REPEAT_CHANGED); //NOSONAR
        filter.addAction(InternalIntents.SHUFFLE_CHANGED); //NOSONAR
        filter.addAction(InternalIntents.QUEUE_CHANGED); //NOSONAR
        filter.addAction(InternalIntents.SERVICE_CONNECTED); //NOSONAR

        addDisposable(RxBroadcast.fromBroadcast(application, filter) //NOSONAR
                .startWith(new Intent(InternalIntents.QUEUE_CHANGED)) //NOSONAR
                .toFlowable(BackpressureStrategy.LATEST) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(intent -> { //NOSONAR
                    final String action = intent.getAction(); //NOSONAR

                    QueuePagerView queuePagerView = getView(); //NOSONAR
                    if (queuePagerView == null) { //NOSONAR
                        return; //NOSONAR
                    } // NOSONAR

                    if (action != null) { //NOSONAR
                        switch (action) { //NOSONAR
                            case InternalIntents.META_CHANGED: //NOSONAR
                                queuePagerView.updateQueuePosition(mediaManager.getQueuePosition()); //NOSONAR
                                break; //NOSONAR
                            case InternalIntents.REPEAT_CHANGED: //NOSONAR
                            case InternalIntents.SHUFFLE_CHANGED: //NOSONAR
                            case InternalIntents.QUEUE_CHANGED: //NOSONAR
                            case InternalIntents.SERVICE_CONNECTED: //NOSONAR

                                List<ViewModel> items = Stream.of(mediaManager.getQueue()) //NOSONAR
                                        .map(queueItem -> new QueuePagerItemView(queueItem.getSong(), requestManager, settingsManager)) //NOSONAR
                                        .collect(Collectors.toList()); //NOSONAR

                                queuePagerView.loadData(items, mediaManager.getQueuePosition()); //NOSONAR
                                break; //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                })); // NOSONAR
    } // NOSONAR
} // NOSONAR
