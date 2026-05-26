package com.simplecity.amp_library.ui.screens.queue.pager; // NOSONAR

import android.content.Context; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.v7.widget.LinearLayoutManager; // NOSONAR
import android.support.v7.widget.PagerSnapHelper; // NOSONAR
import android.support.v7.widget.RecyclerView; // NOSONAR
import android.support.v7.widget.SnapHelper; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.view.ViewTreeObserver; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import butterknife.Unbinder; // NOSONAR
import com.bumptech.glide.GenericRequestBuilder; // NOSONAR
import com.bumptech.glide.ListPreloader; // NOSONAR
import com.bumptech.glide.RequestManager; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.glide.preloader.RecyclerViewPreloader; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment; // NOSONAR
import com.simplecity.amp_library.ui.common.RequestManagerProvider; // NOSONAR
import com.simplecity.amp_library.ui.modelviews.QueuePagerItemView; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.PlaceholderProvider; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.ShuttleUtils; // NOSONAR
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter; // NOSONAR
import com.simplecityapps.recycler_adapter.model.ViewModel; // NOSONAR
import dagger.android.support.AndroidSupportInjection; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.android.schedulers.AndroidSchedulers; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR
import java.util.concurrent.TimeUnit; // NOSONAR
import javax.inject.Inject; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class QueuePagerFragment extends BaseFragment implements //NOSONAR
        RequestManagerProvider, //NOSONAR
        QueuePagerView { //NOSONAR

    private final String TAG = "QueuePagerFragment"; //NOSONAR

    private Unbinder unbinder; //NOSONAR

    @BindView(R.id.recyclerView) //NOSONAR
    RecyclerView recyclerView; //NOSONAR

    @BindView(R.id.textProtectionScrim) //NOSONAR
    View textProtectionScrim; //NOSONAR

    @Inject //NOSONAR
    RequestManager requestManager; //NOSONAR

    @Inject //NOSONAR
    QueuePagerPresenter queuePagerPresenter; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    ViewModelAdapter viewModelAdapter; //NOSONAR

    int[] imageSize = new int[2]; //NOSONAR

    public static QueuePagerFragment newInstance() { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        QueuePagerFragment fragment = new QueuePagerFragment(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    } // NOSONAR

    public QueuePagerFragment() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onAttach(Context context) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onAttach(context); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        viewModelAdapter = new ViewModelAdapter(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //NOSONAR
        View rootView = inflater.inflate(R.layout.fragment_queue_pager, container, false); //NOSONAR

        unbinder = ButterKnife.bind(this, rootView); //NOSONAR

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false); //NOSONAR

        if (ShuttleUtils.isLandscape(getContext())) { //NOSONAR
            textProtectionScrim.setVisibility(View.GONE); //NOSONAR
        } // NOSONAR

        recyclerView.setNestedScrollingEnabled(false); //NOSONAR
        recyclerView.setLayoutManager(layoutManager); //NOSONAR
        recyclerView.setAdapter(viewModelAdapter); //NOSONAR
        SnapHelper snapHelper = new PagerSnapHelper() { //NOSONAR
            @Override //NOSONAR
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) { //NOSONAR

                int snapPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY); //NOSONAR

                if (snapPosition < viewModelAdapter.items.size()) { //NOSONAR
                    Observable.timer(200, TimeUnit.MILLISECONDS) //NOSONAR
                            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                            .subscribe( //NOSONAR
                                    o -> { //NOSONAR
                                        if (mediaManager.getQueuePosition() != snapPosition) { //NOSONAR
                                            mediaManager.setQueuePosition(snapPosition); //NOSONAR
                                        } // NOSONAR
                                    }, // NOSONAR
                                    throwable -> LogUtils.logException(TAG, "Error setting queue position", throwable) //NOSONAR
                            ); // NOSONAR
                } // NOSONAR

                return snapPosition; //NOSONAR
            } // NOSONAR
        }; // NOSONAR
        snapHelper.attachToRecyclerView(recyclerView); //NOSONAR

        recyclerView.addOnScrollListener(new RecyclerViewPreloader<>(new ListPreloader.PreloadModelProvider<QueuePagerItemView>() { //NOSONAR
            @Override //NOSONAR
            public List<QueuePagerItemView> getPreloadItems(int position) { //NOSONAR
                QueuePagerItemView queuePagerItemView = (QueuePagerItemView) viewModelAdapter.items.get(position); //NOSONAR
                return Collections.singletonList(queuePagerItemView); //NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public GenericRequestBuilder getPreloadRequestBuilder(QueuePagerItemView item) { //NOSONAR
                return requestManager //NOSONAR
                        .load(item.song) //NOSONAR
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
                        .error(PlaceholderProvider.getInstance(getContext()).getPlaceHolderDrawable(item.song.name, true, settingsManager)); //NOSONAR
            } // NOSONAR
        }, (item, adapterPosition, perItemPosition) -> imageSize, 3)); //NOSONAR

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { //NOSONAR
            @Override //NOSONAR
            public boolean onPreDraw() { //NOSONAR
                // This null check doesn't make sense to me, but there was an NPE here.. // NOSONAR
                if (recyclerView != null) { //NOSONAR
                    imageSize = new int[] { recyclerView.getWidth(), recyclerView.getHeight() }; //NOSONAR
                    recyclerView.getViewTreeObserver().removeOnPreDrawListener(this); //NOSONAR
                } // NOSONAR
                return false; //NOSONAR
            } // NOSONAR
        }); // NOSONAR

        return rootView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        queuePagerPresenter.bindView(this); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        super.onPause(); //NOSONAR

        queuePagerPresenter.unbindView(this); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public RequestManager getRequestManager() { //NOSONAR
        return requestManager; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void loadData(List<ViewModel> viewModels, int position) { //NOSONAR
        viewModelAdapter.items.clear(); //NOSONAR
        viewModelAdapter.items.addAll(viewModels); //NOSONAR
        viewModelAdapter.notifyDataSetChanged(); //NOSONAR
        recyclerView.getLayoutManager().scrollToPosition(position); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void updateQueuePosition(int position) { //NOSONAR
        recyclerView.getLayoutManager().scrollToPosition(position); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR
} // NOSONAR
