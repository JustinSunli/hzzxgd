package com.scwl.hzzxgd.vo;

import com.scwl.hzzxgd.entity.EnterpriseInformationEntity;
import com.scwl.hzzxgd.entity.InitializationInformationEntity;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class LoginForUserAndEnterpriseInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * token
     */
    private String Authorization;
    /**
     * 用户信息
     */
    private MemberInformationEntity memberInformationEntity;
    /**
     * 企业信息
     */
    private EnterpriseInformationEntity enterpriseInformationEntity;
    /**
     * 初始化信息
     */
    List<InitializationInformationEntity> initializationInformationListVos;
}
