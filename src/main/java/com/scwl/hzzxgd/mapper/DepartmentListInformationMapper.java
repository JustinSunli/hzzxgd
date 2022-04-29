package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
/**
 * 部门列表信息
 */
public interface DepartmentListInformationMapper extends BaseMapper<DepartmentListInformationEntity> {
    /**
     * 获取该企业的部门id
     * @param corpid
     * @return
     */
    List<Integer> getDepartmentIds(@Param("corpid") String corpid);

}
