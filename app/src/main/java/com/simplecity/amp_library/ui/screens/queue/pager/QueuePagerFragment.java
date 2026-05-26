package com.simplecity.amp_library.ui.screens.queue.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.glide.preloader.RecyclerViewPreloader;
import com.simplecity.amp_library.ui.common.BaseFragment;
import com.simplecity.amp_library.ui.common.RequestManagerProvider;
import com.simplecity.amp_library.ui.modelviews.QueuePagerItemView;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.PlaceholderProvider;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter;
import com.simplecityapps.recycler_adapter.model.ViewModel;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

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
    }

    public QueuePagerFragment() { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onAttach(Context context) { //NOSONAR
        AndroidSupportInjection.inject(this); //NOSONAR
        super.onAttach(context); //NOSONAR
    }

    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        viewModelAdapter = new ViewModelAdapter(); //NOSONAR
    }

    @Override //NOSONAR
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //NOSONAR
        View rootView = inflater.inflate(R.layout.fragment_queue_pager, container, false); //NOSONAR

        unbinder = ButterKnife.bind(this, rootView); //NOSONAR

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false); //NOSONAR

        if (ShuttleUtils.isLandscape(getContext())) { //NOSONAR
            textProtectionScrim.setVisibility(View.GONE); //NOSONAR
        }

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
                                        }
                                    },
                                    throwable -> LogUtils.logException(TAG, "Error setting queue position", throwable) //NOSONAR
                            );
                }

                return snapPosition; //NOSONAR
            }
        };
        snapHelper.attachToRecyclerView(recyclerView); //NOSONAR

        recyclerView.addOnScrollListener(new RecyclerViewPreloader<>(new ListPreloader.PreloadModelProvider<QueuePagerItemView>() { //NOSONAR
            @Override //NOSONAR
            public List<QueuePagerItemView> getPreloadItems(int position) { //NOSONAR
                QueuePagerItemView queuePagerItemView = (QueuePagerItemView) viewModelAdapter.items.get(position); //NOSONAR
                return Collections.singletonList(queuePagerItemView); //NOSONAR
            }

            @Override //NOSONAR
            public GenericRequestBuilder getPreloadRequestBuilder(QueuePagerItemView item) { //NOSONAR
                return requestManager //NOSONAR
                        .load(item.song) //NOSONAR
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE) //NOSONAR
                        .error(PlaceholderProvider.getInstance(getContext()).getPlaceHolderDrawable(item.song.name, true, settingsManager)); //NOSONAR
            }
        }, (item, adapterPosition, perItemPosition) -> imageSize, 3)); //NOSONAR

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { //NOSONAR
            @Override //NOSONAR
            public boolean onPreDraw() { //NOSONAR
                // This null check doesn't make sense to me, but there was an NPE here..
                if (recyclerView != null) { //NOSONAR
                    imageSize = new int[] { recyclerView.getWidth(), recyclerView.getHeight() }; //NOSONAR
                    recyclerView.getViewTreeObserver().removeOnPreDrawListener(this); //NOSONAR
                }
                return false; //NOSONAR
            }
        });

        return rootView; //NOSONAR
    }

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR

        queuePagerPresenter.bindView(this); //NOSONAR
    }

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        super.onPause(); //NOSONAR

        queuePagerPresenter.unbindView(this); //NOSONAR
    }

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        unbinder.unbind(); //NOSONAR
        super.onDestroyView(); //NOSONAR
    }

    @Override //NOSONAR
    public RequestManager getRequestManager() { //NOSONAR
        return requestManager; //NOSONAR
    }

    @Override //NOSONAR
    public void loadData(List<ViewModel> viewModels, int position) { //NOSONAR
        viewModelAdapter.items.clear(); //NOSONAR
        viewModelAdapter.items.addAll(viewModels); //NOSONAR
        viewModelAdapter.notifyDataSetChanged(); //NOSONAR
        recyclerView.getLayoutManager().scrollToPosition(position); //NOSONAR
    }

    @Override //NOSONAR
    public void updateQueuePosition(int position) { //NOSONAR
        recyclerView.getLayoutManager().scrollToPosition(position); //NOSONAR
    }

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    }
}
