package com.neil.fpdatabase.fingercore.fingerprint;

/**
 * Created by nhu on 4/20/2017.
 */

public class ScannedBytes {

    private byte[] imgBytes;
    private byte[] regBytes = new byte[2048];

    public byte[] getImgBytes() {
        return imgBytes;
    }

    public void setImgBytes(byte[] imgBytes) {
        this.imgBytes = imgBytes;
    }

    public byte[] getRegBytes() {
        return regBytes;
    }

    public void setRegBytes(byte[] regBytes) {
        this.regBytes = regBytes;
    }
}
