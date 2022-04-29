package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.ProductDetailsEntity;
import com.scwl.hzzxgd.entity.ProductUnitEntity;
import com.scwl.hzzxgd.excel.ExcelUtils;
import com.scwl.hzzxgd.mapper.ProductDetailsMapper;
import com.scwl.hzzxgd.mapper.ProductUnitMapper;
import com.scwl.hzzxgd.service.ProductDetailsService;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProductDetailsExcelVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * 产品资料表
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductDetailsImpl extends ServiceImpl<ProductDetailsMapper, ProductDetailsEntity> implements ProductDetailsService{
    @Resource
    private ProductDetailsMapper productDetailsMapper;
    @Resource
    private ProductUnitMapper productUnitMapper;
    /**
     * 获取产品列表 分页
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
        List<ProductDetailsEntity> productDetailsEntities = productDetailsMapper.getList(corpid,startIndex,pageSize);
        int count = this.count(new QueryWrapper<ProductDetailsEntity>().lambda().eq(ProductDetailsEntity::getCorpid, corpid));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(productDetailsEntities);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }

    /**
     * 根据id获取产品
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ProductDetailsEntity getByid(String id) {
        return productDetailsMapper.getByid(id);
    }

    /**
     * 添加产品
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void create(String corpid, String openUserid, ProductDetailsEntity entity) {
        entity.setCorpid(corpid).setCreateId(openUserid).setCreateTime(new Date()).setModificationId(openUserid).setModificationTime(new Date());
        this.save(entity);
    }

    /**
     * 修改产品
     * @param corpid
     * @param openUserid
     * @param entity
     */
    @Override
    @Transactional
    public void update(String corpid, String openUserid, ProductDetailsEntity entity) {
        entity.setModificationId(openUserid).setModificationTime(new Date());
        this.updateById(entity);
    }

    /**
     * 删除产品
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
    public List<ProductDetailsExcelVo> export(String corpid,String[] ids) {
        List<String> list = null;
        List<ProductDetailsEntity> productDetailsEntities = null;
        if (PubUtil.isNotEmpty(ids)){
            list = Arrays.asList(ids);
            productDetailsEntities = this.listByIds(list);
        }else{
            productDetailsEntities = this.list(new QueryWrapper<ProductDetailsEntity>().lambda().eq(ProductDetailsEntity::getCorpid,corpid).eq(ProductDetailsEntity::getIsActive,"1"));
        }
        List<ProductDetailsExcelVo> productDetailsExcelVos = new ArrayList<>();
        for (ProductDetailsEntity entity : productDetailsEntities) {
            ProductUnitEntity productUnitEntity = productUnitMapper.selectById(entity.getUnitId());
            String unitName = productUnitEntity.getName();
            ProductDetailsExcelVo productDetailsExcelVo = new ProductDetailsExcelVo();
            productDetailsExcelVo.setCode(entity.getCode()).setName(entity.getName()).setSpec(entity.getSpec())
                    .setUnitName(unitName).setSize(entity.getSize()).setColor(entity.getColor()).setAttachment(entity.getAttachment())
                    .setPicture(entity.getPicture()).setRemark(entity.getPicture());
            productDetailsExcelVos.add(productDetailsExcelVo);
        }
        return productDetailsExcelVos;
    }

    /**
     * 导入产品单位表
     * @param corpid
     * @param userid
     * @param file
     */
    @Override
    @Transactional
    public SandResponse importDetail(String corpid, String userid, MultipartFile file) {
        try {
            List<ProductDetailsExcelVo> productDetailsExcelVos = ExcelUtils.readMultipartFile(file, ProductDetailsExcelVo.class);
            for (ProductDetailsExcelVo productDetailsExcelVo : productDetailsExcelVos) {
                if (!"".equals(productDetailsExcelVo.getRowTips())){
                    return SandResponse.error(500,productDetailsExcelVo.getRowTips());
                }
                String unitName = productDetailsExcelVo.getUnitName();
                ProductUnitEntity productUnitEntity = productUnitMapper.selectOne(new QueryWrapper<ProductUnitEntity>().lambda().eq(ProductUnitEntity::getName, unitName));
                if (PubUtil.isEmpty(productUnitEntity)){
                    return SandResponse.error(500,"数据校验错误,第"+productDetailsExcelVo.getRowNum()+"行产品单位不存在,请添加产品单位后再导入");
                }
            }
            for (ProductDetailsExcelVo productDetailsExcelVo : productDetailsExcelVos) {
                String unitName = productDetailsExcelVo.getUnitName();
                ProductUnitEntity productUnitEntity = productUnitMapper.selectOne(new QueryWrapper<ProductUnitEntity>().lambda().eq(ProductUnitEntity::getName, unitName));
                String unitId = productUnitEntity.getId();
                ProductDetailsEntity productDetailsEntity = new ProductDetailsEntity();
                productDetailsEntity.setCorpid(corpid).setCreateId(userid).setModificationId(userid).setModificationTime(new Date()).setCreateTime(new Date())
                        .setCode(productDetailsExcelVo.getCode()).setName(productDetailsExcelVo.getName()).setSpec(productDetailsExcelVo.getSpec())
                        .setUnitId(unitId).setSize(productDetailsExcelVo.getSize()).setColor(productDetailsExcelVo.getColor()).setAttachment(productDetailsExcelVo.getAttachment())
                        .setPicture(productDetailsExcelVo.getPicture()).setRemark(productDetailsExcelVo.getPicture());
                this.save(productDetailsEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SandResponse.ok();
    }
}
