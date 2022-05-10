package com.scwl.hzzxgd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 部门成员信息
 */
@Mapper
public interface MemberInformationMapper extends BaseMapper<MemberInformationEntity> {
    /**
     * 修改成员信息
     * @param openUserid
     * @param corpid
     * @param gender
     * @param avatar
     * @param qrCode
     */
    void updateByCorpidAndOpenUserid(@Param("openUserid") String openUserid, @Param("corpid") String corpid, @Param("gender") String gender, @Param("avatar") String avatar, @Param("qrCode") String qrCode);

    /**
     * 设置管理员身份
     * @param corpid
     * @param userid
     */
    void assginToAdmin(@Param("corpid") String corpid,@Param("userid") String userid);

    /**
     * 取消管理员身份
     * @param corpid
     * @param userid
     */
    void unassignFromAdmin(@Param("corpid") String corpid,@Param("userid") String userid);

    /**
     * 根据部门id获取成员  默认跟部门(部门id=1)
     * @param department
     * @param corpid
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<MemberInformationEntity> getMembersLimit(@Param("department") String department,@Param("corpid") String corpid,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize);
}
