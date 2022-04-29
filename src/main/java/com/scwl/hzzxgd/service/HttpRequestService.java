package com.scwl.hzzxgd.service;


import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.vo.LoginForUserAndEnterpriseInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface HttpRequestService {
    /**
     * 响应url  获取第一步信息
     * @param request
     * @return
     */
    String RequestHttpRequestService(HttpServletRequest request);

    /**
     * 登陆
     * @param request
     */
    LoginForUserAndEnterpriseInfo login(HttpServletRequest request);

    /**
     * 手动存入,获取部门列表
     */
    List<DepartmentListInformationEntity> department(String corpid);

    /**
     * 手动存入,获取部门成员信息
     */
    List<MemberInformationEntity> member(String corpid);

    /**
     * 手动获取accessToken
     */
    String getAccessToken(String corpid);

    /**
     * 手动清除现在存在rides中的suiteAccessToken
     */
    void deleteSuiteAccessToken();

    /**
     * 获取签名
     * @param url
     * @return
     */
    String autograph(String corpid,String url);

//    JSONObject a();
}
