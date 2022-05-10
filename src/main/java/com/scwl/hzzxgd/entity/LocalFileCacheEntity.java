package com.scwl.hzzxgd.entity;

import cn.hutool.core.io.resource.InputStreamResource;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("local_file_cache")
/**
 * 本地文件缓存
 */
public class LocalFileCacheEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 本地文件路径
     */
    private String path;
    /**
     * 下载路径
     */
    @TableField(exist = false)
    private ResponseEntity<InputStreamResource> response;
    /**
     *
     */
    @TableField(exist = false)
    private String fileName;
}
