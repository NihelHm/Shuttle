package com.simplecity.amp_library.model; // NOSONAR

import org.jaudiotagger.tag.FieldKey; // NOSONAR
import org.jaudiotagger.tag.Tag; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TagUpdate { //NOSONAR

    String title; //NOSONAR
    String album; //NOSONAR
    String artist; //NOSONAR
    String albumArtist; //NOSONAR
    String genre; //NOSONAR
    String year; //NOSONAR
    String track; //NOSONAR
    String trackTotal; //NOSONAR
    String disc; //NOSONAR
    String discTotal; //NOSONAR
    String lyrics; //NOSONAR
    String comment; //NOSONAR

    boolean titleHasChanged; //NOSONAR
    boolean albumHasChanged; //NOSONAR
    boolean artistHasChanged; //NOSONAR
    boolean albumArtistHasChanged; //NOSONAR
    boolean genreHasChanged; //NOSONAR
    boolean yearHasChanged; //NOSONAR
    boolean trackHasChanged; //NOSONAR
    boolean trackTotalHasChanged; //NOSONAR
    boolean discHasChanged; //NOSONAR
    boolean discTotalHasChanged; //NOSONAR
    boolean lyricsHasChanged; //NOSONAR
    boolean commentHasChanged; //NOSONAR

    public TagUpdate(Tag tag) { //NOSONAR
        try { //NOSONAR
            this.title = tag.getFirst(FieldKey.TITLE); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.album = tag.getFirst(FieldKey.ALBUM); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.artist = tag.getFirst(FieldKey.ARTIST); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.albumArtist = tag.getFirst(FieldKey.ALBUM_ARTIST); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.genre = tag.getFirst(FieldKey.GENRE); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.year = tag.getFirst(FieldKey.YEAR); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.track = tag.getFirst(FieldKey.TRACK); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.trackTotal = tag.getFirst(FieldKey.TRACK_TOTAL); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.disc = tag.getFirst(FieldKey.DISC_NO); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.discTotal = tag.getFirst(FieldKey.DISC_TOTAL); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.lyrics = tag.getFirst(FieldKey.LYRICS); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
        try { //NOSONAR
            this.comment = tag.getFirst(FieldKey.COMMENT); //NOSONAR
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetTitle(String title) { //NOSONAR
        if (title == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.title == null || !this.title.equals(title)) { //NOSONAR
            this.title = title; //NOSONAR
            titleHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetAlbum(String album) { //NOSONAR
        if (album == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.album == null || !this.album.equals(album)) { //NOSONAR
            this.album = album; //NOSONAR
            albumHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetArtist(String artist) { //NOSONAR
        if (artist == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.artist == null || !this.artist.equals(artist)) { //NOSONAR
            this.artist = artist; //NOSONAR
            artistHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetAlbumArtist(String albumArtist) { //NOSONAR
        if (albumArtist == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.albumArtist == null || !this.albumArtist.equals(albumArtist)) { //NOSONAR
            this.albumArtist = albumArtist; //NOSONAR
            albumArtistHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetGenre(String genre) { //NOSONAR
        if (genre == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.genre == null || !this.genre.equals(genre)) { //NOSONAR
            this.genre = genre; //NOSONAR
            genreHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetYear(String year) { //NOSONAR
        if (year == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.year == null || !this.year.equals(year)) { //NOSONAR
            this.year = year; //NOSONAR
            yearHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetTrack(String track) { //NOSONAR
        if (track == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.track == null || !this.track.equals(track)) { //NOSONAR
            this.track = track; //NOSONAR
            trackHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetTrackTotal(String trackTotal) { //NOSONAR
        if (trackTotal == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.trackTotal == null || !this.trackTotal.equals(trackTotal)) { //NOSONAR
            this.trackTotal = trackTotal; //NOSONAR
            trackTotalHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetDisc(String disc) { //NOSONAR
        if (disc == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.disc == null || !this.disc.equals(disc)) { //NOSONAR
            this.disc = disc; //NOSONAR
            discHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetDiscTotal(String discTotal) { //NOSONAR
        if (discTotal == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.discTotal == null || !this.discTotal.equals(discTotal)) { //NOSONAR
            this.discTotal = discTotal; //NOSONAR
            discTotalHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetLyrics(String lyrics) { //NOSONAR
        if (lyrics == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.lyrics == null || !this.lyrics.equals(lyrics)) { //NOSONAR
            this.lyrics = lyrics; //NOSONAR
            lyricsHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void softSetComment(String comment) { //NOSONAR
        if (comment == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (this.comment == null || !this.comment.equals(comment)) { //NOSONAR
            this.comment = comment; //NOSONAR
            commentHasChanged = true; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public boolean hasChanged() { //NOSONAR
        return titleHasChanged || albumHasChanged || artistHasChanged || //NOSONAR
                albumArtistHasChanged || genreHasChanged || yearHasChanged //NOSONAR
                || trackHasChanged || trackTotalHasChanged || discHasChanged //NOSONAR
                || discTotalHasChanged || lyricsHasChanged || commentHasChanged; //NOSONAR
    } // NOSONAR

    public void updateTag(Tag tag) { //NOSONAR
        if (tag == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (titleHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.TITLE, title); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (albumHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.ALBUM, album); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (artistHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.ARTIST, artist); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (albumArtistHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.ALBUM_ARTIST, albumArtist); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (genreHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.GENRE, genre); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (yearHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.YEAR, year); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (trackHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.TRACK, track); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (trackTotalHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.TRACK_TOTAL, trackTotal); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (discHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.DISC_NO, disc); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (discTotalHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.DISC_TOTAL, discTotal); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (lyricsHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.LYRICS, lyrics); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        if (commentHasChanged) { //NOSONAR
            try { //NOSONAR
                tag.setField(FieldKey.COMMENT, comment); //NOSONAR
            } catch (Exception ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
