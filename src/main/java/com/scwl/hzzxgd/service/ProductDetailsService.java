package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.ProductDetailsEntity;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProductDetailsExcelVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 产品资料表
 */
public interface ProductDetailsService {
    /**
     * 获取产品列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageHelper getList(String corpid, int pageIndex, int pageSize);

    /**
     * 根据id获取产品
     * @param id
     * @return
     */
    ProductDetailsEntity getByid(String id);

    /**
     * 添加产品
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void create(String corpid, String openUserid, ProductDetailsEntity entity);

    /**
     * 修改产品
     * @param corpid
     * @param openUserid
     * @param entity
     */
    void update(String corpid, String openUserid, ProductDetailsEntity entity);

    /**
     * 删除产品
     * @param ids
     */
    void removes(String[] ids);

    /**
     * 导出产品资料表
     * @param corpid
     * @return
     */
    List<ProductDetailsExcelVo> export(String corpid, String[] ids);

    /**
     * 导入产品资料表
     * @param corpid
     * @param userid
     * @param file
     */
    SandResponse importDetail(String corpid, String userid, MultipartFile file);
}
