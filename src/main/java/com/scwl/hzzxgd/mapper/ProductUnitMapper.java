package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.ProductUnitEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
/**
 * 产品单位
 */
public interface ProductUnitMapper extends BaseMapper<ProductUnitEntity> {
    /**
     * 获取产品单位列表 分页
     * @param corpid
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<ProductUnitEntity> getList(@Param("corpid") String corpid, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);
}
