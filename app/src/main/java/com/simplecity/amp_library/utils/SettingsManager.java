package com.simplecity.amp_library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import com.simplecity.amp_library.BuildConfig;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.CategoryItem;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.utils.sorting.SortManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SettingsManager extends BaseSettingsManager { //NOSONAR

    private static final String TAG = "SettingsManager"; //NOSONAR

    @Inject //NOSONAR
    public SettingsManager(@NotNull SharedPreferences sharedPreferences) { //NOSONAR
        super(sharedPreferences); //NOSONAR
    }

    // Support
    public static String KEY_PREF_CHANGELOG = "pref_changelog"; //NOSONAR
    public static String KEY_PREF_FAQ = "pref_faq"; //NOSONAR
    public static String KEY_PREF_HELP = "pref_help"; //NOSONAR
    public static String KEY_PREF_RATE = "pref_rate"; //NOSONAR
    public static String KEY_PREF_RESTORE_PURCHASES = "pref_restore_purchases"; //NOSONAR

    // Display
    public static String KEY_PREF_TAB_CHOOSER = "pref_tab_chooser"; //NOSONAR
    public static String KEY_PREF_DEFAULT_PAGE = "pref_default_page"; //NOSONAR
    public static String KEY_DISPLAY_REMAINING_TIME = "pref_display_remaining_time"; //NOSONAR

    // Themes
    public static String KEY_PREF_THEME_BASE = "pref_theme_base"; //NOSONAR
    public static String KEY_PREF_PRIMARY_COLOR = "pref_theme_primary_color"; //NOSONAR
    public static String KEY_PREF_ACCENT_COLOR = "pref_theme_accent_color"; //NOSONAR
    public static String KEY_PREF_NAV_BAR = "pref_nav_bar"; //NOSONAR
    public static String KEY_PREF_PALETTE = "pref_theme_use_palette"; //NOSONAR
    public static String KEY_PREF_PALETTE_NOW_PLAYING_ONLY = "pref_theme_use_palette_now_playing"; //NOSONAR

    // Artwork
    public static String KEY_PREF_DOWNLOAD_ARTWORK = "pref_download_artwork"; //NOSONAR
    public static String KEY_PREF_DELETE_ARTWORK = "pref_delete_artwork"; //NOSONAR

    // Scrobbler
    public static String KEY_PREF_DOWNLOAD_SCROBBLER = "pref_download_simple_lastfm_scrobbler"; //NOSONAR

    // Blacklist/whitelist
    public static String KEY_PREF_BLACKLIST = "pref_blacklist_view"; //NOSONAR
    public static String KEY_PREF_WHITELIST = "pref_whitelist_view"; //NOSONAR

    // Playback
    public static String KEY_PREF_REMEMBER_SHUFFLE = "pref_remember_shuffle"; //NOSONAR

    // Upgrade
    public static String KEY_PREF_UPGRADE = "pref_upgrade"; //NOSONAR

    // Whether the 'rate' snackbar has been seen during this session
    @SuppressWarnings("java:S1104") //NOSONAR
    public boolean hasSeenRateSnackbar = false; //NOSONAR

    // Whether to display artwork in the songs list
    public static final String KEY_SHOW_LOCKSCREEN_ARTWORK = "pref_show_lockscreen_artwork"; //NOSONAR

    public boolean showLockscreenArtwork() { //NOSONAR
        return getBool(KEY_SHOW_LOCKSCREEN_ARTWORK, true); //NOSONAR
    }

    private static final String KEY_KEEP_SCREEN_ON = "pref_screen_on"; //NOSONAR

    public boolean keepScreenOn() { //NOSONAR
        return getBool(KEY_KEEP_SCREEN_ON, false); //NOSONAR
    }

    public boolean displayRemainingTime() { //NOSONAR
        return getBool(KEY_DISPLAY_REMAINING_TIME, true); //NOSONAR
    }

    private static final String KEY_ALBUM_DISPLAY_TYPE = "album_display_type_new"; //NOSONAR

    public void setAlbumDisplayType(int type) { //NOSONAR
        setInt(KEY_ALBUM_DISPLAY_TYPE, type); //NOSONAR
    }

    @ViewType //NOSONAR
    public int getAlbumDisplayType(Context context) { //NOSONAR
        return getInt(KEY_ALBUM_DISPLAY_TYPE, ShuttleUtils.isTablet(context) ? ViewType.ALBUM_PALETTE : ViewType.ALBUM_LIST); //NOSONAR
    }

    private static final String KEY_ARTIST_DISPLAY_TYPE = "artist_display_type_new"; //NOSONAR

    public void setArtistDisplayType(int type) { //NOSONAR
        setInt(KEY_ARTIST_DISPLAY_TYPE, type); //NOSONAR
    }

    @ViewType //NOSONAR
    public int getArtistDisplayType() { //NOSONAR
        return getInt(KEY_ARTIST_DISPLAY_TYPE, ViewType.ARTIST_PALETTE); //NOSONAR
    }

    private static final String KEY_ARTIST_COLUMN_COUNT = "artist_column_count"; //NOSONAR
    private static final String KEY_ARTIST_COLUMN_COUNT_LAND = "artist_column_count_land"; //NOSONAR
    private static final String KEY_ARTIST_COLUMN_COUNT_TABLET = "artist_column_count_tablet"; //NOSONAR
    private static final String KEY_ARTIST_COLUMN_COUNT_TABLET_LAND = "artist_column_count_tablet_land"; //NOSONAR

    private String getArtistColumnCountKey(Context context) { //NOSONAR
        String key = KEY_ARTIST_COLUMN_COUNT; //NOSONAR

        if (ShuttleUtils.isLandscape(context)) { //NOSONAR
            key = ShuttleUtils.isTablet(context) ? KEY_ARTIST_COLUMN_COUNT_TABLET_LAND : KEY_ARTIST_COLUMN_COUNT_LAND; //NOSONAR
        } else { //NOSONAR
            if (ShuttleUtils.isTablet(context)) key = KEY_ARTIST_COLUMN_COUNT_TABLET; //NOSONAR
        }

        return key; //NOSONAR
    }

    public void setArtistColumnCount(Context context, int count) { //NOSONAR
        setInt(getArtistColumnCountKey(context), count); //NOSONAR
    }

    public int getArtistColumnCount(Context context) { //NOSONAR
        int artistDisplayType = getArtistDisplayType(); //NOSONAR
        int defaultSpanCount = //NOSONAR
                artistDisplayType == ViewType.ARTIST_LIST ? context.getResources().getInteger(R.integer.list_num_columns) : context.getResources().getInteger(R.integer.grid_num_columns); //NOSONAR
        if (artistDisplayType == ViewType.ARTIST_LIST && defaultSpanCount == 1) { //NOSONAR
            return 1; //NOSONAR
        }
        return getInt(getArtistColumnCountKey(context), defaultSpanCount); //NOSONAR
    }

    private static final String KEY_ALBUM_COLUMN_COUNT = "album_column_count"; //NOSONAR
    private static final String KEY_ALBUM_COLUMN_COUNT_LAND = "album_column_count_land"; //NOSONAR
    private static final String KEY_ALBUM_COLUMN_COUNT_TABLET = "album_column_count_tablet"; //NOSONAR
    private static final String KEY_ALBUM_COLUMN_COUNT_TABLET_LAND = "album_column_count_tablet_land"; //NOSONAR

    private String getAlbumColumnCountKey(Context context) { //NOSONAR
        String key = KEY_ALBUM_COLUMN_COUNT; //NOSONAR

        if (ShuttleUtils.isLandscape(context)) { //NOSONAR
            key = ShuttleUtils.isTablet(context) ? KEY_ALBUM_COLUMN_COUNT_TABLET_LAND : KEY_ALBUM_COLUMN_COUNT_LAND; //NOSONAR
        } else { //NOSONAR
            if (ShuttleUtils.isTablet(context)) key = KEY_ALBUM_COLUMN_COUNT_TABLET; //NOSONAR
        }

        return key; //NOSONAR
    }

    public void setAlbumColumnCount(Context context, int count) { //NOSONAR
        setInt(getAlbumColumnCountKey(context), count); //NOSONAR
    }

    public int getAlbumColumnCount(Context context) { //NOSONAR
        int albumDisplayType = getAlbumDisplayType(context); //NOSONAR
        int defaultSpanCount = albumDisplayType == ViewType.ALBUM_LIST ? context.getResources().getInteger(R.integer.list_num_columns) : context.getResources().getInteger(R.integer.grid_num_columns); //NOSONAR
        if (albumDisplayType == ViewType.ALBUM_LIST && defaultSpanCount == 1) { //NOSONAR
            return 1; //NOSONAR
        }
        return getInt(getAlbumColumnCountKey(context), defaultSpanCount); //NOSONAR
    }

    public boolean getEqualizerEnabled() { //NOSONAR
        return getBool("audiofx.global.enable", false); //NOSONAR
    }

    private static final String DOCUMENT_TREE_URI = "document_tree_uri"; //NOSONAR

    public void setDocumentTreeUri(String documentTreeUri) { //NOSONAR
        setString(DOCUMENT_TREE_URI, documentTreeUri); //NOSONAR
    }

    @Nullable //NOSONAR
    public String getDocumentTreeUri() { //NOSONAR
        return getString(DOCUMENT_TREE_URI); //NOSONAR
    }

    private static final String KEY_FOLDER_BROWSER_INITIAL_DIR = "folder_browser_initial_dir"; //NOSONAR

    public void setFolderBrowserInitialDir(String dir) { //NOSONAR
        setString(KEY_FOLDER_BROWSER_INITIAL_DIR, dir); //NOSONAR
    }

    public String getFolderBrowserInitialDir() { //NOSONAR
        return getString(KEY_FOLDER_BROWSER_INITIAL_DIR, ""); //NOSONAR
    }

    private static final String KEY_FOLDER_BROWSER_FILES_SORT_ORDER = "folder_browser_files_sort_order"; //NOSONAR

    public void setFolderBrowserFilesSortOrder(String sortOrder) { //NOSONAR
        setString(KEY_FOLDER_BROWSER_FILES_SORT_ORDER, sortOrder); //NOSONAR
    }

    public String getFolderBrowserFilesSortOrder() { //NOSONAR
        return getString(KEY_FOLDER_BROWSER_FILES_SORT_ORDER, SortManager.SortFiles.DEFAULT); //NOSONAR
    }

    private static final String KEY_FOLDER_BROWSER_FILES_ASCENDING = "folder_browser_files_ascending"; //NOSONAR

    public void setFolderBrowserFilesAscending(boolean ascending) { //NOSONAR
        setBool(KEY_FOLDER_BROWSER_FILES_ASCENDING, ascending); //NOSONAR
    }

    public boolean getFolderBrowserFilesAscending() { //NOSONAR
        return getBool(KEY_FOLDER_BROWSER_FILES_ASCENDING, true); //NOSONAR
    }

    private static final String KEY_FOLDER_BROWSER_FOLDERS_SORT_ORDER = "folder_browser_folders_sort_order"; //NOSONAR

    public void setFolderBrowserFoldersSortOrder(String sortOrder) { //NOSONAR
        setString(KEY_FOLDER_BROWSER_FOLDERS_SORT_ORDER, sortOrder); //NOSONAR
    }

    public String getFolderBrowserFoldersSortOrder() { //NOSONAR
        return getString(KEY_FOLDER_BROWSER_FOLDERS_SORT_ORDER, SortManager.SortFolders.DEFAULT); //NOSONAR
    }

    private static final String KEY_FOLDER_BROWSER_FOLDERS_ASCENDING = "folder_browser_folders_ascending"; //NOSONAR

    public void setFolderBrowserFoldersAscending(boolean ascending) { //NOSONAR
        setBool(KEY_FOLDER_BROWSER_FOLDERS_ASCENDING, ascending); //NOSONAR
    }

    public boolean getFolderBrowserFoldersAscending() { //NOSONAR
        return getBool(KEY_FOLDER_BROWSER_FOLDERS_ASCENDING, true); //NOSONAR
    }

    private static final String KEY_FOLDER_BROWSER_SHOW_FILENAMES = "folder_browser_show_file_names"; //NOSONAR

    public void setFolderBrowserShowFileNames(boolean showFileNames) { //NOSONAR
        setBool(KEY_FOLDER_BROWSER_SHOW_FILENAMES, showFileNames); //NOSONAR
    }

    public boolean getFolderBrowserShowFileNames() { //NOSONAR
        return getBool(KEY_FOLDER_BROWSER_SHOW_FILENAMES, false); //NOSONAR
    }

    private static final String KEY_LAUNCH_COUNT = "launch_count"; //NOSONAR

    public void incrementLaunchCount() { //NOSONAR
        setInt(KEY_LAUNCH_COUNT, getLaunchCount() + 1); //NOSONAR
    }

    public int getLaunchCount() { //NOSONAR
        return getInt(KEY_LAUNCH_COUNT, 0); //NOSONAR
    }

    private static final String KEY_NAG_MESSAGE_READ = "nag_message_read"; //NOSONAR

    public void setNagMessageRead() { //NOSONAR
        setBool(KEY_NAG_MESSAGE_READ, true); //NOSONAR
    }

    public boolean getNagMessageRead() { //NOSONAR
        return getBool(KEY_NAG_MESSAGE_READ, false); //NOSONAR
    }

    private static final String KEY_HAS_RATED = "has_rated"; //NOSONAR

    public void setHasRated() { //NOSONAR
        setBool(KEY_HAS_RATED, true); //NOSONAR
    }

    public boolean getHasRated() { //NOSONAR
        return getBool(KEY_HAS_RATED, false); //NOSONAR
    }

    private static final String KEY_BLUETOOTH_PAUSE_DISCONNECT = "pref_bluetooth_disconnect"; //NOSONAR
    private static final String KEY_BLUETOOTH_RESUME_CONNECT = "pref_bluetooth_connect"; //NOSONAR

    public boolean getBluetoothPauseDisconnect() { //NOSONAR
        return getBool(KEY_BLUETOOTH_PAUSE_DISCONNECT, true); //NOSONAR
    }

    public boolean getBluetoothResumeConnect() { //NOSONAR
        return getBool(KEY_BLUETOOTH_RESUME_CONNECT, false); //NOSONAR
    }

    // Themes

    public boolean getUsePalette() { //NOSONAR
        return getBool(KEY_PREF_PALETTE, true); //NOSONAR
    }

    public boolean getUsePaletteNowPlayingOnly() { //NOSONAR
        return getBool(KEY_PREF_PALETTE_NOW_PLAYING_ONLY, false); //NOSONAR
    }

    public boolean getTintNavBar() { //NOSONAR
        return getBool(KEY_PREF_NAV_BAR, false); //NOSONAR
    }

    public void storePrimaryColor(int color) { //NOSONAR
        setInt(KEY_PREF_PRIMARY_COLOR, color); //NOSONAR
    }

    public int getPrimaryColor() { //NOSONAR
        return getInt(KEY_PREF_PRIMARY_COLOR, -1); //NOSONAR
    }

    public void storeAccentColor(int color) { //NOSONAR
        setInt(KEY_PREF_ACCENT_COLOR, color); //NOSONAR
    }

    public int getAccentColor() { //NOSONAR
        return getInt(KEY_PREF_ACCENT_COLOR, -1); //NOSONAR
    }

    // Artwork

    private static final String KEY_DOWNLOAD_AUTOMATICALLY = "pref_download_artwork_auto"; //NOSONAR
    private static final String KEY_USE_GMAIL_PLACEHOLDERS = "pref_placeholders"; //NOSONAR
    private static final String KEY_QUEUE_ARTWORK = "pref_artwork_queue"; //NOSONAR
    private static final String KEY_QUEUE_SWIPE_LOCKED = "pref_lock_queue"; //NOSONAR
    private static final String KEY_SONG_LIST_ARTWORK = "pref_artwork_song_list"; //NOSONAR
    private static final String KEY_CROP_ARTWORK = "pref_crop_artwork"; //NOSONAR
    public static final String KEY_IGNORE_MEDIASTORE_ART = "pref_ignore_mediastore_artwork"; //NOSONAR
    public static final String KEY_IGNORE_EMBEDDED_ARTWORK = "pref_ignore_embedded_artwork"; //NOSONAR
    public static final String KEY_IGNORE_FOLDER_ARTWORK = "pref_ignore_folder_artwork"; //NOSONAR
    public static final String KEY_PREFER_EMBEDDED_ARTWORK = "pref_prefer_embedded"; //NOSONAR

    public boolean canDownloadArtworkAutomatically() { //NOSONAR
        return getBool(KEY_DOWNLOAD_AUTOMATICALLY, false); //NOSONAR
    }

    public boolean preferEmbeddedArtwork() { //NOSONAR
        return getBool(KEY_PREFER_EMBEDDED_ARTWORK, false); //NOSONAR
    }

    public boolean useGmailPlaceholders() { //NOSONAR
        return getBool(KEY_USE_GMAIL_PLACEHOLDERS, false); //NOSONAR
    }

    public boolean showArtworkInQueue() { //NOSONAR
        return getBool(KEY_QUEUE_ARTWORK, true); //NOSONAR
    }

    public boolean queueSwipeLocked() { //NOSONAR
        return getBool(KEY_QUEUE_SWIPE_LOCKED, false); //NOSONAR
    }

    public void setQueueSwipeLocked(boolean locked) { //NOSONAR
        setBool(KEY_QUEUE_SWIPE_LOCKED, locked); //NOSONAR
    }

    public boolean cropArtwork() { //NOSONAR
        return getBool(KEY_CROP_ARTWORK, false); //NOSONAR
    }

    public boolean ignoreMediaStoreArtwork() { //NOSONAR
        return getBool(KEY_IGNORE_MEDIASTORE_ART, false); //NOSONAR
    }

    public boolean ignoreFolderArtwork() { //NOSONAR
        return getBool(KEY_IGNORE_FOLDER_ARTWORK, false); //NOSONAR
    }

    public boolean ignoreEmbeddedArtwork() { //NOSONAR
        return getBool(KEY_IGNORE_EMBEDDED_ARTWORK, false); //NOSONAR
    }

    private static final String KEY_PLAYLIST_IGNORE_DUPLICATES = "pref_ignore_duplicates"; //NOSONAR

    public boolean ignoreDuplicates() { //NOSONAR
        return getBool(KEY_PLAYLIST_IGNORE_DUPLICATES, false); //NOSONAR
    }

    public void setIgnoreDuplicates(boolean ignoreDuplicates) { //NOSONAR
        setBool(KEY_PLAYLIST_IGNORE_DUPLICATES, ignoreDuplicates); //NOSONAR
    }

    // Search settings

    private static final String KEY_SEARCH_FUZZY = "search_fuzzy"; //NOSONAR

    public void setSearchFuzzy(boolean fuzzy) { //NOSONAR
        setBool(KEY_SEARCH_FUZZY, fuzzy); //NOSONAR
    }

    public boolean getSearchFuzzy() { //NOSONAR
        return getBool(KEY_SEARCH_FUZZY, true); //NOSONAR
    }

    private static final String KEY_SEARCH_ARTISTS = "search_artists"; //NOSONAR

    public void setSearchArtists(boolean searchArtists) { //NOSONAR
        setBool(KEY_SEARCH_ARTISTS, searchArtists); //NOSONAR
    }

    public boolean getSearchArtists() { //NOSONAR
        return getBool(KEY_SEARCH_ARTISTS, true); //NOSONAR
    }

    private static final String KEY_SEARCH_ALBUMS = "search_albums"; //NOSONAR

    public void setSearchAlbums(boolean searchAlbums) { //NOSONAR
        setBool(KEY_SEARCH_ALBUMS, searchAlbums); //NOSONAR
    }

    public boolean getSearchAlbums() { //NOSONAR
        return getBool(KEY_SEARCH_ALBUMS, true); //NOSONAR
    }

    // Changelog

    private static final String KEY_VERSION_CODE = "version_code"; //NOSONAR

    public void setVersionCode() { //NOSONAR
        setInt(KEY_VERSION_CODE, BuildConfig.VERSION_CODE); //NOSONAR
    }

    public int getStoredVersionCode() { //NOSONAR
        return getInt(KEY_VERSION_CODE, -1); //NOSONAR
    }

    private static final String KEY_CHANGELOG_SHOW_ON_LAUNCH = "show_on_launch"; //NOSONAR

    public void setShowChangelogOnLaunch(boolean showOnLaunch) { //NOSONAR
        setBool(KEY_CHANGELOG_SHOW_ON_LAUNCH, showOnLaunch); //NOSONAR
    }

    public boolean getShowChangelogOnLaunch() { //NOSONAR
        return getBool(KEY_CHANGELOG_SHOW_ON_LAUNCH, true); //NOSONAR
    }

    // Playback

    public boolean getRememberShuffle() { //NOSONAR
        return getBool(KEY_PREF_REMEMBER_SHUFFLE, false); //NOSONAR
    }

    public void setRememberShuffle(boolean rememberShuffle) { //NOSONAR
        setBool(KEY_PREF_REMEMBER_SHUFFLE, rememberShuffle); //NOSONAR
    }

    // Library Controller

    private static final String KEY_DEFAULT_PAGE = "default_page"; //NOSONAR

    @CategoryItem.Type //NOSONAR
    public int getDefaultPageType() { //NOSONAR
        return getInt(KEY_DEFAULT_PAGE, CategoryItem.Type.ARTISTS); //NOSONAR
    }

    public void setDefaultPageType(@CategoryItem.Type int type) { //NOSONAR
        setInt(KEY_DEFAULT_PAGE, type); //NOSONAR
    }

    // Legacy Upgrade Preference
    private static final String KEY_UPGRADED = "pref_theme_gold"; //NOSONAR

    public boolean getIsLegacyUpgraded() { //NOSONAR
        return getBool(KEY_UPGRADED, false); //NOSONAR
    }

    // Recently added

    private static final String KEY_NUM_WEEKS = "numweeks"; //NOSONAR

    public int getNumWeeks() { //NOSONAR
        return getInt(KEY_NUM_WEEKS, 2); //NOSONAR
    }

    public void setNumWeeks(int weeks) { //NOSONAR
        setInt(KEY_NUM_WEEKS, weeks); //NOSONAR
    }

    // Song List

    public boolean showArtworkInSongList() { //NOSONAR
        return getBool(KEY_SONG_LIST_ARTWORK, true); //NOSONAR
    }

    public void setShowArtworkInSongList(boolean showArtworkInSongList) { //NOSONAR
        setBool(KEY_SONG_LIST_ARTWORK, showArtworkInSongList); //NOSONAR
    }
}
