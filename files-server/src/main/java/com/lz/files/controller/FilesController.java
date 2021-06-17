package com.lz.files.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lz.files.common.R;
import com.lz.files.service.IFilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private IFilesService filesService;

    @SentinelResource("test")
    @GetMapping("/test")
    public String test(){
        log.info("服务可用");
        return "success";
    }

    @PostMapping("/uploadToNginx")
    public R uploadFilesToNginx(@RequestParam("file") MultipartFile file){
        try {
            String name = file.getOriginalFilename();
            log.info("上传文件[{}]", name);
            filesService.uploadFilesToNginx(file.getBytes(), name);
            return R.ok();
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return R.error("上传文件失败");
        }
    }

}
