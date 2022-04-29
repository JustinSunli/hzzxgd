package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.ProductionWorkshopEntity;
import com.scwl.hzzxgd.mapper.ProductionWorkshopMapper;
import com.scwl.hzzxgd.service.ProductionWorkshopService;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 生产车间
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductionWorkshopServiceImpl extends ServiceImpl<ProductionWorkshopMapper, ProductionWorkshopEntity> implements ProductionWorkshopService {
    @Resource
    private ProductionWorkshopMapper productionWorkshopMapper;
    /**
     * 获取生产车间列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    @Transactional
    public PageHelper getList(String corpid, int pageIndex, int pageSize) {
        if (PubUtil.isEmpty(pageIndex) || PubUtil.isEmpty(pageSize)){
            pageIndex = 1; pageSize = 10;
        }
        int startIndex = (pageIndex-1) * pageSize;
        List<ProductionWorkshopEntity> productionWorkshopEntities = productionWorkshopMapper.getList(corpid,startIndex,pageSize);
        int count = this.count(new QueryWrapper<ProductionWorkshopEntity>().lambda().eq(ProductionWorkshopEntity::getCorpid, corpid));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(productionWorkshopEntities);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }

    /**
     * 根据id获取生产车间
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ProductionWorkshopEntity getByid(String id) {
        return this.getById(id);
    }

    /**
     * 添加生产车间
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void create(String corpid, String openUserid, ProductionWorkshopEntity entity) {
        entity.setCorpid(corpid).setCreateId(openUserid).setCreateTime(new Date()).setModificationId(openUserid).setModificationTime(new Date());
        this.save(entity);
    }

    /**
     * 修改生产车间
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void update(String corpid, String openUserid, ProductionWorkshopEntity entity) {
        entity.setModificationId(openUserid).setModificationTime(new Date());
        this.updateById(entity);
    }

    /**
     * 删除生产车间
     * @param ids
     */
    @Override
    @Transactional
    public void removes(String[] ids) {
        List<String> list = Arrays.asList(ids);
        this.removeByIds(list);
    }
}
