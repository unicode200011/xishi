package com.xishi.user.service.impl;

import com.xishi.user.model.pojo.AgentWallet;
import com.xishi.user.dao.mapper.AgentWalletMapper;
import com.xishi.user.service.IAgentWalletService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-12-25
 */
@Service
public class AgentWalletServiceImpl extends ServiceImpl<AgentWalletMapper, AgentWallet> implements IAgentWalletService {


    @Override
    public void addMoneyById(Long belongAgent, BigDecimal rateMoney) {
        if(rateMoney.compareTo(BigDecimal.ZERO) > 0){
            baseMapper.addMoneyById(belongAgent,rateMoney);
        }
    }

    @Override
    public void subMoneyById(Long belongAgent, BigDecimal subMoney) {
        baseMapper.subMoneyById(belongAgent,subMoney);
    }

    @Override
    public void addGIftMoneyById(Long belongAgent, BigDecimal rateMoney) {
        if(rateMoney.compareTo(BigDecimal.ZERO) > 0){
            baseMapper.addGIftMoneyById(belongAgent,rateMoney);
        }
    }
}
