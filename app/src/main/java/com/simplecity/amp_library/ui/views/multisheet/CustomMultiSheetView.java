package com.simplecity.amp_library.ui.views.multisheet; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.design.widget.BottomSheetBehavior; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager; // NOSONAR
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetSlideEventRelay.SlideEvent; // NOSONAR
import com.simplecity.multisheetview.ui.view.MultiSheetView; // NOSONAR
import io.reactivex.disposables.CompositeDisposable; // NOSONAR

/** // NOSONAR
 * A custom MultiSheetView with an RXRelay for responding to expand/collapse events. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CustomMultiSheetView extends MultiSheetView { //NOSONAR

    private static final String TAG = "CustomMultiSheetView"; //NOSONAR

    MultiSheetEventRelay multiSheetEventRelay; //NOSONAR

    MultiSheetSlideEventRelay multiSheetSlideEventRelay; //NOSONAR

    private CompositeDisposable disposables; //NOSONAR

    DrawerLockManager.DrawerLock sheet1Lock = () -> "Sheet 1"; //NOSONAR
    DrawerLockManager.DrawerLock sheet2Lock = () -> "Sheet 2"; //NOSONAR

    public CustomMultiSheetView(@NonNull Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR

        disposables = new CompositeDisposable(); //NOSONAR

        setSheetStateChangeListener(new SheetStateChangeListener() { //NOSONAR
            @Override //NOSONAR
            public void onSheetStateChanged(int sheet, int state) { //NOSONAR
                if (state == BottomSheetBehavior.STATE_COLLAPSED) { //NOSONAR
                    switch (sheet) { //NOSONAR
                        case Sheet.FIRST: //NOSONAR
                            DrawerLockManager.getInstance().removeDrawerLock(sheet1Lock); //NOSONAR
                            break; //NOSONAR
                        case Sheet.SECOND: //NOSONAR
                            DrawerLockManager.getInstance().removeDrawerLock(sheet2Lock); //NOSONAR
                            break; //NOSONAR
                    } // NOSONAR
                } else if (state == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
                    switch (sheet) { //NOSONAR
                        case Sheet.FIRST: //NOSONAR
                            DrawerLockManager.getInstance().addDrawerLock(sheet1Lock); //NOSONAR
                            break; //NOSONAR
                        case Sheet.SECOND: //NOSONAR
                            DrawerLockManager.getInstance().addDrawerLock(sheet2Lock); //NOSONAR
                            break; //NOSONAR
                    } // NOSONAR
                } // NOSONAR
                multiSheetSlideEventRelay.sendEvent(new SlideEvent(sheet, state)); //NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onSlide(int sheet, float slideOffset) { //NOSONAR
                multiSheetSlideEventRelay.sendEvent(new SlideEvent(sheet, slideOffset)); //NOSONAR
            } // NOSONAR
        }); // NOSONAR
    } // NOSONAR

    public void setMultiSheetEventRelay(MultiSheetEventRelay multiSheetEventRelay) { //NOSONAR
        this.multiSheetEventRelay = multiSheetEventRelay; //NOSONAR
    } // NOSONAR

    public void setMultiSheetSlideEventRelay(MultiSheetSlideEventRelay multiSheetSlideEventRelay) { //NOSONAR
        this.multiSheetSlideEventRelay = multiSheetSlideEventRelay; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        disposables.add(multiSheetEventRelay.getEvents().subscribe(event -> { //NOSONAR
            switch (event.action) { //NOSONAR
                case MultiSheetEventRelay.MultiSheetEvent.Action.GOTO: //NOSONAR
                    goToSheet(event.sheet); //NOSONAR
                    break; //NOSONAR
                case MultiSheetEventRelay.MultiSheetEvent.Action.HIDE: //NOSONAR
                    hide(false, true); //NOSONAR
                    break; //NOSONAR
                case MultiSheetEventRelay.MultiSheetEvent.Action.SHOW_IF_HIDDEN: //NOSONAR
                    unhide(true); //NOSONAR
                    break; //NOSONAR
            } // NOSONAR
        })); // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDetachedFromWindow() { //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR

        disposables.clear(); //NOSONAR
    } // NOSONAR
} // NOSONAR
