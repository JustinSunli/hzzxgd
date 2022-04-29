package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.EnterpriseInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/**
 * 企业信息
 */
public interface EnterpriseInformationMapper extends BaseMapper<EnterpriseInformationEntity> {

    EnterpriseInformationEntity selectByCorpid(@Param("corpid") String corpid);
}
