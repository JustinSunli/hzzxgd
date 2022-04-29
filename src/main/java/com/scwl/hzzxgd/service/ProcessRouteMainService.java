package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProcessRouteMainEntity;
import com.scwl.hzzxgd.utils.PageHelper;

/**
 * 工艺路线主表
 */
public interface ProcessRouteMainService {
    /**
     * 获取工艺列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageHelper getList(String corpid, int pageIndex, int pageSize);

    /**
     * 根据id获取工艺路线详情
     * @param id
     * @return
     */
    ProcessRouteMainEntity getByid(String id);

    /**
     * 添加工艺路线
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void create(String corpid, String openUserid, ProcessRouteMainEntity entity);

    /**
     * 修改工艺路线
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void update(String corpid, String openUserid, ProcessRouteMainEntity entity);

    /**
     * 删除工艺路线
     * @param ids
     */
    void removes(String[] ids);
}
