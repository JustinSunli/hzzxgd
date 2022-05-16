package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.ProductionOperationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
/**
 * 工序明细
 */
public interface ProductionOperationMapper extends BaseMapper<ProductionOperationEntity> {
    /**
     * 根据生产工单主表id查询工序明细
     * @param id
     * @return
     */
    List<ProductionOperationEntity> selectByOrderId(@Param("id") String id);

    /**
     * 根据id查询工序
     * @param operationId
     * @return
     */
    List<ProductionOperationEntity> selectByOperationId(@Param("operationId") String operationId);

    /**
     * 报工单删除 回滚工序数据
     * @param operationId
     * @param feedbackQty
     * @param goodQty
     * @param badQty
     * @param rejectedQty
     */
    void operationRollBack(@Param("operationId") String operationId,@Param("feedbackQty") float feedbackQty,@Param("goodQty") float goodQty,@Param("badQty") float badQty,@Param("rejectedQty") float rejectedQty);
}
