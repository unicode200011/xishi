package com.xishi.user.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xishi.user.model.pojo.UserSign;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户签到 Mapper 接口
 * </p>
 *
 * @author lx
 * @since 2019-02-28
 */
@Repository
public interface UserSignMapper extends BaseMapper<UserSign> {

    List<UserSign> userSign(Long userId);

}
