package com.simplecity.amp_library.playback;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RemoteControlClient;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cantrowitz.rxbroadcast.RxBroadcast;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.androidauto.CarHelper;
import com.simplecity.amp_library.androidauto.MediaIdHelper;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.playback.constants.InternalIntents;
import com.simplecity.amp_library.ui.screens.queue.QueueItem;
import com.simplecity.amp_library.ui.screens.queue.QueueItemKt;
import com.simplecity.amp_library.utils.LogUtils;
import com.simplecity.amp_library.utils.MediaButtonIntentReceiver;
import com.simplecity.amp_library.utils.SettingsManager;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import kotlin.Unit;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
class MediaSessionManager { //NOSONAR

    private static final String TAG = "MediaSessionManager"; //NOSONAR

    private Context context; //NOSONAR

    private MediaSessionCompat mediaSession; //NOSONAR

    private QueueManager queueManager; //NOSONAR

    private PlaybackManager playbackManager; //NOSONAR

    private PlaybackSettingsManager playbackSettingsManager; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private CompositeDisposable disposables = new CompositeDisposable(); //NOSONAR

    private MediaIdHelper mediaIdHelper; //NOSONAR

    private static String SHUFFLE_ACTION = "ACTION_SHUFFLE"; //NOSONAR

    MediaSessionManager( //NOSONAR
            Context context, //NOSONAR
            QueueManager queueManager, //NOSONAR
            PlaybackManager playbackManager, //NOSONAR
            PlaybackSettingsManager playbackSettingsManager, //NOSONAR
            SettingsManager settingsManager, //NOSONAR
            Repository.SongsRepository songsRepository, //NOSONAR
            Repository.AlbumsRepository albumsRepository, //NOSONAR
            Repository.AlbumArtistsRepository albumArtistsRepository, //NOSONAR
            Repository.GenresRepository genresRepository, //NOSONAR
            Repository.PlaylistsRepository playlistsRepository //NOSONAR
    ) {
        this.context = context.getApplicationContext(); //NOSONAR
        this.queueManager = queueManager; //NOSONAR
        this.playbackManager = playbackManager; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
        this.playbackSettingsManager = playbackSettingsManager; //NOSONAR

        mediaIdHelper = new MediaIdHelper((ShuttleApplication) context.getApplicationContext(), songsRepository, albumsRepository, albumArtistsRepository, genresRepository, playlistsRepository); //NOSONAR

        ComponentName mediaButtonReceiverComponent = new ComponentName(context.getPackageName(), MediaButtonIntentReceiver.class.getName()); //NOSONAR
        mediaSession = new MediaSessionCompat(context, "Shuttle", mediaButtonReceiverComponent, null); //NOSONAR
        mediaSession.setCallback(new MediaSessionCompat.Callback() { //NOSONAR
            @Override //NOSONAR
            public void onPause() { //NOSONAR
                playbackManager.pause(true); //NOSONAR
            }

            @Override //NOSONAR
            public void onPlay() { //NOSONAR
                playbackManager.play(); //NOSONAR
            }

            @Override //NOSONAR
            public void onSeekTo(long pos) { //NOSONAR
                playbackManager.seekTo(pos); //NOSONAR
            }

            @Override //NOSONAR
            public void onSkipToNext() { //NOSONAR
                playbackManager.next(true); //NOSONAR
            }

            @Override //NOSONAR
            public void onSkipToPrevious() { //NOSONAR
                playbackManager.previous(false); //NOSONAR
            }

            @Override //NOSONAR
            public void onSkipToQueueItem(long id) { //NOSONAR
                List<QueueItem> queueItems = queueManager.getCurrentPlaylist(); //NOSONAR

                QueueItem queueItem = Stream.of(queueItems) //NOSONAR
                        .filter(aQueueItem -> (long) aQueueItem.hashCode() == id) //NOSONAR
                        .findFirst() //NOSONAR
                        .orElse(null); //NOSONAR

                if (queueItem != null) { //NOSONAR
                    playbackManager.setQueuePosition(queueItems.indexOf(queueItem)); //NOSONAR
                }
            }

            @Override //NOSONAR
            public void onStop() { //NOSONAR
                playbackManager.stop(true); //NOSONAR
            }

            @Override //NOSONAR
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) { //NOSONAR
                Log.e("MediaButtonReceiver", "OnMediaButtonEvent called"); //NOSONAR
                MediaButtonIntentReceiver.handleIntent(context, mediaButtonEvent, playbackSettingsManager); //NOSONAR
                return true; //NOSONAR
            }

            @Override //NOSONAR
            public void onPlayFromMediaId(String mediaId, Bundle extras) { //NOSONAR
                mediaIdHelper.getSongListForMediaId(mediaId, (songs, position) -> { //NOSONAR
                    playbackManager.load((List<Song>) songs, position, true, 0); //NOSONAR
                    return Unit.INSTANCE; //NOSONAR
                });
            }

            @SuppressWarnings("ResultOfMethodCallIgnored") //NOSONAR
            @SuppressLint("CheckResult") //NOSONAR
            @Override //NOSONAR
            public void onPlayFromSearch(String query, Bundle extras) { //NOSONAR
                if (TextUtils.isEmpty(query)) { //NOSONAR
                    playbackManager.play(); //NOSONAR
                } else { //NOSONAR
                    mediaIdHelper.handlePlayFromSearch(query, extras) //NOSONAR
                            .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                            .subscribe( //NOSONAR
                                    pair -> { //NOSONAR
                                        if (!pair.getFirst().isEmpty()) { //NOSONAR
                                            playbackManager.load(pair.getFirst(), pair.getSecond(), true, 0); //NOSONAR
                                        } else { //NOSONAR
                                            playbackManager.pause(false); //NOSONAR
                                        }
                                    },
                                    error -> LogUtils.logException(TAG, "Failed to gather songs from search. Query: " + query, error) //NOSONAR
                            );
                }
            }

            @Override //NOSONAR
            public void onCustomAction(String action, Bundle extras) { //NOSONAR
                if (action.equals(SHUFFLE_ACTION)) { //NOSONAR
                    queueManager.setShuffleMode(queueManager.shuffleMode == QueueManager.ShuffleMode.ON ? QueueManager.ShuffleMode.OFF : QueueManager.ShuffleMode.ON); //NOSONAR
                }
                updateMediaSession(action); //NOSONAR
            }
        });

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS); //NOSONAR

        //For some reason, MediaSessionCompat doesn't seem to pass all of the available 'actions' on as
        //transport control flags for the RCC, so we do that manually
        RemoteControlClient remoteControlClient = (RemoteControlClient) mediaSession.getRemoteControlClient(); //NOSONAR
        if (remoteControlClient != null) { //NOSONAR
            remoteControlClient.setTransportControlFlags( //NOSONAR
                    RemoteControlClient.FLAG_KEY_MEDIA_PAUSE //NOSONAR
                            | RemoteControlClient.FLAG_KEY_MEDIA_PLAY //NOSONAR
                            | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE //NOSONAR
                            | RemoteControlClient.FLAG_KEY_MEDIA_NEXT //NOSONAR
                            | RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS //NOSONAR
                            | RemoteControlClient.FLAG_KEY_MEDIA_STOP); //NOSONAR
        }

        IntentFilter intentFilter = new IntentFilter(); //NOSONAR
        intentFilter.addAction(InternalIntents.QUEUE_CHANGED); //NOSONAR
        intentFilter.addAction(InternalIntents.META_CHANGED); //NOSONAR
        intentFilter.addAction(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
        intentFilter.addAction(InternalIntents.POSITION_CHANGED); //NOSONAR
        disposables.add(RxBroadcast.fromBroadcast(context, intentFilter).subscribe(intent -> { //NOSONAR
            String action = intent.getAction(); //NOSONAR
            if (action != null) { //NOSONAR
                updateMediaSession(intent.getAction()); //NOSONAR
            }
        }));
    }

    private void updateMediaSession(final String action) { //NOSONAR

        int playState = playbackManager.isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED; //NOSONAR

        long playbackActions = getMediaSessionActions(); //NOSONAR

        QueueItem currentQueueItem = queueManager.getCurrentQueueItem(); //NOSONAR

        PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder(); //NOSONAR
        builder.setActions(playbackActions); //NOSONAR

        switch (queueManager.shuffleMode) { //NOSONAR
            case QueueManager.ShuffleMode.OFF: //NOSONAR
                builder.addCustomAction( //NOSONAR
                        new PlaybackStateCompat.CustomAction.Builder(SHUFFLE_ACTION, context.getString(R.string.btn_shuffle_on), R.drawable.ic_shuffle_off_circled).build()); //NOSONAR
                break; //NOSONAR
            case QueueManager.ShuffleMode.ON: //NOSONAR
                builder.addCustomAction( //NOSONAR
                        new PlaybackStateCompat.CustomAction.Builder(SHUFFLE_ACTION, context.getString(R.string.btn_shuffle_off), R.drawable.ic_shuffle_on_circled).build()); //NOSONAR
                break; //NOSONAR
        }

        builder.setState(playState, playbackManager.getSeekPosition(), 1.0f); //NOSONAR

        if (currentQueueItem != null) { //NOSONAR
            builder.setActiveQueueItemId((long) currentQueueItem.hashCode()); //NOSONAR
        }

        PlaybackStateCompat playbackState = builder.build(); //NOSONAR

        if (action.equals(InternalIntents.PLAY_STATE_CHANGED) || action.equals(InternalIntents.POSITION_CHANGED) || action.equals(SHUFFLE_ACTION)) { //NOSONAR
            mediaSession.setPlaybackState(playbackState); //NOSONAR
        } else if (action.equals(InternalIntents.META_CHANGED) || action.equals(InternalIntents.QUEUE_CHANGED)) { //NOSONAR

            if (currentQueueItem != null) { //NOSONAR
                MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder() //NOSONAR
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, String.valueOf(currentQueueItem.getSong().id)) //NOSONAR
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentQueueItem.getSong().artistName) //NOSONAR
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, currentQueueItem.getSong().albumArtistName) //NOSONAR
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, currentQueueItem.getSong().albumName) //NOSONAR
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentQueueItem.getSong().name) //NOSONAR
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, currentQueueItem.getSong().duration) //NOSONAR
                        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, (long) (queueManager.queuePosition + 1)) //NOSONAR
                        //Getting the genre is expensive.. let's not bother for now.
                        //.putString(MediaMetadataCompat.METADATA_KEY_GENRE, getGenreName())
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null) //NOSONAR
                        .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, (long) (queueManager.getCurrentPlaylist().size())); //NOSONAR

                // If we're in car mode, don't wait for the artwork to load before setting session metadata.
                if (CarHelper.isCarUiMode(context)) { //NOSONAR
                    mediaSession.setMetadata(metaData.build()); //NOSONAR
                }

                mediaSession.setPlaybackState(playbackState); //NOSONAR

                mediaSession.setQueue(QueueItemKt.toMediaSessionQueueItems(queueManager.getCurrentPlaylist())); //NOSONAR
                mediaSession.setQueueTitle(context.getString(R.string.menu_queue)); //NOSONAR

                if (settingsManager.showLockscreenArtwork() || CarHelper.isCarUiMode(context)) { //NOSONAR
                    updateMediaSessionArtwork(metaData); //NOSONAR
                } else { //NOSONAR
                    mediaSession.setMetadata(metaData.build()); //NOSONAR
                }
            }
        }
    }

    private void updateMediaSessionArtwork(MediaMetadataCompat.Builder metaData) { //NOSONAR
        QueueItem currentQueueItem = queueManager.getCurrentQueueItem(); //NOSONAR
        if (currentQueueItem != null) { //NOSONAR
            disposables.add(Completable.defer(() -> Completable.fromAction(() -> //NOSONAR
                            Glide.with(context) //NOSONAR
                                    .load(currentQueueItem.getSong().getAlbum()) //NOSONAR
                                    .asBitmap() //NOSONAR
                                    .override(1024, 1024) //NOSONAR
                                    .into(new SimpleTarget<Bitmap>() { //NOSONAR
                                        @Override //NOSONAR
                                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) { //NOSONAR
                                            if (bitmap != null) { //NOSONAR
                                                metaData.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap); //NOSONAR
                                            }
                                            try { //NOSONAR
                                                mediaSession.setMetadata(metaData.build()); //NOSONAR
                                            } catch (NullPointerException e) { //NOSONAR
                                                metaData.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null); //NOSONAR
                                                mediaSession.setMetadata(metaData.build()); //NOSONAR
                                            }
                                        }

                                        @Override //NOSONAR
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) { //NOSONAR
                                            super.onLoadFailed(e, errorDrawable); //NOSONAR
                                            mediaSession.setMetadata(metaData.build()); //NOSONAR
                                        }
                                    })
                    ))
                            .subscribeOn(AndroidSchedulers.mainThread()) //NOSONAR
                            .subscribe() //NOSONAR
            );
        }
    }

    private long getMediaSessionActions() { //NOSONAR
        return PlaybackStateCompat.ACTION_PLAY //NOSONAR
                | PlaybackStateCompat.ACTION_PAUSE //NOSONAR
                | PlaybackStateCompat.ACTION_PLAY_PAUSE //NOSONAR
                | PlaybackStateCompat.ACTION_SKIP_TO_NEXT //NOSONAR
                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS //NOSONAR
                | PlaybackStateCompat.ACTION_STOP //NOSONAR
                | PlaybackStateCompat.ACTION_SEEK_TO //NOSONAR
                | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID //NOSONAR
                | PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH //NOSONAR
                | PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM; //NOSONAR
    }

    MediaSessionCompat.Token getSessionToken() { //NOSONAR
        return mediaSession.getSessionToken(); //NOSONAR
    }

    void setActive(boolean active) { //NOSONAR
        mediaSession.setActive(active); //NOSONAR
    }

    void destroy() { //NOSONAR
        disposables.clear(); //NOSONAR
        mediaSession.release(); //NOSONAR
    }
}
