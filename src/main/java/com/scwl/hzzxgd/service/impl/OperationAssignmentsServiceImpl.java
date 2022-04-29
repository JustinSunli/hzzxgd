package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.OperationAssignmentsEntity;
import com.scwl.hzzxgd.mapper.OperationAssignmentsMapper;
import com.scwl.hzzxgd.service.OperationAssignmentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 派工明细
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OperationAssignmentsServiceImpl extends ServiceImpl<OperationAssignmentsMapper, OperationAssignmentsEntity> implements OperationAssignmentsService {
}
