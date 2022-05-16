package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProductionOperationEntity;
import com.scwl.hzzxgd.entity.ProductionOrderEntity;

import java.util.List;

/**
 * 工序明细
 */
public interface ProductionOperationService {

    /**
     * 根据生产工单id获取工序列表
     * @param id
     * @return
     */
    List<ProductionOperationEntity> getOperationInfo(String id);
}
