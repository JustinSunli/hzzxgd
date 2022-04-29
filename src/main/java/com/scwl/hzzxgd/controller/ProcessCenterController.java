package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProcessCenterEntity;
import com.scwl.hzzxgd.excel.ExcelUtils;
import com.scwl.hzzxgd.service.ProcessCenterService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.ProcessCenterExcelVo;
import com.scwl.hzzxgd.vo.ProductUnitExcelVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/workCenters")
@CrossOrigin(origins ="*")
/**
 * 工序中心
 */
public class ProcessCenterController {
    @Resource
    private ProcessCenterService processCenterService;
    /**
     * 获取工序中心列表 分页
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
        PageHelper pageHelper = processCenterService.getList(corpid,PageIndex,PageSize);
        return SandResponse.ok().put("data",pageHelper);
    }

    /**
     * 根据id获取工序中心
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("")
    public SandResponse getById(@RequestHeader String Authorization, @RequestParam String id){
//        String str = JWTUtils.validateToken(Authorization);
//        String corpid = JWTUtils.getCorpid(str);
        ProcessCenterEntity entity = processCenterService.getByid(id);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 添加工序中心
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/create")
    public SandResponse create(@RequestHeader String Authorization, @RequestBody ProcessCenterEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        processCenterService.create(corpid,openUserid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 修改工序中心
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/update")
    public SandResponse update(@RequestHeader String Authorization, @RequestBody ProcessCenterEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        processCenterService.update(corpid,openUserid,entity);
        return SandResponse.ok();
    }

    /**
     * 删除工序中心
     * @return
     */
    @DeleteMapping("/remove")
    public SandResponse removes(@RequestHeader String Authorization,@RequestBody String[] ids){
        processCenterService.removes(ids);
        return SandResponse.ok();
    }

    /**
     * 获取导入模板
     */
    @GetMapping("/template")
    public void template(HttpServletResponse response){
        ExcelUtils.exportTemplate(response,"工序中心模板", ProcessCenterExcelVo.class);
    }

    /**
     * 导出工序中心表
     * @param Authorization
     * @param response
     */
    @PostMapping("/export")
    public void export(@RequestHeader String Authorization, @RequestBody String[] ids, HttpServletResponse response){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        List<ProcessCenterExcelVo> list = processCenterService.export(corpid,ids);
        ExcelUtils.export(response,"产品单位",list, ProductUnitExcelVo.class);
    }

    /**
     * 导入工序中心表
     * @param Authorization
     * @param file
     * @return
     */
    @PostMapping("/import")
    public SandResponse importCenter(@RequestHeader String Authorization, @RequestPart("file") MultipartFile file){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String userid = JWTUtils.getUserid(str);
        SandResponse sandResponse = processCenterService.importCenter(corpid, userid, file);
        return sandResponse;
    }

}
