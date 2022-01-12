package com.hecode.qrcode.controller;

import com.google.zxing.WriterException;
import com.hecode.qrcode.utils.QRCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
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

    @Value("${qr.picture.length:360}")
    private int QRPictureLength;


    @GetMapping("qrcode")
    public String qrcode() {
        return "contentInput";
    }

    @GetMapping(value = "/qrimage")
    public ResponseEntity<byte[]> getQRImage() {
        if (StringUtils.isBlank(info)) {
            log.info("当前生成二维码的内容为空 ");
            return null;
        }
        log.info("当前生成二维码的内容为： " + info);
        byte[] qrcode = null;
        try {
            qrcode = QRCodeGenerator.createQRCode(info, QRPictureLength, QRPictureLength);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        // Set headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(qrcode, headers, HttpStatus.CREATED);
    }

    @PostMapping("/generate")
    public String submit(@RequestParam("msg") String msg, Model model) {
        if (StringUtils.isBlank(msg)) {
            model.addAttribute("result", "输入内容为空，请重新输入");
            return "contentInput";
        }
        info = msg;
        return "qrcodeGenerate";
    }
}
