package com.simplecity.amp_library.model; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.annotation.WorkerThread; // NOSONAR
import com.simplecity.amp_library.utils.ArtworkUtils; // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR
import java.io.Serializable; // NOSONAR
import java.io.UnsupportedEncodingException; // NOSONAR
import java.net.URLEncoder; // NOSONAR
import java.nio.charset.Charset; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Album implements //NOSONAR
        Serializable, //NOSONAR
        ArtworkProvider, //NOSONAR
        Comparable<Album>, //NOSONAR
        Sortable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String name; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public List<Artist> artists = new ArrayList<>(); //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String albumArtistName; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public int year; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int numSongs; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int numDiscs; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long lastPlayed; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public long dateAdded; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public List<String> paths = new ArrayList<>(); //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public int songPlayCount; //NOSONAR

    private String artworkKey; //NOSONAR

    private String sortKey; //NOSONAR

    public Album(long id, String name, List<Artist> artists, String albumArtistName, int numSongs, int numDiscs, int year, long lastPlayed, long dateAdded, List<String> paths, int songPlayCount) { //NOSONAR
        this.id = id; //NOSONAR
        this.name = name; //NOSONAR
        this.artists = artists; //NOSONAR
        this.albumArtistName = albumArtistName; //NOSONAR
        this.numSongs = numSongs; //NOSONAR
        this.numDiscs = numDiscs; //NOSONAR
        this.year = year; //NOSONAR
        this.lastPlayed = lastPlayed; //NOSONAR
        this.dateAdded = dateAdded; //NOSONAR
        this.paths = paths; //NOSONAR
        this.songPlayCount = songPlayCount; //NOSONAR

        //Populate the artwork key & sort key properties if null. // NOSONAR
        setSortKey(); //NOSONAR
        setArtworkKey(); //NOSONAR
    } // NOSONAR

    public static class Builder { //NOSONAR

        private long id; //NOSONAR
        private String name; //NOSONAR
        private List<Artist> artists = new ArrayList<>(); //NOSONAR
        private String albumArtistName; //NOSONAR
        private int numSongs; //NOSONAR
        private int numDiscs; //NOSONAR
        private int year; //NOSONAR
        private long lastPlayed; //NOSONAR
        private long dateAdded; //NOSONAR
        private List<String> paths = new ArrayList<>(); //NOSONAR
        private int songPlayCount; //NOSONAR

        public Builder id(long id) { //NOSONAR
            this.id = id; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder name(String name) { //NOSONAR
            this.name = name; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder addArtist(Artist artist) { //NOSONAR
            if (!this.artists.contains(artist)) { //NOSONAR
                this.artists.add(artist); //NOSONAR
            } // NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder albumArtist(String albumArtistName) { //NOSONAR
            this.albumArtistName = albumArtistName; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder numSongs(int numSongs) { //NOSONAR
            this.numSongs = numSongs; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder numDiscs(int numDiscs) { //NOSONAR
            this.numDiscs = numDiscs; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder year(int year) { //NOSONAR
            this.year = year; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder lastPlayed(long lastPlayed) { //NOSONAR
            if (lastPlayed > this.lastPlayed) { //NOSONAR
                this.lastPlayed = lastPlayed; //NOSONAR
            } // NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder dateAdded(long dateAdded) { //NOSONAR
            if (dateAdded > this.dateAdded) { //NOSONAR
                this.dateAdded = dateAdded; //NOSONAR
            } // NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder path(String path) { //NOSONAR
            if (!this.paths.contains(path)) { //NOSONAR
                this.paths.add(path); //NOSONAR
            } // NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder songPlayCount(int playCount) { //NOSONAR
            songPlayCount = playCount; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Album build() { //NOSONAR
            return new Album(id, name, artists, albumArtistName, numSongs, numDiscs, year, lastPlayed, dateAdded, paths, songPlayCount); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public AlbumArtist getAlbumArtist() { //NOSONAR
        return new AlbumArtist.Builder() //NOSONAR
                .name(albumArtistName) //NOSONAR
                .album(this) //NOSONAR
                .build(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Album album = (Album) o; //NOSONAR

        if (id != album.id) return false; //NOSONAR
        return name != null ? name.equals(album.name) : album.name == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (name != null ? name.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "Album{" + //NOSONAR
                "id=" + id + //NOSONAR
                ", name='" + name + '\'' + //NOSONAR
                ", artists=" + artists + //NOSONAR
                ", albumArtistName='" + albumArtistName + '\'' + //NOSONAR
                ", year=" + year + //NOSONAR
                ", numSongs=" + numSongs + //NOSONAR
                ", lastPlayed=" + lastPlayed + //NOSONAR
                ", dateAdded=" + dateAdded + //NOSONAR
                ", paths=" + paths + //NOSONAR
                '}'; // NOSONAR
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
        artworkKey = String.format("%s_%s", albumArtistName, name); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public String getRemoteArtworkUrl() { //NOSONAR
        try { //NOSONAR
            return "https://artwork.shuttlemusicplayer.app/api/v1/artwork" //NOSONAR
                    + "?artist=" + URLEncoder.encode(albumArtistName, Charset.forName("UTF-8").name()) //NOSONAR
                    + "&album=" + URLEncoder.encode(name, Charset.forName("UTF-8").name()); //NOSONAR
        } catch (UnsupportedEncodingException e) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public InputStream getMediaStoreArtwork(Context context) { //NOSONAR
        return ArtworkUtils.getMediaStoreArtwork(context, this); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public InputStream getFolderArtwork() { //NOSONAR
        return ArtworkUtils.getFolderArtwork(getArtworkPath()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public InputStream getTagArtwork() { //NOSONAR
        return ArtworkUtils.getTagArtwork(getArtworkPath()); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public List<File> getFolderArtworkFiles() { //NOSONAR
        return ArtworkUtils.getAllFolderArtwork(getArtworkPath()); //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @WorkerThread //NOSONAR
    private String getArtworkPath() { //NOSONAR
        if (paths != null && !paths.isEmpty()) { //NOSONAR
            return paths.get(0); //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int compareTo(@NonNull Album album) { //NOSONAR
        return ComparisonUtils.compare(getSortKey(), album.getSortKey()); //NOSONAR
    } // NOSONAR
} // NOSONAR
