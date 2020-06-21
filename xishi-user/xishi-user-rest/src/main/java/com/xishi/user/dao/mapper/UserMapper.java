package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.entity.vo.LiveVo;
import com.xishi.user.model.pojo.User;
import com.xishi.user.entity.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-14
 */
@Component
public interface UserMapper extends BaseMapper<User> {

    public List<UserInfoVo> queryUserList(@Param("keyword") String keyword, @Param("start") Integer start, @Param("rows") Integer rows);

    User selectByThirdAccount(@Param("account") String account, @Param("type") Integer thirdType);

    void changePraiseNum(@Param("userId") Long id,@Param("type") Integer type);

    void changeAttentionNum(@Param("userId") Long id,@Param("type") Integer type);

    void changeFansNum(@Param("userId") Long id,@Param("type") Integer type);

    Integer selectLiveState(Long userId);

    LiveVo selectLive(Long userId);

    String getCommentLevel(Integer id);
}
