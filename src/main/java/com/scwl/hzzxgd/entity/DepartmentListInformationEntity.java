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
@TableName("department_list_information")
/**
 * 部门列表信息
 */
public class DepartmentListInformationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 创建的部门id
     */
    private Integer departmentListId;
    /**
     * corpid
     */
    private String corpid;
    /**
     * 父部门id。根部门为1
     */
    private Integer parentid;
    /**
     * 部门名称
     */
    private String displayName;
    /**
     * 部门负责人的UserID；第三方仅通讯录应用可获取
     */
    private String departmentLeader;
    /**
     * 在父部门中的次序值。order值大的排序靠前。值范围是[0, 2^32)
     */
    private Integer departmentOrder;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否启用
     */
    private String isActive;

    /**
     * children
     */
    @TableField(exist = false)
    private List<DepartmentListInformationEntity> children;
}
