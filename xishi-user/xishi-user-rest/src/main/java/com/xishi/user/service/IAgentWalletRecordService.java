package com.xishi.user.service;

import com.xishi.user.entity.mqMessage.AgentMoneyRabbitMessage;
import com.xishi.user.model.pojo.AgentWalletRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 代理商钱包记录 服务类
 * </p>
 *
 * @author LX
 * @since 2019-12-25
 */
public interface IAgentWalletRecordService extends IService<AgentWalletRecord> {

    void agentMoneyService(AgentMoneyRabbitMessage agentMoneyRabbitMessage);

}
