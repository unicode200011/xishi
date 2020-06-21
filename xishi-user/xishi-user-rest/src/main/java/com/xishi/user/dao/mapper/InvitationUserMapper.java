package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.model.pojo.InvitationUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 邀请用户列表 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@Repository
public interface InvitationUserMapper extends BaseMapper<InvitationUser> {

    /**
     * 查询我的推荐人
     *
     * @param userId
     * @return
     */
    InvitationUser selectMyInviteUser(@Param("userId") Long userId);

    /**
     * 获取邀请人数排行榜
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map> selectInviteRank(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
