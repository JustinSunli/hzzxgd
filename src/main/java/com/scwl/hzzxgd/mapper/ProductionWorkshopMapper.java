package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.ProductionWorkshopEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
/**
 * 生产车间
 */
public interface ProductionWorkshopMapper extends BaseMapper<ProductionWorkshopEntity> {
    /**
     * 获取生产车间列表 分页
     * @param corpid
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<ProductionWorkshopEntity> getList(@Param("corpid") String corpid, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);
}
