package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProductionOrderEntity;
import com.scwl.hzzxgd.utils.PageHelper;

/**
 * 生产工单
 */
public interface ProductionOrderService {
    /**
     * 新增生产工单
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void create(String corpid, String openUserid, ProductionOrderEntity entity);

    /**
     * 修改生产工单
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void update(String corpid, String openUserid, ProductionOrderEntity entity);

    /**
     * 获取生产工单列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageHelper getList(String corpid, int pageIndex, int pageSize);

    /**
     * 通过id查询生产工单信息
     * @param id
     * @return
     */
    ProductionOrderEntity getByid(String id);

    /**
     * 删除生产工单
     * @param ids
     */
    void removes(String[] ids);
}
