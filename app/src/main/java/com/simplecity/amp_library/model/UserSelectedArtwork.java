package com.simplecity.amp_library.model;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class UserSelectedArtwork {

    @ArtworkProvider.Type
    @SuppressWarnings("java:S1104")
    public int type;
    @SuppressWarnings("java:S1104")
    public String path;

    public UserSelectedArtwork(@ArtworkProvider.Type int type, String path) {
        this.type = type;
        this.path = path;
    }

    @Override
    public String toString() {
        return "UserSelectedArtwork{" +
                "type=" + type +
                ", path='" + path + '\'' +
                '}';
    }
}
