package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.utils.PageHelper;
import org.json.JSONObject;

import java.util.List;

/**
 * 部门成员信息
 */
public interface MemberInformationService {
    /**
     * 存入部门信息
     * @param corpid
     */
    List<MemberInformationEntity> insertAll(String corpid);

    /**
     * 根据openUserid和corpid获取用户信息
     * @param openUserid
     * @param corpid
     * @return
     */
    MemberInformationEntity getUserInfoByOpenUserId(String corpid, String openUserid);

    /**
     * 修改用户信息 返回
     * @param jsonObj
     * @return
     */
    MemberInformationEntity updateAndGetUserInfo(JSONObject jsonObj);

    /**
     * 设置管理员身份
     * @param corpid
     * @param openUserid
     */
    void assginToAdmin(String corpid, String openUserid);

    /**
     * 取消管理员身份
     * @param corpid
     * @param openUserid
     */
    void unassignFromAdmin(String corpid, String openUserid);

    /**
     * 根据部门id获取成员  默认跟部门(部门id=1)
     * @param id
     * @param corpid
     * @return
     */
    PageHelper getMembers(String id, String corpid, int pageIndex, int pageSize);
}
