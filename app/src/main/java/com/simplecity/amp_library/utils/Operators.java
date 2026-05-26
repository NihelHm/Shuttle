package com.simplecity.amp_library.utils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.simplecity.amp_library.model.Album;
import com.simplecity.amp_library.model.AlbumArtist;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.utils.sorting.SortManager;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Operators { //NOSONAR

    private static final String TAG = "Operators"; //NOSONAR

    public static List<Album> songsToAlbums(List<Song> songs) { //NOSONAR

        HashMap<Long, Album> albumMap = new HashMap<>(); //NOSONAR

        for (Song song : songs) { //NOSONAR

            //Create an album representing the album this song belongs to
            Album album = song.getAlbum(); //NOSONAR

            //Now check if there's already an equivalent album in our albumMap
            Album oldAlbum = albumMap.get(album.id); //NOSONAR

            if (oldAlbum != null) { //NOSONAR

                //Increment the number of songs.
                oldAlbum.numSongs++; //NOSONAR

                //The number of discs is just the largest disc number for songs
                oldAlbum.numDiscs = Math.max(song.discNumber, oldAlbum.numDiscs); //NOSONAR

                oldAlbum.songPlayCount += song.playCount; //NOSONAR

                //Add any new artists
                Stream.of(album.artists) //NOSONAR
                        .filter(artist -> !oldAlbum.artists.contains(artist)) //NOSONAR
                        .forEach(artist -> oldAlbum.artists.add(artist)); //NOSONAR

                //Add new paths
                Stream.of(album.paths) //NOSONAR
                        .filter(path -> !oldAlbum.paths.contains(path)) //NOSONAR
                        .forEach(path -> oldAlbum.paths.add(path)); //NOSONAR
            } else { //NOSONAR
                //Couldn't find an existing entry for this album. Add a new one.
                albumMap.put(album.id, album); //NOSONAR
            }
        }

        return new ArrayList<>(albumMap.values()); //NOSONAR
    }

    public static List<AlbumArtist> albumsToAlbumArtists(List<Album> albums) { //NOSONAR

        HashMap<String, AlbumArtist> albumArtistMap = new HashMap<>(); //NOSONAR

        for (Album album : albums) { //NOSONAR

            //Create an album-artist representing the album-artist this album belongs to
            AlbumArtist albumArtist = album.getAlbumArtist(); //NOSONAR

            //Check if there's already an equivalent album-artist in our albumArtistMap
            AlbumArtist oldAlbumArtist = albumArtistMap.get(albumArtist.name); //NOSONAR
            if (oldAlbumArtist != null) { //NOSONAR

                //Add this album to the album artist's albums
                if (!oldAlbumArtist.albums.contains(album)) { //NOSONAR
                    oldAlbumArtist.albums.add(album); //NOSONAR
                }
            } else { //NOSONAR
                albumArtistMap.put(albumArtist.name, albumArtist); //NOSONAR
            }
        }

        return new ArrayList<>(albumArtistMap.values()); //NOSONAR
    }

    public static List<Song> albumShuffleSongs(List<Song> songs, SortManager sortManager) { //NOSONAR

        sortManager.sortSongs(songs, SortManager.SongSort.ALBUM_NAME); //NOSONAR

        List<Map.Entry<Long, List<Song>>> albumSongMap = Stream.of(songs) //NOSONAR
                .groupBy(song -> song.albumId) //NOSONAR
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> { //NOSONAR
                    Collections.shuffle(list); //NOSONAR
                    return list; //NOSONAR
                }));

        return Stream.of(albumSongMap) //NOSONAR
                .flatMap(stringListEntry -> Stream.of(stringListEntry.getValue())) //NOSONAR
                .toList(); //NOSONAR
    }

    public static Single<List<Song>> reduceSongSingles(List<Single<List<Song>>> singles) { //NOSONAR
        return Single.zip(singles, //NOSONAR
                lists -> Stream.of(lists) //NOSONAR
                        .map(o -> (List<Song>) o) //NOSONAR
                        .reduce((value1, value2) -> { //NOSONAR
                            List<Song> allSongs = new ArrayList<>(); //NOSONAR
                            allSongs.addAll(value1); //NOSONAR
                            allSongs.addAll(value2); //NOSONAR
                            return allSongs; //NOSONAR
                        }).orElse(Collections.emptyList())); //NOSONAR
    }
}
