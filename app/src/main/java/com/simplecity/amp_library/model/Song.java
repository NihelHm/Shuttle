package com.simplecity.amp_library.model; // NOSONAR

import android.content.ContentUris; // NOSONAR
import android.content.Context; // NOSONAR
import android.database.Cursor; // NOSONAR
import android.net.Uri; // NOSONAR
import android.provider.MediaStore; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.text.TextUtils; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.sql.SqlUtils; // NOSONAR
import com.simplecity.amp_library.sql.providers.PlayCountTable; // NOSONAR
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils; // NOSONAR
import com.simplecity.amp_library.utils.ArtworkUtils; // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils; // NOSONAR
import com.simplecity.amp_library.utils.FileHelper; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR
import io.reactivex.Single; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR
import java.io.Serializable; // NOSONAR
import java.io.UnsupportedEncodingException; // NOSONAR
import java.net.URLEncoder; // NOSONAR
import java.nio.charset.Charset; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Song implements //NOSONAR
        Serializable, //NOSONAR
        Comparable<Song>, //NOSONAR
        ArtworkProvider, //NOSONAR
        Sortable { //NOSONAR

    private static final String TAG = "Song"; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String name; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String artistName; //NOSONAR
    private long artistId; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String albumName; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long albumId; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long duration; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int year; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int dateAdded; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long playlistSongId; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long playlistSongPlayOrder; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int playCount; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long lastPlayed; //NOSONAR
    private long startTime; //NOSONAR
    private long elapsedTime = 0; //NOSONAR
    private boolean isPaused; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int track; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int discNumber; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public boolean isPodcast; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String path; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long bookMark; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String albumArtistName; //NOSONAR

    private TagInfo tagInfo; //NOSONAR

    private String durationLabel; //NOSONAR
    private String bitrateLabel; //NOSONAR
    private String sampleRateLabel; //NOSONAR
    private String formatLabel; //NOSONAR
    private String trackNumberLabel; //NOSONAR
    private String discNumberLabel; //NOSONAR
    private String fileSizeLabel; //NOSONAR

    private String artworkKey; //NOSONAR
    private String sortKey; //NOSONAR

    public static String[] getProjection() { //NOSONAR
        return new String[] { //NOSONAR
                MediaStore.Audio.Media._ID, //NOSONAR
                MediaStore.Audio.Media.DATA, //NOSONAR
                MediaStore.Audio.Media.TITLE, //NOSONAR
                MediaStore.Audio.Media.ARTIST_ID, //NOSONAR
                MediaStore.Audio.Media.ARTIST, //NOSONAR
                MediaStore.Audio.Media.ALBUM_ID, //NOSONAR
                MediaStore.Audio.Media.ALBUM, //NOSONAR
                MediaStore.Audio.Media.DURATION, //NOSONAR
                MediaStore.Audio.Media.YEAR, //NOSONAR
                MediaStore.Audio.Media.TRACK, //NOSONAR
                MediaStore.Audio.Media.DATE_ADDED, //NOSONAR
                MediaStore.Audio.Media.IS_PODCAST, //NOSONAR
                MediaStore.Audio.Media.BOOKMARK, //NOSONAR
                "album_artist" //NOSONAR
        }; // NOSONAR
    } // NOSONAR

    public static Query getQuery() { //NOSONAR
        return new Query.Builder() //NOSONAR
                .uri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) //NOSONAR
                .projection(Song.getProjection()) //NOSONAR
                .selection(MediaStore.Audio.Media.IS_MUSIC + "=1 OR " + MediaStore.Audio.Media.IS_PODCAST + "=1") //NOSONAR
                .args(null) //NOSONAR
                .sort(MediaStore.Audio.Media.TRACK) //NOSONAR
                .build(); //NOSONAR
    } // NOSONAR

    public Song(Cursor cursor) { //NOSONAR

        id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)); //NOSONAR

        name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)); //NOSONAR

        artistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)); //NOSONAR

        artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); //NOSONAR

        albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)); //NOSONAR

        albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)); //NOSONAR

        duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)); //NOSONAR

        year = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)); //NOSONAR

        track = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)); //NOSONAR

        if (track >= 1000) { //NOSONAR
            discNumber = track / 1000; //NOSONAR
            track = track % 1000; //NOSONAR
        } // NOSONAR

        dateAdded = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)); //NOSONAR

        path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); //NOSONAR

        albumArtistName = artistName; //NOSONAR
        if (cursor.getColumnIndex("album_artist") != -1) { //NOSONAR
            String albumArtist = cursor.getString(cursor.getColumnIndex("album_artist")); //NOSONAR
            if (albumArtist != null) { //NOSONAR
                albumArtistName = albumArtist; //NOSONAR
            } // NOSONAR
        } // NOSONAR

        isPodcast = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_PODCAST)) == 1; //NOSONAR

        bookMark = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.BOOKMARK)); //NOSONAR

        //Populate the artwork key & sort key properties if null. // NOSONAR
        setSortKey(); //NOSONAR
        setArtworkKey(); //NOSONAR
    } // NOSONAR

    public Song() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public Single<Genre> getGenre(Context context) { //NOSONAR
        Query query = Genre.getQuery(); //NOSONAR
        query.uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", (int) id); //NOSONAR
        return SqlBriteUtils.createSingle(context, Genre::new, query, null); //NOSONAR
    } // NOSONAR

    public int getPlayCount(Context context) { //NOSONAR

        int playCount = 0; //NOSONAR

        Uri playCountUri = PlayCountTable.URI; //NOSONAR
        Uri appendedUri = ContentUris.withAppendedId(playCountUri, id); //NOSONAR

        if (appendedUri != null) { //NOSONAR

            Query query = new Query.Builder() //NOSONAR
                    .uri(appendedUri) //NOSONAR
                    .projection(new String[] { PlayCountTable.COLUMN_ID, PlayCountTable.COLUMN_PLAY_COUNT }) //NOSONAR
                    .build(); //NOSONAR

            playCount = SqlUtils.createSingleQuery(context, cursor -> //NOSONAR
                    cursor.getInt(cursor.getColumnIndex(PlayCountTable.COLUMN_PLAY_COUNT)), 0, query); //NOSONAR
        } // NOSONAR

        return playCount; //NOSONAR
    } // NOSONAR

    public void setStartTime() { //NOSONAR
        startTime = System.currentTimeMillis(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Checks whether this track has been played for at least 75% of it's duration // NOSONAR
     * // NOSONAR
     * @return true if the elapsed time is > 75% of the duration false otherwise // NOSONAR
     */ // NOSONAR
    public boolean hasPlayed() { //NOSONAR
        return getElapsedTime() != 0 && ((float) getElapsedTime() / (float) duration) > 0.75f; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets this track as 'paused' to make sure the elapsed time doesn't continue to increase // NOSONAR
     */ // NOSONAR
    public void setPaused() { //NOSONAR
        elapsedTime = elapsedTime + System.currentTimeMillis() - startTime; //NOSONAR
        isPaused = true; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets this track as 'resumed' to resume incrementing the elapsed time // NOSONAR
     */ // NOSONAR
    public void setResumed() { //NOSONAR
        startTime = System.currentTimeMillis(); //NOSONAR
        isPaused = false; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Gets the elapsed time of this track (in millis) // NOSONAR
     * // NOSONAR
     * @return the elapsed time of this track (in millis) // NOSONAR
     */ // NOSONAR
    private long getElapsedTime() { //NOSONAR
        if (isPaused) { //NOSONAR
            return elapsedTime; //NOSONAR
        } else { //NOSONAR
            return elapsedTime + System.currentTimeMillis() - startTime; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public String getDurationLabel(Context context) { //NOSONAR
        if (durationLabel == null) { //NOSONAR
            durationLabel = StringUtils.makeTimeString(context, duration / 1000); //NOSONAR
        } // NOSONAR
        return durationLabel; //NOSONAR
    } // NOSONAR

    public TagInfo getTagInfo() { //NOSONAR
        if (tagInfo == null) { //NOSONAR
            tagInfo = new TagInfo(path); //NOSONAR
        } // NOSONAR
        return tagInfo; //NOSONAR
    } // NOSONAR

    public String getBitrateLabel(Context context) { //NOSONAR
        if (bitrateLabel == null) { //NOSONAR
            bitrateLabel = getTagInfo().bitrate + context.getString(R.string.song_info_bitrate_suffix); //NOSONAR
        } // NOSONAR
        return bitrateLabel; //NOSONAR
    } // NOSONAR

    public String getSampleRateLabel(Context context) { //NOSONAR
        if (sampleRateLabel == null) { //NOSONAR
            int sampleRate = getTagInfo().sampleRate; //NOSONAR
            if (sampleRate == -1) { //NOSONAR
                sampleRateLabel = "Unknown"; //NOSONAR
                return sampleRateLabel; //NOSONAR
            } // NOSONAR
            sampleRateLabel = ((float) sampleRate) / 1000 + context.getString(R.string.song_info_sample_rate_suffix); //NOSONAR
        } // NOSONAR
        return sampleRateLabel; //NOSONAR
    } // NOSONAR

    public String getFormatLabel() { //NOSONAR
        if (formatLabel == null) { //NOSONAR
            formatLabel = getTagInfo().format; //NOSONAR
        } // NOSONAR
        return formatLabel; //NOSONAR
    } // NOSONAR

    public String getTrackNumberLabel() { //NOSONAR
        if (trackNumberLabel == null) { //NOSONAR
            if (track == -1) { //NOSONAR
                trackNumberLabel = String.valueOf(getTagInfo().trackNumber); //NOSONAR
            } else { //NOSONAR
                trackNumberLabel = String.valueOf(track); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return trackNumberLabel; //NOSONAR
    } // NOSONAR

    public String getDiscNumberLabel() { //NOSONAR
        if (discNumberLabel == null) { //NOSONAR
            if (discNumber == -1) { //NOSONAR
                discNumberLabel = String.valueOf(getTagInfo().discNumber); //NOSONAR
            } else { //NOSONAR
                discNumberLabel = String.valueOf(discNumber); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return discNumberLabel; //NOSONAR
    } // NOSONAR

    public String getFileSizeLabel() { //NOSONAR
        if (fileSizeLabel == null) { //NOSONAR
            if (!TextUtils.isEmpty(path)) { //NOSONAR
                File file = new File(path); //NOSONAR
                fileSizeLabel = FileHelper.getHumanReadableSize(file.length()); //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return fileSizeLabel; //NOSONAR
    } // NOSONAR

    public Album getAlbum() { //NOSONAR
        return new Album.Builder() //NOSONAR
                .id(albumId) //NOSONAR
                .name(albumName) //NOSONAR
                .addArtist(new Artist(artistId, artistName)) //NOSONAR
                .albumArtist(albumArtistName) //NOSONAR
                .year(year) //NOSONAR
                .numSongs(1) //NOSONAR
                .numDiscs(discNumber) //NOSONAR
                .lastPlayed(lastPlayed) //NOSONAR
                .dateAdded(dateAdded) //NOSONAR
                .path(path) //NOSONAR
                .songPlayCount(playCount) //NOSONAR
                .build(); //NOSONAR
    } // NOSONAR

    public AlbumArtist getAlbumArtist() { //NOSONAR
        return new AlbumArtist.Builder() //NOSONAR
                .name(albumArtistName) //NOSONAR
                .album(getAlbum()) //NOSONAR
                .build(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Song song = (Song) o; //NOSONAR

        return id == song.id && artistId == song.artistId && albumId == song.albumId; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (int) (artistId ^ (artistId >>> 32)); //NOSONAR
        result = 31 * result + (int) (albumId ^ (albumId >>> 32)); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getSortKey() { //NOSONAR
        if (sortKey == null) { //NOSONAR
            setSortKey(); //NOSONAR
        } // NOSONAR
        return sortKey; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setSortKey() { //NOSONAR
        sortKey = StringUtils.keyFor(name); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    @NonNull //NOSONAR
    public String getArtworkKey() { //NOSONAR
        if (artworkKey == null) setArtworkKey(); //NOSONAR
        return artworkKey; //NOSONAR
    } // NOSONAR

    private void setArtworkKey() { //NOSONAR
        artworkKey = String.format("%s_%s", albumArtistName, albumName); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public String getRemoteArtworkUrl() { //NOSONAR
        try { //NOSONAR
            return "https://artwork.shuttlemusicplayer.app/api/v1/artwork" //NOSONAR
                    + "?artist=" + URLEncoder.encode(albumArtistName, Charset.forName("UTF-8").name()) //NOSONAR
                    + "&album=" + URLEncoder.encode(albumName, Charset.forName("UTF-8").name()); //NOSONAR
        } catch (UnsupportedEncodingException e) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public InputStream getMediaStoreArtwork(Context context) { //NOSONAR
        return ArtworkUtils.getMediaStoreArtwork(context, this); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public InputStream getFolderArtwork() { //NOSONAR
        return ArtworkUtils.getFolderArtwork(path); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public InputStream getTagArtwork() { //NOSONAR
        return ArtworkUtils.getTagArtwork(path); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public List<File> getFolderArtworkFiles() { //NOSONAR
        return ArtworkUtils.getAllFolderArtwork(path); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "\nSong{" + //NOSONAR
                "\nid='" + id + //NOSONAR
                "\nname='" + name + //NOSONAR
                "\nalbumArtistName='" + albumArtistName + //NOSONAR
                '}'; // NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public int compareTo(@NonNull Song song) { //NOSONAR
        return ComparisonUtils.compare(getSortKey(), song.getSortKey()); //NOSONAR
    } // NOSONAR
} // NOSONAR
