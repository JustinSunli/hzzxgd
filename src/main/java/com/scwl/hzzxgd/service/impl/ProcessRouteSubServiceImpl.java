package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.ProcessRouteSubEntity;
import com.scwl.hzzxgd.mapper.ProcessRouteSubMapper;
import com.scwl.hzzxgd.service.ProcessRouteSubService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工艺路线子表
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessRouteSubServiceImpl extends ServiceImpl<ProcessRouteSubMapper, ProcessRouteSubEntity> implements ProcessRouteSubService {
}
