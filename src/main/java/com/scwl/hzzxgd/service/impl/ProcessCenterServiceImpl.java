package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.ProcessCenterEntity;
import com.scwl.hzzxgd.excel.ExcelUtils;
import com.scwl.hzzxgd.mapper.ProcessCenterMapper;
import com.scwl.hzzxgd.service.ProcessCenterService;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProcessCenterExcelVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * 工序中心
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessCenterServiceImpl extends ServiceImpl<ProcessCenterMapper, ProcessCenterEntity> implements ProcessCenterService {
    @Resource
    private ProcessCenterMapper processCenterMapper;
    /**
     * 获取工序中心列表 分页
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
        List<ProcessCenterEntity> processCenterEntity = processCenterMapper.getList(corpid,startIndex,pageSize);
        int count = this.count(new QueryWrapper<ProcessCenterEntity>().lambda().eq(ProcessCenterEntity::getCorpid, corpid));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(processCenterEntity);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }

    /**
     * 根据id获取工序中心
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ProcessCenterEntity getByid(String id) {
        return this.getById(id);
    }

    /**
     * 添加工序中心
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void create(String corpid, String openUserid, ProcessCenterEntity entity) {
        entity.setCorpid(corpid).setCreateId(openUserid).setCreateTime(new Date()).setModificationId(openUserid).setModificationTime(new Date());
        this.save(entity);
    }

    /**
     * 修改工序中心
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void update(String corpid, String openUserid, ProcessCenterEntity entity) {
        entity.setModificationId(openUserid).setModificationTime(new Date());
        this.updateById(entity);
    }

    /**
     * 删除工序中心
     * @param ids
     */
    @Override
    @Transactional
    public void removes(String[] ids) {
        List<String> list = Arrays.asList(ids);
        this.removeByIds(list);
    }

    /**
     * 导出产品单位表
     * @param corpid
     * @return
     */
    @Override
    @Transactional
    public List<ProcessCenterExcelVo> export(String corpid, String[] ids) {
        List<String> list = null;
        List<ProcessCenterEntity> processCenterEntities = null;
        if (PubUtil.isNotEmpty(ids)){
            list = Arrays.asList(ids);
            processCenterEntities = this.listByIds(list);
        }else{
            processCenterEntities = this.list(new QueryWrapper<ProcessCenterEntity>().lambda().eq(ProcessCenterEntity::getCorpid,corpid).eq(ProcessCenterEntity::getIsActive,"1"));
        }
        List<ProcessCenterExcelVo> processCenterExcelVos = new ArrayList<>();
        for (ProcessCenterEntity processCenterEntity : processCenterEntities) {
            ProcessCenterExcelVo productUnitExcelVo = new ProcessCenterExcelVo();
            productUnitExcelVo.setCenterName(processCenterEntity.getName());
            processCenterExcelVos.add(productUnitExcelVo);
        }
        return processCenterExcelVos;
    }

    /**
     * 导入产品单位表
     * @param corpid
     * @param userid
     * @param file
     */
    @Override
    @Transactional
    public SandResponse importCenter(String corpid, String userid, MultipartFile file) {
        try {
            List<ProcessCenterExcelVo> processCenterExcelVos = ExcelUtils.readMultipartFile(file, ProcessCenterExcelVo.class);
            for (ProcessCenterExcelVo processCenterExcelVo : processCenterExcelVos) {
                if (!"".equals(processCenterExcelVo.getRowTips())){
                    return SandResponse.error(500,processCenterExcelVo.getRowTips());
                }
            }
            for (ProcessCenterExcelVo processCenterExcelVo : processCenterExcelVos) {
                ProcessCenterEntity processCenterEntity = new ProcessCenterEntity();
                processCenterEntity.setCorpid(corpid).setCreateId(userid).setModificationId(userid).setModificationTime(new Date()).setCreateTime(new Date()).setName(processCenterExcelVo.getCenterName());
                this.save(processCenterEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SandResponse.ok();
    }
}
