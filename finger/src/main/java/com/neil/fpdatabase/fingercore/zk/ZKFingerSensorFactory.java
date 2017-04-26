package com.neil.fpdatabase.fingercore.zk;

import com.neil.fpdatabase.controller.FingerPrintIdentityHandler;
import com.neil.fpdatabase.controller.FingerPrintRegisterHandler;
import com.neil.fpdatabase.fingercore.PersistentFingerPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by nhu on 4/17/2017.
 * component that accept the finger print
 */
@Component
public class ZKFingerSensorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKFingerSensorFactory.class);

    @Autowired
    private PersistentFingerPrint persistentFingerPrint;

    @Autowired
    private FingerPrintRegisterHandler fingerPrintRegisterHandler;
    @Autowired
    private FingerPrintIdentityHandler fingerPrintIdentityHandler;
    @Autowired
    private ZKFingerPrintCache printCache;

    @Value("${file.location}")
    private String fileLocation;

    private ZKFingerPrintSensor fingerPrintSensor = new ZKFingerPrintSensor();
    ;
    private ZKFingerPrintRegister printRegister;


    @PostConstruct
    public void startFingerAcceptor() {
        LOGGER.info("starting finger acceptor");
        instanceAll();
        fingerPrintSensor.initSensor();
        persistentFingerPrint.loadPersistedFingerPrint();

    }

    private void instanceAll() {
        instanceFingerPrintRegister();
        setupFingerSensor();
    }

    private void setupFingerSensor() {
        ZKFingerPrintCaptureListener listener = new ZKFingerPrintCaptureListener(fingerPrintRegisterHandler,
                fingerPrintIdentityHandler, printRegister, printCache, fingerPrintSensor.getFingerprintSensor());
        fingerPrintSensor.setFingerprintCaptureListener(listener);
    }

    private void instanceFingerPrintRegister() {
        this.printRegister = new ZKFingerPrintRegister(printCache, persistentFingerPrint, fileLocation);
    }

    public ZKFingerPrintSensor getFingerPrintSensor() {
        return fingerPrintSensor;
    }

    public ZKFingerPrintRegister getPrintRegister() {
        return printRegister;
    }
}
