package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProductionWorkshopEntity;
import com.scwl.hzzxgd.utils.PageHelper;

/**
 * 生产车间
 */
public interface ProductionWorkshopService {
    /**
     * 获取生产车间列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageHelper getList(String corpid, int pageIndex, int pageSize);

    /**
     * 根据id获取生产车间
     * @param id
     * @return
     */
    ProductionWorkshopEntity getByid(String id);

    /**
     * 添加工序中心
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void create(String corpid, String openUserid, ProductionWorkshopEntity entity);

    /**
     * 修改生产车间
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void update(String corpid, String openUserid, ProductionWorkshopEntity entity);

    /**
     * 删除生产车间
     * @param ids
     */
    void removes(String[] ids);
}
