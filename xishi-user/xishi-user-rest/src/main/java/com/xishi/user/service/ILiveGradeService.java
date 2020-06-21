package com.xishi.user.service;

import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.user.model.pojo.LiveGrade;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 直播等级 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-19
 */
public interface ILiveGradeService extends IService<LiveGrade> {

    void addLiveGrade(UserRabbitMessage userRabbitMessage);
    Integer checkUserGrade(Long userId);
}
