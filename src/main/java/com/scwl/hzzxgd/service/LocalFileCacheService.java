package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface LocalFileCacheService {
    /**
     * 保存文件到本地和数据库
     * @param file
     * @return
     */
    void saveFileLocally(MultipartFile file, HttpServletResponse response);
}
