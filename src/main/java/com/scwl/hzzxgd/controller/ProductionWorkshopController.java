package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProductionWorkshopEntity;
import com.scwl.hzzxgd.service.ProductionWorkshopService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/workShops")
@CrossOrigin(origins ="*")
/**
 * 生产车间
 */
public class ProductionWorkshopController {
    @Resource
    private ProductionWorkshopService productionWorkshopService;

    /**
     * 获取生产车间列表 分页
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
        PageHelper pageHelper = productionWorkshopService.getList(corpid,PageIndex,PageSize);
        return SandResponse.ok().put("data",pageHelper);
    }

    /**
     * 根据id获取生产车间
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("")
    public SandResponse getById(@RequestHeader String Authorization, @RequestParam String id){
//        String str = JWTUtils.validateToken(Authorization);
//        String corpid = JWTUtils.getCorpid(str);
        ProductionWorkshopEntity entity = productionWorkshopService.getByid(id);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 添加生产车间
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/create")
    public SandResponse create(@RequestHeader String Authorization, @RequestBody ProductionWorkshopEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productionWorkshopService.create(corpid,openUserid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 修改生产车间
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/update")
    public SandResponse update(@RequestHeader String Authorization, @RequestBody ProductionWorkshopEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        productionWorkshopService.update(corpid,openUserid,entity);
        return SandResponse.ok();
    }

    /**
     * 删除生产车间
     * @return
     */
    @DeleteMapping("/remove")
    public SandResponse removes(@RequestHeader String Authorization,@RequestBody String[] ids){
        productionWorkshopService.removes(ids);
        return SandResponse.ok();
    }
}
