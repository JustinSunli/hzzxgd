package com.scwl.hzzxgd.oss;

import lombok.Data;

/**
 * OssData 封装类
 */
@Data
public class OssData {
    /**
     * 服务器域名
     */
    private String host;
    /**
     * 上传文件路径
     */
    private String path;
    /**
     * 上传文件名
     */
    private String fileName;
}
