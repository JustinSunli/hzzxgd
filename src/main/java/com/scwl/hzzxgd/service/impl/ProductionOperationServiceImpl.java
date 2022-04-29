package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.ProductionOperationEntity;
import com.scwl.hzzxgd.entity.ProductionOrderEntity;
import com.scwl.hzzxgd.mapper.ProductionOperationMapper;
import com.scwl.hzzxgd.service.ProductionOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 工序明细
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductionOperationServiceImpl extends ServiceImpl<ProductionOperationMapper, ProductionOperationEntity> implements ProductionOperationService {

}
