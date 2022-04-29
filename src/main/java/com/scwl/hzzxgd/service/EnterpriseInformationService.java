package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.EnterpriseInformationEntity;
import org.json.JSONObject;


/**
 * 企业信息
 */
public interface EnterpriseInformationService {
    /**
     * 存入企业信息
     * @return
     */
    void insertAll(JSONObject jsonObj);

    /**
     * 根据corpid获取企业信息
     * @param corpid
     * @return
     */
    EnterpriseInformationEntity getEnterpriseInfoByCorpid(String corpid);
}
