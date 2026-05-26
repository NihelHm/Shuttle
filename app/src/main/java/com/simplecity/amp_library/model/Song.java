package com.simplecity.amp_library.model;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.sql.SqlUtils;
import com.simplecity.amp_library.sql.providers.PlayCountTable;
import com.simplecity.amp_library.sql.sqlbrite.SqlBriteUtils;
import com.simplecity.amp_library.utils.ArtworkUtils;
import com.simplecity.amp_library.utils.ComparisonUtils;
import com.simplecity.amp_library.utils.FileHelper;
import com.simplecity.amp_library.utils.StringUtils;
import io.reactivex.Single;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

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
        };
    }

    public static Query getQuery() { //NOSONAR
        return new Query.Builder() //NOSONAR
                .uri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) //NOSONAR
                .projection(Song.getProjection()) //NOSONAR
                .selection(MediaStore.Audio.Media.IS_MUSIC + "=1 OR " + MediaStore.Audio.Media.IS_PODCAST + "=1") //NOSONAR
                .args(null) //NOSONAR
                .sort(MediaStore.Audio.Media.TRACK) //NOSONAR
                .build(); //NOSONAR
    }

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
        }

        dateAdded = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)); //NOSONAR

        path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); //NOSONAR

        albumArtistName = artistName; //NOSONAR
        if (cursor.getColumnIndex("album_artist") != -1) { //NOSONAR
            String albumArtist = cursor.getString(cursor.getColumnIndex("album_artist")); //NOSONAR
            if (albumArtist != null) { //NOSONAR
                albumArtistName = albumArtist; //NOSONAR
            }
        }

        isPodcast = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_PODCAST)) == 1; //NOSONAR

        bookMark = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.BOOKMARK)); //NOSONAR

        //Populate the artwork key & sort key properties if null.
        setSortKey(); //NOSONAR
        setArtworkKey(); //NOSONAR
    }

    public Song() { //NOSONAR
        // Intentionally left empty.
    }

    public Single<Genre> getGenre(Context context) { //NOSONAR
        Query query = Genre.getQuery(); //NOSONAR
        query.uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", (int) id); //NOSONAR
        return SqlBriteUtils.createSingle(context, Genre::new, query, null); //NOSONAR
    }

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
        }

        return playCount; //NOSONAR
    }

    public void setStartTime() { //NOSONAR
        startTime = System.currentTimeMillis(); //NOSONAR
    }

    /**
     * Checks whether this track has been played for at least 75% of it's duration
     *
     * @return true if the elapsed time is > 75% of the duration false otherwise
     */
    public boolean hasPlayed() { //NOSONAR
        return getElapsedTime() != 0 && ((float) getElapsedTime() / (float) duration) > 0.75f; //NOSONAR
    }

    /**
     * Sets this track as 'paused' to make sure the elapsed time doesn't continue to increase
     */
    public void setPaused() { //NOSONAR
        elapsedTime = elapsedTime + System.currentTimeMillis() - startTime; //NOSONAR
        isPaused = true; //NOSONAR
    }

    /**
     * Sets this track as 'resumed' to resume incrementing the elapsed time
     */
    public void setResumed() { //NOSONAR
        startTime = System.currentTimeMillis(); //NOSONAR
        isPaused = false; //NOSONAR
    }

    /**
     * Gets the elapsed time of this track (in millis)
     *
     * @return the elapsed time of this track (in millis)
     */
    private long getElapsedTime() { //NOSONAR
        if (isPaused) { //NOSONAR
            return elapsedTime; //NOSONAR
        } else { //NOSONAR
            return elapsedTime + System.currentTimeMillis() - startTime; //NOSONAR
        }
    }

    public String getDurationLabel(Context context) { //NOSONAR
        if (durationLabel == null) { //NOSONAR
            durationLabel = StringUtils.makeTimeString(context, duration / 1000); //NOSONAR
        }
        return durationLabel; //NOSONAR
    }

    public TagInfo getTagInfo() { //NOSONAR
        if (tagInfo == null) { //NOSONAR
            tagInfo = new TagInfo(path); //NOSONAR
        }
        return tagInfo; //NOSONAR
    }

    public String getBitrateLabel(Context context) { //NOSONAR
        if (bitrateLabel == null) { //NOSONAR
            bitrateLabel = getTagInfo().bitrate + context.getString(R.string.song_info_bitrate_suffix); //NOSONAR
        }
        return bitrateLabel; //NOSONAR
    }

    public String getSampleRateLabel(Context context) { //NOSONAR
        if (sampleRateLabel == null) { //NOSONAR
            int sampleRate = getTagInfo().sampleRate; //NOSONAR
            if (sampleRate == -1) { //NOSONAR
                sampleRateLabel = "Unknown"; //NOSONAR
                return sampleRateLabel; //NOSONAR
            }
            sampleRateLabel = ((float) sampleRate) / 1000 + context.getString(R.string.song_info_sample_rate_suffix); //NOSONAR
        }
        return sampleRateLabel; //NOSONAR
    }

    public String getFormatLabel() { //NOSONAR
        if (formatLabel == null) { //NOSONAR
            formatLabel = getTagInfo().format; //NOSONAR
        }
        return formatLabel; //NOSONAR
    }

    public String getTrackNumberLabel() { //NOSONAR
        if (trackNumberLabel == null) { //NOSONAR
            if (track == -1) { //NOSONAR
                trackNumberLabel = String.valueOf(getTagInfo().trackNumber); //NOSONAR
            } else { //NOSONAR
                trackNumberLabel = String.valueOf(track); //NOSONAR
            }
        }
        return trackNumberLabel; //NOSONAR
    }

    public String getDiscNumberLabel() { //NOSONAR
        if (discNumberLabel == null) { //NOSONAR
            if (discNumber == -1) { //NOSONAR
                discNumberLabel = String.valueOf(getTagInfo().discNumber); //NOSONAR
            } else { //NOSONAR
                discNumberLabel = String.valueOf(discNumber); //NOSONAR
            }
        }
        return discNumberLabel; //NOSONAR
    }

    public String getFileSizeLabel() { //NOSONAR
        if (fileSizeLabel == null) { //NOSONAR
            if (!TextUtils.isEmpty(path)) { //NOSONAR
                File file = new File(path); //NOSONAR
                fileSizeLabel = FileHelper.getHumanReadableSize(file.length()); //NOSONAR
            }
        }
        return fileSizeLabel; //NOSONAR
    }

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
    }

    public AlbumArtist getAlbumArtist() { //NOSONAR
        return new AlbumArtist.Builder() //NOSONAR
                .name(albumArtistName) //NOSONAR
                .album(getAlbum()) //NOSONAR
                .build(); //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Song song = (Song) o; //NOSONAR

        return id == song.id && artistId == song.artistId && albumId == song.albumId; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (int) (artistId ^ (artistId >>> 32)); //NOSONAR
        result = 31 * result + (int) (albumId ^ (albumId >>> 32)); //NOSONAR
        return result; //NOSONAR
    }

    @Override //NOSONAR
    public String getSortKey() { //NOSONAR
        if (sortKey == null) { //NOSONAR
            setSortKey(); //NOSONAR
        }
        return sortKey; //NOSONAR
    }

    @Override //NOSONAR
    public void setSortKey() { //NOSONAR
        sortKey = StringUtils.keyFor(name); //NOSONAR
    }

    @Override //NOSONAR
    @NonNull //NOSONAR
    public String getArtworkKey() { //NOSONAR
        if (artworkKey == null) setArtworkKey(); //NOSONAR
        return artworkKey; //NOSONAR
    }

    private void setArtworkKey() { //NOSONAR
        artworkKey = String.format("%s_%s", albumArtistName, albumName); //NOSONAR
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public String getRemoteArtworkUrl() { //NOSONAR
        try { //NOSONAR
            return "https://artwork.shuttlemusicplayer.app/api/v1/artwork" //NOSONAR
                    + "?artist=" + URLEncoder.encode(albumArtistName, Charset.forName("UTF-8").name()) //NOSONAR
                    + "&album=" + URLEncoder.encode(albumName, Charset.forName("UTF-8").name()); //NOSONAR
        } catch (UnsupportedEncodingException e) { //NOSONAR
            return null; //NOSONAR
        }
    }

    @Override //NOSONAR
    public InputStream getMediaStoreArtwork(Context context) { //NOSONAR
        return ArtworkUtils.getMediaStoreArtwork(context, this); //NOSONAR
    }

    @Override //NOSONAR
    public InputStream getFolderArtwork() { //NOSONAR
        return ArtworkUtils.getFolderArtwork(path); //NOSONAR
    }

    @Override //NOSONAR
    public InputStream getTagArtwork() { //NOSONAR
        return ArtworkUtils.getTagArtwork(path); //NOSONAR
    }

    @Override //NOSONAR
    public List<File> getFolderArtworkFiles() { //NOSONAR
        return ArtworkUtils.getAllFolderArtwork(path); //NOSONAR
    }

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "\nSong{" + //NOSONAR
                "\nid='" + id + //NOSONAR
                "\nname='" + name + //NOSONAR
                "\nalbumArtistName='" + albumArtistName + //NOSONAR
                '}';
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public int compareTo(@NonNull Song song) { //NOSONAR
        return ComparisonUtils.compare(getSortKey(), song.getSortKey()); //NOSONAR
    }
}
