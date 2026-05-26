package com.simplecity.amp_library.model;

import java.io.Serializable;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Artist implements Serializable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public long id; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String name; //NOSONAR
    private int numAlbums; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int numSongs; //NOSONAR

    public Artist(long id, String name) { //NOSONAR
        this.id = id; //NOSONAR
        this.name = name; //NOSONAR
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        Artist artist = (Artist) o; //NOSONAR

        if (id != artist.id) return false; //NOSONAR
        if (numAlbums != artist.numAlbums) return false; //NOSONAR
        if (numSongs != artist.numSongs) return false; //NOSONAR
        return name != null ? name.equals(artist.name) : artist.name == null; //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = (int) (id ^ (id >>> 32)); //NOSONAR
        result = 31 * result + (name != null ? name.hashCode() : 0); //NOSONAR
        result = 31 * result + numAlbums; //NOSONAR
        result = 31 * result + numSongs; //NOSONAR
        return result; //NOSONAR
    }
}
