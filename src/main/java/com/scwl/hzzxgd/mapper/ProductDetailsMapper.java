package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.ProductDetailsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
/**
 * 产品资料表
 */
public interface ProductDetailsMapper extends BaseMapper<ProductDetailsEntity> {

    /**
     * 获取产品列表 分页
     * @param corpid
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<ProductDetailsEntity> getList(@Param("corpid") String corpid,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize);

    /**
     * 根据id获取产品
     * @param id
     * @return
     */
    ProductDetailsEntity getByid(@Param("id") String id);

}
