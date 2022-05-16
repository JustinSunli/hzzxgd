package com.scwl.hzzxgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scwl.hzzxgd.entity.EnterpriseInformationEntity;
import com.scwl.hzzxgd.mapper.EnterpriseInformationMapper;
import com.scwl.hzzxgd.service.EnterpriseInformationService;
import com.scwl.hzzxgd.utils.PubUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EnterpriseInformationServiceImpl extends ServiceImpl<EnterpriseInformationMapper, EnterpriseInformationEntity> implements EnterpriseInformationService {

    /**
     * 存入企业信息
     *
     * @return
     */
    @Override
    @Transactional
    public void insertAll(JSONObject jsonObj) {
        try {
            String corpid = jsonObj.getJSONObject("auth_corp_info").getString("corpid");
            String corpType = jsonObj.getJSONObject("auth_corp_info").getString("corp_type");
            String corpName = jsonObj.getJSONObject("auth_corp_info").getString("corp_name");
//            String corpFullName = jsonObj.getJSONObject("auth_corp_info").getString("corp_full_name");
            Integer subjectType = jsonObj.getJSONObject("auth_corp_info").getInt("subject_type");
            String corpSubIndustry = jsonObj.getJSONObject("auth_corp_info").getString("corp_sub_industry");
            String corpScale = jsonObj.getJSONObject("auth_corp_info").getString("corp_scale");
            Integer corpUserMax = jsonObj.getJSONObject("auth_corp_info").getInt("corp_user_max");
//            Long time = jsonObj.getJSONObject("auth_corp_info").getLong("verified_end_time");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String format = sdf.format(time);
//            Date verifiedEndTime = sdf.parse(format);
            String corpWxqrcode = jsonObj.getJSONObject("auth_corp_info").getString("corp_wxqrcode");
            String corpSquareLogoUrl = jsonObj.getJSONObject("auth_corp_info").getString("corp_square_logo_url");
            String corpRoundLogoUrl = jsonObj.getJSONObject("auth_corp_info").getString("corp_round_logo_url");
            String corpIndustry = jsonObj.getJSONObject("auth_corp_info").getString("corp_industry");
            String location = jsonObj.getJSONObject("auth_corp_info").getString("location");
            String userName = jsonObj.getJSONObject("auth_user_info").getString("name");
            String userid = jsonObj.getJSONObject("auth_user_info").getString("userid");
            String avatar = jsonObj.getJSONObject("auth_user_info").getString("avatar");
            String openUserid = jsonObj.getJSONObject("auth_user_info").getString("open_userid");
            String permanentCode = jsonObj.getString("permanent_code");
            JSONArray jsonArray = jsonObj.getJSONObject("auth_info").getJSONArray("agent");
            Integer agentid = null;
            if (PubUtil.isNotEmpty(jsonArray)){
                agentid = jsonArray.getJSONObject(0).getInt("agentid");
            }
            EnterpriseInformationEntity enterpriseInformationEntity = new EnterpriseInformationEntity();
            enterpriseInformationEntity.setCorpid(corpid)
                    .setCorpType(corpType)
                    .setCorpName(corpName)
                    .setCorpFullName(null)
                    .setSubjectType(subjectType)
                    .setCorpSubIndustry(corpSubIndustry)
                    .setCorpScale(corpScale)
                    .setCorpUserMax(corpUserMax)
                    .setVerifiedEndTime(null)
                    .setCorpWxqrcode(corpWxqrcode)
                    .setCorpSquareLogoUrl(corpSquareLogoUrl)
                    .setCorpRoundLogoUrl(corpRoundLogoUrl)
                    .setCorpIndustry(corpIndustry)
                    .setLocation(location)
                    .setUserName(userName)
                    .setUserid(userid)
                    .setAvatar(avatar)
                    .setOpenUserid(openUserid)
                    .setPermanentCode(permanentCode)
                    .setCreateTime(new Date())
                    .setIsActive("1")
                    .setAgentid(agentid);
//          企业信息是否存在  不存在新增  存在修改
            // EnterpriseInformationEntity entity = this.baseMapper.selectByCorpid(corpid);
            EnterpriseInformationEntity entity = this.getOne(new QueryWrapper<EnterpriseInformationEntity>().lambda()
                    .eq(EnterpriseInformationEntity::getCorpid, corpid)
                    .eq(EnterpriseInformationEntity::getIsActive,"1"));
            if (PubUtil.isEmpty(entity)) {
                this.save(enterpriseInformationEntity);
            } else {
                QueryWrapper<EnterpriseInformationEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("corpid",corpid);
                this.baseMapper.update(enterpriseInformationEntity,queryWrapper);
//                this.update(new QueryWrapper<EnterpriseInformationEntity>().lambda().eq(EnterpriseInformationEntity::getCorpid, corpid));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据corpid获取企业信息
     * @param corpid
     * @return
     */
    @Override
    @Transactional
    public EnterpriseInformationEntity getEnterpriseInfoByCorpid(String corpid) {
        EnterpriseInformationEntity enterpriseInformationEntity =
                this.getOne(new QueryWrapper<EnterpriseInformationEntity>().lambda()
                        .eq(EnterpriseInformationEntity::getCorpid, corpid)
                        .eq(EnterpriseInformationEntity::getIsActive,"1"));
        return enterpriseInformationEntity;
    }
}



















