package com.simplecity.amp_library.ui.screens.drawer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.jakewharton.rxrelay2.PublishRelay;
import com.simplecity.amp_library.ShuttleApplication;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.ShuttleUtils;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class NavigationEventRelay { //NOSONAR

    static NavigationEvent librarySelectedEvent = new NavigationEvent(NavigationEvent.Type.LIBRARY_SELECTED); //NOSONAR
    static NavigationEvent sleepTimerSelectedEvent = new NavigationEvent(NavigationEvent.Type.SLEEP_TIMER_SELECTED); //NOSONAR
    static NavigationEvent equalizerSelectedEvent = new NavigationEvent(NavigationEvent.Type.EQUALIZER_SELECTED); //NOSONAR
    static NavigationEvent settingsSelectedEvent = new NavigationEvent(NavigationEvent.Type.SETTINGS_SELECTED); //NOSONAR
    static NavigationEvent supportSelectedEvent = new NavigationEvent(NavigationEvent.Type.SUPPORT_SELECTED); //NOSONAR

    static NavigationEvent getFoldersSelectedEvent(ShuttleApplication application, SettingsManager settingsManager) { //NOSONAR
        return new NavigationEvent(NavigationEvent.Type.FOLDERS_SELECTED) { //NOSONAR
            @Override //NOSONAR
            public boolean isActionable() { //NOSONAR
                return ShuttleUtils.isUpgraded(application, settingsManager); //NOSONAR
            }
        };
    }

    private PublishRelay<NavigationEvent> relay = PublishRelay.create(); //NOSONAR

    @Inject //NOSONAR
    public NavigationEventRelay() { //NOSONAR
        // Intentionally left empty.
    }

    public void sendEvent(@NonNull NavigationEvent event) { //NOSONAR
        relay.accept(event); //NOSONAR
    }

    public Observable<NavigationEvent> getEvents() { //NOSONAR
        // Delay the event a tiny bit, to allow the drawer to close.
        return relay;//.delay(250, TimeUnit.MILLISECONDS); //NOSONAR
    }

    public static class NavigationEvent { //NOSONAR

        public @interface Type { //NOSONAR
            int LIBRARY_SELECTED = 0; //NOSONAR
            int FOLDERS_SELECTED = 1; //NOSONAR
            int SLEEP_TIMER_SELECTED = 2; //NOSONAR
            int EQUALIZER_SELECTED = 3; //NOSONAR
            int SETTINGS_SELECTED = 4; //NOSONAR
            int SUPPORT_SELECTED = 5; //NOSONAR
            int PLAYLIST_SELECTED = 6; //NOSONAR
            int GO_TO_ARTIST = 7; //NOSONAR
            int GO_TO_ALBUM = 8; //NOSONAR
            int GO_TO_GENRE = 9; //NOSONAR
        }

        @Type //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public int type; //NOSONAR

        @Nullable //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public Object data; //NOSONAR

        private boolean isActionable = true; //NOSONAR

        /**
         * @param type the {@link Type of event}
         * @param data optional Object to be passed with this event
         * @param isActionable true if navigational changes should be performed in response to this NavigationEvent
         * Defaults to true.
         */
        public NavigationEvent(int type, @Nullable Object data, boolean isActionable) { //NOSONAR
            this.type = type; //NOSONAR
            this.data = data; //NOSONAR
            this.isActionable = isActionable; //NOSONAR
        }

        /**
         * @param type the {@link Type of event}
         * @param data optional Object to be passed with this event
         */
        public NavigationEvent(int type, @Nullable Object data) { //NOSONAR
            this.type = type; //NOSONAR
            this.data = data; //NOSONAR
        }

        /**
         * @param type the {@link Type of event}
         */
        NavigationEvent(int type) { //NOSONAR
            this.type = type; //NOSONAR
        }

        public boolean isActionable() { //NOSONAR
            return isActionable; //NOSONAR
        }
    }
}
