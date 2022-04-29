package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.mapper.DepartmentListInformationMapper;
import com.scwl.hzzxgd.mapper.MemberInformationMapper;
import com.scwl.hzzxgd.service.MemberInformationService;
import com.scwl.hzzxgd.utils.HttpHelper;
import com.scwl.hzzxgd.utils.PageHelper;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.WeChatUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 部门成员信息
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberInformationServiceImpl extends ServiceImpl<MemberInformationMapper, MemberInformationEntity> implements MemberInformationService {
    @Resource
    private DepartmentListInformationMapper departmentListInformationMapper;
    @Resource
    private MemberInformationMapper memberInformationMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 存入部门成员信息
     * @param corpid
     */
    @Override
    @Transactional
    public List<MemberInformationEntity> insertAll(String corpid) {
        //获取该企业的部门id
        List<Integer> departmentIds = departmentListInformationMapper.getDepartmentIds(corpid);
        List<MemberInformationEntity> list = this.list(new QueryWrapper<MemberInformationEntity>().lambda()
                .eq(MemberInformationEntity::getCorpid, corpid)
                .eq(MemberInformationEntity::getIsActive,"1"));
        if (PubUtil.isEmpty(list)){
            for (int i = 0; i < departmentIds.size(); i++) {
                try {
                    //循环查找各个部门存入部门信息
                    String url =  WeChatUtils.THIRD_BUS_WECHAT_DEPART_USER_DETAIL + redisTemplate.opsForValue().get(corpid + "accessToken")
                            + "&department_id=" + departmentIds.get(i) + "&fetch_child=0";
                    JSONObject jsonObj = HttpHelper.doGet(url);
                    JSONArray userlist = jsonObj.getJSONArray("userlist");
                    if (PubUtil.isNotEmpty(userlist)) {
                        for (int j = 0; j < userlist.length(); j++) {
                            JSONObject jsonObject = userlist.getJSONObject(j);
                            String userid = jsonObject.getString("userid");
                            String userName = jsonObject.getString("name");
//                            String address = jsonObject.getString("address");
                            String gender = jsonObject.getString("gender");
//                            String mobile = jsonObject.getString("mobile");
//                            String telephone = jsonObject.getString("telephone");
                            String avatar = jsonObject.getString("avatar");
//                            String thumbAvatar = jsonObject.getString("thumb_avatar");
//                            String alias = jsonObject.getString("alias");
                            Integer status = jsonObject.getInt("status");
//                            String qrCode = jsonObject.getString("qr_code");
//                            String externalPosition = jsonObject.getString("position");
//                            Integer mainDepartment = jsonObject.getInt("main_department");
                            String openUserid = jsonObject.getString("open_userid");
                            JSONArray departmentArray = jsonObject.getJSONArray("department");
                            String departmentstr = "";
                            String department = null;
                            if (PubUtil.isNotEmpty(departmentArray)) {
                                for (int h = 0; h < departmentArray.length(); h++) {
                                    departmentstr += (departmentArray.get(h)+",");
                                }
                                department = PubUtil.delLastChar(departmentstr);
                            }
                            MemberInformationEntity memberInformationEntity = new MemberInformationEntity();
                            memberInformationEntity.setUserid(userid)
                                    .setCorpid(corpid)
                                    .setName(userName)
                                    .setDepartment(department)
                                    .setGender(gender)
//                                    .setMobile(mobile)
//                                    .setTelephone(telephone)
                                    .setAvatar(avatar)
//                                    .setThumbAvatar(thumbAvatar)
//                                    .setAlias(alias)
                                    .setStatus(status)
//                                    .setQrCode(qrCode)
//                                    .setExternalPosition(externalPosition)
                                    .setOpenUserid(openUserid)
                                    .setCreateTime(new Date())
                                    .setModificationTime(new Date())
                                    .setIsActive("1");
                            this.save(memberInformationEntity);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            List<MemberInformationEntity> entityList = this.list(new QueryWrapper<MemberInformationEntity>().lambda()
                    .eq(MemberInformationEntity::getCorpid, corpid)
                    .eq(MemberInformationEntity::getIsActive,"1"));
            return entityList;
        }else{
            return list;
        }
    }

    /**
     * 根据openUserid和corpid获取用户信息
     * @param openUserid
     * @param corpid
     * @return
     */
    @Override
    @Transactional
    public MemberInformationEntity getUserInfoByOpenUserId(String corpid, String openUserid) {
        MemberInformationEntity memberInformationEntity =
                this.getOne(new QueryWrapper<MemberInformationEntity>().lambda()
                        .eq(MemberInformationEntity::getCorpid, corpid)
                        .eq(MemberInformationEntity::getOpenUserid, openUserid)
                        .eq(MemberInformationEntity::getIsActive,"1"));
        return memberInformationEntity;
    }

    /**
     * 修改用户信息 返回
     * @param jsonObj
     * @return
     */
    @Override
    @Transactional
    public MemberInformationEntity updateAndGetUserInfo(JSONObject jsonObj) {
        try {
            String openUserid = jsonObj.getString("open_userid");
            String corpid = jsonObj.getString("corpid");
            String gender = jsonObj.getString("gender");
            String avatar = jsonObj.getString("avatar");
            String qrCode = jsonObj.getString("qr_code");
            memberInformationMapper.updateByCorpidAndOpenUserid(openUserid,corpid,gender,avatar,qrCode);
            MemberInformationEntity memberInformationEntity =
                    this.getOne(new QueryWrapper<MemberInformationEntity>().lambda()
                            .eq(MemberInformationEntity::getCorpid, corpid)
                            .eq(MemberInformationEntity::getOpenUserid, openUserid)
                            .eq(MemberInformationEntity::getIsActive,"1"));
            return memberInformationEntity;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置管理员身份
     * @param corpid
     * @param openUserid
     */
    @Override
    @Transactional
    public void assginToAdmin(String corpid, String openUserid) {
        memberInformationMapper.assginToAdmin(corpid,openUserid);
    }

    /**
     * 取消管理员身份
     * @param corpid
     * @param openUserid
     */
    @Override
    @Transactional
    public void unassignFromAdmin(String corpid, String openUserid) {
        memberInformationMapper.unassignFromAdmin(corpid,openUserid);
    }


    /**
     * 根据部门id获取成员  默认跟部门(部门id=1)
     * @param id
     * @param corpid
     * @return
     */
    @Override
    @Transactional
    public PageHelper getMembers(String id, String corpid, int pageIndex, int pageSize) {
        if (PubUtil.isEmpty(pageIndex) || PubUtil.isEmpty(pageSize)){
            pageIndex = 1; pageSize = 10;
        }
        String department = null;
        int startIndex = (pageIndex-1) * pageSize;
        if (PubUtil.isEmpty(id)){
            department = "1";
        }else {
            department = id;
        }
        List<MemberInformationEntity> entityList = memberInformationMapper.getMembersLimit(department,corpid,startIndex,pageSize);
        int count = this.count(new QueryWrapper<MemberInformationEntity>().lambda().eq(MemberInformationEntity::getCorpid, corpid).eq(MemberInformationEntity::getDepartment, department));
        long totalElements = new Long(count);
        PageHelper pageHelper = new PageHelper();
        List<Object> list = new ArrayList<>();
        list.addAll(entityList);
        pageHelper.setPageIndex(pageIndex).setPageSize(pageSize).setTotalElements(totalElements).setContent(list);
        return pageHelper;
    }
}

