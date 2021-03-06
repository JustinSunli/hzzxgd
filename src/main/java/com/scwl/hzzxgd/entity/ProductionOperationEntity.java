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
    private float dispatchQty;
    /**
     * 工序顺序
     */
    private int sequenceNo;
    /**
     * 工序中心id
     */
    private String workCenterId;
    /**
     * 制造方
     */
    private int maker;
    /**
     * 计薪方式
     */
    private int salaryMethod;
    /**
     * 工资
     */
    private float wage;
    /**
     * 标准工时
     */
    private float standardHours;
    /**
     * 备注
     */
    private String remark;
    /**
     * 损耗率
     */
    private float lossRate;
    /**
     * 工单状态(0待开工 1进行中 2已取消 3手工关闭 4 已完工)
     */
    private String operationStatus;
    /**
     * 该工序要求完成数量,默认=工单dispatch_qty =(1+lossrate)*qty
     */
    private float requireQty;
    /**
     * 良品数量
     */
    private float goodQty;
    /**
     * 报工数量
     */
    private float feedbackQty;
    /**
     * 不良数量
     */
    private float badQty;
    /**
     * 报废数量
     */
    private float rejectedQty;
    /**
     * 工序库存数量
     */
    private float tempStockQty;
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