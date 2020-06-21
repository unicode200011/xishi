package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.model.pojo.InvitationCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 邀请码 Mapper 接口
 */
@Repository
public interface InvitationCodeMapper extends BaseMapper<InvitationCode> {

    /**
     * 根据邀请码查询
     */
    InvitationCode selectByCode(@Param("code") String code);

    /**
     * 查询用户邀请码
     */
    InvitationCode selectOneByUserId(@Param("userId") Long userId);
}
