package com.scwl.hzzxgd.service;

import com.scwl.hzzxgd.entity.InitializationInformationEntity;

import java.util.List;

/**
 * 初始化信息
 */
public interface InitializationInformationService {
    /**
     * 获取初始化信息
     * @param corpid
     * @param openUserid
     * @return
     */
    List<InitializationInformationEntity> initialization(String corpid,String openUserid);
}
