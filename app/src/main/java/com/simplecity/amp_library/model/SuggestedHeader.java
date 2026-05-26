package com.simplecity.amp_library.model; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SuggestedHeader { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String title; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String subtitle; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public Playlist playlist; //NOSONAR

    public SuggestedHeader(String title, String subtitle, Playlist playlist) { //NOSONAR
        this.title = title; //NOSONAR
        this.subtitle = subtitle; //NOSONAR
        this.playlist = playlist; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR

        SuggestedHeader that = (SuggestedHeader) o; //NOSONAR

        if (title != null ? !title.equals(that.title) : that.title != null) return false; //NOSONAR
        if (subtitle != null ? !subtitle.equals(that.subtitle) : that.subtitle != null) { //NOSONAR
            return false; //NOSONAR
        } // NOSONAR
        return playlist != null ? playlist.equals(that.playlist) : that.playlist == null; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = title != null ? title.hashCode() : 0; //NOSONAR
        result = 31 * result + (subtitle != null ? subtitle.hashCode() : 0); //NOSONAR
        result = 31 * result + (playlist != null ? playlist.hashCode() : 0); //NOSONAR
        return result; //NOSONAR
    } // NOSONAR
} // NOSONAR
