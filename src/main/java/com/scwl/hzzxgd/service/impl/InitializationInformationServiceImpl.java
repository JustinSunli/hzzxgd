package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.InitializationInformationEntity;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.mapper.InitializationInformationMapper;
import com.scwl.hzzxgd.service.InitializationInformationService;
import com.scwl.hzzxgd.service.MemberInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化信息
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InitializationInformationServiceImpl extends ServiceImpl<InitializationInformationMapper, InitializationInformationEntity> implements InitializationInformationService {
    @Resource
    private MemberInformationServiceImpl memberInformationService;

    /**
     * 获取初始化信息
     * @param corpid
     * @param openUserid
     * @return
     */
    @Override
    @Transactional
    public List<InitializationInformationEntity> initialization(String corpid,String openUserid) {
        MemberInformationEntity memberInformationEntity =
                memberInformationService.getOne(new QueryWrapper<MemberInformationEntity>().lambda()
                        .eq(MemberInformationEntity::getId, openUserid)
                        .eq(MemberInformationEntity::getCorpid,corpid));
        String role = memberInformationEntity.getIsAdmin();
        List<InitializationInformationEntity> entityList = new ArrayList<>();
        List<InitializationInformationEntity> list =
                this.list(new QueryWrapper<InitializationInformationEntity>().lambda()
                        .eq(InitializationInformationEntity::getPId,0L)
                        .eq(InitializationInformationEntity::getRole,role)
                        .eq(InitializationInformationEntity::getIsActive,"1"));
        for (InitializationInformationEntity entity : list) {
            String id = entity.getId();
            List<InitializationInformationEntity> entities = this.list(new QueryWrapper<InitializationInformationEntity>().lambda()
                    .eq(InitializationInformationEntity::getPId, id)
                    .eq(InitializationInformationEntity::getRole, role)
                    .eq(InitializationInformationEntity::getIsActive, "1"));
            InitializationInformationEntity initializationInformationEntity = new InitializationInformationEntity();
            initializationInformationEntity.setMenuUrl(entity.getMenuUrl())
                    .setMenuName(entity.getMenuName()).setIcon(entity.getIcon())
                    .setChildren(entities);
            entityList.add(initializationInformationEntity);
        }
        return entityList;
    }

}




















