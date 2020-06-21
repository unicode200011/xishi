package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.user.model.pojo.User;
import com.xishi.user.model.pojo.UserGrade;
import com.xishi.user.dao.mapper.UserGradeMapper;
import com.xishi.user.model.pojo.UserWallet;
import com.xishi.user.service.IUserGradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.service.IUserService;
import com.xishi.user.service.IUserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 会员等级 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-19
 */
@Service
@Slf4j
public class UserGradeServiceImpl extends ServiceImpl<UserGradeMapper, UserGrade> implements IUserGradeService {

    @Autowired
    IUserWalletService userWalletService;

    @Autowired
    IUserService userService;

    @Override
    public void addUserGrade(UserRabbitMessage userRabbitMessage) {
        Integer level = this.checkUserGrade(userRabbitMessage.getUserId());
        User user = userService.getById(userRabbitMessage.getUserId());
        Integer userLevel = user.getUserLevel();
        if(userLevel != level){
            user.setUserLevel(level);
            log.info("用户【{}】升级了，原等级=【{}】升级后等级为【{}】",user.getId(),userLevel,level);
            userService.updateById(user);
        }
    }

    @Override
    public Integer checkUserGrade(Long userId) {
        UserWallet walletByUserId = userWalletService.findWalletByUserId(userId);
        //消费总值
        BigDecimal giveAmount = walletByUserId.getGiveAmount();
        Integer levelVal = 1;//默认等级

        List<UserGrade> list = this.list();
        for (int i = 1; i <= list.size(); i++) {
            UserGrade level = this.getOne(new QueryWrapper<UserGrade>().eq("level", i));
            if (giveAmount.compareTo(level.getAmount()) >= 0) {
                levelVal = level.getLevel();
            }else {
                break;
            }
        }
        return levelVal;
    }


}
