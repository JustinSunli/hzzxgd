package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.SystemSettingsEntity;
import com.scwl.hzzxgd.mapper.SystemSettingsMapper;
import com.scwl.hzzxgd.service.SystemSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统设置
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemSettingsServiceImpl extends ServiceImpl<SystemSettingsMapper, SystemSettingsEntity> implements SystemSettingsService {
}
