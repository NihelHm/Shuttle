package com.simplecity.amp_library; // NOSONAR

import android.Manifest; // NOSONAR
import android.content.ContentProviderOperation; // NOSONAR
import android.content.ContentUris; // NOSONAR
import android.content.ContentValues; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.content.pm.PackageManager; // NOSONAR
import android.os.Environment; // NOSONAR
import android.os.StrictMode; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v7.preference.PreferenceManager; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.util.Log; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.crashlytics.android.Crashlytics; // NOSONAR
import com.crashlytics.android.answers.Answers; // NOSONAR
import com.crashlytics.android.core.CrashlyticsCore; // NOSONAR
import com.google.firebase.FirebaseApp; // NOSONAR
import com.google.firebase.analytics.FirebaseAnalytics; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.di.app.DaggerAppComponent; // NOSONAR
import com.simplecity.amp_library.model.Genre; // NOSONAR
import com.simplecity.amp_library.model.Query; // NOSONAR
import com.simplecity.amp_library.model.UserSelectedArtwork; // NOSONAR
import com.simplecity.amp_library.sql.SqlUtils; // NOSONAR
import com.simplecity.amp_library.sql.databases.CustomArtworkTable; // NOSONAR
import com.simplecity.amp_library.sql.providers.PlayCountTable; // NOSONAR
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils; // NOSONAR
import com.simplecity.amp_library.utils.AnalyticsManager; // NOSONAR
import com.simplecity.amp_library.utils.InputMethodManagerLeaks; // NOSONAR
import com.simplecity.amp_library.utils.LegacyUtils; // NOSONAR
import com.simplecity.amp_library.utils.LogUtils; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR
import com.simplecity.amp_library.utils.extensions.GenreExtKt; // NOSONAR
import com.squareup.leakcanary.LeakCanary; // NOSONAR
import com.squareup.leakcanary.RefWatcher; // NOSONAR
import com.uber.rxdogtag.RxDogTag; // NOSONAR
import dagger.android.AndroidInjector; // NOSONAR
import dagger.android.DaggerApplication; // NOSONAR
import io.fabric.sdk.android.Fabric; // NOSONAR
import io.reactivex.Completable; // NOSONAR
import io.reactivex.Observable; // NOSONAR
import io.reactivex.schedulers.Schedulers; // NOSONAR
import java.io.File; // NOSONAR
import java.io.IOException; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.HashMap; // NOSONAR
import java.util.List; // NOSONAR
import java.util.concurrent.TimeUnit; // NOSONAR
import java.util.logging.Level; // NOSONAR
import java.util.logging.Logger; // NOSONAR
import javax.inject.Inject; // NOSONAR
import org.jaudiotagger.audio.AudioFile; // NOSONAR
import org.jaudiotagger.audio.AudioFileIO; // NOSONAR
import org.jaudiotagger.audio.exceptions.CannotReadException; // NOSONAR
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException; // NOSONAR
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException; // NOSONAR
import org.jaudiotagger.tag.FieldKey; // NOSONAR
import org.jaudiotagger.tag.Tag; // NOSONAR
import org.jaudiotagger.tag.TagException; // NOSONAR
import org.jaudiotagger.tag.TagOptionSingleton; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ShuttleApplication extends DaggerApplication { //NOSONAR

    private static final String TAG = "ShuttleApplication"; //NOSONAR

    private boolean isUpgraded; //NOSONAR

    private RefWatcher refWatcher; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public HashMap<String, UserSelectedArtwork> userSelectedArtwork = new HashMap<>(); //NOSONAR

    private static Logger jaudioTaggerLogger1 = Logger.getLogger("org.jaudiotagger.audio"); //NOSONAR
    private static Logger jaudioTaggerLogger2 = Logger.getLogger("org.jaudiotagger"); //NOSONAR

    @Inject //NOSONAR
    Repository.SongsRepository songsRepository; //NOSONAR

    @Inject //NOSONAR
    AnalyticsManager analyticsManager; //NOSONAR

    @Inject //NOSONAR
    SettingsManager settingsManager; //NOSONAR

    @Override //NOSONAR
    public void onCreate() { //NOSONAR
        super.onCreate(); //NOSONAR

        DaggerAppComponent.builder() //NOSONAR
                .create(this) //NOSONAR
                .inject(this); //NOSONAR

        if (LeakCanary.isInAnalyzerProcess(this)) { //NOSONAR
            // This process is dedicated to LeakCanary for heap analysis. // NOSONAR
            // You should not init your app in this process. // NOSONAR
            return; //NOSONAR
        } // NOSONAR

        // To do later: Remove for production builds. Useful for tracking down crashes in beta. // NOSONAR
        RxDogTag.install(); //NOSONAR

        if (BuildConfig.DEBUG) { //NOSONAR
            // enableStrictMode(); // NOSONAR
        } // NOSONAR

        refWatcher = LeakCanary.install(this); //NOSONAR
        // workaround to fix InputMethodManager leak as suggested by LeakCanary lib // NOSONAR
        InputMethodManagerLeaks.fixFocusedViewLeak(this); //NOSONAR

        //Crashlytics // NOSONAR
        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder() //NOSONAR
                .disabled(BuildConfig.DEBUG) //NOSONAR
                .build(); //NOSONAR

        Fabric.with(this, //NOSONAR
                new Crashlytics.Builder() //NOSONAR
                        .core(crashlyticsCore) //NOSONAR
                        .answers(new Answers()) //NOSONAR
                        .build()); //NOSONAR

        // Firebase // NOSONAR
        FirebaseApp.initializeApp(this); //NOSONAR
        FirebaseAnalytics.getInstance(this); //NOSONAR

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); //NOSONAR
        // we cannot call setDefaultValues for multiple fragment based XML preference // NOSONAR
        // files with readAgain flag set to false, so always check KEY_HAS_SET_DEFAULT_VALUES // NOSONAR
        if (!prefs.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false)) { //NOSONAR
            PreferenceManager.setDefaultValues(this, R.xml.settings_headers, true); //NOSONAR
            PreferenceManager.setDefaultValues(this, R.xml.settings_artwork, true); //NOSONAR
            PreferenceManager.setDefaultValues(this, R.xml.settings_blacklist, true); //NOSONAR
            PreferenceManager.setDefaultValues(this, R.xml.settings_display, true); //NOSONAR
            PreferenceManager.setDefaultValues(this, R.xml.settings_headset, true); //NOSONAR
            PreferenceManager.setDefaultValues(this, R.xml.settings_scrobbling, true); //NOSONAR
            PreferenceManager.setDefaultValues(this, R.xml.settings_themes, true); //NOSONAR
        } // NOSONAR

        // Turn off logging for jaudiotagger. // NOSONAR
        jaudioTaggerLogger1.setLevel(Level.OFF); //NOSONAR
        jaudioTaggerLogger2.setLevel(Level.OFF); //NOSONAR

        TagOptionSingleton.getInstance().setPadNumbers(true); //NOSONAR

        settingsManager.incrementLaunchCount(); //NOSONAR

        Completable.fromAction(() -> { //NOSONAR
            Query query = new Query.Builder() //NOSONAR
                    .uri(CustomArtworkTable.URI) //NOSONAR
                    .projection(new String[] { CustomArtworkTable.COLUMN_ID, CustomArtworkTable.COLUMN_KEY, CustomArtworkTable.COLUMN_TYPE, CustomArtworkTable.COLUMN_PATH }) //NOSONAR
                    .build(); //NOSONAR

            SqlUtils.createActionableQuery(ShuttleApplication.this, cursor -> //NOSONAR
                            userSelectedArtwork.put( //NOSONAR
                                    cursor.getString(cursor.getColumnIndexOrThrow(CustomArtworkTable.COLUMN_KEY)), //NOSONAR
                                    new UserSelectedArtwork( //NOSONAR
                                            cursor.getInt(cursor.getColumnIndexOrThrow(CustomArtworkTable.COLUMN_TYPE)), //NOSONAR
                                            cursor.getString(cursor.getColumnIndexOrThrow(CustomArtworkTable.COLUMN_PATH))) //NOSONAR
                            ), // NOSONAR
                    query); //NOSONAR
        }) // NOSONAR
                .doOnError(throwable -> LogUtils.logException(TAG, "Error updating user selected artwork", throwable)) //NOSONAR
                .onErrorComplete() //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe(); //NOSONAR

        Completable.timer(5, TimeUnit.SECONDS) //NOSONAR
                .andThen(Completable.defer(this::repairMediaStoreYearFromTags)) //NOSONAR
                .doOnError(throwable -> LogUtils.logException(TAG, "Failed to update year from tags", throwable)) //NOSONAR
                .onErrorComplete() //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe(); //NOSONAR

        Completable.timer(10, TimeUnit.SECONDS) //NOSONAR
                .andThen(Completable.defer(this::cleanGenres)) //NOSONAR
                .doOnError(throwable -> LogUtils.logException(TAG, "Failed to clean genres", throwable)) //NOSONAR
                .onErrorComplete() //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe(); //NOSONAR

        Completable.timer(15, TimeUnit.SECONDS) //NOSONAR
                .andThen(Completable.defer(this::cleanMostPlayedPlaylist)) //NOSONAR
                .doOnError(throwable -> LogUtils.logException(TAG, "Failed to clean most played", throwable)) //NOSONAR
                .onErrorComplete() //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe(); //NOSONAR

        Completable.timer(20, TimeUnit.SECONDS) //NOSONAR
                .andThen(Completable.defer(() -> LegacyUtils.deleteOldResources(this))) //NOSONAR
                .doOnError(throwable -> LogUtils.logException(TAG, "Failed to delete old resources", throwable)) //NOSONAR
                .onErrorComplete() //NOSONAR
                .subscribeOn(Schedulers.io()) //NOSONAR
                .subscribe(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected AndroidInjector<? extends dagger.android.DaggerApplication> applicationInjector() { //NOSONAR
        return DaggerAppComponent.builder().create(this); //NOSONAR
    } // NOSONAR

    public RefWatcher getRefWatcher() { //NOSONAR
        return this.refWatcher; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onLowMemory() { //NOSONAR
        super.onLowMemory(); //NOSONAR

        Glide.get(this).clearMemory(); //NOSONAR
    } // NOSONAR

    public String getVersion() { //NOSONAR
        try { //NOSONAR
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName; //NOSONAR
        } catch (PackageManager.NameNotFoundException | NullPointerException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        return "unknown"; //NOSONAR
    } // NOSONAR

    public void setIsUpgraded(boolean isUpgraded) { //NOSONAR
        this.isUpgraded = isUpgraded; //NOSONAR
        analyticsManager.setIsUpgraded(isUpgraded); //NOSONAR
    } // NOSONAR

    public boolean getIsUpgraded() { //NOSONAR
        return isUpgraded || BuildConfig.DEBUG; //NOSONAR
    } // NOSONAR

    public File getDiskCacheDir(String uniqueName) { //NOSONAR
        try { //NOSONAR
            // Check if media is mounted or storage is built-in, if so, try and use external cache dir // NOSONAR
            // otherwise use internal cache dir // NOSONAR
            String cachePath = null; //NOSONAR
            File externalCacheDir = getExternalCacheDir(); //NOSONAR
            if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) && externalCacheDir != null) { //NOSONAR
                cachePath = externalCacheDir.getPath(); //NOSONAR
            } else if (getCacheDir() != null) { //NOSONAR
                cachePath = getCacheDir().getPath(); //NOSONAR
            } // NOSONAR
            if (cachePath != null) { //NOSONAR
                return new File(cachePath + File.separator + uniqueName); //NOSONAR
            } // NOSONAR
        } catch (RuntimeException e) { //NOSONAR
            Log.e(TAG, "getDiskCacheDir() failed. " + e.toString()); //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Check items in the Most Played playlist and ensure their ids exist in the MediaStore. // NOSONAR
     * <p> // NOSONAR
     * If they don't, remove them from the playlist. // NOSONAR
     */ // NOSONAR
    @NonNull //NOSONAR
    private Completable cleanMostPlayedPlaylist() { //NOSONAR

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //NOSONAR
            return Completable.complete(); //NOSONAR
        } // NOSONAR

        return Completable.fromAction(() -> { //NOSONAR
            List<Integer> playCountIds = new ArrayList<>(); //NOSONAR

            Query query = new Query.Builder() //NOSONAR
                    .uri(PlayCountTable.URI) //NOSONAR
                    .projection(new String[] { PlayCountTable.COLUMN_ID }) //NOSONAR
                    .build(); //NOSONAR

            SqlUtils.createActionableQuery(this, cursor -> //NOSONAR
                    playCountIds.add(cursor.getInt(cursor.getColumnIndex(PlayCountTable.COLUMN_ID))), query); //NOSONAR

            List<Integer> songIds = new ArrayList<>(); //NOSONAR

            query = new Query.Builder() //NOSONAR
                    .uri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) //NOSONAR
                    .projection(new String[] { MediaStore.Audio.Media._ID }) //NOSONAR
                    .build(); //NOSONAR

            SqlUtils.createActionableQuery(this, cursor -> //NOSONAR
                    songIds.add(cursor.getInt(cursor.getColumnIndex(PlayCountTable.COLUMN_ID))), query); //NOSONAR

            StringBuilder selection = new StringBuilder(PlayCountTable.COLUMN_ID + " IN ("); //NOSONAR

            selection.append(TextUtils.join(",", Stream.of(playCountIds) //NOSONAR
                    .filter(playCountId -> !songIds.contains(playCountId)) //NOSONAR
                    .toList())); //NOSONAR

            selection.append(")"); //NOSONAR

            try { //NOSONAR
                getContentResolver().delete(PlayCountTable.URI, selection.toString(), null); //NOSONAR
            } catch (IllegalArgumentException ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        }); // NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    private Completable cleanGenres() { //NOSONAR

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //NOSONAR
            return Completable.complete(); //NOSONAR
        } // NOSONAR

        // This observable emits a genre every 50ms. We then make a query against the genre database to populate the song count. // NOSONAR
        // If the count is zero, then the genre can be deleted. // NOSONAR
        // The reason for the delay is, on some slower devices, if the user has tons of genres, a ton of cursors get created. // NOSONAR
        // If the maximum number of cursors is created (based on memory/processor speed or god knows what else), then the device // NOSONAR
        // will start throwing CursorWindow exceptions, and the queries will slow down massively. This ends up making all queries slow. // NOSONAR
        // This task isn't time critical, so we can afford to let it just casually do its job. // NOSONAR
        return SqlBriteUtils.createSingleList(this, Genre::new, Genre.getQuery()) //NOSONAR
                .flatMapObservable(Observable::fromIterable) //NOSONAR
                .concatMap(genre -> Observable.just(genre).delay(50, TimeUnit.MILLISECONDS)) //NOSONAR
                .flatMapSingle(genre -> GenreExtKt.getSongsObservable(genre, getApplicationContext()) //NOSONAR
                        .doOnSuccess(songs -> { //NOSONAR
                            if (songs.isEmpty()) { //NOSONAR
                                try { //NOSONAR
                                    getContentResolver().delete(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, MediaStore.Audio.Genres._ID + " == " + genre.id, null); //NOSONAR
                                } catch (IllegalArgumentException | UnsupportedOperationException ignored) { //NOSONAR
                                    //Don't care if we couldn't delete this uri. // NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                        }) // NOSONAR
                ).flatMapCompletable(songs -> Completable.complete()); //NOSONAR
    } // NOSONAR

    @NonNull //NOSONAR
    private Completable repairMediaStoreYearFromTags() { //NOSONAR

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //NOSONAR
            return Completable.complete(); //NOSONAR
        } // NOSONAR

        return songsRepository.getSongs(value -> value.year < 1) //NOSONAR
                .first(Collections.emptyList()) //NOSONAR
                .flatMapObservable(Observable::fromIterable) //NOSONAR
                .concatMap(song -> Observable.just(song).delay(50, TimeUnit.MILLISECONDS)) //NOSONAR
                .flatMap(song -> { //NOSONAR
                            if (!TextUtils.isEmpty(song.path)) { //NOSONAR
                                File file = new File(song.path); //NOSONAR
                                // Don't bother checking files > 100mb, uses too much memory. // NOSONAR
                                if (file.exists() && file.length() < 100 * 1024 * 1024) { //NOSONAR
                                    try { //NOSONAR
                                        AudioFile audioFile = AudioFileIO.read(file); //NOSONAR
                                        Tag tag = audioFile.getTag(); //NOSONAR
                                        if (tag != null) { //NOSONAR
                                            String year = tag.getFirst(FieldKey.YEAR); //NOSONAR
                                            int yearInt = StringUtils.parseInt(year); //NOSONAR
                                            if (yearInt > 0) { //NOSONAR
                                                song.year = yearInt; //NOSONAR
                                                ContentValues contentValues = new ContentValues(); //NOSONAR
                                                contentValues.put(MediaStore.Audio.Media.YEAR, yearInt); //NOSONAR

                                                return Observable.just(ContentProviderOperation //NOSONAR
                                                        .newUpdate(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id)) //NOSONAR
                                                        .withValues(contentValues) //NOSONAR
                                                        .build()); //NOSONAR
                                            } // NOSONAR
                                        } // NOSONAR
                                    } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | OutOfMemoryError e) { //NOSONAR
                                        LogUtils.logException(TAG, "Failed to repair media store year", e); //NOSONAR
                                    } // NOSONAR
                                } // NOSONAR
                            } // NOSONAR
                            return Observable.empty(); //NOSONAR
                        } // NOSONAR

                ).toList() //NOSONAR
                .doOnSuccess(contentProviderOperations -> { //NOSONAR
                    getContentResolver().applyBatch(MediaStore.AUTHORITY, new ArrayList<>(contentProviderOperations)); //NOSONAR
                }) // NOSONAR
                .flatMapCompletable(songs -> Completable.complete()); //NOSONAR
    } // NOSONAR

    private void enableStrictMode() { //NOSONAR
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder() //NOSONAR
                .detectAll() //NOSONAR
                .penaltyLog() //NOSONAR
                .build()); //NOSONAR

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //NOSONAR
                .detectAll() //NOSONAR
                .penaltyLog() //NOSONAR
                .penaltyFlashScreen() //NOSONAR
                .build()); //NOSONAR
    } // NOSONAR
} // NOSONAR
