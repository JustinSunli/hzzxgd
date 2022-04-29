package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.InitializationInformationEntity;
import com.scwl.hzzxgd.service.InitializationInformationService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.SandResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins ="*")
public class InitializationInformationController {
    @Resource
    private InitializationInformationService initializationInformationService;

    @RequestMapping("/get-role-menus")
    public SandResponse initialization(@RequestHeader String Authorization){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String openUserid = JWTUtils.getUserid(str);
        List<InitializationInformationEntity> informationListVos = initializationInformationService.initialization(corpid,openUserid);
        return SandResponse.ok().put("data",informationListVos);
    }
}
