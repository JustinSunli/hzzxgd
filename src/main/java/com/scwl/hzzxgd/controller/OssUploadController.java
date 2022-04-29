package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.exception.SandException;
import com.scwl.hzzxgd.oss.OssData;
import com.scwl.hzzxgd.service.OssUploadService;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  oss上传文件
 */
@RestController
@Scope("prototype")
@RequestMapping("/api/oss")
@CrossOrigin(origins ="*")
public class OssUploadController {

    private final OssUploadService ossUploadService;

    @Value("${oss.fileHost}")
    String fileHost;

    @Autowired
    public OssUploadController(OssUploadService ossUploadService) {
        this.ossUploadService = ossUploadService;
    }

    /**
     * 获取服务器的ip地址
     * @return
     */
    @GetMapping("/getHost")
    public SandResponse getHost() {
        OssData ossData = new OssData();
        ossData.setHost(fileHost);
        return SandResponse.ok().put("data",ossData);
    }

    /**
     * oss上传文件(单个文件上传)
     * @param file
     * @return
     */
    @PostMapping("/fileUpload")
    public SandResponse fileUpload(@RequestBody MultipartFile file) {
        OssData ossData = new OssData();
        // 上传文件返回url
        String url = ossUploadService.upload(file);
        if (url != null) {
            ossData.setHost(fileHost);
            ossData.setPath(fileHost + url);
            return SandResponse.ok().put("data",ossData);
        } else {
            throw new SandException("上传失败");
        }
    }

    /**
     * oss多文件上传
     * @param files
     * @return
     */
    @PostMapping("/filesUpload")
    public SandResponse filesUpload(@RequestBody List<MultipartFile> files) {
        OssData ossData = new OssData();
        Map<String, String> urls = new HashMap<>();
        for (MultipartFile file : files) {
            String url = ossUploadService.upload(file);
            if (url != null) {
                String fileName = file.getOriginalFilename();
                url = fileHost + url;
                urls.put(fileName, url);
            } else {
                ossData.setFileName(file.getOriginalFilename());
                ossData.setPath(fileHost + url);
                throw new SandException("上传失败");
            }
        }
        return SandResponse.ok().put("data",urls);
    }

    /**
     * oss 分俩个file,多文件上传
     * @param file01
     * @param file02
     * @return
     */
    @PostMapping("filesUploadMore")
    public SandResponse filesUploadMore(@RequestBody List<MultipartFile> file01, @RequestBody List<MultipartFile> file02) {
        OssData ossData = new OssData();
        Map<String, String> urls = new HashMap<>();
        for (MultipartFile file : file01) {
            try {
                String url = ossUploadService.upload(file);
                if (url != null) {
                    String fileName = file.getOriginalFilename();
                    url = fileHost + url;
                    urls.put(fileName, url);
                } else {
                    ossData.setFileName(file.getOriginalFilename());
                    throw new SandException("上传失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ossData.setFileName(file.getOriginalFilename());
                throw new SandException("上传失败");
            }
        }

        for (MultipartFile file : file02) {
            try {
                String url = ossUploadService.upload(file);
                if (url != null) {
                    String fileName = file.getOriginalFilename();
                    url = fileHost + url;
                    urls.put(fileName, url);
                } else {
                    ossData.setFileName(file.getOriginalFilename());
                    throw new SandException("上传失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ossData.setFileName(file.getOriginalFilename());
                throw new SandException("上传失败");
            }
        }
        return SandResponse.ok().put("data",urls);
    }
}

