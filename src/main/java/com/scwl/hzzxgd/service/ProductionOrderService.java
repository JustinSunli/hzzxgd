package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProductionOrderEntity;
import com.scwl.hzzxgd.utils.PageHelper;

import java.util.List;

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

    /**
     * 开工(工单状态待开工改为进行中)
     * @param ids
     */
    void start(String[] ids,String corpid,String userid);

    /**
     * 手工关闭(工单状态进行中改为手工关闭)
     * @param ids
     */
    void close(String[] ids,String corpid,String userid);

//    /**
//     * 获取生产工单列表
//     * @param corpid
//     * @return
//     */
//    List<ProductionOrderEntity> getProductionInfo(String corpid);
}
