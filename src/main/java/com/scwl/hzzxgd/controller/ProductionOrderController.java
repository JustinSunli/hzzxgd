package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProductionOrderEntity;
import com.scwl.hzzxgd.service.ProductionOperationService;
import com.scwl.hzzxgd.service.ProductionOrderService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/production-orders")
@CrossOrigin(origins ="*")
/**
 * 生产工单
 */
public class ProductionOrderController {
    @Resource
    private ProductionOrderService productionOrderService;

    /**
     * 新增生产工单
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/create")
    public SandResponse create(@RequestHeader String Authorization, @RequestBody ProductionOrderEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productionOrderService.create(corpid,openUserid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 修改生产工单
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/update")
    public SandResponse update(@RequestHeader String Authorization, @RequestBody ProductionOrderEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productionOrderService.update(corpid,openUserid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 获取生产工单列表 分页
     * @param Authorization
     * @param PageIndex
     * @param PageSize
     * @return
     */
    @GetMapping("/getlist")
    public SandResponse getList(@RequestHeader String Authorization, @RequestParam int PageIndex, @RequestParam int PageSize){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        PageHelper pageHelper = productionOrderService.getList(corpid,PageIndex,PageSize);
        return SandResponse.ok().put("data",pageHelper);
    }

    /**
     * 通过id查询生产工单信息
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("")
    public SandResponse getById(@RequestHeader String Authorization, @RequestParam String id){
        ProductionOrderEntity entity = productionOrderService.getByid(id);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 删除生产工单
     * @param Authorization
     * @param ids
     * @return
     */
    @DeleteMapping("/remove")
    public SandResponse removes(@RequestHeader String Authorization,@RequestBody String[] ids){
        productionOrderService.removes(ids);
        return SandResponse.ok();
    }

}
