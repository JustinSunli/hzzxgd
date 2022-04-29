package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProductUnitEntity;
import com.scwl.hzzxgd.excel.ExcelUtils;
import com.scwl.hzzxgd.service.ProductUnitService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProductUnitExcelVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品单位
 */
@RestController
@RequestMapping("/api/units")
@CrossOrigin(origins ="*")
public class ProductUnitController {
    @Resource
    private ProductUnitService productUnitService;

    /**
     * 获取产品单位列表 分页
     * @param Authorization
     * @param PageIndex
     * @param PageSize
     * @return
     */
    @GetMapping("/getlist")
    public SandResponse getList(@RequestHeader String Authorization, @RequestParam int PageIndex, @RequestParam int PageSize){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
//        String openUserid = JWTUtils.getOpenUserid(str);
        PageHelper pageHelper = productUnitService.getList(corpid,PageIndex,PageSize);
        return SandResponse.ok().put("data",pageHelper);
    }

    /**
     * 根据id获取产品单位
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("")
    public SandResponse getById(@RequestHeader String Authorization, @RequestParam String id){
//        String str = JWTUtils.validateToken(Authorization);
//        String corpid = JWTUtils.getCorpid(str);
        ProductUnitEntity entity = productUnitService.getByid(id);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 添加产品单位
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/create")
    public SandResponse create(@RequestHeader String Authorization, @RequestBody ProductUnitEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productUnitService.create(corpid,openUserid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 修改产品单位
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/update")
    public SandResponse update(@RequestHeader String Authorization, @RequestBody ProductUnitEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productUnitService.update(corpid,openUserid,entity);
        return SandResponse.ok();
    }

    /**
     * 删除产品单位
     * @return
     */
    @DeleteMapping("/remove")
    public SandResponse removes(@RequestHeader String Authorization, @RequestBody String[] ids){
        productUnitService.removes(ids);
        return SandResponse.ok();
    }

    /**
     * 获取导入模板
     */
    @GetMapping("/template")
    public void template(HttpServletResponse response){
        ExcelUtils.exportTemplate(response,"产品单位模板", ProductUnitExcelVo.class);
    }

    /**
     * 导出产品单位表
     * @param Authorization
     * @param response
     */
    @PostMapping("/export")
    public void export(@RequestHeader String Authorization, @RequestBody String[] ids, HttpServletResponse response){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        List<ProductUnitExcelVo> list = productUnitService.excelPortUnit(corpid,ids);
        ExcelUtils.export(response,"产品单位",list,ProductUnitExcelVo.class);
    }

    /**
     * 导入产品单位表
     * @param Authorization
     * @param file
     * @return
     */
    @PostMapping("/import")
    public SandResponse importUnit(@RequestHeader String Authorization, @RequestPart("file") MultipartFile file){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String userid = JWTUtils.getUserid(str);
        SandResponse sandResponse = productUnitService.importUnit(corpid, userid, file);
        return sandResponse;
    }


}
