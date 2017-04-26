package com.neil.fpdatabase.fingercore.zk;

import com.neil.fpdatabase.controller.FingerPrintIdentityHandler;
import com.neil.fpdatabase.controller.FingerPrintRegisterHandler;
import com.zkteco.biometric.FingerprintCaptureListener;
import com.zkteco.biometric.FingerprintSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nhu on 4/17/2017.
 * Listener that invoke caching and sending img
 */
public class ZKFingerPrintCaptureListener implements FingerprintCaptureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKFingerPrintCaptureListener.class);

    private FingerPrintRegisterHandler fingerPrintRegisterHandler;

    private FingerPrintIdentityHandler fingerPrintIdentityHandler;

    private ZKFingerPrintRegister printRegister;

    private ZKFingerPrintCache printCache;

    private FingerprintSensor printSensor;

    public ZKFingerPrintCaptureListener(FingerPrintRegisterHandler fingerPrintRegisterHandler,
                                        FingerPrintIdentityHandler fingerPrintIdentityHandler,
                                        ZKFingerPrintRegister printRegister,
                                        ZKFingerPrintCache printCache,
                                        FingerprintSensor printSensor) {
        this.fingerPrintRegisterHandler = fingerPrintRegisterHandler;
        this.fingerPrintIdentityHandler = fingerPrintIdentityHandler;
        this.printRegister = printRegister;
        this.printCache = printCache;
        this.printSensor = printSensor;
    }

    @Override
    public void captureOK(byte[] bytes) {
        LOGGER.info("capturing data.");
        try {
            printRegister.setPicImg(bytes, printSensor.getImageWidth(), printSensor.getImageHeight());
        } catch (Exception e) {
            LOGGER.error("error during sending capture:", e);
        }

    }

    @Override
    public void captureError(int i) {
//        /LOGGER.error("error during capture:", i);
    }

    @Override
    public void extractOK(byte[] bytes) {
        LOGGER.info("extracting data.");
        try {
            int[] fid = new int[1];
            int[] score = new int[1];
            int ret = printSensor.IdentifyFP(bytes, fid, score);
            int index = fid[0];
            if (index > 0 && ret == 0) {
                //find matching
                fingerPrintIdentityHandler.sendIdentity(printCache.getFingerPrint(index));
            } else {
                //not found, registering
                printRegister.registerFingerPrint(bytes);
                String file = printRegister.getCurrentFile();
                fingerPrintRegisterHandler.sendImage("img/" + file);
            }
        } catch (Exception o) {
            LOGGER.error("unable to register due to ", o);
        }

    }
}
