package com.xishi.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.webcore.config.redisson.RedissonUtil;
import com.xishi.user.dao.mapper.AdminWalletRecordMapper;
import com.xishi.user.dao.mapper.AgentWalletRecordMapper;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.entity.mqMessage.AdminMoneyRabbitMessage;
import com.xishi.user.entity.mqMessage.AgentMoneyRabbitMessage;
import com.xishi.user.entity.mqMessage.LiveGiftRabbitMessage;
import com.xishi.user.entity.mqMessage.LivePayRabbitMessage;
import com.xishi.user.model.pojo.AdminWalletRecord;
import com.xishi.user.model.pojo.AgentWallet;
import com.xishi.user.model.pojo.AgentWalletRecord;
import com.xishi.user.service.IAdminWalletRecordService;
import com.xishi.user.service.IAgentService;
import com.xishi.user.service.IAgentWalletRecordService;
import com.xishi.user.service.IAgentWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 平台钱包记录 服务实现类
 * </p>
 *
 * @author MS
 * @since 2019-12-25
 */
@Slf4j
@Service
public class AdminWalletRecordServiceImpl extends ServiceImpl<AdminWalletRecordMapper, AdminWalletRecord> implements IAdminWalletRecordService {


    @Override
    public void adminMoneyService(AdminMoneyRabbitMessage adminMoneyRabbitMessage) {
        Integer type = adminMoneyRabbitMessage.getServiceType();
        switch (type){
            case 0:{ //收礼物
                this.dealGiftMoney(adminMoneyRabbitMessage);
                break;
            }

            case 1:{ //观看收费直播
                Integer messageType = adminMoneyRabbitMessage.getMessageType();
                if(messageType == 2){
                    //生成记录
//                    AgentWallet agentWallet = agentWalletService.getOne(new QueryWrapper<AgentWallet>().eq("agent_id", adminMoneyRabbitMessage.getAgentId()));
                    LivePayRabbitMessage livePayRabbitMessage =JSONObject.parseObject(adminMoneyRabbitMessage.getData(),LivePayRabbitMessage.class);
                    AdminWalletRecord agentWalletRecord = new AdminWalletRecord();
                    agentWalletRecord.setUserId(adminMoneyRabbitMessage.getUserId());
                    agentWalletRecord.setIo(0);
                    agentWalletRecord.setAmount(adminMoneyRabbitMessage.getRateMoney());
                    agentWalletRecord.setType(adminMoneyRabbitMessage.getType());
                    //agentWalletRecord.setWalletAmount(agentWallet.getGbAmount());
                    agentWalletRecord.setRemark(adminMoneyRabbitMessage.getRemark());
                    agentWalletRecord.setCustId(livePayRabbitMessage.getLiveId());
                    agentWalletRecord.setCustName(livePayRabbitMessage.getStreamName());
                    agentWalletRecord.setCustNum(livePayRabbitMessage.getLiveTime());
                    agentWalletRecord.setLiveRecordId(livePayRabbitMessage.getLiveRecordId());
                    agentWalletRecord.setLinkUid(livePayRabbitMessage.getUserId());
                    agentWalletRecord.setShowerId(livePayRabbitMessage.getLiveUserId());
                    this.save(agentWalletRecord);
                }
                break;
            }
        }
    }

    //处理收礼物
    @Transactional
    public void dealGiftMoney(AdminMoneyRabbitMessage adminMoneyRabbitMessage) {
        //增加代理商余额 及 总收益
        //BigDecimal bigDecimal = this.submitAddAgentMoney(agentMoneyRabbitMessage.getAgentId(), agentMoneyRabbitMessage.getRateMoney());

        //生成记录
        LiveGiftRabbitMessage liveGiftRabbitMessage =JSONObject.parseObject(adminMoneyRabbitMessage.getData(),LiveGiftRabbitMessage.class);
        AdminWalletRecord adminWalletRecord = new AdminWalletRecord();
        adminWalletRecord.setUserId(adminMoneyRabbitMessage.getUserId());
        adminWalletRecord.setIo(0);
        adminWalletRecord.setAmount(adminMoneyRabbitMessage.getRateMoney());
        adminWalletRecord.setType(adminMoneyRabbitMessage.getType());
        //adminWalletRecord.setWalletAmount(bigDecimal);
        adminWalletRecord.setRemark(adminMoneyRabbitMessage.getRemark());
        adminWalletRecord.setCustId(liveGiftRabbitMessage.getId());
        adminWalletRecord.setCustName(liveGiftRabbitMessage.getGiftName());
        adminWalletRecord.setCustNum(liveGiftRabbitMessage.getNum());
        adminWalletRecord.setLiveRecordId(liveGiftRabbitMessage.getLiveRecordId());
        adminWalletRecord.setLinkUid(liveGiftRabbitMessage.getUserId());
        adminWalletRecord.setShowerId(liveGiftRabbitMessage.getLinkUid());

        log.info("liveGiftRabbitMessage:【{}】", adminMoneyRabbitMessage);
        baseMapper.insert(adminWalletRecord);
    }
}
