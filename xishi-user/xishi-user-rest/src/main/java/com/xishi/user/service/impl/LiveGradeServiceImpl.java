package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.user.model.pojo.LiveGrade;
import com.xishi.user.dao.mapper.LiveGradeMapper;
import com.xishi.user.model.pojo.User;
import com.xishi.user.model.pojo.UserGrade;
import com.xishi.user.model.pojo.UserWallet;
import com.xishi.user.service.ILiveGradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.service.IUserService;
import com.xishi.user.service.IUserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 直播等级 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-19
 */
@Service
public class LiveGradeServiceImpl extends ServiceImpl<LiveGradeMapper, LiveGrade> implements ILiveGradeService {
    @Autowired
    IUserWalletService userWalletService;

    @Autowired
    IUserService userService;

    @Override
    public void addLiveGrade(UserRabbitMessage userRabbitMessage) {
        Integer level = this.checkUserGrade(userRabbitMessage.getUserId());
        User user = userService.getById(userRabbitMessage.getUserId());
        Integer liveLevel = user.getLiveLevel();
        if(liveLevel != level){
            user.setLiveLevel(level);
            userService.updateById(user);
        }
    }

    @Override
    public Integer checkUserGrade(Long userId) {
        UserWallet walletByUserId = userWalletService.findWalletByUserId(userId);
        //收益总值
        BigDecimal giftAmount = walletByUserId.getGiftAmount();
        Integer levelVal = 1;//默认等级
        List<LiveGrade> list = this.list();
        for (int i = 1; i <= list.size(); i++) {
            LiveGrade level = this.getOne(new QueryWrapper<LiveGrade>().eq("level", i));
            if (giftAmount.compareTo(level.getAmount()) >= 0) {
                levelVal = level.getLevel();
            }else {
                break;
            }
        }
        return levelVal;
    }

}
