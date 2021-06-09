package com.lz.files.controller;

import com.itextpdf.text.DocumentException;
import com.lz.files.pdf.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/sign")
public class SignController {


    @Autowired
    ISignService signService;

    @GetMapping("/mytest")
    public String sign(){

        log.info("开始合成。。。");
        long start = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            signService.executeAsyncSign(i+1);
        }
        long end = System.currentTimeMillis();
        log.info("成功合成。耗时：" + (end - start) + "ms");

        return "success";
    }

    @GetMapping("/mytest2")
    public String sign2() throws IOException, DocumentException {

        log.info("开始合成。。。");
        long start = System.currentTimeMillis();
        for(int i=0;i<30;i++){
            signService.executeAsyncSign2(i);
        }
        long end = System.currentTimeMillis();
        log.info("成功合成。耗时：" + (end - start) + "ms");

        return "success";
    }

}
