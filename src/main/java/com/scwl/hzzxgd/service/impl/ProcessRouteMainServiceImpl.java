package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.*;
import com.scwl.hzzxgd.exception.SandException;
import com.scwl.hzzxgd.mapper.*;
import com.scwl.hzzxgd.oss.OssData;
import com.scwl.hzzxgd.service.OssUploadService;
import com.scwl.hzzxgd.service.ProcessRouteMainService;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * 工艺路线主表
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessRouteMainServiceImpl extends ServiceImpl<ProcessRouteMainMapper, ProcessRouteMainEntity> implements ProcessRouteMainService {
    @Resource
    private ProcessRouteMainMapper processRouteMainMapper;
    @Resource
    private ProcessRouteSubMapper processRouteSubMapper;
    @Resource
    private ProductDetailsMapper productDetailsMapper;
    @Resource
    private MemberInformationMapper memberInformationMapper;
    @Resource
    private RouteOperatorsMapper routeOperatorsMapper;
    @Resource
    private LocalFileCacheMapper localFileCacheMapper;
    @Resource
    private OssUploadService ossUploadService;

    @Value("${oss.fileHost}")
    String fileHost;
    /**
     * 获取工艺列表 分页
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
        List<ProcessRouteMainEntity> processRouteMainEntities = processRouteMainMapper.getList(corpid,startIndex,pageSize);
        for (ProcessRouteMainEntity processRouteMainEntity : processRouteMainEntities) {
            String id = processRouteMainEntity.getId();
            List<ProcessRouteSubEntity> processRouteSubEntities = processRouteSubMapper.selectByRouteId(id);
            for (ProcessRouteSubEntity processRouteSubEntity : processRouteSubEntities) {
                String operationId = processRouteSubEntity.getId();
                List<RouteOperatorsEntity> operators = routeOperatorsMapper.selectList(new QueryWrapper<RouteOperatorsEntity>().lambda().eq(RouteOperatorsEntity::getOperationId, operationId));
                processRouteSubEntity.setOperators(operators);
            }
            String productId = processRouteMainEntity.getProductId();
            ProductDetailsEntity productDetailsEntity = productDetailsMapper.getByid(productId);
            processRouteMainEntity.setOperations(processRouteSubEntities).setProduct(productDetailsEntity);
        }
        int count = this.count(new QueryWrapper<ProcessRouteMainEntity>().lambda().eq(ProcessRouteMainEntity::getCorpid, corpid));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(processRouteMainEntities);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }

    /**
     * 根据id获取工艺路线详情
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ProcessRouteMainEntity getByid(String id) {
        ProcessRouteMainEntity processRouteMainEntity = this.getById(id);
        String routeMainEntityIdid = processRouteMainEntity.getId();
        List<ProcessRouteSubEntity> processRouteSubEntities = processRouteSubMapper.selectByRouteId(routeMainEntityIdid);
        for (ProcessRouteSubEntity processRouteSubEntity : processRouteSubEntities) {
            String operationId = processRouteSubEntity.getId();
            List<RouteOperatorsEntity> operators = routeOperatorsMapper.selectList(new QueryWrapper<RouteOperatorsEntity>().lambda().eq(RouteOperatorsEntity::getOperationId, operationId));
            processRouteSubEntity.setOperators(operators);
        }
        String productId = processRouteMainEntity.getProductId();
        ProductDetailsEntity productDetailsEntity = productDetailsMapper.getByid(productId);
        processRouteMainEntity.setOperations(processRouteSubEntities).setProduct(productDetailsEntity);
        return processRouteMainEntity;
    }

    /**
     * 添加工艺路线
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void create(String corpid, String openUserid, ProcessRouteMainEntity entity) {
        Date date = new Date();
//        ProductDetailsEntity productDetailsEntity = entity.getProduct();
        ProcessRouteMainEntity processRouteMainEntity = new ProcessRouteMainEntity();
        processRouteMainEntity.setCode(entity.getCode()).setName(entity.getName()).setProductId(entity.getProductId()).setRemark(entity.getRemark())
        .setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
        this.save(processRouteMainEntity);
        List<ProcessRouteSubEntity> processRouteSubEntity = entity.getOperations();
        for (ProcessRouteSubEntity routeSubEntity : processRouteSubEntity) {
            List<RouteOperatorsEntity> operators = routeSubEntity.getOperators();
            for (RouteOperatorsEntity operator : operators) {
                String operatorName = memberInformationMapper.selectOne(new QueryWrapper<MemberInformationEntity>().lambda().eq(MemberInformationEntity::getId,operator.getOperatorId())).getName();
                operator.setOperationId(routeSubEntity.getId()).setOperatorName(operatorName).setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
                routeOperatorsMapper.insert(operator);
            }
            String attachmentId = routeSubEntity.getAttachment();
            String attachment = ossUpload(attachmentId);
            String pictureId = routeSubEntity.getPicture();
            String picture = ossUpload(pictureId);

            routeSubEntity.setAttachment(attachment).setPicture(picture).setRouteId(processRouteMainEntity.getId()).setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
            processRouteSubMapper.insert(routeSubEntity);
        }
//        productDetailsMapper.insert(productDetailsEntity);
    }

    /**
     * 文件上传oss 并删除本地文件和数据库
     * @param id
     */
    private String ossUpload(String id) {
        if (PubUtil.isNotEmpty(id)) {
            LocalFileCacheEntity entity = localFileCacheMapper.selectById(id);
            String path = entity.getPath();
            File file = new File(path);
            if (file.exists()){
                OssData ossData = new OssData();
                // 上传文件返回url
                String url = ossUploadService.upload((MultipartFile) file);
                /* 删除本地文件 */
                file.delete();
                localFileCacheMapper.deleteById(id);
                if (url != null) {
                    ossData.setHost(fileHost);
                    ossData.setPath(fileHost + url);
                    return fileHost + url;
                } else {
                    throw new SandException("上传失败");
                }
            }
        }
        return null;
    }

    /**
     * 修改工艺路线
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void update(String corpid, String openUserid, ProcessRouteMainEntity entity) {
        Date date = new Date();
        ProcessRouteMainEntity processRouteMainEntity = new ProcessRouteMainEntity();
        processRouteMainEntity.setId(entity.getId()).setCode(entity.getCode()).setName(entity.getName()).setProductId(entity.getProductId()).setRemark(entity.getRemark())
                .setModificationId(openUserid).setModificationTime(date);
        this.updateById(processRouteMainEntity);
//        processRouteSubMapper.delete(new QueryWrapper<ProcessRouteSubEntity>().lambda().eq(ProcessRouteSubEntity::getRouteId,entity.getId()));
        List<ProcessRouteSubEntity> processRouteSubEntity = entity.getOperations();
        for (ProcessRouteSubEntity routeSubEntity : processRouteSubEntity) {
            String attachmentId = routeSubEntity.getAttachment();
            String attachment = ossUpload(attachmentId);
            String pictureId = routeSubEntity.getPicture();
            String picture = ossUpload(pictureId);
            routeSubEntity.setAttachment(attachment).setPicture(picture).setModificationId(openUserid).setModificationTime(date);
            processRouteSubMapper.updateById(routeSubEntity);
            List<RouteOperatorsEntity> operators = routeSubEntity.getOperators();
            for (RouteOperatorsEntity operator : operators) {
                operator.setModificationId(openUserid).setModificationTime(date);
                routeOperatorsMapper.updateById(operator);
            }

//            List<RouteOperatorsEntity> operators = routeSubEntity.getOperators();
//            String operationId = operators.get(0).getOperationId();
//            routeOperatorsMapper.delete(new QueryWrapper<RouteOperatorsEntity>().lambda().eq(RouteOperatorsEntity::getOperationId,operationId));
//            for (RouteOperatorsEntity operator : operators) {
//                operator.setCorpid(corpid).setCreateId(openUserid).setCreateTime(date).setModificationId(openUserid).setModificationTime(date);
//                routeOperatorsMapper.insert(operator);
//            }
//            routeSubEntity.setRouteId(entity.getId()).setCorpid(corpid).setModificationId(openUserid).setModificationTime(date);
//            processRouteSubMapper.insert(routeSubEntity);
        }
    }

    /**
     * 删除工艺路线
     * @param ids
     */
    @Override
    @Transactional
    public void removes(String[] ids) {
        List<String> list = Arrays.asList(ids);
        this.removeByIds(list);
        for (String routeId : list) {
            List<ProcessRouteSubEntity> processRouteSubEntities = processRouteSubMapper.selectByRouteId(routeId);
            for (ProcessRouteSubEntity processRouteSubEntity : processRouteSubEntities) {
                String id = processRouteSubEntity.getId();
                routeOperatorsMapper.delete(new QueryWrapper<RouteOperatorsEntity>().lambda().eq(RouteOperatorsEntity::getOperationId,id));
            }
            processRouteSubMapper.delete(new QueryWrapper<ProcessRouteSubEntity>().lambda().eq(ProcessRouteSubEntity::getRouteId,routeId));
        }
    }

}
