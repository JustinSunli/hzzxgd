package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.ProductionOperationEntity;
import com.scwl.hzzxgd.entity.ProductionOrderEntity;
import com.scwl.hzzxgd.mapper.ProductionOperationMapper;
import com.scwl.hzzxgd.service.ProductionOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 工序明细
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductionOperationServiceImpl extends ServiceImpl<ProductionOperationMapper, ProductionOperationEntity> implements ProductionOperationService {
    @Resource
    private ProductionOperationMapper productionOperationMapper;

    /**
     * 根据生产工单id获取工序列表
     * @param id
     * @return
     */
    @Override
    public List<ProductionOperationEntity> getOperationInfo(String id) {
        List<ProductionOperationEntity> productionOperationEntities = productionOperationMapper.selectByOrderId(id);
        return productionOperationEntities;
    }
}
