package com.simplecity.amp_library.ui.screens.drawer; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class DrawerLockManager { //NOSONAR

    public interface DrawerLock { //NOSONAR
        String getTag(); //NOSONAR
    } // NOSONAR

    private static DrawerLockManager instance; //NOSONAR

    private List<DrawerLock> drawerLocks = new ArrayList<>(); //NOSONAR

    @Nullable //NOSONAR
    private DrawerLockController drawerLockController; //NOSONAR

    private DrawerLockManager() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    public static DrawerLockManager getInstance() { //NOSONAR
        if (instance == null) { //NOSONAR
            instance = new DrawerLockManager(); //NOSONAR
        } // NOSONAR
        return instance; //NOSONAR
    } // NOSONAR

    public void setDrawerLockController(@Nullable DrawerLockController drawerLockController) { //NOSONAR
        this.drawerLockController = drawerLockController; //NOSONAR
    } // NOSONAR

    public void addDrawerLock(DrawerLock drawerLock) { //NOSONAR
        if (!drawerLocks.contains(drawerLock)) { //NOSONAR
            drawerLocks.add(drawerLock); //NOSONAR
        } // NOSONAR
        if (drawerLockController != null) { //NOSONAR
            drawerLockController.lockDrawer(); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void removeDrawerLock(DrawerLock drawerLock) { //NOSONAR
        if (drawerLocks.contains(drawerLock)) { //NOSONAR
            drawerLocks.remove(drawerLock); //NOSONAR
        } // NOSONAR
        if (drawerLocks.isEmpty()) { //NOSONAR
            if (drawerLockController != null) { //NOSONAR
                drawerLockController.unlockDrawer(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
