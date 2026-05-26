package com.simplecity.amp_library.glide.fetcher; // NOSONAR

import android.content.Context; // NOSONAR
import com.bumptech.glide.Priority; // NOSONAR
import com.bumptech.glide.load.data.DataFetcher; // NOSONAR
import com.simplecity.amp_library.model.ArtworkProvider; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TypeFetcher implements DataFetcher<InputStream> { //NOSONAR

    private static final String TAG = "MultiFetcher"; //NOSONAR

    private Context applicationContext; //NOSONAR

    private DataFetcher<InputStream> dataFetcher; //NOSONAR

    private ArtworkProvider artworkProvider; //NOSONAR

    @ArtworkProvider.Type //NOSONAR
    private int type; //NOSONAR

    private File file; //NOSONAR

    public TypeFetcher(Context context, ArtworkProvider artworkProvider, @ArtworkProvider.Type int type, File file) { //NOSONAR
        applicationContext = context.getApplicationContext(); //NOSONAR
        this.artworkProvider = artworkProvider; //NOSONAR
        this.type = type; //NOSONAR
        this.file = file; //NOSONAR
    } // NOSONAR

    private InputStream loadData(DataFetcher<InputStream> dataFetcher, Priority priority) { //NOSONAR
        InputStream inputStream; //NOSONAR
        try { //NOSONAR
            inputStream = dataFetcher.loadData(priority); //NOSONAR
        } catch (Exception e) { //NOSONAR
            if (dataFetcher != null) { //NOSONAR
                dataFetcher.cleanup(); //NOSONAR
            } // NOSONAR
            inputStream = null; //NOSONAR
        } // NOSONAR
        return inputStream; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public InputStream loadData(Priority priority) throws Exception { //NOSONAR
        switch (type) { //NOSONAR
            case ArtworkProvider.Type.MEDIA_STORE: //NOSONAR
                dataFetcher = new MediaStoreFetcher(applicationContext, artworkProvider); //NOSONAR
                break; //NOSONAR
            case ArtworkProvider.Type.FOLDER: //NOSONAR
                dataFetcher = new FolderFetcher(artworkProvider, file); //NOSONAR
                break; //NOSONAR
            case ArtworkProvider.Type.TAG: //NOSONAR
                dataFetcher = new TagFetcher(artworkProvider); //NOSONAR
                break; //NOSONAR
            case ArtworkProvider.Type.REMOTE: //NOSONAR
                dataFetcher = new RemoteFetcher(artworkProvider); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
        return loadData(dataFetcher, priority); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void cleanup() { //NOSONAR
        if (dataFetcher != null) { //NOSONAR
            dataFetcher.cleanup(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void cancel() { //NOSONAR
        if (dataFetcher != null) { //NOSONAR
            dataFetcher.cancel(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        String id = artworkProvider.getArtworkKey() + "_" + type; //NOSONAR
        if (file != null) { //NOSONAR
            id += "_" + file.hashCode(); //NOSONAR
        } // NOSONAR
        return id; //NOSONAR
    } // NOSONAR
} // NOSONAR
