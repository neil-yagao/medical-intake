package com.neil.fpdatabase.fingercore;

import com.neil.fpdatabase.fingercore.fingerprint.CachedFingerPrint;

/**
 * Created by nhu on 4/20/2017.
 */
public interface FingerPrintCache {

    int cacheFingerPrint(CachedFingerPrint cachedFingerPrint);

    CachedFingerPrint getFingerPrint(int index);

    void deleteRegisteredFingerPrint(String identityCode);

}
