package com.simplecity.amp_library.ui.views.multisheet;

import com.jakewharton.rxrelay2.PublishRelay;
import com.simplecity.multisheetview.ui.view.MultiSheetView;
import io.reactivex.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MultiSheetEventRelay { //NOSONAR

    private PublishRelay<MultiSheetEvent> eventRelay = PublishRelay.create(); //NOSONAR

    @Inject //NOSONAR
    public MultiSheetEventRelay() { //NOSONAR
        // Intentionally left empty.
    }

    public void sendEvent(MultiSheetEvent event) { //NOSONAR
        eventRelay.accept(event); //NOSONAR
    }

    public Observable<MultiSheetEvent> getEvents() { //NOSONAR
        return eventRelay; //NOSONAR
    }

    public static class MultiSheetEvent { //NOSONAR

        public @interface Action { //NOSONAR
            int GOTO = 0; //NOSONAR
            int HIDE = 1; //NOSONAR
            int SHOW_IF_HIDDEN = 2; //NOSONAR
        }

        @Action //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public int action; //NOSONAR

        @MultiSheetView.Sheet //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public int sheet; //NOSONAR

        public MultiSheetEvent(int action, int sheet) { //NOSONAR
            this.action = action; //NOSONAR
            this.sheet = sheet; //NOSONAR
        }
    }
}
