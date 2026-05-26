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
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TagInfo implements Serializable { //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public String artistName; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String albumArtistName; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String albumName; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String trackName; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int trackNumber; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int trackTotal; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int discNumber; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int discTotal; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String bitrate; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String format; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public int sampleRate; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public String genre; //NOSONAR

    public TagInfo(String filePath) { //NOSONAR
        if (filePath != null) { //NOSONAR
            File file = new File(filePath); //NOSONAR
            if (file.exists()) { //NOSONAR
                try { //NOSONAR
                    AudioFile audioFile = AudioFileIO.read(file); //NOSONAR
                    this.artistName = getTag(audioFile, FieldKey.ARTIST); //NOSONAR
                    this.albumArtistName = getTag(audioFile, FieldKey.ALBUM_ARTIST); //NOSONAR
                    this.albumName = getTag(audioFile, FieldKey.ALBUM); //NOSONAR
                    this.trackName = getTag(audioFile, FieldKey.TITLE); //NOSONAR
                    this.trackNumber = StringUtils.parseInt(getTag(audioFile, FieldKey.TRACK)); //NOSONAR
                    this.trackTotal = StringUtils.parseInt(getTag(audioFile, FieldKey.TRACK_TOTAL)); //NOSONAR
                    this.discNumber = StringUtils.parseInt(getTag(audioFile, FieldKey.DISC_NO)); //NOSONAR
                    this.discTotal = StringUtils.parseInt(getTag(audioFile, FieldKey.DISC_TOTAL)); //NOSONAR
                    this.bitrate = getBitrate(audioFile); //NOSONAR
                    this.format = getFormat(audioFile); //NOSONAR
                    this.sampleRate = getSampleRate(audioFile); //NOSONAR
                    this.genre = getTag(audioFile, FieldKey.GENRE); //NOSONAR
                } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) { //NOSONAR
                    e.printStackTrace(); //NOSONAR
                }
            }
        }
    }

    public String getTag(AudioFile audioFile, FieldKey key) { //NOSONAR
        try { //NOSONAR
            if (audioFile != null) { //NOSONAR
                Tag tag = audioFile.getTag(); //NOSONAR
                if (tag != null) { //NOSONAR
                    String result = tag.getFirst(key); //NOSONAR
                    if (!TextUtils.isEmpty(result)) { //NOSONAR
                        return result; //NOSONAR
                    }
                }
            }
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty.
        }
        return "Unknown"; //NOSONAR
    }

    public static String getBitrate(AudioFile audioFile) { //NOSONAR
        try { //NOSONAR
            if (audioFile != null) { //NOSONAR
                AudioHeader audioHeader = audioFile.getAudioHeader(); //NOSONAR
                return audioHeader.getBitRate(); //NOSONAR
            }
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty.
        }
        return "Unknown"; //NOSONAR
    }

    public static String getFormat(AudioFile audioFile) { //NOSONAR
        try { //NOSONAR
            if (audioFile != null) { //NOSONAR
                AudioHeader audioHeader = audioFile.getAudioHeader(); //NOSONAR
                return audioHeader.getFormat(); //NOSONAR
            }
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty.
        }
        return "Unknown"; //NOSONAR
    }

    public static int getSampleRate(AudioFile audioFile) { //NOSONAR
        try { //NOSONAR
            if (audioFile != null) { //NOSONAR
                AudioHeader audioHeader = audioFile.getAudioHeader(); //NOSONAR
                return audioHeader.getSampleRateAsNumber(); //NOSONAR
            }
        } catch (UnsupportedOperationException ignored) { //NOSONAR
            // Intentionally left empty.
        }
        return -1; //NOSONAR
    }
}
