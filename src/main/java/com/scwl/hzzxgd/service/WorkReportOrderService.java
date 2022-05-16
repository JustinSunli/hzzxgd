package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.WorkReportOrderEntity;
import com.scwl.hzzxgd.utils.PageHelper;

/**
 * 报工
 */
public interface WorkReportOrderService {
    /**
     * 新增报工单
     * @param corpid
     * @param userid
     * @param entity
     */
    void create(String corpid, String userid, WorkReportOrderEntity entity);

    /**
     * 获取报工单列表 分页
     * @param corpid
     * @param userid
     * @param pageIndex
     * @param pageSize
     * @param seqNo
     * @param productionOrderSeqNo
     * @param workCenterName
     * @param operatorName
     * @param isCheck
     * @param productCode
     * @param productName
     * @return
     */
    PageHelper getList(String corpid, String userid, int pageIndex, int pageSize, String seqNo, String productionOrderSeqNo, String workCenterName, String operatorName, String isCheck, String productCode, String productName);

    /**
     * 通过id查询报工单
     * @param id
     * @return
     */
    WorkReportOrderEntity getByid(String id);

    /**
     * 删除报工单
     * @param ids
     */
    void removes(String[] ids);
}
