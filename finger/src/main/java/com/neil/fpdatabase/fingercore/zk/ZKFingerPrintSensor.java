package com.neil.fpdatabase.fingercore.zk;

import com.neil.fpdatabase.fingercore.FingerPrintSensor;
import com.zkteco.biometric.FingerprintCaptureListener;
import com.zkteco.biometric.FingerprintSensor;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nhu on 4/20/2017.
 * this is the zk finger print senor implementation
 */
public class ZKFingerPrintSensor implements FingerPrintSensor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKFingerPrintSensor.class);

    private FingerprintSensor fingerprintSensor = new FingerprintSensor();
    private FingerprintCaptureListener fingerprintCaptureListener;

    public void setFingerprintCaptureListener(FingerprintCaptureListener fingerprintCaptureListener) {
        this.fingerprintCaptureListener = fingerprintCaptureListener;
    }

    public FingerprintSensor getFingerprintSensor() {
        return fingerprintSensor;
    }

    @Override
    public void initSensor() {
        int ret = fingerprintSensor.getDeviceCount();
        if (ret < 0) {
            LOGGER.error("no device detected!");
            return;
        }
        if (FingerprintSensorErrorCode.ERROR_SUCCESS != (ret = fingerprintSensor.openDevice(0))) {
            LOGGER.error("device open failed!");
        }
        fingerprintSensor.setFingerprintCaptureListener(fingerprintCaptureListener);
        if (!fingerprintSensor.startCapture()) {
            freeSensor();
        }
    }

    @Override
    public int IdentifyFP(Object... params) {
        byte[] content = (byte[]) params[0];
        int[] fid = new int[1];
        int[] score = new int[1];
        int ret = fingerprintSensor.IdentifyFP(content, fid, score);
        if (ret == 0) {
            return fid[0];
        }
        return -1;
    }

    private void freeSensor() {
        LOGGER.info("freeing finger acceptor");
        if (null != fingerprintSensor) {
            fingerprintSensor.stopCapture();
            fingerprintSensor.closeDevice();
            fingerprintSensor.destroy();
            fingerprintSensor = null;
        }
    }
}
