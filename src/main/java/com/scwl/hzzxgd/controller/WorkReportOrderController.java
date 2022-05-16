package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.ProductionOperationEntity;
import com.scwl.hzzxgd.entity.WorkReportOrderEntity;
import com.scwl.hzzxgd.service.ProductionOperationService;
import com.scwl.hzzxgd.service.WorkReportOrderService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报工
 */
@RestController
@RequestMapping("/api/production-feedbacks")
@CrossOrigin(origins ="*")
public class WorkReportOrderController {
    @Resource
    private WorkReportOrderService workReportOrderService;
    @Resource
    private ProductionOperationService productionOperationService;

//    /**
//     * 获取生产工单列表
//     * @param Authorization
//     * @return
//     */
//    @GetMapping("/get-production-info")
//    public SandResponse getProductionInfo(@RequestHeader String Authorization){
//        String str = JWTUtils.validateToken(Authorization);
//        String corpid = JWTUtils.getCorpid(str);
////        String userid = JWTUtils.getUserid(str);
//        List<ProductionOrderEntity> productionOrderEntities = productionOrderService.getProductionInfo(corpid);
//        return SandResponse.ok().put("data",productionOrderEntities);
//    }

    /**
     * 根据生产工单id获取工序列表
     * @param Authorization
     * @return
     */
    @GetMapping("/get-operation-info")
    public SandResponse getOperationInfo(@RequestHeader String Authorization, @RequestParam String id){
        List<ProductionOperationEntity> productionOperationEntities = productionOperationService.getOperationInfo(id);
        return SandResponse.ok().put("data",productionOperationEntities);
    }

    /**
     * 新增报工单
     * @param Authorization
     * @param entity
     * @return
     */
    @PostMapping("/create")
    public SandResponse create(@RequestHeader String Authorization, @RequestBody WorkReportOrderEntity entity){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String userid = JWTUtils.getUserid(str);
        workReportOrderService.create(corpid,userid,entity);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 获取报工单列表 分页
     * @param Authorization
     * @param PageIndex
     * @param PageSize
     * @return
     */
    @GetMapping("/getlist")
    public SandResponse getList(@RequestHeader String Authorization, @RequestParam int PageIndex, @RequestParam int PageSize,
                                @RequestParam String SeqNo, @RequestParam String ProductionOrderSeqNo, @RequestParam String WorkCenterName,
                                @RequestParam String OperatorName, @RequestParam String IsCheck, @RequestParam String ProductCode, @RequestParam String ProductName
                                ){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String userid = JWTUtils.getUserid(str);
        PageHelper pageHelper = workReportOrderService.getList(corpid,userid,PageIndex,PageSize,
                SeqNo,ProductionOrderSeqNo,WorkCenterName,OperatorName,IsCheck,ProductCode,ProductName
                );
        return SandResponse.ok().put("data",pageHelper);
    }

    /**
     * 通过id查询报工单
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("")
    public SandResponse getById(@RequestHeader String Authorization, @RequestParam String id){
        WorkReportOrderEntity entity = workReportOrderService.getByid(id);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 删除报工单
     * @param Authorization
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public SandResponse removes(@RequestHeader String Authorization,@RequestBody String[] ids){
        workReportOrderService.removes(ids);
        return SandResponse.ok();
    }
}
