package com.simplecity.amp_library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.playback.MusicService;
import com.simplecity.amp_library.playback.PlaybackSettingsManager;
import com.simplecity.amp_library.playback.constants.MediaButtonCommand;
import com.simplecity.amp_library.playback.constants.ServiceCommand;
import com.simplecity.amp_library.ui.screens.main.MainActivity;
import dagger.android.DaggerBroadcastReceiver;
import javax.inject.Inject;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MediaButtonIntentReceiver extends DaggerBroadcastReceiver { //NOSONAR

    private static final int MSG_LONGPRESS_TIMEOUT = 1; //NOSONAR
    private static final int MSG_HEADSET_DOUBLE_CLICK_TIMEOUT = 2; //NOSONAR
    private static final int LONG_PRESS_DELAY = 1000; //NOSONAR
    private static final int DOUBLE_CLICK = 800; //NOSONAR

    private static int clickCounter = 0; //NOSONAR
    private static long lastClickTime = 0; //NOSONAR
    private static boolean down = false; //NOSONAR
    private static boolean launched = false; //NOSONAR

    private static PowerManager.WakeLock wakeLock = null; //NOSONAR

    private static MediaButtonMessageHander mediaButtonMessageHander = new MediaButtonMessageHander(); //NOSONAR

    @Inject //NOSONAR
    PlaybackSettingsManager playbackSettingsManager; //NOSONAR

    public MediaButtonIntentReceiver() { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onReceive(Context context, Intent intent) { //NOSONAR
        super.onReceive(context, intent); //NOSONAR

        handleIntent(context, intent, playbackSettingsManager); //NOSONAR

        if (isOrderedBroadcast()) { //NOSONAR
            abortBroadcast(); //NOSONAR
        }
    }

    public static void handleIntent(Context context, Intent intent, PlaybackSettingsManager playbackSettingsManager) { //NOSONAR
        String intentAction = intent.getAction(); //NOSONAR

        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction) && playbackSettingsManager.getPauseOnHeadsetDisconnect()) { //NOSONAR
            startService(context, MediaButtonCommand.PAUSE); //NOSONAR
        } else if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) { //NOSONAR
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT); //NOSONAR
            if (event == null) { //NOSONAR
                return; //NOSONAR
            }

            int keyCode = event.getKeyCode(); //NOSONAR
            int action = event.getAction(); //NOSONAR
            long eventTime = event.getEventTime(); //NOSONAR

            String command = null; //NOSONAR
            switch (keyCode) { //NOSONAR
                case KeyEvent.KEYCODE_MEDIA_STOP: //NOSONAR
                    command = MediaButtonCommand.STOP; //NOSONAR
                    break; //NOSONAR
                case KeyEvent.KEYCODE_HEADSETHOOK: //NOSONAR
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: //NOSONAR
                    command = MediaButtonCommand.TOGGLE_PAUSE; //NOSONAR
                    break; //NOSONAR
                case KeyEvent.KEYCODE_MEDIA_NEXT: //NOSONAR
                    command = MediaButtonCommand.NEXT; //NOSONAR
                    break; //NOSONAR
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS: //NOSONAR
                    command = MediaButtonCommand.PREVIOUS; //NOSONAR
                    break; //NOSONAR
                case KeyEvent.KEYCODE_MEDIA_PAUSE: //NOSONAR
                    command = MediaButtonCommand.PAUSE; //NOSONAR
                    break; //NOSONAR
                case KeyEvent.KEYCODE_MEDIA_PLAY: //NOSONAR
                    command = MediaButtonCommand.PLAY; //NOSONAR
                    break; //NOSONAR
            }

            if (command != null) { //NOSONAR
                if (action == KeyEvent.ACTION_DOWN) { //NOSONAR
                    if (down) { //NOSONAR
                        if ((MediaButtonCommand.TOGGLE_PAUSE.equals(command) || //NOSONAR
                                MediaButtonCommand.PLAY.equals(command))) { //NOSONAR
                            if (lastClickTime != 0 && eventTime - lastClickTime > LONG_PRESS_DELAY) { //NOSONAR
                                acquireWakeLockAndSendMessage(context, mediaButtonMessageHander.obtainMessage(MSG_LONGPRESS_TIMEOUT, context), 0); //NOSONAR
                            }
                        }
                    } else if (event.getRepeatCount() == 0) { //NOSONAR
                        // Only consider the first event in a sequence, not the repeat events,
                        // so that we don't trigger in cases where the first event went to a
                        // different app (e.g. when the user ends a phone call by long pressing
                        // the headset button)

                        // The service may or may not be running, but we need to send it a command
                        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) { //NOSONAR
                            if (eventTime - lastClickTime >= DOUBLE_CLICK) { //NOSONAR
                                clickCounter = 0; //NOSONAR
                            }

                            clickCounter++; //NOSONAR

                            mediaButtonMessageHander.removeMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT); //NOSONAR

                            Message msg = mediaButtonMessageHander.obtainMessage(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT, clickCounter, 0, context); //NOSONAR

                            long delay = clickCounter < 3 ? DOUBLE_CLICK : 0; //NOSONAR
                            if (clickCounter >= 3) { //NOSONAR
                                clickCounter = 0; //NOSONAR
                            }
                            lastClickTime = eventTime; //NOSONAR
                            acquireWakeLockAndSendMessage(context, msg, delay); //NOSONAR
                        } else { //NOSONAR
                            startService(context, command); //NOSONAR
                        }
                        launched = false; //NOSONAR
                        down = true; //NOSONAR
                    }
                } else { //NOSONAR
                    mediaButtonMessageHander.removeMessages(MSG_LONGPRESS_TIMEOUT); //NOSONAR
                    down = false; //NOSONAR
                }

                releaseWakeLockIfHandlerIdle(); //NOSONAR
            }
        }
    }

    static void beep(Context context) { //NOSONAR
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_headset_beep", true)) { //NOSONAR
            AsyncPlayer beepPlayer = new AsyncPlayer("BeepPlayer"); //NOSONAR
            Uri beepSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + //NOSONAR
                    context.getResources().getResourcePackageName(R.raw.beep) + '/' + //NOSONAR
                    context.getResources().getResourceTypeName(R.raw.beep) + '/' + //NOSONAR
                    context.getResources().getResourceEntryName(R.raw.beep)); //NOSONAR

            if (ShuttleUtils.hasMarshmallow()) { //NOSONAR
                AudioAttributes audioAttributes = new AudioAttributes.Builder() //NOSONAR
                        // Could use AudioAttributes.ASSISTANCE_SONIFICATION here, since this represents a button press type action..
                        // However, that seems to play our audio a little too quietly (and the beep track is already adjusted to be relatively quiet).
                        // So let's just treat it as music, which will use the user's music stream's volume anyway.
                        .setUsage(AudioAttributes.USAGE_MEDIA) //NOSONAR
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) //NOSONAR
                        .build(); //NOSONAR
                beepPlayer.play(context, beepSoundUri, false, audioAttributes); //NOSONAR
            } else { //NOSONAR
                beepPlayer.play(context, beepSoundUri, false, AudioManager.STREAM_MUSIC); //NOSONAR
            }
        }
    }

    static void startService(Context context, String command) { //NOSONAR

        // If we're attempting to pause, and the service isn't already running, return early. This prevents an issue where
        // we call startForegroundService, and then we don't proceed to call startForeground() on the service, since the service
        // basically gets shutdown again due to the fact that we're not playing anything.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && MediaButtonCommand.PAUSE.equals(command)) { //NOSONAR
            if (MusicServiceConnectionUtils.serviceBinder == null || MusicServiceConnectionUtils.serviceBinder.getService() == null) { //NOSONAR
                return; //NOSONAR
            }
        }

        Intent intent = new Intent(context, MusicService.class); //NOSONAR
        intent.setAction(ServiceCommand.COMMAND); //NOSONAR
        intent.putExtra(MediaButtonCommand.CMD_NAME, command); //NOSONAR
        intent.putExtra(MediaButtonCommand.FROM_MEDIA_BUTTON, true); //NOSONAR

        if (MediaButtonCommand.PREVIOUS.equals(command)) { //NOSONAR
            intent.putExtra(MediaButtonCommand.FORCE_PREVIOUS, true); //NOSONAR
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //NOSONAR
            context.startForegroundService(intent); //NOSONAR
        } else { //NOSONAR
            context.startService(intent); //NOSONAR
        }
    }

    static void acquireWakeLockAndSendMessage(Context context, Message msg, long delay) { //NOSONAR
        if (wakeLock == null) { //NOSONAR
            Context appContext = context.getApplicationContext(); //NOSONAR
            PowerManager pm = (PowerManager) appContext.getSystemService(Context.POWER_SERVICE); //NOSONAR
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Shuttle:HeadsetButton"); //NOSONAR
            wakeLock.setReferenceCounted(false); //NOSONAR
        }

        // Make sure we don't indefinitely hold the wake lock under any circumstances
        wakeLock.acquire(10000); //NOSONAR

        mediaButtonMessageHander.sendMessageDelayed(msg, delay); //NOSONAR
    }

    static void releaseWakeLockIfHandlerIdle() { //NOSONAR
        if (mediaButtonMessageHander.hasMessages(MSG_LONGPRESS_TIMEOUT) || mediaButtonMessageHander.hasMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT)) { //NOSONAR
            return; //NOSONAR
        }

        if (wakeLock != null) { //NOSONAR
            wakeLock.release(); //NOSONAR
            wakeLock = null; //NOSONAR
        }
    }

    private static class MediaButtonMessageHander extends Handler { //NOSONAR

        @Override //NOSONAR
        public void handleMessage(Message msg) { //NOSONAR

            switch (msg.what) { //NOSONAR
                case MSG_LONGPRESS_TIMEOUT: //NOSONAR
                    if (!launched) { //NOSONAR
                        Context context = (Context) msg.obj; //NOSONAR
                        Intent intent = new Intent(); //NOSONAR
                        intent.setClass(context, MainActivity.class); //NOSONAR
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //NOSONAR
                        context.startActivity(intent); //NOSONAR
                        launched = true; //NOSONAR
                    }
                    break; //NOSONAR

                case MSG_HEADSET_DOUBLE_CLICK_TIMEOUT: //NOSONAR
                    int clickCount = msg.arg1; //NOSONAR
                    String command; //NOSONAR

                    switch (clickCount) { //NOSONAR
                        case 1: //NOSONAR
                            command = MediaButtonCommand.TOGGLE_PAUSE; //NOSONAR
                            break; //NOSONAR
                        case 2: //NOSONAR
                            command = MediaButtonCommand.NEXT; //NOSONAR
                            break; //NOSONAR
                        case 3: //NOSONAR
                            command = MediaButtonCommand.PREVIOUS; //NOSONAR
                            break; //NOSONAR
                        default: //NOSONAR
                            command = null; //NOSONAR
                            break; //NOSONAR
                    }

                    if (command != null) { //NOSONAR
                        Context context = (Context) msg.obj; //NOSONAR
                        if (MediaButtonCommand.NEXT.equals((command))) { //NOSONAR
                            beep(context); //NOSONAR
                        }
                        startService(context, command); //NOSONAR
                    }
                    break; //NOSONAR
            }
            releaseWakeLockIfHandlerIdle(); //NOSONAR
        }
    }
}
