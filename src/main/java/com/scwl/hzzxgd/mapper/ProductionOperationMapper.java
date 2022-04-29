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
}
