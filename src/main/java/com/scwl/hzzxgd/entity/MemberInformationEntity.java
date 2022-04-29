package com.scwl.hzzxgd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("member_information")
/**
 * 部门成员信息
 */
public class MemberInformationEntity implements Serializable  {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 成员UserID。对应管理端的帐号
     */
    private String userid;
    /**
     * corpid
     */
    private String corpid;
    /**
     * 成员名称
     */
    private String name;
    /**
     * 成员所属部门列表
     */
    private String department;
    /**
     * 全局唯一。对于同一个服务商，不同应用获取到企业内同一个成员的open_userid是相同的，最多64个字节。仅第三方应用可获取
     */
    private String openUserid;
    /**
     * 性别。0表示未定义，1表示男性，2表示女性。仅在用户同意snsapi_privateinfo授权时返回真实值，否则返回0.
     */
    private String gender;
    /**
     * 员工个人二维码（扫描可添加为外部联系人），仅在用户同意snsapi_privateinfo授权时返回
     */
    private String qrCode;
    /**
     * 头像url。仅在用户同意snsapi_privateinfo授权时返回
     */
    private String avatar;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 部门内的排序值
     */
    private String userOrder;
    /**
     * 职务信息
     */
    private String position;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 企业邮箱
     */
    private String bizMail;
    /**
     * 表示在所在的部门内是否为部门负责人。0-否；1-是
     */
    private String isLeaderInDept;
    /**
     * 直属上级UserID
     */
    private String directLeader;
    /**
     * 头像缩略图url
     */
    private String thumbAvatar;
    /**
     * 座机
     */
    private String telephone;
    /**
     * 别名
     */
    private String alias;
    /**
     * 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
     */
    private Integer status;
    /**
     * 对外职务
     */
    private String externalPosition;
    /**
     * 地址
     */
    private String address;
    /**
     * 主部门，仅当应用对主部门有查看权限时返回
     */
    private Integer mainDepartment;
    /**
     * 创建人
     */
    private String createId;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 最后修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modificationTime;
    /**
     * 修改人
     */
    private String modificationId;
    /**
     * 是否启用
     */
    private String isActive;
    /**
     * '0' 是所有人 '1' 管理员
     */
    private String isAdmin;
}
