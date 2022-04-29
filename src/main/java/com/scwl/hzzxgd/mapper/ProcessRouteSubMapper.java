package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.ProcessRouteSubEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
/**
 * 工艺路线子表
 */
public interface ProcessRouteSubMapper extends BaseMapper<ProcessRouteSubEntity> {
    /**
     * 根据工艺路线主表id查询子表
     * @param id
     * @return
     */
    List<ProcessRouteSubEntity> selectByRouteId(@Param("id") String id);
}
