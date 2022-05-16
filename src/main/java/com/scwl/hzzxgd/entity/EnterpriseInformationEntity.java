package com.scwl.hzzxgd.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("enterprise_information")
/**
 * 企业信息
 */
public class EnterpriseInformationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * corpid
     */
    private String corpid;
    /**
     * 授权方企业微信类型，认证号：verified, 注册号：unverified
     */
    private String corpType;
    /**
     * 授权方企业微信名称
     */
    private String corpName;
    /**
     * 所绑定的企业微信主体名称(仅认证过的企业有)
     */
    private String corpFullName;
    /**
     * 企业类型，1. 企业; 2. 政府以及事业单位; 3. 其他组织, 4.团队号
     */
    private Integer subjectType;
    /**
     * 企业所属子行业。当企业未设置该属性时，值为空
     */
    private String corpSubIndustry;
    /**
     * 企业规模。当企业未设置该属性时，值为空
     */
    private String corpScale;
    /**
     * 授权方企业微信用户规模
     */
    private Integer corpUserMax;
    /**
     * 认证到期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date verifiedEndTime;
    /**
     * 授权企业在微工作台（原企业号）的二维码，可用于关注微工作台
     */
    private String corpWxqrcode;
    /**
     * 授权方企业微信方形头像
     */
    private String corpSquareLogoUrl;
    /**
     * 授权方企业微信圆形头像
     */
    private String corpRoundLogoUrl;
    /**
     * 企业所属行业。当企业未设置该属性时，值为空
     */
    private String corpIndustry;
    /**
     * 企业所在地信息, 为空时表示未知
     */
    private String location;
    /**
     * 授权管理员的name，可能为空（内部管理员一定有，不可更改）
     */
    private String userName;
    /**
     * 授权管理员的userid，可能为空（内部管理员一定有，不可更改）
     */
    private String userid;
    /**
     * 授权管理员的头像url
     */
    private String avatar;
    /**
     * open_userid
     */
    private String openUserid;
    /**
     * 企业微信永久授权码,最长为512字节
     */
    private String permanentCode;
    /**
     * 授权方应用id
     */
    private Integer agentid;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否启用
     */
    private String isActive;
}
