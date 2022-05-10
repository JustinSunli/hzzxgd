package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.service.LocalFileCacheService;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")
public class LocalFileCacheController {
    @Resource
    private LocalFileCacheService localFileCacheService;

    @PostMapping("/local")
    public void SaveFileLocally(@RequestBody MultipartFile file , HttpServletResponse response) {
        localFileCacheService.saveFileLocally(file,response);
    }
}
