package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.service.MemberInformationService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/identity/users")
@CrossOrigin(origins ="*")
public class MemberInformationController {
    @Resource
    private MemberInformationService memberInformationService;

    /**
     * 设置管理员身份
     * @param Authorization
     * @return
     */
    @PostMapping("/assign-to-admin")
    public SandResponse assginToAdmin(@RequestHeader String Authorization){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        memberInformationService.assginToAdmin(corpid,openUserid);
        return SandResponse.ok();
    }

    /**
     * 取消管理员身份
     * @param Authorization
     * @return
     */
    @PostMapping("/unassign-from-admin")
    public SandResponse unassignFromAdmin(@RequestHeader String Authorization){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        memberInformationService.unassignFromAdmin(corpid,openUserid);
        return SandResponse.ok();
    }
}
