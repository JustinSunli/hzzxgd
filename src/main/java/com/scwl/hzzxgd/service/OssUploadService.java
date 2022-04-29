package com.scwl.hzzxgd.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssUploadService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
