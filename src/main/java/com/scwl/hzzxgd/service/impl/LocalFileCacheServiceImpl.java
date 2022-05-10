package com.scwl.hzzxgd.service.impl;

import cn.hutool.core.io.resource.InputStreamResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.LocalFileCacheEntity;
import com.scwl.hzzxgd.mapper.LocalFileCacheMapper;
import com.scwl.hzzxgd.service.LocalFileCacheService;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class LocalFileCacheServiceImpl extends ServiceImpl<LocalFileCacheMapper, LocalFileCacheEntity> implements LocalFileCacheService {
    /**
     * 保存文件到本地和数据库
     *
     * @param file
     * @return
     */
    @Override
    public void saveFileLocally(MultipartFile file, HttpServletResponse response) {
        String path = "G:\\uploadDir\\";
        String fileName = file.getOriginalFilename();
        String substring = fileName.substring(fileName.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String allName = path + uuid + substring;
        String newName = uuid + substring;
        try (FileOutputStream fos = new FileOutputStream(allName)) {
            fos.write(file.getBytes());
            LocalFileCacheEntity localFileCacheEntity = new LocalFileCacheEntity();
            localFileCacheEntity.setPath(allName);
            this.save(localFileCacheEntity);
            localFileCacheEntity.setFileName(newName);
            /* 返回本地文件 */
            download(allName,response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void download(String path,HttpServletResponse response) {
        try {
            // path是指想要下载的文件的路径
            File file = new File(path);
            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            System.out.println("ext = " + ext);
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition: inline", "filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
