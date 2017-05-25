package com.neil.fpdatabase.fingercore.zk;

import com.neil.fpdatabase.fingercore.FingerPrintRegister;
import com.neil.fpdatabase.fingercore.PersistentFingerPrint;
import com.neil.fpdatabase.fingercore.fingerprint.CachedFingerPrint;
import com.zkteco.biometric.ZKFPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nhu on 4/20/2017.
 */
public class ZKFingerPrintRegister implements FingerPrintRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKFingerPrintRegister.class);

    private CachedFingerPrint currentRegistering = new CachedFingerPrint();

    private ZKFingerPrintCache fingerPrintCache;

    private PersistentFingerPrint persistentFingerPrint;

    private String fileLocation;

    private String currentFile;

    public ZKFingerPrintRegister(ZKFingerPrintCache fingerPrintCache, PersistentFingerPrint persistentFingerPrint, String fileLocation) {
        this.fingerPrintCache = fingerPrintCache;
        this.persistentFingerPrint = persistentFingerPrint;
        this.fileLocation = fileLocation;
    }

    @Override
    public int registerFingerPrint(byte[] fingerPrint) throws IOException {
        setReg(fingerPrint);
        int index = currentRegistering.getActiveIndex();
        currentRegistering.doneCurrentRegister();
/*        if (currentRegistering.hasRegisterIdentity() && currentRegistering.isReady()) {
            doRegister();
        }*/
        return index;
    }

    public void setIdentity(String code, String identity) {
        this.currentRegistering.setIdentity(identity);
        this.currentRegistering.setPrisonCode(code);
    }


    private String writeBitmap(byte[] imageBuf, int nWidth, int nHeight) throws IOException {
        String fileName;
        String exactName = "finger" + currentRegistering.getActiveIndex() + ".png";
        fileName = fileLocation + "/img/" + exactName;
        FileOutputStream fos = new FileOutputStream(fileName);
        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

        int bfType = 0x424d; // 位图文件类型（0—1字节）
        int bfSize = 54 + 1024 + nWidth * nHeight;// bmp文件的大小（2—5字节）
        int bfReserved1 = 0;// 位图文件保留字，必须为0（6-7字节）
        int bfReserved2 = 0;// 位图文件保留字，必须为0（8-9字节）
        int bfOffBits = 54 + 1024;// 文件头开始到位图实际数据之间的字节的偏移量（10-13字节）

        dos.writeShort(bfType); // 输入位图文件类型'BM'
        dos.write(changeByte(bfSize), 0, 4); // 输入位图文件大小
        dos.write(changeByte(bfReserved1), 0, 2);// 输入位图文件保留字
        dos.write(changeByte(bfReserved2), 0, 2);// 输入位图文件保留字
        dos.write(changeByte(bfOffBits), 0, 4);// 输入位图文件偏移量

        int biSize = 40;// 信息头所需的字节数（14-17字节）
        int biPlanes = 1; // 目标设备的级别，必须是1（26-27字节）
        int biBitcount = 8;// 每个像素所需的位数（28-29字节），必须是1位（双色）、4位（16色）、8位（256色）或者24位（真彩色）之一。
        int biCompression = 0;// 位图压缩类型，必须是0（不压缩）（30-33字节）、1（BI_RLEB压缩类型）或2（BI_RLE4压缩类型）之一。
        int biSizeImage = nWidth * nHeight;// 实际位图图像的大小，即整个实际绘制的图像大小（34-37字节）
        int biXPelsPerMeter = 0;// 位图水平分辨率，每米像素数（38-41字节）这个数是系统默认值
        int biYPelsPerMeter = 0;// 位图垂直分辨率，每米像素数（42-45字节）这个数是系统默认值
        int biClrUsed = 0;// 位图实际使用的颜色表中的颜色数（46-49字节），如果为0的话，说明全部使用了
        int biClrImportant = 0;// 位图显示过程中重要的颜色数(50-53字节)，如果为0的话，说明全部重要

        dos.write(changeByte(biSize), 0, 4);// 输入信息头数据的总字节数
        dos.write(changeByte(nWidth), 0, 4);// 输入位图的宽
        dos.write(changeByte(nHeight), 0, 4);// 输入位图的高
        dos.write(changeByte(biPlanes), 0, 2);// 输入位图的目标设备级别
        dos.write(changeByte(biBitcount), 0, 2);// 输入每个像素占据的字节数
        dos.write(changeByte(biCompression), 0, 4);// 输入位图的压缩类型
        dos.write(changeByte(biSizeImage), 0, 4);// 输入位图的实际大小
        dos.write(changeByte(biXPelsPerMeter), 0, 4);// 输入位图的水平分辨率
        dos.write(changeByte(biYPelsPerMeter), 0, 4);// 输入位图的垂直分辨率
        dos.write(changeByte(biClrUsed), 0, 4);// 输入位图使用的总颜色数
        dos.write(changeByte(biClrImportant), 0, 4);// 输入位图使用过程中重要的颜色数

        for (int i = 0; i < 256; i++) {
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(0);
        }

        for (int i = 0; i < nHeight; i++)
            dos.write(imageBuf, (nHeight - 1 - i) * nWidth, nWidth);
        dos.flush();
        dos.close();
        fos.close();
        return exactName;
    }

    private byte[] changeByte(int data) {
        byte b4 = (byte) ((data) >> 24);
        byte b3 = (byte) (((data) << 8) >> 24);
        byte b2 = (byte) (((data) << 16) >> 24);
        byte b1 = (byte) (((data) << 24) >> 24);
        return new byte[]{b1, b2, b3, b4};
    }

    public void doRegister() throws IOException {
        int[] _retLen = new int[1];
        _retLen[0] = 2048;
        int ret;
        byte[] regTemp = new byte[_retLen[0]];

        ret = ZKFPService.GenRegFPTemplate(currentRegistering.getImg(0).getRegBytes(),
                currentRegistering.getImg(1).getRegBytes(),
                currentRegistering.getImg(2).getRegBytes(),
                regTemp, _retLen);
        currentRegistering.setCombinedTemplate(regTemp);
        if (ret == 0) {
            fingerPrintCache.cacheFingerPrint(currentRegistering);
            persistentFingerPrint.persistentFingerPrint(currentRegistering);
            resetRegisterInfo();
        }
    }

    public void setPicImg(byte[] bitmap, int width, int height) throws IOException {
        this.currentFile = writeBitmap(bitmap, width, height);
    }

    public void setReg(byte[] reg) {
        System.arraycopy(reg, 0, currentRegistering.getCurrentReg(), 0, 2048);
    }

    public void resetRegisterInfo() {
        currentRegistering = new CachedFingerPrint();
    }


    public String getCurrentFile() {
        return currentFile;
    }
}
