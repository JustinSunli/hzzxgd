package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProcessRouteMainEntity;
import com.scwl.hzzxgd.service.ProcessRouteMainService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 工艺路线主表
 */
@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins ="*")
public class ProcessRouteMainController {
    @Resource
    private ProcessRouteMainService processRouteMainService;

    /**
     * 获取工艺列表 分页
     * @param Authorization
     * @param PageIndex
     * @param PageSize
     * @return
     */
    @GetMapping("/getlist")
    public SandResponse getList(@RequestHeader String Authorization, @RequestParam int PageIndex, @RequestParam int PageSize) {
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        PageHelper pageHelper = processRouteMainService.getList(corpid,PageIndex,PageSize);
        return SandResponse.ok().put("data",pageHelper);
    }

    /**
     * 根据id获取工艺路线详情
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("")
    public SandResponse getById(@RequestHeader String Authorization, @RequestParam String id){
//        String str = JWTUtils.validateToken(Authorization);
//        String corpid = JWTUtils.getCorpid(str);
        ProcessRouteMainEntity entity = processRouteMainService.getByid(id);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 添加工艺路线
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/create")
    public SandResponse create(@RequestHeader String Authorization, @RequestBody ProcessRouteMainEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        processRouteMainService.create(corpid,openUserid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 修改工艺路线
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/update")
    public SandResponse update(@RequestHeader String Authorization, @RequestBody ProcessRouteMainEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        processRouteMainService.update(corpid,openUserid,entity);
        return SandResponse.ok();
    }

    /**
     * 删除工艺路线
     * @return
     */
    @DeleteMapping("/remove")
    public SandResponse removes(@RequestHeader String Authorization,@RequestBody String[] ids){
        processRouteMainService.removes(ids);
        return SandResponse.ok();
    }

}
