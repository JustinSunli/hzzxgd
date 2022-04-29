package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import org.json.JSONObject;

import java.util.List;

/**
 * 部门列表信息
 */
public interface DepartmentListInformationService {
    /**
     * 存入部门列表信息
     * @param jsonObj
     */
    List<DepartmentListInformationEntity> insertAll(JSONObject jsonObj, String corpid);

    /**
     * 查询部门列表
     * @param corpid
     * @return
     */
    List<DepartmentListInformationEntity> getDepartmentList(String corpid);
}
