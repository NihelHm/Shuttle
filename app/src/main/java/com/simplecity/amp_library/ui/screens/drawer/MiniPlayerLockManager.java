package com.simplecity.amp_library.ui.screens.drawer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MiniPlayerLockManager { //NOSONAR

    public interface MiniPlayerLock { //NOSONAR
        String getTag(); //NOSONAR
    }

    private static MiniPlayerLockManager instance; //NOSONAR

    private List<MiniPlayerLock> miniPlayerLocks = new ArrayList<>(); //NOSONAR

    private MiniPlayerLockManager() { //NOSONAR
        // Intentionally left empty.
    }

    public boolean canShowMiniPlayer() { //NOSONAR
        return miniPlayerLocks.isEmpty(); //NOSONAR
    }

    public static MiniPlayerLockManager getInstance() { //NOSONAR
        if (instance == null) { //NOSONAR
            instance = new MiniPlayerLockManager(); //NOSONAR
        }
        return instance; //NOSONAR
    }

    public void addMiniPlayerLock(MiniPlayerLock miniPlayerLock) { //NOSONAR
        if (!miniPlayerLocks.contains(miniPlayerLock)) { //NOSONAR
            miniPlayerLocks.add(miniPlayerLock); //NOSONAR
        }
    }

    public void removeMiniPlayerLock(MiniPlayerLock miniPlayerLock) { //NOSONAR
        if (miniPlayerLocks.contains(miniPlayerLock)) { //NOSONAR
            miniPlayerLocks.remove(miniPlayerLock); //NOSONAR
        }
    }
}
