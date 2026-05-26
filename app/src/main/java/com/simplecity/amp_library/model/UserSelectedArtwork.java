package com.simplecity.amp_library.model; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class UserSelectedArtwork { //NOSONAR

    @ArtworkProvider.Type //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int type; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String path; //NOSONAR

    public UserSelectedArtwork(@ArtworkProvider.Type int type, String path) { //NOSONAR
        this.type = type; //NOSONAR
        this.path = path; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String toString() { //NOSONAR
        return "UserSelectedArtwork{" + //NOSONAR
                "type=" + type + //NOSONAR
                ", path='" + path + '\'' + //NOSONAR
                '}'; // NOSONAR
    } // NOSONAR
} // NOSONAR
