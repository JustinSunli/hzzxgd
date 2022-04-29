package com.scwl.hzzxgd.controller;

import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.exception.SandException;
import com.scwl.hzzxgd.service.HttpRequestService;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.SandResponse;
import com.scwl.hzzxgd.vo.LoginForUserAndEnterpriseInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/hzzxgd/index")
@CrossOrigin(origins ="*")
public class RequestController {
    @Resource
    private HttpRequestService httpRequestService;

    /**
     * 响应url  获取第一步信息
     * @param request
     * @return
     */
    @RequestMapping("/request")
    public String get(HttpServletRequest request){
        String context = httpRequestService.RequestHttpRequestService(request);
        return context;
    }

    /**
     * 登陆
     * @param request
     */
    @RequestMapping("/login")
    public SandResponse login(HttpServletRequest request){
        LoginForUserAndEnterpriseInfo loginForUserAndEnterpriseInfo = httpRequestService.login(request);
        return SandResponse.ok().put("data",loginForUserAndEnterpriseInfo);
    }

    /**
     * 手动存入,获取部门列表
     */
    @GetMapping("/department/{corpid}")
    public SandResponse department(@PathVariable("corpid") String corpid){
        List<DepartmentListInformationEntity> list = httpRequestService.department(corpid);
        return SandResponse.ok().put("data",list);
    }

    /**
     * 手动存入,获取部门成员信息
     */
    @GetMapping("/member/{corpid}")
    public SandResponse member(@PathVariable("corpid") String corpid){
        List<MemberInformationEntity> list = httpRequestService.member(corpid);
        return SandResponse.ok().put("data",list);
    }

    /**
     * 手动获取accessToken
     */
    @GetMapping("/accessToken/{corpid}")
    public SandResponse getAccessToken(@PathVariable("corpid") String corpid){
        String accessToken = httpRequestService.getAccessToken(corpid);
        return SandResponse.ok().put("accessToken",accessToken);
    }

    /**
     * 手动清除存在rides中的suiteAccessToken
     * @return
     */
    @GetMapping("/suiteAccessToken")
    public SandResponse deleteSuiteAccessToken(){
        httpRequestService.deleteSuiteAccessToken();
        return SandResponse.ok();
    }

    /**
     * 测试专用通道
     * @param Authorization
     * @return
     */
    @RequestMapping("/test")
    public String test(@RequestHeader String Authorization){
        if (PubUtil.isEmpty(Authorization))
            throw new SandException("失败");
        return Authorization;
    }

//    @PostMapping("/a")
//    public JSONObject a(){
//        JSONObject jsonObj = httpRequestService.a();
//        return jsonObj;
//    }

    /**
     * 获取签名
     * @param url
     * @return
     */
    @GetMapping("/autograph")
    public SandResponse autograph(@RequestHeader String Authorization, @RequestParam String url){
        String str = JWTUtils.validateToken(Authorization);
        String corpid = JWTUtils.getCorpid(str);
        String sign = httpRequestService.autograph(corpid,url);
        return SandResponse.ok().put("data",sign);
    }

}

