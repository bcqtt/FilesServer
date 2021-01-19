package com.lz.files.service.impl;

import com.lz.files.common.Constant;
import com.lz.files.common.DeviceEnv;
import com.lz.files.common.ResponseDto;
import com.lz.files.service.IFilesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

@Slf4j
@Service
public class FilesServiceImpl implements IFilesService {

    @Value("${nginx.file.relative-path}")
    private String relativePath = "";

    @Value("${nginx.file.directory}")
    private String directory = "";

    @Override
    public ResponseDto uploadFilesToNginx(byte[] bytes, String fileName) {
        ResponseDto response = new ResponseDto(Constant.SUCCESS_CODE, null, Constant.SUCCESS_MESSAGE);

        String nginxHost = "http://" + DeviceEnv.getLocalIp() + ":81/";
        try {
            String filePath = directory + fileName;;
            String uri = nginxHost + relativePath + fileName;

            log.info("文件上传到服务器绝对地址[{}],访问URI:{}", filePath, uri);

            //目标目录
            File targetDir = new File(directory);
            if(!targetDir.exists()) {
                targetDir.mkdirs();
            }

            File uploadFile = new File(filePath);
            InputStream in = new ByteArrayInputStream(bytes);
            FileUtils.copyInputStreamToFile(in, uploadFile);

            response.setData(uri);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            response = new ResponseDto(Constant.FAIL_CODE, null, "上传文件失败");
        }
        return response;
    }
}
