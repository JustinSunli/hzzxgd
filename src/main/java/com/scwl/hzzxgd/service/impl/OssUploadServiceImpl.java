package com.scwl.hzzxgd.service.impl;


import com.scwl.hzzxgd.oss.AliyunOSSUtil;
import com.scwl.hzzxgd.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ouj
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class OssUploadServiceImpl implements OssUploadService {

    private  final AliyunOSSUtil aliyunOSSUtil;

    @Autowired
    public OssUploadServiceImpl(AliyunOSSUtil aliyunOSSUtil){
        this.aliyunOSSUtil = aliyunOSSUtil;
    }

    @Value("${oss.filePath}")
    private String filePath;

    @Override
    @Transactional
    public String upload(MultipartFile file) {
        // 返回客户端文件系统中的原始文件名
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);

        try{
            if (file != null) {
                // 判定文件名是否为 ""
                if (!"".equals(fileName.trim())) {
                    File newFile = new File(fileName);
                    FileOutputStream os = new FileOutputStream(newFile);
                    // 以字节数组的形式返回文件的内容,再输出到文件输出流中
                    os.write(file.getBytes());
                    os.close();
                    // 将接受的文件传输到给定的目标文件 file-->newFile
                    file.transferTo(newFile);
                    // 根据不同文件 类型/日期 生成不同的文件夹
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    String datePath = format.format(date);
                    String timeStamp = String.valueOf(System.currentTimeMillis());
                    fileName = timeStamp + fileName.substring(fileName.lastIndexOf("."));
                    String path;
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
                        // images
                        path = filePath + "images/" + datePath + "/" + fileName;
                    } else {
                        path = filePath + "other/" + datePath + "/" + fileName;
                    }
                    // 上传到OSS
                    String uploadUrl = aliyunOSSUtil.upLoad(newFile, path);
                    newFile.delete();
                    if (uploadUrl != null) {
                        return uploadUrl;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

