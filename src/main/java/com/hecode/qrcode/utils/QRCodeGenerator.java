package com.hecode.qrcode.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

@Slf4j
public class QRCodeGenerator {

    public static byte[] createQRCode(String text, int width, int height) throws WriterException, IOException {
        MultiFormatWriter writer = new MultiFormatWriter();
        log.info("createQRCode: width=" + width + "  height=" + height);
        //使用hints解决中文乱码问题
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
}
