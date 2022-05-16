package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.WorkReportOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报工
 */
@Mapper
public interface WorkReportOrderMapper extends BaseMapper<WorkReportOrderEntity> {
    /**
     * 获取报工单列表 分页
     * @param corpid
     * @param userid
     * @param startIndex
     * @param pageSize
     * @param seqNo
     * @param productionOrderSeqNo
     * @param centerId
     * @param memberId
     * @param isCheck
     * @param productCode
     * @param productName
     * @return
     */
    List<WorkReportOrderEntity> getList(@Param("corpid") String corpid, @Param("userid") String userid, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize,
                                        @Param("seqNo") String seqNo, @Param("productionOrderSeqNo") String productionOrderSeqNo, @Param("centerId") String centerId,
                                        @Param("memberId") String memberId, @Param("isCheck") String isCheck, @Param("productCode") String productCode, @Param("productName") String productName);

    /**
     * 通过id查询报工单
     * @param id
     * @return
     */
    WorkReportOrderEntity getByid(@Param("id") String id);
}
