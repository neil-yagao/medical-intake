package com.neil.fpdatabase.fingercore;

import com.neil.fpdatabase.fingercore.fingerprint.CachedFingerPrint;

/**
 * Created by nhu on 4/20/2017.
 */
public interface PersistentFingerPrint {

    void persistentFingerPrint(CachedFingerPrint fingerPrint);

    void loadPersistedFingerPrint();
}
