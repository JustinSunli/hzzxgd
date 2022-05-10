package com.scwl.hzzxgd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName("initialization_information")
public class InitializationInformationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 父id 没有为0
     */
    private String pId;
    /**
     * url
     */
    private String menuUrl;
    /**
     * url
     */
    private String menuName;
    /**
     * 格式
     */
    private String icon;
    /**
     *
     */
    private Boolean isSingle;
    /**
     * 角色 1管理员 2普通
     */
    private String role;
    /**
     * 是否可用 '1'可用 '0'不可用
     */
    private String isActive;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * children
     */
    @TableField(exist = false)
    private List<InitializationInformationEntity> children;
}
