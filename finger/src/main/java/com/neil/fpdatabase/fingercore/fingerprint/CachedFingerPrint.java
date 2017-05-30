package com.neil.fpdatabase.fingercore.fingerprint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhu on 4/17/2017.
 * prison finger print which will be stored in memory
 */
public class CachedFingerPrint {

    private List<ScannedBytes> scannedBytes = new ArrayList<>();

    private ScannedBytes activeScannedByte = new ScannedBytes();

    private int activeIndex = 0;

    private String prisonCode = "";
    private String identity = "";

    private byte[] combinedTemplate;

    private String headPic = "";

    public Boolean isReady() {
        return scannedBytes.size() >= 3;
    }

    public String getIdentityCode() {
        return prisonCode;
    }

    public void setPrisonCode(String prisonCode) {
        this.prisonCode = prisonCode;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public boolean isIdentityValidate() {
        return this.identity.equals("police") || this.identity.equals("prison");
    }

    public ScannedBytes getImg(int index) {
        return scannedBytes.get(index);
    }

    public void doneCurrentRegister() {
        scannedBytes.add(activeScannedByte);
        activeScannedByte = new ScannedBytes();
        activeIndex++;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public byte[] getCurrentReg() {
        return activeScannedByte.getRegBytes();
    }

    public byte[] getCurrentImg() {
        return activeScannedByte.getImgBytes();
    }

    public void setCurrentImg(byte[] bytes) {
        activeScannedByte.setImgBytes(bytes);
    }

    public boolean hasRegisterIdentity() {
        return this.getIdentity().length() > 0 &&
                this.isIdentityValidate() &&
                this.getIdentityCode().length() > 0;
    }

    public byte[] getCombinedTemplate() {
        return combinedTemplate;
    }

    public void setCombinedTemplate(byte[] combinedTemplate) {
        this.combinedTemplate = combinedTemplate;
    }


    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }
}
