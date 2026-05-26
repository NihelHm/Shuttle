package com.simplecity.amp_library.ui.views.multisheet;

import com.jakewharton.rxrelay2.PublishRelay;
import com.simplecity.multisheetview.ui.view.MultiSheetView;
import io.reactivex.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class MultiSheetEventRelay {

    private PublishRelay<MultiSheetEvent> eventRelay = PublishRelay.create();

    @Inject
    public MultiSheetEventRelay() {
        // Intentionally left empty.
    }

    public void sendEvent(MultiSheetEvent event) {
        eventRelay.accept(event);
    }

    public Observable<MultiSheetEvent> getEvents() {
        return eventRelay;
    }

    public static class MultiSheetEvent {

        public @interface Action {
            int GOTO = 0;
            int HIDE = 1;
            int SHOW_IF_HIDDEN = 2;
        }

        @Action
        @SuppressWarnings("java:S1104")
        public int action;

        @MultiSheetView.Sheet
        @SuppressWarnings("java:S1104")
        public int sheet;

        public MultiSheetEvent(int action, int sheet) {
            this.action = action;
            this.sheet = sheet;
        }
    }
}
