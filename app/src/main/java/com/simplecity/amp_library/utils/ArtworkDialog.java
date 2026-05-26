package com.simplecity.amp_library.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.model.ArtworkModel;
import com.simplecity.amp_library.model.ArtworkProvider;
import com.simplecity.amp_library.model.UserSelectedArtwork;
import com.simplecity.amp_library.sql.databases.CustomArtworkTable;
import com.simplecity.amp_library.ui.modelviews.ArtworkLoadingView;
import com.simplecity.amp_library.ui.modelviews.ArtworkView;
import com.simplecity.amp_library.ui.views.recyclerview.SpacesItemDecoration;
import com.simplecityapps.recycler_adapter.adapter.ViewModelAdapter;
import com.simplecityapps.recycler_adapter.model.ViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.RecyclerListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArtworkDialog { //NOSONAR

    private static final String TAG = "ArtworkDialog"; //NOSONAR

    private ArtworkDialog() { //NOSONAR
        // Intentionally left empty.
    }

    public static MaterialDialog build(Context context, ArtworkProvider artworkProvider) { //NOSONAR

        @SuppressLint("InflateParams") //NOSONAR
        View customView = LayoutInflater.from(context).inflate(R.layout.dialog_artwork, null); //NOSONAR

        ViewModelAdapter adapter = new ViewModelAdapter(); //NOSONAR

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false); //NOSONAR
        RecyclerView recyclerView = customView.findViewById(R.id.recyclerView); //NOSONAR
        recyclerView.addItemDecoration(new SpacesItemDecoration(16)); //NOSONAR
        recyclerView.setLayoutManager(layoutManager); //NOSONAR
        recyclerView.setHasFixedSize(true); //NOSONAR
        recyclerView.setItemViewCacheSize(0); //NOSONAR
        recyclerView.setRecyclerListener(new RecyclerListener()); //NOSONAR

        adapter.items.add(0, new ArtworkLoadingView()); //NOSONAR
        adapter.notifyDataSetChanged(); //NOSONAR
        recyclerView.setAdapter(adapter); //NOSONAR

        ArtworkView.GlideListener glideListener = artworkView -> { //NOSONAR
            int index = adapter.items.indexOf(artworkView); //NOSONAR
            if (index != -1) { //NOSONAR
                adapter.removeItem(index); //NOSONAR
            }
        };

        List<ViewModel> viewModels = new ArrayList<>(); //NOSONAR

        UserSelectedArtwork userSelectedArtwork = ((ShuttleApplication) context.getApplicationContext()).userSelectedArtwork.get(artworkProvider.getArtworkKey()); //NOSONAR
        if (userSelectedArtwork != null) { //NOSONAR
            File file = null; //NOSONAR
            if (userSelectedArtwork.path != null) { //NOSONAR
                file = new File(userSelectedArtwork.path); //NOSONAR
            }
            ArtworkView artworkView = new ArtworkView(userSelectedArtwork.type, artworkProvider, glideListener, file, true); //NOSONAR
            artworkView.setSelected(true); //NOSONAR
            viewModels.add(artworkView); //NOSONAR
        }

        if (userSelectedArtwork == null || userSelectedArtwork.type != ArtworkProvider.Type.MEDIA_STORE) { //NOSONAR
            viewModels.add(new ArtworkView(ArtworkProvider.Type.MEDIA_STORE, artworkProvider, glideListener)); //NOSONAR
        }
        if (userSelectedArtwork == null || userSelectedArtwork.type != ArtworkProvider.Type.TAG) { //NOSONAR
            viewModels.add(new ArtworkView(ArtworkProvider.Type.TAG, artworkProvider, glideListener)); //NOSONAR
        }
        if (userSelectedArtwork == null || userSelectedArtwork.type != ArtworkProvider.Type.REMOTE) { //NOSONAR
            viewModels.add(new ArtworkView(ArtworkProvider.Type.REMOTE, artworkProvider, glideListener)); //NOSONAR
        }

        //Dummy Folder ArtworkView - will be replaced or removed depending on availability of folder images
        ArtworkView folderView = new ArtworkView(ArtworkProvider.Type.FOLDER, null, null); //NOSONAR
        viewModels.add(folderView); //NOSONAR

        ArtworkView.ClickListener listener = artworkView -> { //NOSONAR
            Stream.of(viewModels) //NOSONAR
                    .filter(viewModel -> viewModel instanceof ArtworkView) //NOSONAR
                    .forEachIndexed((i, viewModel) -> ((ArtworkView) viewModel).setSelected(viewModel == artworkView)); //NOSONAR
            adapter.notifyItemRangeChanged(0, adapter.getItemCount(), 0); //NOSONAR
        };

        Stream.of(viewModels) //NOSONAR
                .filter(viewModel -> viewModel instanceof ArtworkView) //NOSONAR
                .forEach(viewModel -> ((ArtworkView) viewModel).setListener(listener)); //NOSONAR

        adapter.setItems(viewModels); //NOSONAR

        Observable.fromCallable(artworkProvider::getFolderArtworkFiles) //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe(files -> { //NOSONAR
                    adapter.removeItem(adapter.items.indexOf(folderView)); //NOSONAR
                    if (files != null) { //NOSONAR
                        Stream.of(files) //NOSONAR
                                .filter(file -> userSelectedArtwork == null || !file.getPath().equals(userSelectedArtwork.path)) //NOSONAR
                                .forEach(file -> //NOSONAR
                                        adapter.addItem(new ArtworkView(ArtworkProvider.Type.FOLDER, artworkProvider, glideListener, file, false))); //NOSONAR
                    }
                }, error -> LogUtils.logException(TAG, "Error getting artwork files", error)); //NOSONAR

        return new MaterialDialog.Builder(context) //NOSONAR
                .title(R.string.artwork_edit) //NOSONAR
                .customView(customView, false) //NOSONAR
                .autoDismiss(false) //NOSONAR
                .positiveText(context.getString(R.string.save)) //NOSONAR
                .onPositive((dialog, which) -> { //NOSONAR
                    ArtworkView checkedView = ArtworkDialog.getCheckedView(adapter.items); //NOSONAR
                    if (checkedView != null) { //NOSONAR
                        ArtworkModel artworkModel = checkedView.getItem(); //NOSONAR
                        ContentValues values = new ContentValues(); //NOSONAR
                        values.put(CustomArtworkTable.COLUMN_KEY, artworkProvider.getArtworkKey()); //NOSONAR
                        values.put(CustomArtworkTable.COLUMN_TYPE, artworkModel.type); //NOSONAR
                        values.put(CustomArtworkTable.COLUMN_PATH, artworkModel.file == null ? null : artworkModel.file.getPath()); //NOSONAR
                        context.getContentResolver().insert(CustomArtworkTable.URI, values); //NOSONAR

                        ((ShuttleApplication) context.getApplicationContext()).userSelectedArtwork.put(artworkProvider.getArtworkKey(), //NOSONAR
                                new UserSelectedArtwork(artworkModel.type, artworkModel.file == null ? null : artworkModel.file.getPath())); //NOSONAR
                    } else { //NOSONAR
                        context.getContentResolver().delete(CustomArtworkTable.URI, CustomArtworkTable.COLUMN_KEY + "='" + artworkProvider.getArtworkKey().replaceAll("'", "\''") + "'", null); //NOSONAR
                        ((ShuttleApplication) context.getApplicationContext()).userSelectedArtwork.remove(artworkProvider.getArtworkKey()); //NOSONAR
                    }
                    dialog.dismiss(); //NOSONAR
                })
                .negativeText(context.getString(R.string.close)) //NOSONAR
                .onNegative((dialog, which) -> dialog.dismiss()) //NOSONAR
                .neutralText(context.getString(R.string.artwork_gallery)) //NOSONAR
                .onNeutral((dialog, which) -> RxImagePicker.with(context) //NOSONAR
                        .requestImage(Sources.GALLERY) //NOSONAR
                        .flatMap(uri -> { //NOSONAR

                            // The directory will be shuttle/custom_artwork/key_hashcode/currentSystemTime.artwork
                            // We want the directory to be based on the key, so we can delete old artwork, and the
                            // filename to be unique, because it's used for Glide caching.
                            File dir = new File(context.getFilesDir() + "/shuttle/custom_artwork/" + artworkProvider.getArtworkKey().hashCode() + "/"); //NOSONAR

                            // Create dir if necessary
                            if (!dir.exists()) { //NOSONAR
                                dir.mkdirs(); //NOSONAR
                            } else { //NOSONAR
                                // Delete any existing artwork for this key.
                                if (dir.isDirectory()) { //NOSONAR
                                    String[] children = dir.list(); //NOSONAR
                                    for (String child : children) { //NOSONAR
                                        new File(dir, child).delete(); //NOSONAR
                                    }
                                }
                            }

                            File file = new File(dir.getPath() + System.currentTimeMillis() + ".artwork"); //NOSONAR

                            try { //NOSONAR
                                file.createNewFile(); //NOSONAR
                                if (file.exists()) { //NOSONAR
                                    return RxImageConverters.uriToFile(context, uri, file); //NOSONAR
                                }
                            } catch (IOException e) { //NOSONAR
                                e.printStackTrace(); //NOSONAR
                            }

                            return null; //NOSONAR
                        })
                        .filter(file -> file != null && file.exists()) //NOSONAR
                        .subscribe(file -> { //NOSONAR
                            // If we've already got user-selected artwork in the adapter, remove it.
                            if (adapter.getItemCount() != 0) { //NOSONAR
                                File aFile = ((ArtworkView) adapter.items.get(0)).file; //NOSONAR
                                if (aFile != null && aFile.getPath().contains(artworkProvider.getArtworkKey())) { //NOSONAR
                                    adapter.removeItem(0); //NOSONAR
                                }
                            }

                            ArtworkView artworkView = new ArtworkView(ArtworkProvider.Type.FOLDER, artworkProvider, glideListener, file, true); //NOSONAR
                            artworkView.setSelected(true); //NOSONAR
                            adapter.addItem(0, artworkView); //NOSONAR
                            recyclerView.scrollToPosition(0); //NOSONAR
                        }, error -> LogUtils.logException(TAG, "Error picking from gallery", error))) //NOSONAR
                .cancelable(false) //NOSONAR
                .build(); //NOSONAR
    }

    @Nullable //NOSONAR
    public static ArtworkView getCheckedView(List<ViewModel> viewModels) { //NOSONAR
        return (ArtworkView) Stream.of(viewModels) //NOSONAR
                .filter(viewModel -> viewModel instanceof ArtworkView && ((ArtworkView) viewModel).isSelected()) //NOSONAR
                .findFirst() //NOSONAR
                .orElse(null); //NOSONAR
    }
}
