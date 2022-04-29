package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProductDetailsEntity;
import com.scwl.hzzxgd.excel.ExcelUtils;
import com.scwl.hzzxgd.service.ProductDetailsService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProductDetailsExcelVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品资料
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins ="*")
public class ProductDetailsController {
    @Resource
    private ProductDetailsService productDetailsService;

    /**
     * 获取产品列表 分页
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
        PageHelper pageHelper = productDetailsService.getList(corpid,PageIndex,PageSize);
        return SandResponse.ok().put("data",pageHelper);
    }

    /**
     * 根据id获取产品
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("")
    public SandResponse getById(@RequestHeader String Authorization, @RequestParam String id){
//        String str = JWTUtils.validateToken(Authorization);
//        String corpid = JWTUtils.getCorpid(str);
        ProductDetailsEntity entity = productDetailsService.getByid(id);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 添加产品
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/create")
    public SandResponse create(@RequestHeader String Authorization, @RequestBody ProductDetailsEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productDetailsService.create(corpid,openUserid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 修改产品
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/update")
    public SandResponse update(@RequestHeader String Authorization, @RequestBody ProductDetailsEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productDetailsService.update(corpid,openUserid,entity);
        return SandResponse.ok();
    }

    /**
     * 删除产品
     * @return
     */
    @DeleteMapping("/remove")
    public SandResponse removes(@RequestHeader String Authorization,@RequestBody String[] ids){
        productDetailsService.removes(ids);
        return SandResponse.ok();
    }

    /**
     * 获取导入模板
     */
    @GetMapping("/template")
    public void template(HttpServletResponse response){
        ExcelUtils.exportTemplate(response,"产品资料模板", ProductDetailsExcelVo.class);
    }

    /**
     * 导出产品资料表
     * @param Authorization
     * @param response
     */
    @PostMapping("/export")
    public void export(@RequestHeader String Authorization, @RequestBody String[] ids, HttpServletResponse response){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        List<ProductDetailsExcelVo> list = productDetailsService.export(corpid,ids);
        ExcelUtils.export(response,"产品单位",list,ProductDetailsExcelVo.class);
    }

    /**
     * 导入产品资料表
     * @param Authorization
     * @param file
     * @return
     */
    @PostMapping("/import")
    public SandResponse importUnit(@RequestHeader String Authorization, @RequestPart("file") MultipartFile file){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String userid = JWTUtils.getUserid(str);
        SandResponse sandResponse = productDetailsService.importDetail(corpid, userid, file);
        return sandResponse;
    }

}
