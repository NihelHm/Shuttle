package com.simplecity.amp_library.http;

import com.simplecity.amp_library.http.lastfm.LastFmService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class HttpClient { //NOSONAR

    public static final String TAG = "HttpClient"; //NOSONAR

    private static final String URL_LAST_FM = "https://ws.audioscrobbler.com/2.0/"; //NOSONAR
    private static final String URL_ITUNES = "https://itunes.apple.com/search/"; //NOSONAR

    private static HttpClient sInstance; //NOSONAR

    private OkHttpClient okHttpClient; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public LastFmService lastFmService; //NOSONAR

    public static final String TAG_ARTWORK = "artwork"; //NOSONAR

    public static synchronized HttpClient getInstance() { //NOSONAR
        if (sInstance == null) { //NOSONAR
            sInstance = new HttpClient(); //NOSONAR
        }
        return sInstance; //NOSONAR
    }

    private HttpClient() { //NOSONAR

        okHttpClient = new OkHttpClient.Builder() //NOSONAR
                //                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.3", 8888)))
                .build(); //NOSONAR

        Retrofit lastFmRestAdapter = new Retrofit.Builder() //NOSONAR
                .baseUrl(URL_LAST_FM) //NOSONAR
                .client(okHttpClient) //NOSONAR
                .addConverterFactory(GsonConverterFactory.create()) //NOSONAR
                .build(); //NOSONAR
        lastFmService = lastFmRestAdapter.create(LastFmService.class); //NOSONAR
    }
}
