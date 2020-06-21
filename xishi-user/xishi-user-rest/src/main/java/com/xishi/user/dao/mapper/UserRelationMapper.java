package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.model.pojo.UserRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 好友 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@Repository
public interface UserRelationMapper extends BaseMapper<UserRelation> {
    /**
     * 添加好友关系
     *
     * @param userId
     * @param linkId
     */
    void insertRelationBoth(@Param("userId") Long userId, @Param("linkId") Long linkId);

    /**
     * 删除好友关系
     *
     * @param userId
     * @param linkId
     */
    void deleteBoth(@Param("userId") Long userId, @Param("linkId") Long linkId);
}
