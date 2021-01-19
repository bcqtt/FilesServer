package com.lz.files.service;

import com.lz.files.common.ResponseDto;

public interface IFilesService {

    ResponseDto uploadFilesToNginx(byte[] bytes, String fileName);

}
