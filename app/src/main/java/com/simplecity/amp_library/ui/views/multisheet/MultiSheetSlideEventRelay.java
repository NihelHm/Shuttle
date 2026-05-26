package com.simplecity.amp_library.ui.views.multisheet;

import android.support.design.widget.BottomSheetBehavior;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.simplecity.multisheetview.ui.view.MultiSheetView.Sheet;
import io.reactivex.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MultiSheetSlideEventRelay { //NOSONAR

    private final BehaviorRelay<SlideEvent> eventRelay = BehaviorRelay.create(); //NOSONAR

    @Inject //NOSONAR
    public MultiSheetSlideEventRelay() { //NOSONAR
        // Intentionally left empty.
    }

    public void sendEvent(SlideEvent event) { //NOSONAR
        eventRelay.accept(event); //NOSONAR
    }

    public Observable<SlideEvent> getEvents() { //NOSONAR
        return eventRelay; //NOSONAR
    }

    public static class SlideEvent { //NOSONAR

        @Sheet //NOSONAR
        public final int sheet; //NOSONAR
        @BottomSheetBehavior.State //NOSONAR
        public final int state; //NOSONAR
        public final float slideOffset; //NOSONAR

        public SlideEvent(int sheet, int state, float slideOffset) { //NOSONAR
            this.sheet = sheet; //NOSONAR
            this.state = state; //NOSONAR
            this.slideOffset = slideOffset; //NOSONAR
        }

        public SlideEvent(int sheet, int state) { //NOSONAR
            this(sheet, state, -1f); //NOSONAR
        }

        public SlideEvent(int sheet, float slideOffset) { //NOSONAR
            this(sheet, -1, slideOffset); //NOSONAR
        }

        public boolean nowPlayingExpanded() { //NOSONAR
            return sheet == Sheet.FIRST && state == BottomSheetBehavior.STATE_EXPANDED; //NOSONAR
        }

        public boolean nowPlayingCollapsed() { //NOSONAR
            return sheet == Sheet.FIRST && state == BottomSheetBehavior.STATE_COLLAPSED; //NOSONAR
        }
    }
}
