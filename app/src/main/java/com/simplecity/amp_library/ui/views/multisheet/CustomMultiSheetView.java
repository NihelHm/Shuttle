package com.simplecity.amp_library.ui.views.multisheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager;
import com.simplecity.amp_library.ui.views.multisheet.MultiSheetSlideEventRelay.SlideEvent;
import com.simplecity.multisheetview.ui.view.MultiSheetView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * A custom MultiSheetView with an RXRelay for responding to expand/collapse events.
 */
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
                    }
                } else if (state == BottomSheetBehavior.STATE_EXPANDED) { //NOSONAR
                    switch (sheet) { //NOSONAR
                        case Sheet.FIRST: //NOSONAR
                            DrawerLockManager.getInstance().addDrawerLock(sheet1Lock); //NOSONAR
                            break; //NOSONAR
                        case Sheet.SECOND: //NOSONAR
                            DrawerLockManager.getInstance().addDrawerLock(sheet2Lock); //NOSONAR
                            break; //NOSONAR
                    }
                }
                multiSheetSlideEventRelay.sendEvent(new SlideEvent(sheet, state)); //NOSONAR
            }

            @Override //NOSONAR
            public void onSlide(int sheet, float slideOffset) { //NOSONAR
                multiSheetSlideEventRelay.sendEvent(new SlideEvent(sheet, slideOffset)); //NOSONAR
            }
        });
    }

    public void setMultiSheetEventRelay(MultiSheetEventRelay multiSheetEventRelay) { //NOSONAR
        this.multiSheetEventRelay = multiSheetEventRelay; //NOSONAR
    }

    public void setMultiSheetSlideEventRelay(MultiSheetSlideEventRelay multiSheetSlideEventRelay) { //NOSONAR
        this.multiSheetSlideEventRelay = multiSheetSlideEventRelay; //NOSONAR
    }

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
            }
        }));
    }

    @Override //NOSONAR
    public void onDetachedFromWindow() { //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR

        disposables.clear(); //NOSONAR
    }
}
