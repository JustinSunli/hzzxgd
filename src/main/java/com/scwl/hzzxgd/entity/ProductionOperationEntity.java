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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工序明细
 */
@Data
@Accessors(chain = true)
@TableName("production_operation")
public class ProductionOperationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * corpid
     */
    private String corpid;
    /**
     * 关联工单id
     */
    private String productionOrderId;
    /**
     * 派工数量(默认 = 工单的派工数量)
     */
    private Double dispatchQty;
    /**
     * 工序顺序
     */
    private Integer sequenceNo;
    /**
     * 工序中心id
     */
    private String workCenterId;
    /**
     * 制造方
     */
    private Integer maker;
    /**
     * 计薪方式
     */
    private Integer salaryMethod;
    /**
     * 工资
     */
    private BigDecimal wage;
    /**
     * 标准工时
     */
    private Double standardHours;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人
     */
    private String createId;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 最后修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modificationTime;
    /**
     * 修改人
     */
    private String modificationId;
    /**
     * 是否启用
     */
    private String isActive;
    /**
     * 派工明细
     */
    @TableField(exist = false)
    private List<OperationAssignmentsEntity> assignments;
    /**
     * 工序中心名称
     */
    @TableField(exist = false)
    private String workCenterName;
}