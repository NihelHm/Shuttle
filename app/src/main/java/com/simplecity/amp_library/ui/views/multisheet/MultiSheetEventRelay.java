package com.simplecity.amp_library.ui.views.multisheet; // NOSONAR

import com.jakewharton.rxrelay2.PublishRelay; // NOSONAR
import com.simplecity.multisheetview.ui.view.MultiSheetView; // NOSONAR
import io.reactivex.Observable; // NOSONAR

import javax.inject.Inject; // NOSONAR
import javax.inject.Singleton; // NOSONAR

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MultiSheetEventRelay { //NOSONAR

    private PublishRelay<MultiSheetEvent> eventRelay = PublishRelay.create(); //NOSONAR

    @Inject //NOSONAR
    public MultiSheetEventRelay() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public void sendEvent(MultiSheetEvent event) { //NOSONAR
        eventRelay.accept(event); //NOSONAR
    } // NOSONAR

    public Observable<MultiSheetEvent> getEvents() { //NOSONAR
        return eventRelay; //NOSONAR
    } // NOSONAR

    public static class MultiSheetEvent { //NOSONAR

        public @interface Action { //NOSONAR
            int GOTO = 0; //NOSONAR
            int HIDE = 1; //NOSONAR
            int SHOW_IF_HIDDEN = 2; //NOSONAR
        } // NOSONAR

        @Action //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public int action; //NOSONAR

        @MultiSheetView.Sheet //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public int sheet; //NOSONAR

        public MultiSheetEvent(int action, int sheet) { //NOSONAR
            this.action = action; //NOSONAR
            this.sheet = sheet; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
