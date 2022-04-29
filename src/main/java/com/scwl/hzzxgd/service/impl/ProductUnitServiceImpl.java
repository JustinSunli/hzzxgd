package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.entity.ProductUnitEntity;
import com.scwl.hzzxgd.excel.ExcelUtils;
import com.scwl.hzzxgd.exception.SandException;
import com.scwl.hzzxgd.mapper.DepartmentListInformationMapper;
import com.scwl.hzzxgd.mapper.MemberInformationMapper;
import com.scwl.hzzxgd.mapper.ProductUnitMapper;
import com.scwl.hzzxgd.service.ProductUnitService;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProductUnitExcelVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.*;

/**
 * 产品单位
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductUnitServiceImpl  extends ServiceImpl<ProductUnitMapper, ProductUnitEntity> implements ProductUnitService {
    @Resource
    private ProductUnitMapper productUnitMapper;
    @Resource
    private MemberInformationMapper memberInformationMapper;
    @Resource
    private DepartmentListInformationMapper departmentListInformationMapper;
    /**
     * 获取产品单位列表 分页
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
        List<ProductUnitEntity> productUnitEntity = productUnitMapper.getList(corpid,startIndex,pageSize);
        int count = this.count(new QueryWrapper<ProductUnitEntity>().lambda().eq(ProductUnitEntity::getCorpid, corpid).eq(ProductUnitEntity::getIsActive,"1"));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(productUnitEntity);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }

    /**
     * 根据id获取产品单位
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ProductUnitEntity getByid(String id) {
        return this.getById(id);
    }

    /**
     * 添加产品单位
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void create(String corpid, String openUserid, ProductUnitEntity entity) {
        entity.setCorpid(corpid).setCreateId(openUserid).setCreateTime(new Date()).setModificationId(openUserid).setModificationTime(new Date());
        this.save(entity);
    }

    /**
     * 修改产品单位
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void update(String corpid, String openUserid, ProductUnitEntity entity) {
        entity.setModificationId(openUserid).setModificationTime(new Date());
        this.updateById(entity);
    }

    /**
     * 删除产品单位
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
    public List<ProductUnitExcelVo> excelPortUnit(String corpid,String[] ids) {
        List<String> list = null;
        List<ProductUnitEntity> productUnitEntities = null;
        if (PubUtil.isNotEmpty(ids)){
            list = Arrays.asList(ids);
            productUnitEntities = this.listByIds(list);
        }else{
            productUnitEntities = this.list(new QueryWrapper<ProductUnitEntity>().lambda().eq(ProductUnitEntity::getCorpid,corpid).eq(ProductUnitEntity::getIsActive,"1"));
        }
        List<ProductUnitExcelVo> productUnitExcelVos = new ArrayList<>();
        for (ProductUnitEntity productUnitEntity : productUnitEntities) {
            ProductUnitExcelVo productUnitExcelVo = new ProductUnitExcelVo();
            productUnitExcelVo.setUnitName(productUnitEntity.getName());
            productUnitExcelVos.add(productUnitExcelVo);
        }
        return productUnitExcelVos;
    }

    /**
     * 导入产品单位表
     * @param corpid
     * @param userid
     * @param file
     */
    @Override
    @Transactional
    public SandResponse importUnit(String corpid, String userid, MultipartFile file) {
        try {
            List<ProductUnitExcelVo> productUnitExcelVos = ExcelUtils.readMultipartFile(file, ProductUnitExcelVo.class);
            for (ProductUnitExcelVo productUnitExcelVo : productUnitExcelVos) {
                if (!"".equals(productUnitExcelVo.getRowTips())){
                    return SandResponse.error(500,productUnitExcelVo.getRowTips());
                }
            }
            for (ProductUnitExcelVo productUnitExcelVo : productUnitExcelVos) {
                ProductUnitEntity productUnitEntity = new ProductUnitEntity();
                productUnitEntity.setCorpid(corpid).setCreateId(userid).setModificationId(userid).setModificationTime(new Date()).setCreateTime(new Date()).setName(productUnitExcelVo.getUnitName());
                this.save(productUnitEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SandResponse.ok();
    }
}
