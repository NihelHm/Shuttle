package com.simplecity.amp_library.model;

import java.io.Serializable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class Artist implements Serializable {

    @SuppressWarnings("java:S1104")

    public long id;
    @SuppressWarnings("java:S1104")
    public String name;
    private int numAlbums;
    @SuppressWarnings("java:S1104")
    public int numSongs;

    public Artist(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (id != artist.id) return false;
        if (numAlbums != artist.numAlbums) return false;
        if (numSongs != artist.numSongs) return false;
        return name != null ? name.equals(artist.name) : artist.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + numAlbums;
        result = 31 * result + numSongs;
        return result;
    }
}
