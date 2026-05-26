package com.simplecity.amp_library.model;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class SuggestedHeader {

    @SuppressWarnings("java:S1104")

    public String title;
    @SuppressWarnings("java:S1104")
    public String subtitle;
    @SuppressWarnings("java:S1104")
    public Playlist playlist;

    public SuggestedHeader(String title, String subtitle, Playlist playlist) {
        this.title = title;
        this.subtitle = subtitle;
        this.playlist = playlist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuggestedHeader that = (SuggestedHeader) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (subtitle != null ? !subtitle.equals(that.subtitle) : that.subtitle != null) {
            return false;
        }
        return playlist != null ? playlist.equals(that.playlist) : that.playlist == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (subtitle != null ? subtitle.hashCode() : 0);
        result = 31 * result + (playlist != null ? playlist.hashCode() : 0);
        return result;
    }
}
