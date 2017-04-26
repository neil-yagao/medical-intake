package com.neil.fpdatabase.fingercore;

/**
 * Created by nhu on 4/20/2017.
 * The interface define for finger print senor
 */
public interface FingerPrintSensor {

    void initSensor();

    int IdentifyFP(Object... params);
}
