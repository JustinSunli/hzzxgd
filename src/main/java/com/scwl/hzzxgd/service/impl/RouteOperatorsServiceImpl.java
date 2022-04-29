package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.RouteOperatorsEntity;
import com.scwl.hzzxgd.mapper.RouteOperatorsMapper;
import com.scwl.hzzxgd.service.RouteOperatorsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工艺路线操作员
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RouteOperatorsServiceImpl extends ServiceImpl<RouteOperatorsMapper, RouteOperatorsEntity> implements RouteOperatorsService {
}
