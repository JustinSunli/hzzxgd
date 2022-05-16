package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.ProductionOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
/**
 * 生产工单
 */
public interface ProductionOrderMapper extends BaseMapper<ProductionOrderEntity> {
    /**
     * 获取生产工单列表 分页
     * @param corpid
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<ProductionOrderEntity> getList(@Param("corpid") String corpid, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    /**
     * 开工(工单状态待开工改为进行中)
     * @param id
     */
    Integer start(@Param("id") String id, @Param("userid") String userid, @Param("date") Date date);

    /**
     * 手工关闭(工单状态进行中改为手工关闭)
     * @param id
     * @return
     */
    Integer close(@Param("id") String id, @Param("userid") String userid, @Param("date") Date date);
}
