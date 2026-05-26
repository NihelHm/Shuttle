package com.simplecity.amp_library.model;

import android.text.TextUtils;
import com.simplecity.amp_library.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 * A holder for various id3 tag information associated with a file.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class TagInfo implements Serializable {

    @SuppressWarnings("java:S1104")

    public String artistName;
    @SuppressWarnings("java:S1104")
    public String albumArtistName;
    @SuppressWarnings("java:S1104")
    public String albumName;
    @SuppressWarnings("java:S1104")
    public String trackName;
    @SuppressWarnings("java:S1104")
    public int trackNumber;
    @SuppressWarnings("java:S1104")
    public int trackTotal;
    @SuppressWarnings("java:S1104")
    public int discNumber;
    @SuppressWarnings("java:S1104")
    public int discTotal;
    @SuppressWarnings("java:S1104")
    public String bitrate;
    @SuppressWarnings("java:S1104")
    public String format;
    @SuppressWarnings("java:S1104")
    public int sampleRate;
    @SuppressWarnings("java:S1104")
    public String genre;

    public TagInfo(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    AudioFile audioFile = AudioFileIO.read(file);
                    this.artistName = getTag(audioFile, FieldKey.ARTIST);
                    this.albumArtistName = getTag(audioFile, FieldKey.ALBUM_ARTIST);
                    this.albumName = getTag(audioFile, FieldKey.ALBUM);
                    this.trackName = getTag(audioFile, FieldKey.TITLE);
                    this.trackNumber = StringUtils.parseInt(getTag(audioFile, FieldKey.TRACK));
                    this.trackTotal = StringUtils.parseInt(getTag(audioFile, FieldKey.TRACK_TOTAL));
                    this.discNumber = StringUtils.parseInt(getTag(audioFile, FieldKey.DISC_NO));
                    this.discTotal = StringUtils.parseInt(getTag(audioFile, FieldKey.DISC_TOTAL));
                    this.bitrate = getBitrate(audioFile);
                    this.format = getFormat(audioFile);
                    this.sampleRate = getSampleRate(audioFile);
                    this.genre = getTag(audioFile, FieldKey.GENRE);
                } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getTag(AudioFile audioFile, FieldKey key) {
        try {
            if (audioFile != null) {
                Tag tag = audioFile.getTag();
                if (tag != null) {
                    String result = tag.getFirst(key);
                    if (!TextUtils.isEmpty(result)) {
                        return result;
                    }
                }
            }
        } catch (UnsupportedOperationException ignored) {
            // Intentionally left empty.
        }
        return "Unknown";
    }

    public static String getBitrate(AudioFile audioFile) {
        try {
            if (audioFile != null) {
                AudioHeader audioHeader = audioFile.getAudioHeader();
                return audioHeader.getBitRate();
            }
        } catch (UnsupportedOperationException ignored) {
            // Intentionally left empty.
        }
        return "Unknown";
    }

    public static String getFormat(AudioFile audioFile) {
        try {
            if (audioFile != null) {
                AudioHeader audioHeader = audioFile.getAudioHeader();
                return audioHeader.getFormat();
            }
        } catch (UnsupportedOperationException ignored) {
            // Intentionally left empty.
        }
        return "Unknown";
    }

    public static int getSampleRate(AudioFile audioFile) {
        try {
            if (audioFile != null) {
                AudioHeader audioHeader = audioFile.getAudioHeader();
                return audioHeader.getSampleRateAsNumber();
            }
        } catch (UnsupportedOperationException ignored) {
            // Intentionally left empty.
        }
        return -1;
    }
}
