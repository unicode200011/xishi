package com.xishi.user.service;

import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.user.model.pojo.UserGrade;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员等级 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-19
 */
public interface IUserGradeService extends IService<UserGrade> {

    void addUserGrade(UserRabbitMessage userRabbitMessage);
    Integer checkUserGrade(Long userId);
}
