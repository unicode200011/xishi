package com.xishi.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.config.redisson.RedissonUtil;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.mqMessage.AgentMoneyRabbitMessage;
import com.xishi.user.entity.mqMessage.LiveGiftRabbitMessage;
import com.xishi.user.entity.mqMessage.LivePayRabbitMessage;
import com.xishi.user.model.pojo.AgentWallet;
import com.xishi.user.model.pojo.AgentWalletRecord;
import com.xishi.user.dao.mapper.AgentWalletRecordMapper;
import com.xishi.user.service.IAgentService;
import com.xishi.user.service.IAgentWalletRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.service.IAgentWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 代理商钱包记录 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-12-25
 */
@Slf4j
@Service
public class AgentWalletRecordServiceImpl extends ServiceImpl<AgentWalletRecordMapper, AgentWalletRecord> implements IAgentWalletRecordService {

    @Autowired
    IAgentWalletService agentWalletService;

    @Autowired
    IAgentService agentService;
    @Autowired
    RedissonUtil redissonUtil;

    @Override
    public void agentMoneyService(AgentMoneyRabbitMessage agentMoneyRabbitMessage) {
        Integer type = agentMoneyRabbitMessage.getServiceType();
        switch (type){
            case 0:{ //收礼物
                this.dealGiftMoney(agentMoneyRabbitMessage);
                break;
            }
            case 1:{ //观看收费直播
                Integer messageType = agentMoneyRabbitMessage.getMessageType();
                if(messageType == 1){
                    //增加代理商余额 及 总收益
                    BigDecimal bigDecimal = this.submitAddAgentMoney(agentMoneyRabbitMessage.getAgentId(), agentMoneyRabbitMessage.getRateMoney());
                }else if(messageType == 2){
                    //生成记录
                    AgentWallet agentWallet = agentWalletService.getOne(new QueryWrapper<AgentWallet>().eq("agent_id", agentMoneyRabbitMessage.getAgentId()));
                    LivePayRabbitMessage livePayRabbitMessage =JSONObject.parseObject(agentMoneyRabbitMessage.getData(),LivePayRabbitMessage.class);
                    AgentWalletRecord agentWalletRecord = new AgentWalletRecord();
                    agentWalletRecord.setAgentId(agentMoneyRabbitMessage.getAgentId());
                    agentWalletRecord.setIo(0);
                    agentWalletRecord.setAmount(agentMoneyRabbitMessage.getRateMoney());
                    agentWalletRecord.setType(agentMoneyRabbitMessage.getType());
                    agentWalletRecord.setWalletAmount(agentWallet.getGbAmount());
                    agentWalletRecord.setRemark(agentMoneyRabbitMessage.getRemark());
                    agentWalletRecord.setCustId(livePayRabbitMessage.getLiveId());
                    agentWalletRecord.setCustName(livePayRabbitMessage.getStreamName());
                    agentWalletRecord.setCustNum(livePayRabbitMessage.getLiveTime());
                    agentWalletRecord.setLiveRecordId(livePayRabbitMessage.getLiveRecordId());
                    agentWalletRecord.setLinkUid(livePayRabbitMessage.getUserId());
                    agentWalletRecord.setShowerId(livePayRabbitMessage.getLiveUserId());
                    this.save(agentWalletRecord);
                }else {
                    this.dealLiveMoney(agentMoneyRabbitMessage,4);
                }
                 break;
            }
        }
    }

    @Transactional
    public void dealLiveMoney(AgentMoneyRabbitMessage agentMoneyRabbitMessage,Integer type) {
        //增加代理商余额 及 总收益
        BigDecimal bigDecimal = this.submitAddAgentMoney(agentMoneyRabbitMessage.getAgentId(), agentMoneyRabbitMessage.getRateMoney());
        //生成记录
        LivePayRabbitMessage livePayRabbitMessage = JSONObject.parseObject(agentMoneyRabbitMessage.getData(),LivePayRabbitMessage.class);
        AgentWalletRecord agentWalletRecord = new AgentWalletRecord();
        agentWalletRecord.setAgentId(agentMoneyRabbitMessage.getAgentId());
        agentWalletRecord.setIo(0);
        agentWalletRecord.setAmount(agentMoneyRabbitMessage.getRateMoney());
        agentWalletRecord.setType(agentMoneyRabbitMessage.getType());
        agentWalletRecord.setWalletAmount(bigDecimal);
        agentWalletRecord.setRemark(agentMoneyRabbitMessage.getRemark());
        agentWalletRecord.setCustId(livePayRabbitMessage.getLiveId());
        agentWalletRecord.setCustName(livePayRabbitMessage.getStreamName());
        agentWalletRecord.setCustNum(livePayRabbitMessage.getLiveTime());
        agentWalletRecord.setLiveRecordId(livePayRabbitMessage.getLiveRecordId());
        agentWalletRecord.setLinkUid(livePayRabbitMessage.getUserId());
        agentWalletRecord.setShowerId(livePayRabbitMessage.getLiveUserId());
        this.save(agentWalletRecord);
    }

    //处理收礼物
    @Transactional
    public void dealGiftMoney(AgentMoneyRabbitMessage agentMoneyRabbitMessage) {
        //增加代理商余额 及 总收益
        BigDecimal bigDecimal = this.submitAddAgentMoney(agentMoneyRabbitMessage.getAgentId(), agentMoneyRabbitMessage.getRateMoney());



        //生成记录
        LiveGiftRabbitMessage liveGiftRabbitMessage =JSONObject.parseObject(agentMoneyRabbitMessage.getData(),LiveGiftRabbitMessage.class);
        AgentWalletRecord agentWalletRecord = new AgentWalletRecord();
        agentWalletRecord.setAgentId(agentMoneyRabbitMessage.getAgentId());
        agentWalletRecord.setIo(0);
        agentWalletRecord.setAmount(agentMoneyRabbitMessage.getRateMoney());
        agentWalletRecord.setType(agentMoneyRabbitMessage.getType());
        agentWalletRecord.setWalletAmount(bigDecimal);
        agentWalletRecord.setRemark(agentMoneyRabbitMessage.getRemark());
        agentWalletRecord.setCustId(liveGiftRabbitMessage.getId());
        agentWalletRecord.setCustName(liveGiftRabbitMessage.getGiftName());
        agentWalletRecord.setCustNum(liveGiftRabbitMessage.getNum());
        agentWalletRecord.setLiveRecordId(liveGiftRabbitMessage.getLiveRecordId());
        agentWalletRecord.setLinkUid(liveGiftRabbitMessage.getUserId());
        agentWalletRecord.setShowerId(liveGiftRabbitMessage.getLinkUid());
        this.save(agentWalletRecord);
    }

    @Transactional
    public BigDecimal submitAddAgentMoney(Long agentId, BigDecimal rateMoney) {
        String agreeKey = RedisConstants.AGENT_MONEY +agentId;
        boolean locked = redissonUtil.lock(agreeKey, 60);
        AgentWallet agentWallet = new AgentWallet();
        if (!locked) {
            return BigDecimal.valueOf(-1);
        }
        try {
            //增加余额
            agentWalletService.addMoneyById(agentId,rateMoney);
            //增加总收益
            agentWalletService.addGIftMoneyById(agentId,rateMoney);
            agentWallet = agentWalletService.getOne(new QueryWrapper<AgentWallet>().eq("agent_id", agentId));
        }finally {
            redissonUtil.unlock(agreeKey);
        }
        return agentWallet.getGbAmount();
    }
}
