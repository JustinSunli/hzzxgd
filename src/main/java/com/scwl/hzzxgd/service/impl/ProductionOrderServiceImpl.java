package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.*;
import com.scwl.hzzxgd.exception.SandException;
import com.scwl.hzzxgd.mapper.*;
import com.scwl.hzzxgd.service.ProductionOrderService;
import com.scwl.hzzxgd.utils.HttpHelper;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.WeChatUtils;
import com.scwl.hzzxgd.vo.SandAppMessageVo;
import com.scwl.hzzxgd.vo.TextContentVo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    @Resource
    private WorkReportOrderMapper workReportOrderMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private HttpRequestServiceImpl httpRequestService;
    @Resource
    private EnterpriseInformationMapper enterpriseInformationMapper;
    @Resource
    private MemberInformationMapper memberInformationMapper;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        int count = this.count(new QueryWrapper<ProductionOrderEntity>().lambda().eq(ProductionOrderEntity::getCorpid,corpid).eq(ProductionOrderEntity::getIsActive,"1"));
        String index = "01";
        if (count>0 && count<9){
            index = "0"+(count+1);
        }else if (count>=9){
            index = count+1+"";
        }
        String seqNo = "WO"+sdf.format(date).replace("/", "")+index;
        ProductionOrderEntity productionOrderEntity = new ProductionOrderEntity();
        productionOrderEntity.setSeqNo(seqNo).setProductId(entity.getProductId()).setProductCode(entity.getProductCode()).setProductName(entity.getProductName())
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
        if (!"0".equals(entity.getProductionStatus())){
            throw new SandException("当前工单不允许修改");
        }
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
    @Transactional
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

    /**
     * 开工(工单状态待开工改为进行中)
     * @param ids
     */
    @Override
    @Transactional
    public void start(String[] ids,String corpid,String userid) {
        Date date = new Date();
        int agentid = enterpriseInformationMapper.selectOne(new QueryWrapper<EnterpriseInformationEntity>().lambda().eq(EnterpriseInformationEntity::getCorpid, corpid).eq(EnterpriseInformationEntity::getIsActive, "1")).getAgentid();
        for (String id : ids) {
            ProductionOrderEntity productionOrderEntity = this.getById(id);
            String productionStatus = productionOrderEntity.getProductionStatus();
            String seqNo = productionOrderEntity.getSeqNo();
            if (!"0".equals(productionStatus)){
                throw new SandException("当前工单 "+ seqNo +" 不允许开启");
            }
            List<ProductionOperationEntity> productionOperationEntities = productionOperationMapper.selectList(new QueryWrapper<ProductionOperationEntity>().lambda().eq(ProductionOperationEntity::getProductionOrderId,id).eq(ProductionOperationEntity::getIsActive,"1"));
            for (ProductionOperationEntity productionOperationEntity : productionOperationEntities) {
                List<OperationAssignmentsEntity> operationAssignmentsEntities = operationAssignmentsMapper.selectList(new QueryWrapper<OperationAssignmentsEntity>().lambda().eq(OperationAssignmentsEntity::getOperationId, productionOperationEntity.getId()).eq(OperationAssignmentsEntity::getIsActive, "1"));
                for (OperationAssignmentsEntity operationAssignmentsEntity : operationAssignmentsEntities) {
                    WorkReportOrderEntity workReportOrderEntity = new WorkReportOrderEntity();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    int count = workReportOrderMapper.selectCount(new QueryWrapper<WorkReportOrderEntity>().lambda().eq(WorkReportOrderEntity::getCorpid,corpid).eq(WorkReportOrderEntity::getIsActive,"1"));
                    String index = "01";
                    if (count>0 && count<9){
                        index = "0"+(count+1);
                    }else if (count>=9){
                        index = count+1+"";
                    }
                    String seqNoWork = "WO"+sdf.format(date).replace("/", "")+index;
                    workReportOrderEntity.setCorpid(corpid).setCreateId(userid).setCreateTime(date).setModificationId(userid).setModificationTime(date)
                            .setOperationId(productionOperationEntity.getId()).setOperatorId(operationAssignmentsEntity.getOperatorId()).setWage(productionOperationEntity.getWage())
                            .setSalaryMethod(productionOperationEntity.getSalaryMethod()).setSeqNo(seqNoWork);
                    workReportOrderMapper.insert(workReportOrderEntity);
                    String touser = memberInformationMapper.selectById(operationAssignmentsEntity.getOperatorId()).getUserid();
                    try {
                        SandAppMessageVo sandAppMessageVo = new SandAppMessageVo();
                        TextContentVo textContentVo = new TextContentVo();
                        textContentVo.setTitle("派工明细").setUrl("https://www.baidu.com/").setBtntxt("详情")
                                .setDescription("<div class=\"normal\">报工单号 "+ workReportOrderEntity.getSeqNo() +"</div> <div class=\"normal\">员工确认 "+ touser +"</div> <div class=\"normal\">派工数量 " + productionOperationEntity.getDispatchQty() + "</div>");
                        sandAppMessageVo.setTouser(touser).setMsgtype("textcard").setAgentid(agentid).setTextcard(textContentVo);
                        JSONObject jsonObj = HttpHelper.sandMessage(WeChatUtils.THIRD_BUS_WECHAT_SEND + httpRequestService.getAccessToken(corpid),sandAppMessageVo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            Integer start = productionOrderMapper.start(id,userid,date);
            if (start == 0){
                throw new SandException("开启任务失败");
            }
        }
    }

    /**
     * 手工关闭(工单状态进行中改为手工关闭)
     * @param ids
     */
    @Override
    @Transactional
    public void close(String[] ids,String corpid,String userid) {
        for (String id : ids) {
            ProductionOrderEntity productionOrderEntity = this.getById(id);
            String productionStatus = productionOrderEntity.getProductionStatus();
            if (!"1".equals(productionStatus)){
                throw new SandException("当前工单 "+productionOrderEntity.getSeqNo()+" 不允许手工关闭");
            }
            Integer close = productionOrderMapper.close(id,userid,new Date());
            if (close == 0){
                throw new SandException("手工关闭失败");
            }
        }
    }


}
