package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.*;
import com.scwl.hzzxgd.mapper.*;
import com.scwl.hzzxgd.service.WorkReportOrderService;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 报工
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkReportOrderServiceImpl extends ServiceImpl<WorkReportOrderMapper, WorkReportOrderEntity> implements WorkReportOrderService {
    @Resource
    private WorkReportOrderMapper workReportOrderMapper;
    @Resource
    private ProductionOperationMapper productionOperationMapper;
    @Resource
    private ProductionOrderMapper productionOrderMapper;
    @Resource
    private ProcessCenterMapper processCenterMapper;
    @Resource
    private MemberInformationMapper memberInformationMapper;
    @Resource
    private SystemSettingsMapper systemSettingsMapper;

    /**
     * 新增报工单
     * @param corpid
     * @param userid
     * @param entity
     */
    @Override
    @Transactional
    public void create(String corpid, String userid, WorkReportOrderEntity entity) {
        SystemSettingsEntity systemSettingsEntity = systemSettingsMapper.selectOne(new QueryWrapper<SystemSettingsEntity>().lambda().eq(SystemSettingsEntity::getCorpid, corpid));
        Boolean isFbDispatchQtyLimited = systemSettingsEntity.getIsFbDispatchQtyLimited();
        Boolean isFbDownstreamQtyLimited = systemSettingsEntity.getIsFbDownstreamQtyLimited();
        if (isFbDispatchQtyLimited == false){
            if (isFbDownstreamQtyLimited == false){

            }
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        int count = this.count(new QueryWrapper<WorkReportOrderEntity>().lambda().eq(WorkReportOrderEntity::getCorpid,corpid).eq(WorkReportOrderEntity::getIsActive,"1"));
        String index = "01";
        if (count>0 && count<9){
            index = "0"+(count+1);
        }else if (count>=9){
            index = count+1+"";
        }
        String seqNo = "BG"+sdf.format(date).replace("/", "")+index;
        entity.setSeqNo(seqNo).setCorpid(corpid).setCreateId(userid).setModificationId(userid).setCreateTime(date).setModificationTime(date);
        this.save(entity);
    }

    /**
     * 获取报工单列表 分页
     * @param corpid
     * @param userid
     * @param pageIndex
     * @param pageSize
     * @param seqNo
     * @param productionOrderSeqNo
     * @param workCenterName
     * @param operatorName
     * @param isCheck
     * @param productCode
     * @param productName
     * @return
     */
    @Override
    @Transactional
    public PageHelper getList(String corpid, String userid, int pageIndex, int pageSize, String seqNo, String productionOrderSeqNo, String workCenterName, String operatorName, String isCheck, String productCode, String productName) {
        if (PubUtil.isEmpty(pageIndex) || PubUtil.isEmpty(pageSize)){
            pageIndex = 1; pageSize = 10;
        }
        int startIndex = (pageIndex-1) * pageSize;
        String centerId = null;
        if (PubUtil.isNotEmpty(workCenterName)){
            centerId = processCenterMapper.selectIdByCenterName(corpid,workCenterName);
        }
        String memberId = null;
        if (PubUtil.isNotEmpty(operatorName)){
            String isAdmin = memberInformationMapper.selectById(userid).getIsAdmin();
            if ("0".equals(isAdmin)){
                operatorName = memberInformationMapper.selectById(userid).getName();
            }
            memberId = memberInformationMapper.selectOne(new QueryWrapper<MemberInformationEntity>().lambda().eq(MemberInformationEntity::getCorpid, centerId).eq(MemberInformationEntity::getName, operatorName)).getId();
        }
        List<WorkReportOrderEntity> workReportOrderEntities = workReportOrderMapper.getList(corpid,userid,startIndex,pageSize,seqNo,productionOrderSeqNo,centerId,memberId,isCheck,productCode,productName);
        for (WorkReportOrderEntity workReportOrderEntity : workReportOrderEntities) {
            List<ProductionOperationEntity> productionOperationEntities = productionOperationMapper.selectByOperationId(workReportOrderEntity.getOperationId());
            ProductionOrderEntity productionOrderEntity = productionOrderMapper.selectById(productionOperationEntities.get(0).getProductionOrderId());
            workReportOrderEntity.setOperation(productionOperationEntities).setProduct(productionOrderEntity);
        }

        int count = this.count(new QueryWrapper<WorkReportOrderEntity>().lambda().eq(WorkReportOrderEntity::getCorpid, corpid).eq(WorkReportOrderEntity::getOperatorId,userid));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(workReportOrderEntities);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }

    /**
     * 通过id查询报工单
     * @param id
     * @return
     */
    @Override
    @Transactional
    public WorkReportOrderEntity getByid(String id) {
        WorkReportOrderEntity workReportOrderEntity = workReportOrderMapper.getByid(id);
        List<ProductionOperationEntity> productionOperationEntities = productionOperationMapper.selectByOperationId(workReportOrderEntity.getOperationId());
        ProductionOrderEntity productionOrderEntity = productionOrderMapper.selectById(productionOperationEntities.get(0).getProductionOrderId());
        workReportOrderEntity.setOperation(productionOperationEntities).setProduct(productionOrderEntity);
        return workReportOrderEntity;
    }

    /**
     * 删除报工单
     * @param ids
     */
    @Override
    @Transactional
    public void removes(String[] ids) {
        for (String id : ids) {
            WorkReportOrderEntity workReportOrderEntity = this.getById(id);
            String operationId = workReportOrderEntity.getOperationId();
            float feedbackQty = workReportOrderEntity.getFeedbackQty();//报工
            float goodQty = workReportOrderEntity.getGoodQty();//良品
            float badQty = workReportOrderEntity.getBadQty();//不良
            float rejectedQty = workReportOrderEntity.getRejectedQty();//报废
            productionOperationMapper.operationRollBack(operationId,feedbackQty,goodQty,badQty,rejectedQty);
            this.removeById(id);
        }
    }


}
