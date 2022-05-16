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
@TableName("work_report_order")
/**
 * 报工
 */
public class WorkReportOrderEntity implements Serializable {
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
     * 报工流水号
     */
    private String seqNo;
    /**
     * 关联工序id
     */
    private String operationId;
    /**
     * 报工数量
     */
    private float feedbackQty;
    /**
     * 操作员id
     */
    private String operatorId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 计价工资
     */
    private float wage;
    /**
     * 标准工时
     */
    private float standardHours;
    /**
     * 实际工时
     */
    private float actHours;
    /**
     * 良品数量
     */
    private float goodQty;
    /**
     * 不良数量
     */
    private float badQty;
    /**
     * 报废数量
     */
    private float rejectedQty;
    /**
     * 计薪方式
     */
    private int salaryMethod;
    /**
     * 工资合计
     */
    private float salaryAmount;
    /**
     * 是否已检验
     */
    private Boolean isChecked;
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
     * 操作员名称
     */
    @TableField(exist = false)
    private String operatorName;
    /**
     * 工单工序
     */
    @TableField(exist = false)
    private List<ProductionOperationEntity> operation;
    /**
     * 工单
     */
    @TableField(exist = false)
    private ProductionOrderEntity product;
}
