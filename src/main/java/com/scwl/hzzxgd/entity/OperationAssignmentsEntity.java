package com.scwl.hzzxgd.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
@Accessors(chain = true)
@TableName("operation_assignments")
/**
 * 派工明细
 */
public class OperationAssignmentsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * operation_id
     */
    private String operationId;
    /**
     * operator_id
     */
    private String operatorId;
    /**
     * corpid
     */
    private String corpid;
    /**
     * 分派数量
     */
    private Double allocateQty;
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
}
