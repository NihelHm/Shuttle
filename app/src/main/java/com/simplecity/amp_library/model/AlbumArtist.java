package com.simplecity.amp_library.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.annimon.stream.Stream;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.utils.ComparisonUtils;
import com.simplecity.amp_library.utils.StringUtils;
import io.reactivex.Single;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    }

    public Single<List<Song>> getSongsSingle(Repository.SongsRepository songsRepository) { //NOSONAR
        return songsRepository.getSongs(song -> Stream.of(albums) //NOSONAR
                .map(album -> album.id) //NOSONAR
                .anyMatch(albumId -> albumId == song.albumId)) //NOSONAR
                .first(Collections.emptyList()); //NOSONAR
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
        return name; //NOSONAR
    }

    public static class Builder { //NOSONAR
        private String name; //NOSONAR
        private List<Album> albums = new ArrayList<>(); //NOSONAR

        public Builder name(String name) { //NOSONAR
            this.name = name; //NOSONAR
            return this; //NOSONAR
        }

        public Builder albums(List<Album> albums) { //NOSONAR
            this.albums = albums; //NOSONAR
            return this; //NOSONAR
        }

        public Builder album(Album album) { //NOSONAR
            this.albums.add(album); //NOSONAR
            return this; //NOSONAR
        }

        public AlbumArtist build() { //NOSONAR
            return new AlbumArtist(this.name, this.albums); //NOSONAR
        }
    }

    public int getNumAlbums() { //NOSONAR
        return albums.size(); //NOSONAR
    }

    public int getNumSongs() { //NOSONAR
        int numSongs = 0; //NOSONAR
        for (Album album : albums) { //NOSONAR
            numSongs += album.numSongs; //NOSONAR
        }
        return numSongs; //NOSONAR
    }

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "AlbumArtist{" + //NOSONAR
                "name='" + name + '\'' + //NOSONAR
                ", albums=" + albums + //NOSONAR
                '}';
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        AlbumArtist that = (AlbumArtist) o; //NOSONAR

        if (name != null ? !name.equals(that.name) : that.name != null) return false; //NOSONAR
        return albums != null ? albums.equals(that.albums) : that.albums == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = name != null ? name.hashCode() : 0; //NOSONAR
        result = 31 * result + (albums != null ? albums.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public String getRemoteArtworkUrl() { //NOSONAR
        try { //NOSONAR
            return "https://artwork.shuttlemusicplayer.app/api/v1/artwork?artist=" + URLEncoder.encode(name, Charset.forName("UTF-8").name()); //NOSONAR
        } catch (UnsupportedEncodingException e) { //NOSONAR
            return null; //NOSONAR
        }
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public InputStream getMediaStoreArtwork(Context context) { //NOSONAR
        return null; //NOSONAR
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public InputStream getFolderArtwork() { //NOSONAR
        return null; //NOSONAR
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public InputStream getTagArtwork() { //NOSONAR
        return null; //NOSONAR
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public List<File> getFolderArtworkFiles() { //NOSONAR
        return Collections.emptyList(); //NOSONAR
    }

    @Override //NOSONAR
    public int compareTo(@NonNull AlbumArtist albumArtist) { //NOSONAR
        return ComparisonUtils.compare(getSortKey(), albumArtist.getSortKey()); //NOSONAR
    }
}
