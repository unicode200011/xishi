package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.mqMessage.LiveGiftRabbitMessage;
import com.xishi.user.entity.mqMessage.LivePayRabbitMessage;
import com.xishi.user.entity.mqMessage.MoviePayRabbitMessage;
import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.user.model.pojo.UserWallet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
public interface IUserWalletService extends IService<UserWallet> {

    BigDecimal findByUserId(Long userId);

    UserWallet findWalletByUserId(Long userId);

    //加币
    boolean addByUserId(Long userId,BigDecimal inMoney);

    //减币
    boolean subByUserId(Long userId,BigDecimal inMoney);

    String findCommon();

    void sendGift(LiveGiftRabbitMessage liveGiftRabbitMessage);

    void sendLivePay(LivePayRabbitMessage livePayRabbitMessage);


    void sendLivePayTimeCountRecord(LivePayRabbitMessage livePayRabbitMessage);

    void sendMoviePay(MoviePayRabbitMessage moviePayRabbitMessage);

    //判断主播打赏余额是否提现
    Boolean isUserWallet(Long id);
}
