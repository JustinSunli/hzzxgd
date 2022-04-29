package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.ProcessCenterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序中心
 */
@Mapper
public interface ProcessCenterMapper extends BaseMapper<ProcessCenterEntity> {
    /**
     * 获取工序中心列表 分页
     * @param corpid
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<ProcessCenterEntity> getList(@Param("corpid") String corpid, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);
}
