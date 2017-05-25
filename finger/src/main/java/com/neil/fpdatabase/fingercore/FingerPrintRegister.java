package com.neil.fpdatabase.fingercore;

import java.io.IOException;

/**
 * Created by nhu on 4/20/2017.
 */
public interface FingerPrintRegister {

    int registerFingerPrint(byte[] fingerPrint) throws IOException;

    void setIdentity(String code, String identity);

    void resetRegisterInfo();

    void doRegister() throws IOException;
}
