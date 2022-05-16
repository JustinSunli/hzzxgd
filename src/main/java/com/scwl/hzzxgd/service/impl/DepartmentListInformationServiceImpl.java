package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import com.scwl.hzzxgd.mapper.DepartmentListInformationMapper;
import com.scwl.hzzxgd.service.DepartmentListInformationService;
import com.scwl.hzzxgd.utils.HttpHelper;
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
import java.util.Date;
import java.util.List;

/**
 * 部门列表信息
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentListInformationServiceImpl extends ServiceImpl<DepartmentListInformationMapper, DepartmentListInformationEntity> implements DepartmentListInformationService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 存入部门列表信息
     * @param jsonObj
     */
    @Override
    @Transactional
    public List<DepartmentListInformationEntity> insertAll(JSONObject jsonObj,String corpid) {
        List<DepartmentListInformationEntity> list = getDepartmentList(corpid);
        if (PubUtil.isEmpty(list)){
            try {
                JSONArray department = jsonObj.getJSONArray("department");
                if (PubUtil.isNotEmpty(department)){
                    for (int i=0 ; i< department.length(); i++) {
                        JSONObject jsonObject = department.getJSONObject(i);
                        Integer departmentListId = jsonObject.getInt("id");
                        Integer parentid = jsonObject.getInt("parentid");
                        Integer departmentOrder = jsonObject.getInt("order");
                        String name = jsonObject.getString("name");
                        JSONArray departmentLeaderObject = jsonObject.getJSONArray("department_leader");
                        String departmentLeaderstr = "";
                        String departmentLeader = null;
//                        String url = WeChatUtils.THIRD_BUS_WECHAT_DEPART_DETAIL + redisTemplate.opsForValue().get(corpid + "accessToken") + "&id=" + departmentListId;
//                        JSONObject jsonObj2 = HttpHelper.doGet(url);
//                        Integer parentid = jsonObj2.getJSONObject("department").getInt("parentid");
//                        Integer departmentOrder = jsonObj2.getJSONObject("department").getInt("order");
//                        String name = jsonObj2.getJSONObject("department").getString("name");
//                        JSONArray departmentLeaderObject = jsonObj2.getJSONObject("department").getJSONArray("department_leader");
//                        String departmentLeaderstr = "";
//                        String departmentLeader = null;
                        if (PubUtil.isNotEmpty(departmentLeaderObject)) {
                            for (int j = 0; j < departmentLeaderObject.length(); j++) {
                                departmentLeaderstr += (departmentLeaderObject.get(i)+",");
                            }
                            departmentLeader = PubUtil.delLastChar(departmentLeaderstr);
                        }
//                        Integer parentid = jsonObject.getInt("parentid");
//                        Integer departmentOrder = jsonObject.getInt("order");
//                        String name = jsonObject.getString("name");
//                        JSONArray departmentLeaderObject = jsonObject.getJSONArray("department_leader");
//                        String departmentLeaderstr = "";
//                        String departmentLeader = null;
//                        if (PubUtil.isNotEmpty(departmentLeaderObject)) {
//                            for (int j = 0; j < departmentLeaderObject.length(); j++) {
//                                departmentLeaderstr += (departmentLeaderObject.get(i)+",");
//                            }
//                            departmentLeader = PubUtil.delLastChar(departmentLeaderstr);
//                        }
                        DepartmentListInformationEntity departmentListInformationEntity = new DepartmentListInformationEntity();
                        departmentListInformationEntity
                                .setDepartmentListId(departmentListId)
                                .setCorpid(corpid)
                                .setParentid(parentid)
                                .setDepartmentOrder(departmentOrder)
                                .setDisplayName(name)
                                .setDepartmentLeader(departmentLeader)
                                .setCreateTime(new Date())
                                .setIsActive("1");
                        this.save(departmentListInformationEntity);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<DepartmentListInformationEntity> entityList = getDepartmentList(corpid);
            return entityList;
        }else{
            return list;
        }
    }


    /**
     * 获取部门列表
     * @param corpid
     * @return
     */
    @Override
    @Transactional
    public List<DepartmentListInformationEntity> getDepartmentList(String corpid){
        DepartmentListInformationEntity entity = this.getOne(new QueryWrapper<DepartmentListInformationEntity>().lambda()
                .eq(DepartmentListInformationEntity::getCorpid, corpid)
                .eq(DepartmentListInformationEntity::getIsActive, "1")
                .eq(DepartmentListInformationEntity::getParentid, 0));
        if (PubUtil.isEmpty(entity))
            return null;
        Integer departmentListId = entity.getDepartmentListId();

        List<DepartmentListInformationEntity> list = this.list(new QueryWrapper<DepartmentListInformationEntity>().lambda()
                .eq(DepartmentListInformationEntity::getCorpid, corpid)
                .eq(DepartmentListInformationEntity::getIsActive, "1")
                .eq(DepartmentListInformationEntity::getParentid,departmentListId));

        List<DepartmentListInformationEntity> recursion = recursion(list, corpid);
        entity.setChildren(recursion);

        List<DepartmentListInformationEntity> children = new ArrayList<>();
        children.add(entity);
        return children;
    }

    private List<DepartmentListInformationEntity> recursion(List<DepartmentListInformationEntity> list,String corpid){
        for (DepartmentListInformationEntity entity : list) {
            Integer id = entity.getDepartmentListId();
            List<DepartmentListInformationEntity> list1 = this.list(new QueryWrapper<DepartmentListInformationEntity>().lambda()
                    .eq(DepartmentListInformationEntity::getCorpid, corpid)
                    .eq(DepartmentListInformationEntity::getIsActive, "1")
                    .eq(DepartmentListInformationEntity::getParentid, id));
            if (PubUtil.isNotEmpty(list1)){
                List<DepartmentListInformationEntity> recursion = recursion(list1, corpid);
                entity.setChildren(recursion);
            }
        }
        return list;
    }
}
