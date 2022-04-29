package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProcessCenterEntity;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProcessCenterExcelVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 工序中心
 */
public interface ProcessCenterService {
    /**
     * 获取工序中心列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageHelper getList(String corpid, int pageIndex, int pageSize);

    /**
     * 根据id获取工序中心
     * @param id
     * @return
     */
    ProcessCenterEntity getByid(String id);

    /**
     * 添加工序中心
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void create(String corpid, String openUserid, ProcessCenterEntity entity);

    /**
     * 修改工序中心
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void update(String corpid, String openUserid, ProcessCenterEntity entity);

    /**
     * 删除工序中心
     * @param ids
     */
    void removes(String[] ids);

    /**
     * 导出工序中心表
     * @param corpid
     * @return
     */
    List<ProcessCenterExcelVo> export(String corpid, String[] ids);

    /**
     * 导入工序中心表
     * @param corpid
     * @param userid
     * @param file
     */
    SandResponse importCenter(String corpid, String userid, MultipartFile file);
}
