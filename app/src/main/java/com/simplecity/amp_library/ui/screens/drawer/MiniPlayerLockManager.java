package com.simplecity.amp_library.ui.screens.drawer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class MiniPlayerLockManager {

    public interface MiniPlayerLock {
        String getTag();
    }

    private static MiniPlayerLockManager instance;

    private List<MiniPlayerLock> miniPlayerLocks = new ArrayList<>();

    private MiniPlayerLockManager() {
        // Intentionally left empty.
    }

    public boolean canShowMiniPlayer() {
        return miniPlayerLocks.isEmpty();
    }

    public static MiniPlayerLockManager getInstance() {
        if (instance == null) {
            instance = new MiniPlayerLockManager();
        }
        return instance;
    }

    public void addMiniPlayerLock(MiniPlayerLock miniPlayerLock) {
        if (!miniPlayerLocks.contains(miniPlayerLock)) {
            miniPlayerLocks.add(miniPlayerLock);
        }
    }

    public void removeMiniPlayerLock(MiniPlayerLock miniPlayerLock) {
        if (miniPlayerLocks.contains(miniPlayerLock)) {
            miniPlayerLocks.remove(miniPlayerLock);
        }
    }
}
