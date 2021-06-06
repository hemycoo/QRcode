package com.hecode.qrcode.controller;

import com.google.zxing.WriterException;
import com.hecode.qrcode.utils.QRCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Slf4j
@Controller
public class DefaultController {

    /**
     * 二维码内的信息
     */
    private String info = null;

    @GetMapping("qrcode")
    public String qrcode() {
        return "/qrcode";
    }

    @GetMapping(value = "/qrimage")
    public ResponseEntity<byte[]> getQRImage() {
        log.info("当前生成二维码的内容为： " + info);
        if (null == info) {
            return null;
        }
        byte[] qrcode = null;
        try {
            qrcode = QRCodeGenerator.createQRCode(info, 360, 360);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {

            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        // Set headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]>(qrcode, headers, HttpStatus.CREATED);
    }

    @PostMapping("/process")
    public String submit(@RequestParam("msg") String msg) {
        info = msg;
        return "qrcode";
    }
}
