package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.OperationAssignmentsEntity;
import com.scwl.hzzxgd.entity.ProductionOperationEntity;
import com.scwl.hzzxgd.entity.ProductionOrderEntity;
import com.scwl.hzzxgd.mapper.OperationAssignmentsMapper;
import com.scwl.hzzxgd.mapper.ProductionOperationMapper;
import com.scwl.hzzxgd.mapper.ProductionOrderMapper;
import com.scwl.hzzxgd.service.ProductionOrderService;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductionOrderServiceImpl extends ServiceImpl<ProductionOrderMapper, ProductionOrderEntity> implements ProductionOrderService {
    @Resource
    private ProductionOrderMapper productionOrderMapper;
    @Resource
    private ProductionOperationMapper productionOperationMapper;
    @Resource
    private OperationAssignmentsMapper operationAssignmentsMapper;

    /**
     * 新增生产工单
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void create(String corpid, String openUserid, ProductionOrderEntity entity) {
        Date date = new Date();
        ProductionOrderEntity productionOrderEntity = new ProductionOrderEntity();
        productionOrderEntity.setSeqNo(entity.getSeqNo()).setProductId(entity.getProductId()).setProductCode(entity.getProductCode()).setProductName(entity.getProductName())
                .setProductSpec(entity.getProductSpec()).setProductUnit(entity.getProductUnit()).setProductPicture(entity.getProductPicture()).setRouteId(entity.getRouteId())
                .setDispatchQty(entity.getDispatchQty()).setCompletedQty(entity.getCompletedQty()).setScheduleStartTime(entity.getScheduleStartTime()).setScheduleEndTime(entity.getScheduleEndTime())
                .setProductionStartTime(entity.getProductionStartTime()).setProductionEndTime(entity.getProductionEndTime()).setManagerId(entity.getManagerId()).setProductionStatus(entity.getProductionStatus())
                .setRemark(entity.getRemark()).setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
        this.save(productionOrderEntity);
        List<ProductionOperationEntity> operations = entity.getOperations();
        for (ProductionOperationEntity operation : operations) {
            operation.setProductionOrderId(productionOrderEntity.getId()).setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
            productionOperationMapper.insert(operation);
            List<OperationAssignmentsEntity> assignments = operation.getAssignments();
            for (OperationAssignmentsEntity assignment : assignments) {
                assignment.setOperationId(operation.getId()).setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
                operationAssignmentsMapper.insert(assignment);
            }
        }
    }

    /**
     * 修改生产工单
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void update(String corpid, String openUserid, ProductionOrderEntity entity) {
        Date date = new Date();
        ProductionOrderEntity productionOrderEntity = new ProductionOrderEntity();
        productionOrderEntity.setId(entity.getId()).setSeqNo(entity.getSeqNo()).setProductId(entity.getProductId()).setProductCode(entity.getProductCode()).setProductName(entity.getProductName())
                .setProductSpec(entity.getProductSpec()).setProductUnit(entity.getProductUnit()).setProductPicture(entity.getProductPicture()).setRouteId(entity.getRouteId())
                .setDispatchQty(entity.getDispatchQty()).setCompletedQty(entity.getCompletedQty()).setScheduleStartTime(entity.getScheduleStartTime()).setScheduleEndTime(entity.getScheduleEndTime())
                .setProductionStartTime(entity.getProductionStartTime()).setProductionEndTime(entity.getProductionEndTime()).setManagerId(entity.getManagerId()).setProductionStatus(entity.getProductionStatus())
                .setRemark(entity.getRemark()).setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
        this.updateById(productionOrderEntity);
        List<ProductionOperationEntity> operations = entity.getOperations();
        for (ProductionOperationEntity operation : operations) {
            operation.setModificationId(openUserid).setModificationTime(date);
            productionOperationMapper.updateById(operation);
            List<OperationAssignmentsEntity> assignments = operation.getAssignments();
            for (OperationAssignmentsEntity assignment : assignments) {
                assignment.setModificationId(openUserid).setModificationTime(date);
                operationAssignmentsMapper.update(assignment,new QueryWrapper<OperationAssignmentsEntity>().lambda().eq(OperationAssignmentsEntity::getOperationId,operation.getId()));
            }
        }
    }

    /**
     * 获取生产工单列表 分页
     * @param corpid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    @Transactional
    public PageHelper getList(String corpid, int pageIndex, int pageSize) {
        if (PubUtil.isEmpty(pageIndex) || PubUtil.isEmpty(pageSize)){
            pageIndex = 1; pageSize = 10;
        }
        int startIndex = (pageIndex-1) * pageSize;
        List<ProductionOrderEntity> productionOrderEntities = productionOrderMapper.getList(corpid,startIndex,pageSize);
        for (ProductionOrderEntity productionOrderEntity : productionOrderEntities) {
            String id = productionOrderEntity.getId();
            List<ProductionOperationEntity> productionOperationEntities = productionOperationMapper.selectByOrderId(id);
            for (ProductionOperationEntity productionOperationEntity : productionOperationEntities) {
                String operationEntityId = productionOperationEntity.getId();
                List<OperationAssignmentsEntity> assignments = operationAssignmentsMapper.selectList(new QueryWrapper<OperationAssignmentsEntity>().lambda().eq(OperationAssignmentsEntity::getOperationId,operationEntityId));
                productionOperationEntity.setAssignments(assignments);
            }
            productionOrderEntity.setOperations(productionOperationEntities);
        }
        int count = this.count(new QueryWrapper<ProductionOrderEntity>().lambda().eq(ProductionOrderEntity::getCorpid, corpid));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(productionOrderEntities);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }

    /**
     * 通过id查询生产工单信息
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ProductionOrderEntity getByid(String id) {
        ProductionOrderEntity productionOrderEntity = this.getById(id);
        String orderId = productionOrderEntity.getId();
        List<ProductionOperationEntity> productionOperationEntities = productionOperationMapper.selectByOrderId(orderId);
        for (ProductionOperationEntity productionOperationEntity : productionOperationEntities) {
            String operationEntityId = productionOperationEntity.getId();
            List<OperationAssignmentsEntity> assignments = operationAssignmentsMapper.selectList(new QueryWrapper<OperationAssignmentsEntity>().lambda().eq(OperationAssignmentsEntity::getOperationId,operationEntityId));
            productionOperationEntity.setAssignments(assignments);
        }
        productionOrderEntity.setOperations(productionOperationEntities);
        return productionOrderEntity;
    }

    /**
     * 删除生产工单
     * @param ids
     */
    @Override
    public void removes(String[] ids) {
        List<String> list = Arrays.asList(ids);
        this.removeByIds(list);
        for (String orderId : list) {
            List<ProductionOperationEntity> productionOperationEntities = productionOperationMapper.selectByOrderId(orderId);
            for (ProductionOperationEntity productionOperationEntity : productionOperationEntities) {
                String operationEntityId = productionOperationEntity.getId();
                operationAssignmentsMapper.delete(new QueryWrapper<OperationAssignmentsEntity>().lambda().eq(OperationAssignmentsEntity::getOperationId,operationEntityId));
            }
            productionOperationMapper.delete(new QueryWrapper<ProductionOperationEntity>().lambda().eq(ProductionOperationEntity::getProductionOrderId,orderId));
        }
    }


}
