package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProductUnitEntity;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProductUnitExcelVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 产品单位
 */
public interface ProductUnitService {
    /**
     * 获取产品单位列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageHelper getList(String corpid, int pageIndex, int pageSize);

    /**
     * 根据id获取产品单位
     * @param id
     * @return
     */
    ProductUnitEntity getByid(String id);

    /**
     * 添加产品单位
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void create(String corpid, String openUserid, ProductUnitEntity entity);

    /**
     * 修改产品单位
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void update(String corpid, String openUserid, ProductUnitEntity entity);

    /**
     * 删除产品单位
     * @param ids
     */
    void removes(String[] ids);

    /**
     * 导出产品单位表
     * @param corpid
     * @return
     */
    List<ProductUnitExcelVo> excelPortUnit(String corpid, String[] ids);

    /**
     * 导入产品单位表
     * @param corpid
     * @param userid
     * @param file
     */
    SandResponse importUnit(String corpid, String userid, MultipartFile file);
}
