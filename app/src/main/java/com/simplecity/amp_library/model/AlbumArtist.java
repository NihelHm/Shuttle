package com.simplecity.amp_library.model; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.simplecity.amp_library.data.Repository; // NOSONAR
import com.simplecity.amp_library.utils.ComparisonUtils; // NOSONAR
import com.simplecity.amp_library.utils.StringUtils; // NOSONAR
import io.reactivex.Single; // NOSONAR
import java.io.File; // NOSONAR
import java.io.InputStream; // NOSONAR
import java.io.Serializable; // NOSONAR
import java.io.UnsupportedEncodingException; // NOSONAR
import java.net.URLEncoder; // NOSONAR
import java.nio.charset.Charset; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AlbumArtist implements //NOSONAR
        Serializable, //NOSONAR
        Comparable<AlbumArtist>, //NOSONAR
        ArtworkProvider, //NOSONAR
        Sortable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String name; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public List<Album> albums = new ArrayList<>(); //NOSONAR

    private String sortKey; //NOSONAR

    public AlbumArtist(String name, List<Album> albums) { //NOSONAR
        this.name = name; //NOSONAR
        this.albums = albums; //NOSONAR
    } // NOSONAR

    public Single<List<Song>> getSongsSingle(Repository.SongsRepository songsRepository) { //NOSONAR
        return songsRepository.getSongs(song -> Stream.of(albums) //NOSONAR
                .map(album -> album.id) //NOSONAR
                .anyMatch(albumId -> albumId == song.albumId)) //NOSONAR
                .first(Collections.emptyList()); //NOSONAR
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
        return name; //NOSONAR
    } // NOSONAR

    public static class Builder { //NOSONAR
        private String name; //NOSONAR
        private List<Album> albums = new ArrayList<>(); //NOSONAR

        public Builder name(String name) { //NOSONAR
            this.name = name; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder albums(List<Album> albums) { //NOSONAR
            this.albums = albums; //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public Builder album(Album album) { //NOSONAR
            this.albums.add(album); //NOSONAR
            return this; //NOSONAR
        } // NOSONAR

        public AlbumArtist build() { //NOSONAR
            return new AlbumArtist(this.name, this.albums); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public int getNumAlbums() { //NOSONAR
        return albums.size(); //NOSONAR
    } // NOSONAR

    public int getNumSongs() { //NOSONAR
        int numSongs = 0; //NOSONAR
        for (Album album : albums) { //NOSONAR
            numSongs += album.numSongs; //NOSONAR
        } // NOSONAR
        return numSongs; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "AlbumArtist{" + //NOSONAR
                "name='" + name + '\'' + //NOSONAR
                ", albums=" + albums + //NOSONAR
                '}'; // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        AlbumArtist that = (AlbumArtist) o; //NOSONAR

        if (name != null ? !name.equals(that.name) : that.name != null) return false; //NOSONAR
        return albums != null ? albums.equals(that.albums) : that.albums == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = name != null ? name.hashCode() : 0; //NOSONAR
        result = 31 * result + (albums != null ? albums.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public String getRemoteArtworkUrl() { //NOSONAR
        try { //NOSONAR
            return "https://artwork.shuttlemusicplayer.app/api/v1/artwork?artist=" + URLEncoder.encode(name, Charset.forName("UTF-8").name()); //NOSONAR
        } catch (UnsupportedEncodingException e) { //NOSONAR
            return null; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public InputStream getMediaStoreArtwork(Context context) { //NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public InputStream getFolderArtwork() { //NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public InputStream getTagArtwork() { //NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public List<File> getFolderArtworkFiles() { //NOSONAR
        return Collections.emptyList(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int compareTo(@NonNull AlbumArtist albumArtist) { //NOSONAR
        return ComparisonUtils.compare(getSortKey(), albumArtist.getSortKey()); //NOSONAR
    } // NOSONAR
} // NOSONAR
