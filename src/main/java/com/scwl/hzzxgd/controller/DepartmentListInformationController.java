package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.service.DepartmentListInformationService;
import com.scwl.hzzxgd.service.MemberInformationService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/api/organization-units")
@CrossOrigin(origins ="*")
public class DepartmentListInformationController {
    @Resource
    private DepartmentListInformationService departmentListInformationService;
    @Resource
    private MemberInformationService memberInformationService;

    /**
     * 获取部门全部列表信息
     * @param Authorization
     * @return
     */
    @GetMapping("/get-tree")
    public SandResponse getDepartmentList(@RequestHeader String Authorization) {
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        List<DepartmentListInformationEntity> entity = departmentListInformationService.getDepartmentList(corpid);
        return SandResponse.ok().put("data",entity);
    }

    /**
     * 根据部门id获取成员  默认跟部门(部门id=1)
     * @param Authorization
     * @param id
     * @return
     */
    @GetMapping("/get-members")
    public SandResponse getMembers(@RequestHeader String Authorization, @RequestParam String id, @RequestParam int PageIndex, @RequestParam int PageSize) {
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        PageHelper pageHelper  = memberInformationService.getMembers(id, corpid,PageIndex,PageSize);
        return SandResponse.ok().put("data",pageHelper);
    }
}
