package com.xishi.user.service;

import com.xishi.user.model.pojo.AgentWallet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LX
 * @since 2019-12-25
 */
public interface IAgentWalletService extends IService<AgentWallet> {

    void addMoneyById(Long belongAgent, BigDecimal rateMoney);
    void subMoneyById(Long belongAgent, BigDecimal subMoney);
    void addGIftMoneyById(Long belongAgent, BigDecimal rateMoney);
}
